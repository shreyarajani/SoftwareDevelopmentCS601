package cs601.webmail.services;

import cs601.webmail.managers.AccountManager;
import cs601.webmail.misc.Account;
import cs601.webmail.protocols.POP;
import cs601.webmail.protocols.SMTP;
import org.apache.log4j.Logger;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by shreyarajani on 4/24/15.
 */
public class AccountService {
    static final Logger logger = Logger.getLogger(AccountService.class);

    private static final boolean DEBUG = true;
    HttpServletRequest request;
    HttpServletResponse response;
    String reqType;
    AccountManager accountManager;
    String userID;
    String email;
    String password;
    String popServer;
    String popPort;
    String smtpServer;
    String smtpPort;
    Account account;

    public AccountService(HttpServletRequest request, HttpServletResponse response, String reqType) {
        this.reqType = reqType;
        this.request = request;
        this.response = response;
        this.accountManager = new AccountManager();
    }

    public void account() {
        email = request.getParameter("email");
        password = request.getParameter("password");
        popServer = request.getParameter("popserver");
        popPort = request.getParameter("popport");
        smtpServer = request.getParameter("smtpserver");
        smtpPort = request.getParameter("smtpport");

        int popP = 0, smtpP = 0;
        try {
        popP = Integer.parseInt(popPort);
        smtpP = Integer.parseInt(smtpPort);

        } catch (NumberFormatException e) {
            logger.error(e);
            sendRedirect("/account");
            return;
        }

        userID = getSession();
        Account account=new Account(userID, email, password, popServer, popP, smtpServer, smtpP);

        boolean present = accountManager.checkAccountinDB(account);

        if (present) {
            sendRedirect("/accountexists");
            return;
        } else { //add to database and check for the validity of the deails
            if (!checkPOP(popServer, popP, email, password)) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                sendRedirect("/account");
                return;
            }

            if (!checkSMTP(smtpServer, smtpP, email, password)) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                sendRedirect("/account");
                return;
            }
            accountManager.addAccountinDB(account);
            addToSession();
            sendRedirect("/accountadded");
            return;
        }
    }

    private boolean checkPOP(String host, int popP, String userName, String password) {
        POP pop = null;
        try {
            pop = new POP(userName, password, host, popP);
            return true;
        } catch (IOException | MessagingException e) {
            logger.error(e);
        } finally {
            if (pop != null) {
                try {
                    pop.quit();
                } catch (IOException e) {
                    logger.error(e);
                }
            }
        }
        return false;
    }

    private boolean checkSMTP(String host, int smtpP, String userName, String password) {
        SMTP smtp = null;
        try {
            smtp = new SMTP(userName, password, host, smtpP);
            return true;
        } catch (IOException e) {
            logger.error(e);
        } finally {
            if (smtp != null) {
                try {
                    smtp.quit();
                } catch (IOException e) {
                    logger.error(e);
                }
            }
        }
        return false;
    }

    private void sendRedirect(String uri) {
        try {
            response.sendRedirect(uri);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    public String getSession(){
        String userID=null;
        HttpSession session = request.getSession();
        Object ses = session.getAttribute("SESSION_USER");
        if (ses != null)
        {
            userID = ses.toString();
        }
        return userID;
    }

    public void addToSession(){
        HttpSession session = request.getSession();
        session.setAttribute("SESSION_ACCOUNT", email);
        session.setAttribute("SESSION_PASSWORD", password);
        session.setAttribute("SESSION_POP", popServer);
        session.setAttribute("SESSION_POPPORT", popPort);
        session.setAttribute("SESSION_SMTP", smtpServer);
        session.setAttribute("SESSION_SMTPPORT", smtpPort);
    }

    public ArrayList<Account> checkForAlreadyPresent(String userid) {
        return  accountManager.checkAccounts(userid);
    }

    public void loadaccount() {
        String accountID = request.getParameter("id");
        account=new Account();
        account = accountManager.getAccountfromDB(accountID);
        HttpSession session = request.getSession();
        session.setAttribute("SESSION_ACCOUNT", account.getAccount());
        session.setAttribute("SESSION_PASSWORD", account.getPassword());
        session.setAttribute("SESSION_POP", account.getPopserver());
        session.setAttribute("SESSION_POPPORT", account.getPopport());
        session.setAttribute("SESSION_SMTP", account.getSmtpserver());
        session.setAttribute("SESSION_SMTPPORT", account.getSmtpport());
        sendRedirect("/inbox");
    }

    public Account getAccount() {
        String accountID = request.getParameter("id");
        return accountManager.getAccountfromDB(accountID);
    }
}

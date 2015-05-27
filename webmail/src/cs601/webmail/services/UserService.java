package cs601.webmail.services;

/**
 * Created by shreyarajani on 4/18/15.
 */

import cs601.webmail.managers.UserManager;
import cs601.webmail.misc.Account;
import cs601.webmail.misc.DatabaseException;
import cs601.webmail.misc.User;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

public class UserService {
    static final Logger logger = Logger.getLogger(FolderService.class);
    private static final boolean DEBUG = true;
    HttpServletRequest request;
    HttpServletResponse response;
    String reqType;
    UserManager userManager;
    Account account;

    public UserService(HttpServletRequest request, HttpServletResponse response, String reqType) {
        this.reqType = reqType;
        this.request = request;
        this.response = response;
        this.userManager = new UserManager();
    }

    public void login() {
        ArrayList<Account> accounts;
        String userid = request.getParameter("userid");
        String pwd = request.getParameter("password");

        try {
            boolean check = userManager.checkInDB(userid, pwd);
            if (check) { // matched! redirect to inbox
                createSession(userid);
                accounts = checkforPOPandSMTP(userid);
                if (accounts.size() == 0) {
                    sendRedirect("/mailbox");
                } else {
                    sendRedirect("/selectaccount");
                }

            } else {
                sendRedirect("/login");
            }
        } catch (DatabaseException e) {
            logger.error(e);
            sendRedirect("/login");
        }
    }

    private ArrayList<Account> checkforPOPandSMTP(String userid) {
        AccountService accountService = new AccountService(request, response, reqType);
        return accountService.checkForAlreadyPresent(userid);
    }

    public ArrayList<Account> checForAccount() {
        HttpSession session = request.getSession();
        String userid = session.getAttribute("SESSION_USER").toString();
        AccountService accountService = new AccountService(request, response, reqType);
        return accountService.checkForAlreadyPresent(userid);
    }

    private void createSession(String userid) {
        HttpSession session = request.getSession();
        session.setAttribute("SESSION_USER", userid);
    }

    public void registerUser() throws IOException, DatabaseException {
        String userid = request.getParameter("userid");
        String pwd = request.getParameter("password");
        String fName = request.getParameter("firstname");
        String lName = request.getParameter("lastname");

        User user = new User(userid, pwd, fName, lName);

        try {
            int insertId = userManager.addUserToDB(user);

            if (insertId == 0) {
                sendRedirect("/exists");
            } else {
                sendRedirect("/login");
            }
        } catch (DatabaseException e) {
            logger.error(e);
            sendRedirect("/register");
        }
    }

    private void sendRedirect(String uri) {
        try {
            response.sendRedirect(uri);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    public void changePassword() {
        HttpSession session = request.getSession();
        String userid = session.getAttribute("SESSION_USER").toString();
        String oldpwd = request.getParameter("currentpassword");
        String newpwd = request.getParameter("newpassword");

        userManager = new UserManager();

        int update = userManager.updatePasswordinDB(userid, oldpwd, newpwd);

        if (update == 0) { //invalid password
            sendRedirect("/changepassword");
            return;
        }
        session.invalidate();
        sendRedirect("/login");
    }
}
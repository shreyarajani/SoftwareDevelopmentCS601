package cs601.webmail.services;

import cs601.webmail.managers.FolderManager;
import cs601.webmail.managers.MailManager;
import cs601.webmail.misc.Folder;
import cs601.webmail.misc.Mail;
import cs601.webmail.protocols.NaiveBayesClassifier;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by shreyarajani on 4/21/15.
 */
public class FolderService {
    static final Logger logger = Logger.getLogger(FolderService.class);

    HttpServletRequest request;
    HttpServletResponse response;
    String reqType;
    FolderManager folderManager;
    String userid;
    String accountid;
    Folder folder;


    public FolderService(HttpServletRequest request, HttpServletResponse response, String reqType) {
        this.reqType = reqType;
        this.request = request;
        this.response = response;
    }

    public void addFolder() {
        String foldername = (request.getParameter("folder")).toUpperCase();
        getSession();
        folderManager = new FolderManager();
        folder = new Folder(foldername, userid, accountid);
        int isPresent = folderManager.addNewFolderToDB(folder); //0 is added to database
        if (isPresent == 1) { //Folder already present --> redirect to folder exists
            sendRedirect("/showallfolders");
        } else { //take to page that shows folder added --> redirect to show all folders
            sendRedirect("/showallfolders");
        }
    }

    private void getSession() {
        HttpSession session = request.getSession();
        userid = session.getAttribute("SESSION_USER").toString();
        accountid = session.getAttribute("SESSION_ACCOUNT").toString();
    }

    private void sendRedirect(String uri) {
        try {
            response.sendRedirect(uri);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    public ArrayList<Folder> getAllFolders() {
        folderManager = new FolderManager();
        getSession();
        return folderManager.getAllFOlderFromDB(userid, accountid);
    }

    public void moveToFolder() {
        String emailIDArray[] = request.getParameterValues("mailid"); //list of mailid to be moved
        String requestParameter = (request.getParameter("foldername")).toLowerCase();
        if (emailIDArray == null) {
            sendRedirect("/inbox");
            return;
        }

        String oldFolder = null, newFolder = null;

        if (requestParameter.equals("not spam")) { //coming from spam ==> marking it to Not Spam
            requestParameter = "spam_inbox";
        } else if (requestParameter.equals("restore")) { //restoring email from trash to inbox
            requestParameter = "trash_inbox";
        } else if (requestParameter.equals("delete from sent")) {
            requestParameter = "sent_trash";
        }

        if (requestParameter.contains("_")) {
            int index = requestParameter.indexOf('_');
            oldFolder = requestParameter.substring(0, index);
            newFolder = requestParameter.substring(index + 1, requestParameter.length());
        }

        folderManager = new FolderManager();
        for (String mailid : emailIDArray) {
            folderManager.changeFolderinMailTable(mailid, newFolder);
        }

        if (oldFolder != null) {
            if (newFolder.equals("spam")) {
                NaiveBayesClassifier n = new NaiveBayesClassifier();
                MailManager mailManager = new MailManager();
                for (String mailid : emailIDArray) {
                    Mail mail = mailManager.getEmailfromDB(mailid);
                    String email = mail.getSubject() + " " + mail.getBody();
                    n.trainForSpam(email);
                }
            }
            if (oldFolder.equals("inbox") || oldFolder.equals("trash") || oldFolder.equals("spam") || oldFolder.equals("sent")) {
                sendRedirect(oldFolder);
                return;
            } else {
                sendRedirect("/showallfolders");
                return;
            }
        }
        sendRedirect("/inbox");
    }

    public void deletePermanently() {
        String emailIDArray[] = request.getParameterValues("mailid"); //list of mailid to be moved
        String folder = "TRASH";
        if (emailIDArray == null) {
            sendRedirect("/trash");
            return;
        }

        MailManager mailManager = new MailManager();
        for (String mailid : emailIDArray) {
            mailManager.deleteEmailFromDB(mailid, folder);
        }
        sendRedirect("/trash");
    }

    public void emptyTrash() {
        getSession();
        MailManager mailManager = new MailManager();
        int d = mailManager.emptyTrashFromDB(userid, accountid);
        sendRedirect("/trash");
    }

    public void deleteFolder() {
        getSession();
        String foldername = request.getParameter("id");
        folderManager = new FolderManager();
        MailManager mailManager = new MailManager();
        folder = new Folder();
        folder.setFolder(foldername);
        folder.setUserid(userid);
        folder.setAccountid(accountid);
        int check = folderManager.deleteFolderFromDB(folder);
        if (check > 0) {
            int checkMail = mailManager.deleteEmailFromDBUsingFolder(folder);
        }
        sendRedirect("/showallfolders");
    }
}

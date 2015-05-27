package cs601.webmail.services;

import cs601.webmail.misc.DatabaseException;
import cs601.webmail.pages.*;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.TreeMap;

public class DispatchServlet extends HttpServlet {
    static final boolean DEBUG = true;
    public static Map<String, Class> mapping = new TreeMap<>();
    SessionService ss=new SessionService();
    static final Logger logger = Logger.getLogger(DispatchServlet.class);


    static {
        mapping.put("/", HomePage.class);
        mapping.put("/login", Login.class);
        mapping.put("/exists", Exists.class);
        mapping.put("/accountexists", AccountExists.class);
        mapping.put("/inbox", Inbox.class);
        mapping.put("/account", AccountPage.class);
        mapping.put("/accountadded", AccountAdded.class);
        mapping.put("/register", Register.class);
        mapping.put("/mailbox", Mailbox.class);
        mapping.put("/error", ErrorPage.class);
        mapping.put("/selectaccount", SelectAccount.class);
        mapping.put("/viewemail", ViewEmail.class);
        mapping.put("/reply", Reply.class);
        mapping.put("/forward", Forward.class);
        mapping.put("/compose", Compose.class);
        mapping.put("/seeaccount", SeeAccount.class);
        mapping.put("/addfolder", AddFolder.class);
        mapping.put("/showallfolders", ShowAllFolders.class);
        mapping.put("/spam", Spam.class);
        mapping.put("/trash", Trash.class);
        mapping.put("/sent", Sent.class);
        mapping.put("/showfolder", ShowFolder.class);
        mapping.put("/changepassword", ChangePassword.class);
        mapping.put("/invalidcurrentpassword", InvalidCurrentPassword.class);
        mapping.put("/searchemail", SearchEmail.class);
        mapping.put("/settings", Settings.class);
        mapping.put("/showallcontacts", ShowAllContacts.class);
        mapping.put("/editcontact", EditContact.class);
        mapping.put("/addcontact", AddContact.class);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();

        switch (uri) {
            case "/process_login":
                new UserService(request, response, uri).login();
                break;
            case "/process_register":
                try {
                    new UserService(request, response, uri).registerUser();
                } catch (DatabaseException e) {
                    logger.error(e);
                }
                break;
            default:
                if (ss.isLoggedIn(request, response)) {
                    if (uri.equals("/process_account")) {
                        new AccountService(request, response, uri).account();
                    } else if (uri.equals("/change_password")) {
                        new UserService(request, response, uri).changePassword();
                    } else if (ss.checkAccountSession(request, response)) {
                        switch (uri) {
                            case "/send_email":
                                new MailService(request, response, uri).sendMail();
                                break;
                            case "/process_adding_folder":
                                new FolderService(request, response, uri).addFolder();
                                break;
                            case "/move_to_folder":
                                new FolderService(request, response, uri).moveToFolder();
                                break;
                            case "/delete_permanently":
                                new FolderService(request, response, uri).deletePermanently();
                                break;
                            case "/add_contact":
                                new ContactService(request, response, uri).addContact();
                                break;
                            case "/update_contact":
                                new ContactService(request, response, uri).updateContact();
                                break;
                        }
                    } else {
                        uri = "/selectaccount";
                        createPage(request, response, uri);
                    }
                } else {
                    uri = "/login";
                    createPage(request, response, uri);
                }

                break;
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        switch (uri) {
            case "/login":
            case "/exists":
            case "/":
            case "/error":
            case "/register":
                createPage(request, response, uri);
                break;
            default:
                if (ss.isLoggedIn(request, response)) {
                    if (uri.equals("/selectaccount") || uri.equals("/seeaccount") || uri.equals("/mailbox") || uri.equals("/account") || uri.equals("/accountadded") || uri.equals("/accountexists")) {
                        createPage(request, response, uri);
                    } else if (uri.equals("/logout")) {
                        ss.invalidateSession(request);
                        createPage(request, response, "/login");
                    } else if (uri.equals("/loadaccount")) {
                        new AccountService(request, response, uri).loadaccount();
                    } else if (uri.equals("/changepassword") || uri.equals("/settings")) {
                        createPage(request, response, uri);
                    } else if (ss.checkAccountSession(request, response)) {
                        switch (uri) {
                            case "/checkmail":
                                new MailService(request, response, uri).checkMail();
                                break;
                            case "/switchaccount":
                                ss.removeAccountSession(request);
                                createPage(request, response, "/selectaccount");
                                break;
                            case "/searchemail":
                            case "/showallcontacts":
                            case "/editcontact":
                            case "/addcontact":
                            case "/inbox":
                            case "/viewemail":
                            case "/forward":
                            case "/compose":
                            case "/reply":
                            case "/addfolder":
                            case "/showallfolders":
                            case "/spam":
                            case "/trash":
                            case "/sent":
                            case "/showfolder":
                                createPage(request, response, uri);
                                break;
                            case "/toggle":
                                new MailService(request, response, uri).toggleMail();
                                break;
                            case "/deletecontact":
                                new ContactService(request, response, uri).deleteContact();
                                break;
                            case "/deletefolder":
                                new FolderService(request, response, uri).deleteFolder();
                                break;
                            case "/empty_trash":
                                new FolderService(request, response, uri).emptyTrash();
                                break;
                        }
                    } else {
                        uri = "/selectaccount";
                        createPage(request, response, uri);
                    }
                } else {
                    uri = "/login";
                    createPage(request, response, uri);
                }
                break;
        }
    }

    private void createPage(HttpServletRequest request, HttpServletResponse response, String uri) throws IOException {
        Page p = null;
        Class pageClass = mapping.get(uri);
        if (pageClass != null) {
            try {
                Constructor<Page> ctor = pageClass.getConstructor(HttpServletRequest.class,
                        HttpServletResponse.class);
                p = ctor.newInstance(request, response);
            } catch (Exception e) {
                logger.error(e);
            }
        }

        if (p == null) {
            response.sendRedirect("/error");
            return;
        }
        response.setContentType("text/html");
        p.generate();
    }
}
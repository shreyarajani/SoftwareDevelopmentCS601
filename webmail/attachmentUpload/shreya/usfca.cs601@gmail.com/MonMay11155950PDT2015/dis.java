package cs601.webmail.services;

import cs601.webmail.managers.ErrorManager;
import cs601.webmail.misc.DatabaseException;
import cs601.webmail.pages.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.TreeMap;

public class DispatchServlet extends HttpServlet {
    static final boolean DEBUG = true;
    public static Map<String, Class> mapping = new TreeMap<>();
    public static Map<String, String> intMapping = new TreeMap<>();

    static {
        mapping.put("DEFAULT", HomePage.class);
        mapping.put("LOGIN", Login.class);
        mapping.put("EXISTS", Exists.class);
        mapping.put("ACCOUNT_EXISTS", AccountExists.class);
        mapping.put("INBOX", Inbox.class);
        mapping.put("ACCOUNT", AccountPage.class);
        mapping.put("ACCOUNT_ADDED", AccountAdded.class);
        mapping.put("REGISTER", Register.class);
        mapping.put("MAILBOX", Mailbox.class);
        mapping.put("ERROR", Error.class);

        intMapping.put("DEFAULT", "/");
        intMapping.put("REGISTER", "/register");
        intMapping.put("MAILBOX", "/mailbox");
        intMapping.put("ACCOUNT_EXISTS", "/account_exists");
        intMapping.put("ACCOUNT_ADDED", "/account_added");
        intMapping.put("PROCESS_REGISTER", "/process_register");
        intMapping.put("PROCESS_LOGIN", "/process_login");
        intMapping.put("PROCESS_ACCOUNT", "/process_account");
        intMapping.put("LOGIN", "/login");
        intMapping.put("CHECK_MAIL", "/checkmail");
        intMapping.put("LOGOUT", "/logout");
        intMapping.put("EXISTS", "/exists");
        intMapping.put("INBOX", "/inbox");
        intMapping.put("ACCOUNT", "/account");
        intMapping.put("CHECKMAIL", "/checkmail");
        intMapping.put("COMPOSE", "/compose");
        intMapping.put("DRAFTS", "/drafts");
        intMapping.put("SENT", "/sent");
        intMapping.put("JUNK", "/junk");
        intMapping.put("TRASH", "/trash");
        intMapping.put("ERROR", "/error");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (DEBUG) {
            System.out.println("DoPost");
            System.out.println("========================");
        }
        String req = request.getRequestURI();
        String map = "";
        String reqType = "";

        for (Map.Entry<String, String> entry : intMapping.entrySet()) {
            if (entry.getValue().equals(req)) {
                map = entry.getKey();
            }
        }

        if (map != null) {
            reqType = map;
        } else {
            reqType = "ERROR";
        }

        if (DEBUG) {
            System.out.println("DoPost Request: " + request);
            System.out.println("DoPost Response: " + response);
            System.out.println("DoPost ReqType: " + reqType);
        }
        if (DEBUG)
            System.out.println("DoPost reqType: " + req);

        if (reqType == "PROCESS_LOGIN") {
            new UserService(request, response, reqType).login();
        } else if (reqType == "PROCESS_REGISTER") {
            if (DEBUG)
                System.out.println("DoPost Inside req == process_register");
            try {
                if (DEBUG) {
                    System.out.println("DoPost Request: " + request);
                    System.out.println("DoPost Response: " + response);
                    System.out.println("DoPost ReqType: " + reqType);
                }
                new UserService(request, response, reqType).registerUser();
            } catch (DatabaseException e) {
                e.printStackTrace();
            }
        } else {
            if (checkSession(request, response)) {
                if (reqType == "PROCESS_ACCOUNT") {
                    new AccountService(request, response, reqType).account();
                }
            }
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (DEBUG) {
            System.out.println("DoGet");
            System.out.println("========================");
        }

        String req = request.getRequestURI();
        String map = "";
        String reqType = "";

        for (Map.Entry<String, String> entry : intMapping.entrySet()) {
            if (entry.getValue().equals(req)) {
                map = entry.getKey();
            }
        }

        if (map != null) {
            reqType = map;
        } else {
            reqType = "ERROR";
        }

        if (DEBUG)
            System.out.println("DoGet URI: " + reqType);

        String error = request.getParameter("error");

        if (reqType == "LOGIN" || reqType == "EXISTS" || reqType == "DEFAULT" || reqType == "ERROR" || reqType == "REGISTER") {
            if (DEBUG)
                System.out.println("DoGet Inside uri IF");
            loadPage(request, response, reqType, error);
        } else {
            if (checkSession(request, response)) {
                if (reqType == "MAILBOX") {
                    loadPage(request, response, reqType, error);
                } else if (reqType == "LOGOUT") {
                    invalidateSession(request);
                    reqType = "LOGIN";
                    loadPage(request, response, reqType, error);
                } else if (reqType == "CHECK_MAIL") {
                    new MailService(request, response, reqType, error).checkMail();
                } else if (reqType == "ACCOUNT") {
                    loadPage(request, response, reqType, error);
                } else if (reqType == "ACCOUNT_ADDED") {
                    loadPage(request, response, reqType, error);
                } else if (reqType == "ACCOUNT_EXISTS") {
                    loadPage(request, response, reqType, error);
                }
            } else {
                reqType = "LOGIN";
                loadPage(request, response, reqType, error);
            }
        }
    }

    private void invalidateSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    private boolean checkSession(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        if (session.getAttribute("SESSION_USER") != null) {
            return true;
        } else {
            return false;
        }
    }

    private void loadPage(HttpServletRequest request, HttpServletResponse response, String reqType, String error) throws IOException {
        if (DEBUG)
            System.out.println("Request: " + request + " Response: " + response + " URI---: " + reqType);

        Page p = createPage(reqType, request, response);
        if (p == null) {
            response.sendRedirect("/error");
            return;
        }
        response.setContentType("text/html");
        p.generate();
    }

    public Page createPage(String reqType, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("REQ_TYPE:" + reqType);
        System.out.println("MAPPING: " + mapping.get(reqType));
        Class pageClass = mapping.get(reqType);
        if (pageClass != null) {
            try {
                Constructor<Page> ctor = pageClass.getConstructor(HttpServletRequest.class,
                        HttpServletResponse.class);
                return ctor.newInstance(request, response);
            } catch (Exception e) {
                ErrorManager.instance().error(e);
            }
        }
        return null;
    }
}
package cs601.webmail.services;

import cs601.webmail.managers.ContactManager;
import cs601.webmail.misc.Contact;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by shreyarajani on 5/4/15.
 */
public class ContactService {
    static final Logger logger = Logger.getLogger(ContactService.class);

    HttpServletRequest request;
    HttpServletResponse response;
    String reqType;
    String userID;
    String accountID;

    public ContactService(HttpServletRequest request, HttpServletResponse response, String reqType) {
        this.reqType = reqType;
        this.request = request;
        this.response = response;
    }

    public ArrayList<Contact> getAllContacts() {
        ContactManager contactManager = new ContactManager();
        getSession();
        return contactManager.getAllContactsFromDB(userID, accountID);
    }

    public void getSession() {
        HttpSession session = request.getSession();
        userID = session.getAttribute("SESSION_USER").toString();
        accountID = session.getAttribute("SESSION_ACCOUNT").toString();
    }

    public Contact getContact(String email) {
        getSession();
        ContactManager contactManager = new ContactManager();
        return contactManager.getContactFromDB(userID, accountID, email);
    }

    public void addContact() {
        getSession();
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        Contact contact = new Contact();
        contact.setAccountid(accountID);
        contact.setEmail(email);
        contact.setUserid(userID);
        contact.setName(name);
        ContactManager contactManager=new ContactManager();
        contactManager.addContactToDB(contact);
        sendRedirect("/showallcontacts");
    }
    private void sendRedirect(String uri) {
        try {
            response.sendRedirect(uri);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    public void updateContact() {
        getSession();
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        Contact contact = new Contact();
        contact.setAccountid(accountID);
        contact.setEmail(email);
        contact.setUserid(userID);
        contact.setName(name);
        ContactManager contactManager=new ContactManager();
        int check = contactManager.updateContactinDB(contact);
        sendRedirect("/showallcontacts");
    }

    public void deleteContact() {
        getSession();
        String email = request.getParameter("id");
        Contact contact=new Contact();
        contact.setAccountid(accountID);
        contact.setEmail(email);
        contact.setUserid(userID);
        ContactManager contactManager=new ContactManager();
        int check = contactManager.deleteContactFromDB(contact);
        sendRedirect("/showallcontacts");
    }
}

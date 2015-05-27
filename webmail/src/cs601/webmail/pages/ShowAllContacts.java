package cs601.webmail.pages;

import cs601.webmail.misc.Contact;
import cs601.webmail.services.ContactService;
import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/**
 * Created by shreyarajani on 5/4/15.
 */
public class ShowAllContacts extends Page{
    public ShowAllContacts(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }
    ContactService contactService=new ContactService(request, response, "/showallcontacts");
    ArrayList<Contact> contacts = new ArrayList<>();

    @Override
    public ST body() {
        ST template = templates.getInstanceOf("showallcontacts");
        contacts = contactService.getAllContacts();
        template.add("contacts", contacts); //List<Email>
        return template;
    }

    @Override
    public ST getTitle() {
        return new ST("All Contacts");
    }
}

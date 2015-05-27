package cs601.webmail.pages;

import cs601.webmail.misc.Contact;
import cs601.webmail.services.ContactService;
import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/**
 * Created by shreyarajani on 4/29/15.
 */
public class Compose extends Page{
    ContactService contactService=new ContactService(request,response,"/compose");

    public Compose(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    public void verify() { }

    @Override
    public ST body() {
        ST template = templates.getInstanceOf("compose");
        ArrayList<Contact> contacts;
        contacts = contactService.getAllContacts();
        template.add("contacts", contacts);
        return template;
    }

    @Override
    public ST getTitle() {
        return new ST("Compose Email");
    }
}


package cs601.webmail.pages;

import cs601.webmail.misc.Contact;
import cs601.webmail.misc.Mail;
import cs601.webmail.services.ContactService;
import cs601.webmail.services.MailService;
import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/**
 * Created by shreyarajani on 4/28/15.
 */
public class Reply extends Page {

    public Reply(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }
    MailService mailService=new MailService(request, response, "/reply");
    ContactService contactService=new ContactService(request,response,"/reply");

    @Override
    public ST body() {
        ST template = templates.getInstanceOf("reply");
        Mail mail;
        mail = mailService.getEmail();
        String subject = mail.getSubject();
        subject = "RE: "+subject;
        mail.setSubject(subject);
        String body = mail.getBody();
        body = "\r\n\r\n------------Original Email------------" + "\r\n" + body;
        mail.setBody(body);
        template.add("email", mail); //1 email object that we are trying to view

        ArrayList<Contact> contacts;
        contacts = contactService.getAllContacts();
        template.add("contacts", contacts);
        return template;
    }

    @Override
    public ST getTitle() {
        return new ST("Reply");
    }
}

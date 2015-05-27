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
public class Forward extends Page{


    public Forward(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }
    MailService mailService=new MailService(request, response, "/forward");
    ContactService contactService=new ContactService(request,response,"/forward");

    public void verify() { }

    @Override
    public ST body() {
        ST template = templates.getInstanceOf("forward");
        System.out.println("Inside Forward.class ");
        Mail mail;
        mail = mailService.getEmail();
        String subject = mail.getSubject();
        subject = "FW: "+subject;
        mail.setSubject(subject);
        String body = mail.getBody();
        body = "\n\n------------Forwarded Email------------" + "\r\n" + body;
        mail.setBody(body);
        template.add("email", mail); //1 email object that we are trying to view
        ArrayList<Contact> contacts;
        contacts = contactService.getAllContacts();
        template.add("contacts", contacts);
        return template;
    }

    @Override
    public ST getTitle() {
        return new ST("Forward Email");
    }
}

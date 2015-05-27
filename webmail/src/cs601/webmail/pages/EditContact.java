package cs601.webmail.pages;

import cs601.webmail.misc.Contact;
import cs601.webmail.services.ContactService;
import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by shreyarajani on 5/5/15.
 */
public class EditContact extends Page{
    public EditContact(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }
    ContactService contactService=new ContactService(request, response, "/editcontact");


    @Override
    public ST body() {
        ST template = templates.getInstanceOf("editcontact");
        System.out.println("Inside Edit Contact.class ");
        Contact contact;
        String email = request.getParameter("id");
        contact = contactService.getContact(email);
        template.add("contact", contact); //1 email object that we are trying to view
        return template;
    }

    @Override
    public ST getTitle() {
        return new ST("Edit Contact");
    }
}

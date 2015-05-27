package cs601.webmail.pages;

import cs601.webmail.misc.Attachment;
import cs601.webmail.misc.Mail;
import cs601.webmail.services.MailService;
import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/**
 * Created by shreyarajani on 4/28/15.
 */
public class ViewEmail extends Page {

    public ViewEmail(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }
    MailService mailService=new MailService(request, response, "/viewemail");


    @Override
    public ST body() {
        Mail mail=new Mail();
        ArrayList<Attachment> attachments=new ArrayList<>();
        ST template = templates.getInstanceOf("viewemail");
        mail=mailService.getEmail();
        if(mail.isAttachment()) {
            attachments = mail.getAttachmentobject();
        }
        template.add("email", mail); //1 email object that we are trying to view
        template.add("attachments", attachments);
        return template;
    }

        @Override
        public ST getTitle() {
            return new ST("View Email");
        }
    }

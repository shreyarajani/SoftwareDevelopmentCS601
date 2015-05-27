package cs601.webmail.pages;

import cs601.webmail.services.MailService;
import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by shreyarajani on 5/4/15.
 */
public class SearchEmail extends Page{
    public SearchEmail(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }
    MailService mailService=new MailService(request, response, "/search_email");


    public void verify() { }

    @Override
    public ST body() {
        ST template = templates.getInstanceOf("searchemail");
        template.add("emails", mailService.searchMails()); //List<Email>
        return template;
    }

    @Override
    public ST getTitle() {
        return new ST("Search");
    }
}
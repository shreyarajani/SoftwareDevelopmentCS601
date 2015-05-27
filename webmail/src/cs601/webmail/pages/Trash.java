package cs601.webmail.pages;

import cs601.webmail.services.MailService;
import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/**
 * Created by shreyarajani on 5/2/15.
 */
public class Trash extends Page{
    public Trash(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }
    MailService mailService=new MailService(request, response, "/trash");

    public void verify() { }

    @Override
    public ST body() {
        ST template = templates.getInstanceOf("trash");
        String folder = "TRASH";

        String sortBy, p;
        int pageNo;

        sortBy = request.getParameter("fo");
        p = request.getParameter("page");
        if(p == null){
            pageNo = 1;
        } else{
            pageNo = Integer.parseInt(request.getParameter("page"));
        }
        if(sortBy == null){
            sortBy = "D-MAILDATE";
        }
        int count = mailService.getEmailCount(folder);
        ArrayList<Integer> pages = mailService.getNoOfPages(count);
        template.add("emails", mailService.getEmailList(folder, sortBy, pageNo)); //List<Email>
        template.add("pages", pages);
        return template;
    }

    @Override
    public ST getTitle() {
        return new ST("Trash");
    }
}

package cs601.webmail.pages;

import cs601.webmail.services.MailService;
import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/**
 * Created by shreyarajani on 5/3/15.
 */
public class ShowFolder extends Page{
    public ShowFolder(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }
    MailService mailService=new MailService(request, response, "/showfolder");
    public static String folder;

    public void verify() { }

    @Override
    public ST body() {
        folder = (request.getParameter("id")).toUpperCase();
        ST template = templates.getInstanceOf("showfolder");
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

        template.add("folder", folder);
        return template;

    }

    @Override
    public ST getTitle() {
        return new ST(folder.toUpperCase());
    }
}

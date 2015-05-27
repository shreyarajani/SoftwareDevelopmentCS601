package cs601.webmail.pages;

import cs601.webmail.services.UserService;
import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by shreyarajani on 4/28/15.
 */
public class SelectAccount extends Page{
    public SelectAccount(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }
    UserService userService=new UserService(request, response, "/selectaccount");

    public void verify() { }

    @Override
    public ST body() {
        ST template = templates.getInstanceOf("selectaccount");
        template.add("accounts", userService.checForAccount()); //List<Email>
        return template;

    }

    @Override
    public ST getTitle() {
        return new ST("Accounts");
    }
}
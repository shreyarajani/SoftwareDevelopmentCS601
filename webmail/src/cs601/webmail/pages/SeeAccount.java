package cs601.webmail.pages;

import cs601.webmail.services.AccountService;
import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by shreyarajani on 4/30/15.
 */
public class SeeAccount extends Page{
    public SeeAccount(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }
    AccountService accountService=new AccountService(request, response, "/seeaccount");

    public void verify() { }

    @Override
    public ST body() {
        ST template = templates.getInstanceOf("seeaccount");
        template.add("account", accountService.getAccount());
        return template;
    }

    @Override
    public ST getTitle() {
        return new ST("Account");
    }
}
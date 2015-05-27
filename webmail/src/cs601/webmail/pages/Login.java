package cs601.webmail.pages;

import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by shreyarajani on 4/17/15.
 */
public class Login extends Page {
    public Login(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    public void verify() { }

    @Override
    public ST body() {
        return templates.getInstanceOf("login");
    }

    @Override
    public ST getTitle() {
        return new ST("Login page");
    }
}

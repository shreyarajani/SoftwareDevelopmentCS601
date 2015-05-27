package cs601.webmail.pages;

import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by shreyarajani on 4/18/15.
 */
public class Register extends Page {
    public Register(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    public void verify() { }

    @Override
    public ST body() {
        return templates.getInstanceOf("register");
    }

    @Override
    public ST getTitle() {
        return new ST("Register");
    }

//    @Override public boolean mustBeLoggedIn() { return false; }
}

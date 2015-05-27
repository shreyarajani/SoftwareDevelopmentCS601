package cs601.webmail.pages;

import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by shreyarajani on 5/3/15.
 */
public class ChangePassword extends Page{
    public ChangePassword(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    @Override
    public ST body() {
        return templates.getInstanceOf("changepassword");
    }

    @Override
    public ST getTitle() {
        return new ST("Change Password");
    }
}

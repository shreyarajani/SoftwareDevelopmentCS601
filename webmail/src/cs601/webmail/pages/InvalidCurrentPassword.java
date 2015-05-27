package cs601.webmail.pages;

import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by shreyarajani on 5/3/15.
 */
public class InvalidCurrentPassword extends Page{
    public InvalidCurrentPassword(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    @Override
    public ST body() {
        return templates.getInstanceOf("invalidcurrentpassword");
    }

    @Override
    public ST getTitle() {
        return new ST("Invalid Password");
    }
}

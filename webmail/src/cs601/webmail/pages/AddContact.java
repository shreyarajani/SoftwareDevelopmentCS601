package cs601.webmail.pages;

import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by shreyarajani on 5/5/15.
 */
public class AddContact extends Page{
    public AddContact(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    @Override
    public ST body() {
        return templates.getInstanceOf("addcontact");
    }

    @Override
    public ST getTitle() {
        return new ST("Add Contact");
    }
}

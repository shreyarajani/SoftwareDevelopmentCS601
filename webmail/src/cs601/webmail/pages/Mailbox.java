package cs601.webmail.pages;

import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by shreyarajani on 4/26/15.
 */
public class Mailbox extends Page{
    public Mailbox(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    public void verify() { }

    @Override
    public ST body() {

        return templates.getInstanceOf("mailbox");
    }

    @Override
    public ST getTitle() {
        return new ST("Mailbox");
    }
}

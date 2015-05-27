package cs601.webmail.pages;

import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by shreyarajani on 4/19/15.
 */

public class Exists extends Page {
    public Exists(HttpServletRequest request, HttpServletResponse response) {
            super(request, response);
        }
    public void verify() { }

    @Override
    public ST body() {
            return templates.getInstanceOf("exists");
        }

    @Override
    public ST getTitle() {
        return new ST("Already Registered");
    }
}

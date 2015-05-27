package cs601.webmail.pages;

import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by shreyarajani on 5/12/15.
 */
public class ErrorPage extends Page {
    public ErrorPage(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    @Override
    public ST body() {
        return templates.getInstanceOf("error");
    }

    @Override
    public ST getTitle() {
        return new ST("Error");
    }
}

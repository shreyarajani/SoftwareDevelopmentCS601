package cs601.webmail.pages;

import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NewsPage extends Page {
    public NewsPage(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    @Override
    public ST body() {
        out.println("This is the news page");
        return null;
    }

    @Override
    public ST getTitle() {
        return null;
    }
}
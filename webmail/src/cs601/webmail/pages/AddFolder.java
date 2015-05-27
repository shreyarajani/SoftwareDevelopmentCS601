package cs601.webmail.pages;

import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by shreyarajani on 4/30/15.
 */
public class AddFolder extends Page{
    public AddFolder(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    @Override
    public ST body() {
        return templates.getInstanceOf("addfolder");
    }

    @Override
    public ST getTitle() {
        return new ST("Add Folder");
    }
}

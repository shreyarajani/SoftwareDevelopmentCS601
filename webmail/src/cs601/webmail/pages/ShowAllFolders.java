package cs601.webmail.pages;

import cs601.webmail.misc.Folder;
import cs601.webmail.services.FolderService;
import org.stringtemplate.v4.ST;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/**
 * Created by shreyarajani on 5/1/15.
 */
public class ShowAllFolders extends Page {
    public ShowAllFolders(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }
    FolderService folderService=new FolderService(request, response, "/showallfolders");
    ArrayList<Folder> folders = new ArrayList<>();

    @Override
    public ST body() {
        ST template = templates.getInstanceOf("showallfolders");
        folders = folderService.getAllFolders();
        template.add("folders", folders); //List<Email>
        return template;
    }

    @Override
    public ST getTitle() {
        return new ST("All Folders");
    }
}
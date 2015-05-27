package cs601.webmail.misc;

/**
 * Created by shreyarajani on 4/21/15.
 */
public class Folder {
    String userid;
    String accountid;
    String folder;

    public Folder(String folder, String userid, String accountid) {
        this.accountid = accountid;
        this.userid = userid;
        this.folder = folder;
    }

    public Folder(){}

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAccountid() {
        return accountid;
    }

    public void setAccountid(String accountid) {
        this.accountid = accountid;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }
}

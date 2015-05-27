package cs601.webmail.misc;

/**
 * Created by shreyarajani on 4/21/15.
 */
public class Attachment {

    String accountid;
    String userid;
    String mailID;
    String filename;
    String filepath;
    int size;

    public Attachment(String accountid, String userid, String mailid, String filename, String filepath, int size) {
        this.accountid = accountid;
        this.userid = userid;
        this.mailID = mailid;
        this.filename = filename;
        this.filepath=filepath;
        this.size = size;
    }

    public Attachment(){}

    public String getAccountid() {
        return accountid;
    }

    public void setAccountid(String accountid) {
        this.accountid = accountid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getMailID() {
        return mailID;
    }

    public void setMailID(String mailID) {
        this.mailID = mailID;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
}
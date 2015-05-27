package cs601.webmail.misc;

/**
 * Created by shreyarajani on 4/25/15.
 */
public class Account {
    private String account;
    private String password;
    private int popport;
    private String popserver;
    private int smtpport;
    private String smtpserver;
    private String userID;

    public Account(){}

    public Account(String userID, String account, String password, String popserver, int popport, String smtpserver, int smtpport) {
        this.account =account;
        this.password=password;
        this.popserver = popserver;
        this.popport = popport;
        this.smtpserver = smtpserver;
        this.smtpport = smtpport;
        this.userID=userID;

    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPopport() {
        return popport;
    }

    public void setPopport(int popport) {
        this.popport = popport;
    }

    public String getPopserver() {
        return popserver;
    }

    public void setPopserver(String popserver) {
        this.popserver = popserver;
    }

    public int getSmtpport() {
        return smtpport;
    }

    public void setSmtpport(int smtpport) {
        this.smtpport = smtpport;
    }

    public String getSmtpserver() {
        return smtpserver;
    }

    public void setSmtpserver(String smtpserver) {
        this.smtpserver = smtpserver;
    }
}

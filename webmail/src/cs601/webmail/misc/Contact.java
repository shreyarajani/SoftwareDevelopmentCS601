package cs601.webmail.misc;

/**
 * Created by shreyarajani on 5/4/15.
 */
public class Contact {
    String userid;
    String accountid;
    String name;
    String email;

    public Contact(String name, String email, String userid, String accountid){
        this.name = name;
        this.email = email;
        this.userid = userid;
        this.accountid = accountid;
    }

    public Contact(){}

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

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

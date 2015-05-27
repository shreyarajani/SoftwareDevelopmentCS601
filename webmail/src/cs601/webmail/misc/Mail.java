package cs601.webmail.misc;

import java.util.ArrayList;

/**
 * Created by shreyarajani on 4/21/15.
 */
public class Mail {


    private String mailid;
    private String from;
    private String to;
    private String cc;
    private String bcc;
    private String date;
    private String subject;
    private boolean attachment;
    private String folder;
    private int size;
    private String body;
    private Boolean outgoing;
    private int hasread;
    private String userID;
    private String accountID;
    private String maildisplay;
    private ArrayList<Attachment> attachmentobject;

    public Mail(String mailid, String from, String to, String date, String subject, String body, int size, boolean outgoing, int hasread, String cc, String bcc,  boolean attachment, String folder, String userID, String accountID, String maildisplay) {
        this.mailid = mailid;
        this.from = from;
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
        this.date = date;
        this.subject = subject;
        this.attachment = attachment;
        this.folder = folder;
        this.size = size;
        this.body = body;
        this.outgoing = outgoing;
        this.hasread = hasread;
        this.userID = userID;
        this.accountID = accountID;
        this.maildisplay=maildisplay;

    }

    public Mail(String mailid, String from, String to, String date, String subject, String body, int size, boolean outgoing, int hasread, String cc, String bcc,  boolean attachment, String folder, String userID, String accountID, String maildisplay, ArrayList<Attachment> attachmentobject) {
        this.mailid = mailid;
        this.from = from;
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
        this.date = date;
        this.subject = subject;
        this.attachment = attachment;
        this.folder = folder;
        this.size = size;
        this.body = body;
        this.outgoing = outgoing;
        this.hasread = hasread;
        this.userID = userID;
        this.accountID = accountID;
        this.maildisplay=maildisplay;
        this.attachmentobject=attachmentobject;

    }

    public Mail() {}

    public String getMailid() {
        return mailid;
    }

    public void setMailid(String mailid) {
        this.mailid = mailid;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean isAttachment() {
        return attachment;
    }

    public void setAttachment(boolean attachment) {
        this.attachment = attachment;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Boolean getOutgoing() {
        return outgoing;
    }

    public void setOutgoing(Boolean outgoing) {
        this.outgoing = outgoing;
    }

    public int getHasread() {
        return hasread;
    }

    public void setHasread(int hasread) {
        this.hasread = hasread;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }
    public String getMaildisplay() {
        return maildisplay;
    }

    public void setMaildisplay(String maildisplay) {
        this.maildisplay = maildisplay;
    }

    public ArrayList<Attachment> getAttachmentobject() {
        return attachmentobject;
    }

    public void setAttachmentobject(ArrayList<Attachment> attachmentobject) {
        this.attachmentobject = attachmentobject;
    }
}
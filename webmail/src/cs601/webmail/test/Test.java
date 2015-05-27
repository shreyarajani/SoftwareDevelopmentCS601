package cs601.webmail.test;

import cs601.webmail.protocols.SMTPwithAttachments;

/**
 * Created by shreyarajani on 5/11/15.
 */
public class Test {

    public static void main(String[] args) {
        // SMTP info
        String host = "smtp.gmail.com";
        String port = "587";
        String mailFrom = "usfca.cs601@gmail.com";
        String password = "usfcacs601";

        // message info


        // attachments
        String[] attachFiles = new String[1];
        attachFiles[0] = "attachmentUpload/shreya/usfca.cs601@gmail.com/SunMay10220610PDT2015/fff.txt";
//        attachFiles[1] = "e:/Test/Music.mp3";
//        attachFiles[2] = "e:/Test/Video.mp4";

        try {
            SMTPwithAttachments s=new SMTPwithAttachments();
           // s.withAttachments(host, mailFrom, password, attachFiles);
            System.out.println("Email sent.");
        } catch (Exception ex) {
            System.out.println("Could not send email.");
            ex.printStackTrace();
        }
    }
}

package cs601.webmail.protocols;

import cs601.webmail.misc.Attachment;
import cs601.webmail.misc.Mail;
import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

/**
 * Reference: http://www.codejava.net/java-ee/javamail/send-e-mail-with-attachment-in-java
 */

public class SMTPwithAttachments {
    static final Logger logger = Logger.getLogger(SMTPwithAttachments.class);

    public void withAttachments(String userName, String password, String host, Mail mail) {
        System.out.println("Inside new.....");

        String toAddress = mail.getTo();
        String subject = mail.getSubject();
        String message = mail.getBody();
        ArrayList<Attachment> attachments = mail.getAttachmentobject();
        String filePath;

        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.user", userName);
        properties.put("mail.password", password);

        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
        Session session = Session.getInstance(properties, auth);
        // creates a new e-mail message
        Message msg = new MimeMessage(session);

        try {
            msg.setFrom(new InternetAddress(userName));

            InternetAddress[] toAddresses = {new InternetAddress(toAddress)};
            msg.setRecipients(Message.RecipientType.TO, toAddresses);
            msg.setSubject(subject);
            msg.setSentDate(new Date());

            // creates message part
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(message, "text/html");

            // creates multi-part
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            // adds attachments
            for (Attachment a : attachments) {
                filePath = a.getFilepath();
                MimeBodyPart attachPart = new MimeBodyPart();
                try {
                    attachPart.attachFile(filePath);
                } catch (IOException e) {
                    logger.error(e);
                }
                multipart.addBodyPart(attachPart);
                //   }
            }
            // sets the multi-part as e-mail's content
            msg.setContent(multipart);
            // sends the e-mail
            Transport.send(msg);
        } catch (MessagingException e) {
            logger.error(e);
        }

    }
}
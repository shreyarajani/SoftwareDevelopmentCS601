package cs601.webmail.protocols;

/**
 * Created by shreyarajani on 4/14/15.
 * Reference: http://inetjava.sourceforge.net/lectures/part1_sockets/InetJava-1.8-Email-Examples.html
 */

import cs601.webmail.misc.Attachment;
import cs601.webmail.misc.Mail;
import cs601.webmail.services.SpamService;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

public class POP {
    static final Logger logger = Logger.getLogger(POP.class);
    private static final int TIMEOUT = 10 * 1000; //10 sec

    private String host;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private int port;
    private String email;
    private String password;
    int noOfMessages;

    public POP(String email, String password, String host, int port) throws IOException, MessagingException {
        this.host = host;
        this.port = port;
        this.email = email;
        this.password = password;
        init(host, port, email, password);
    }

    public void init(String host, int port, String userName, String password) throws IOException, MessagingException {
        makeConnection(host, port);
        sendUserName(userName);
        sendPassword(password);
    }

    public void makeConnection(String host, int port) throws IOException {
        Security.addProvider(
                new com.sun.net.ssl.internal.ssl.Provider());
        String response = "";
        try {
            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            socket = factory.createSocket(host, port);
            socket.setSoTimeout(TIMEOUT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            response = in.readLine();

        } catch (SocketTimeoutException e) {
            socket = new Socket(host, port);
            socket.setSoTimeout(TIMEOUT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            response = in.readLine();
        }
        checkForError(response);
    }

    public void sendUserName(String userName) throws IOException {
        String response = createCommand("USER " + userName);
    }

    public void sendPassword(String password) throws IOException {
        String response = createCommand("PASS " + password);
    }

    public void checkForError(String response) throws IOException {
        if (response.charAt(0) != '+') {
            throw new IOException(response);
        }
    }

    public void deleteAllEmail() throws IOException {
        for(int i=1; i<=noOfMessages; i++){
            String response = createCommand("DELE " + i);
        }
    }

    public int getMessageCount() throws IOException {
        String response = createCommand("STAT");
        String[] number = response.split(" ");
        noOfMessages = Integer.parseInt(number[1]);
        return noOfMessages;
    }

    public Mail readMessage(int n, String userID, ArrayList<String> uidl) throws IOException, MessagingException {
        String msg;
        msg = getMail(n);
        String mailID = uidl.get(n-1); //n starts with 1 and the array list index starts with 0
        return parseMail(msg, userID, mailID);
    }

    public String getMail(int n) throws IOException {
        String msg;
        String response = createCommand("RETR " + n);
        StringBuffer message = new StringBuffer();

        String line;
        while (!(line = in.readLine()).equalsIgnoreCase(".")) {
            message.append(line);
            message.append("\n");
        }

        message.append(".\n");
        msg = new String(message);
        return msg;
    }

    public Mail parseMail(String msg, String userID, String mailID) throws MessagingException, IOException {
        SpamService spam = new SpamService();
        InputStream is = new ByteArrayInputStream(msg.getBytes());
        Session s = Session.getDefaultInstance(new Properties());
        MimeMessage message = new MimeMessage(s, is);
        Mail mail = new Mail();
        ArrayList<Attachment> attachmentArrayList=new ArrayList<>();
        Boolean att=false;
        String subject = "", from = "", type = "", date = "", messageContent = "", messageTextContent = "", bccrecipients = "", ccrecipients = "", torecipients = "";

        subject = message.getSubject();
        if (subject == null || subject.equals("")) {
            subject = "(no subject)";
        }

        for (Address f : message.getFrom()) {
            from = from + "," + f.toString();
        }
        if (from.startsWith(",")) {
            from = from.replaceFirst(",", "");
        }

        Date d = message.getSentDate();
        if (d == null) {
            d = message.getReceivedDate();
            if (d == null) {
                d = new Date();
            }
        }
        date = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(d);
        int size = message.getSize();
        type = message.getContentType();

        Address[] bccRecipients = message.getRecipients(Message.RecipientType.BCC);
        if (bccRecipients != null) {
            for (Address bccrecipient : bccRecipients) {
                bccrecipients += "," + (bccrecipient.toString());
            }
        }
        Address[] ccRecipients = message.getRecipients(Message.RecipientType.CC);
        if (ccRecipients != null) {
            for (Address ccrecipient : ccRecipients) {
                ccrecipients += "," + (ccrecipient.toString());
            }
        }
        Address[] toRecipients = message.getRecipients(Message.RecipientType.TO);
        if (toRecipients != null) {
            for (Address torecipient : toRecipients) {
                torecipients += "," + (torecipient.toString());
            }
        }
        bccrecipients = bccrecipients.replace(",", "");
        ccrecipients = ccrecipients.replace(",", "");
        torecipients = torecipients.replace(",", "");

        if (!(type.contains("multipart"))) {
            Object object = message.getContent();
            if (object != null) {
                messageTextContent = object.toString();
            }
            if (type.contains("text/html")) {
                try {
                    Document doc = Jsoup.parse(messageContent);
                    Elements elements = doc.select("body");
                    elements.tagName("div");
                    messageContent = (doc.html());
                } catch (Exception e) {
                    logger.error(e);
                }
            }
        } else if (type.contains("multipart")) {
            Multipart multiPart = (Multipart) message.getContent();
            int count = multiPart.getCount();
            for (int i = 0; i < count; i++) {
                MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(i);
                if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                    String fileName = part.getFileName();
                    String dirName = "attachment" + "/" + userID + "/" + email + "/" + mailID;
                    File dir = new File(dirName);
                    dir.setExecutable(true, false);
                    dir.setReadable(true, false);
                    dir.setWritable(true, false);
                    if (!dir.exists() && !dir.mkdirs()) {
                        logger.error("Unable to create dir");
                        throw new IOException("Unable to create dir");
                    }
                    File attachment = new File(dir, fileName);
                    part.saveFile(attachment);
                    String attachmentPath = dirName + "/" + fileName;
                    Attachment a=new Attachment(email, userID, mailID, fileName, attachmentPath, (int)attachment.length());
                    attachmentArrayList.add(a);
                    att = true;
                    mail.setAttachmentobject(attachmentArrayList);
                } else {
                    Object inner = part.getContent();
                    if (inner instanceof MimeMultipart) {
                        MimeMultipart innerParts = (MimeMultipart) inner;
                        int numberOfInnerParts = innerParts.getCount();
                        for (int c = 0; c < numberOfInnerParts; c++) {
                            MimeBodyPart innerBody = (MimeBodyPart) innerParts.getBodyPart(c);

                            if (innerBody.getContentType().contains("text/html")) {
                                messageContent = innerBody.getContent().toString();
                                messageContent = parseContent(messageContent);
                            }
                        }
                    } else {
                        messageContent = part.getContent().toString();
                        messageContent = parseContent(messageContent);
                    }
                }
            }
        }
        String body = messageContent;
        if (body.equals("")) {
            body = messageTextContent;
        }
        mail.setBody(body);
        mail.setMailid(mailID);
        mail.setSize(size);
        mail.setOutgoing(false);
        mail.setAccountID(email);
        mail.setDate(date);
        mail.setUserID(userID);
        mail.setSubject(subject);
        mail.setFrom(from);
        mail.setAttachment(att);
        mail.setBcc(bccrecipients);
        mail.setCc(ccrecipients);
        mail.setTo(torecipients);
        mail.setHasread(0);
        mail.setFolder("INBOX");
//        if(att) {
//            mail.setAttachmentobject(attachmentArrayList);
//        }

        if (spam.checkForSpam(mail)) { //if true then mark it as spam
            mail.setFolder("SPAM");
        }

        return mail;
    }

    private String parseContent(String messageContent) {
        Document document = Jsoup.parse(messageContent);
        document.outputSettings(new Document.OutputSettings().prettyPrint(false));//makes html() preserve linebreaks and spacing
        document.select("br").append("\\n");
        document.select("p").prepend("\\n\\n");
        String str = document.html().replaceAll("\\\\n", "\n");
        messageContent = Jsoup.clean(str, "", Whitelist.none(), new Document.OutputSettings().prettyPrint(false));
        return messageContent;
    }

    public String createCommand(String command) throws IOException {
        out.println(command);
        out.flush();
        String response = in.readLine();
        checkForError(response);
        return response;
    }

    public void quit() throws IOException {
        String response = createCommand("QUIT");
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            logger.error(e);
        }
    }

    public ArrayList<Mail> getAllMails(String userID) {
        ArrayList<String> uidl = null;
        uidl = getUIDL();
        ArrayList<Mail> mails = new ArrayList<>();
        try {
            int n = getMessageCount();
            for (int i = 1; i <= n; i++) {
                mails.add(readMessage(i, userID, uidl));

            }
        } catch (IOException | MessagingException e) {
            logger.error(e);
        }
        return mails;
    }

    public ArrayList<String> getUIDL(){
        ArrayList<String> uidl = new ArrayList<>();
        try {
            String response = createCommand("UIDL");
            while (!(response = in.readLine()).equalsIgnoreCase(".")) {
                String[] a = response.split(" ");
                uidl.add(a[1]);
            }
        } catch (IOException e) {
            logger.error(e);
        }
        return uidl;
    }
}
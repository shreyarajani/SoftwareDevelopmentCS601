package cs601.webmail.test;

/**
 * Created by shreyarajani on 4/30/15.
 */

import cs601.webmail.managers.ErrorManager;
import cs601.webmail.misc.Mail;
import sun.misc.BASE64Encoder;

import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.Security;
import java.util.ArrayList;
import java.util.Date;

public class SMTPTest {

    private static final boolean DEBUG = true;
    private static final int TIMEOUT = 10 * 1000; //10 sec

    private Socket socket;
    private BufferedReader in;
    private DataOutputStream out;
    private String host;
    private int port;
    private String userName;
    private String password;


    public SMTPTest(String userName, String password, String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        this.userName = userName;
        this.password = password;
        init(host, port, userName, password);
    }

    public void init(String host, int port, String userName, String password) throws IOException {
        makeConnection(host, port);
        String response;

        createCommand("HELO "+ host);
        response = in.readLine();
        verifyResponse(response, "250");
        if (DEBUG) {
            System.out.println("Sending HELO...");
            System.out.println("Response: " + response);
        }

        createCommand("AUTH LOGIN");
        response = in.readLine();
        verifyResponse(response, "334");
        if (DEBUG) {
            System.out.println("Sending AUTH LOGIN...");
            System.out.println("Response: " + response);
        }


        createCommand(new BASE64Encoder().encode(userName.getBytes()));
        response = in.readLine();
        verifyResponse(response, "334");
        if (DEBUG) {
            System.out.println("Sending USERNAME...");
            System.out.println("Response: " + response);
        }

        createCommand(new BASE64Encoder().encode(password.getBytes()));
        response = in.readLine();
        verifyResponse(response, "235");
        if (DEBUG) {
            System.out.println("Sending PASSWORD...");
            System.out.println("Response: " + response);
        }
    }

    public void makeConnection(String host, int port) throws IOException {
        if (DEBUG)
            System.out.println("Connecting to SMTP server...");
        String response = "";
        try {
            Security.addProvider(
                    new com.sun.net.ssl.internal.ssl.Provider());
            SSLSocketFactory factory =
                    (SSLSocketFactory) SSLSocketFactory.getDefault();
            socket = factory.createSocket(host, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());
            response = in.readLine();

        } catch (SocketTimeoutException e) {
            ErrorManager.instance().error(getClass().getName(), e);
            socket = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());
            response = in.readLine();
        }
        if (DEBUG) {
            System.out.println("Connected to SMTP server.");
            System.out.println("Response: " + response);
        }
        verifyResponse(response, "220");
    }


    public void sendMail(Mail mail) throws IOException {
        String toEmail = mail.getTo();
        String subject = mail.getSubject();
        String cc = mail.getCc();
        String bcc = mail.getBcc();
        String body = mail.getBody();

        ArrayList<String> toList;
        ArrayList<String> ccList;
        ArrayList<String> bccList;

        toList = formatAddress(toEmail);
        ccList=formatAddress(cc);
        bccList=formatAddress(bcc);

        mailFrom();
        rcptTo(toList);
        rcptCc(ccList);
        rcptBcc(bccList);
        data();
        createHeaders(toEmail, subject, cc, bcc);
        setBody(body);
    }

    public void mailFrom() throws IOException {
        String response;
        createCommand("MAIL FROM: <" + userName + ">");
        response = in.readLine();
        verifyResponse(response, "250");
        if (DEBUG) {
            System.out.println("Sending MAIL FROM...");
            System.out.println(response);
        }
    }

    private void setBody(String body) {
        String response;
        try {
            createCommand(body);
            createCommand(".");
            response = in.readLine();
            verifyResponse(response, "250");
        } catch (IOException e) {
            ErrorManager.instance().error(getClass().getName(), e);
        }

    }

    public void data() throws IOException {
        String response;
        createCommand("DATA");
        response = in.readLine();
        verifyResponse(response, "354");
        if (DEBUG) {
            System.out.println("Sending DATA...");
            System.out.println(response);
        }
    }

    public void rcptTo(ArrayList<String> toList) throws IOException {
        String response;
        for (String toEmail : toList) {
            createCommand("RCPT TO: <" + toEmail + ">");
            response = in.readLine();
            verifyResponse(response, "250");
            if (DEBUG) {
                System.out.println("Sending RCPT TO...");
                System.out.println(response);
            }
        }
    }

    public void rcptCc(ArrayList<String> ccList) throws IOException {
        String response;
        for (String toEmail : ccList) {
            createCommand("RCPT TO: <" + toEmail + ">");
            response = in.readLine();
            verifyResponse(response, "250");
            if (DEBUG) {
                System.out.println("Sending RCPT TO...");
                System.out.println(response);
            }
        }
    }

    public void rcptBcc(ArrayList<String> bccList) throws IOException {
        String response;
        for (String toEmail : bccList) {
            createCommand("RCPT TO: <" + toEmail + ">");
            response = in.readLine();
            verifyResponse(response, "250");
            if (DEBUG) {
                System.out.println("Sending RCPT TO...");
                System.out.println(response);
            }
        }
    }

    private ArrayList<String> formatAddress(String addressData) {
        ArrayList<String> finalAddress = new ArrayList<>();
        String[] mailAddresses = addressData.split(",");
        for (String address : mailAddresses) {
            String[] mailAddress = address.split("<");
            String from = "";
            if (mailAddress.length == 1) {
                from = mailAddress[0] + ">";
            } else {
                from = mailAddress[1];
            }

            from = "<" + from;
            finalAddress.add(from);
        }
        return finalAddress;
    }



    public String createHeaders(String toEmail, String subject, String cc, String bcc) {
        String headers = "";
        headers = headers + "Date: " + new Date().toString() + "\n";
        headers = headers + "From: " + userName +"\n";
        headers = headers + "To: " + toEmail + "\n";
        headers = headers + "cc: " + cc + "\n";
        headers = headers + "bcc: " + bcc + "\n";
        headers = headers + "Subject: " + subject + "\n";
        return headers + "\n\n";
    }

    public void verifyResponse(String response, String expected) throws IOException {
        if (!response.startsWith(expected)) {
            throw new IOException(response);
        }
    }

    public void createCommand(String command) throws IOException {
        out.writeBytes(command + "\r\n");
        out.flush();
    }

    public void quit() throws IOException {
        createCommand("QUIT");
        verifyResponse(in.readLine(), "221");
        out.close();
        try {
            in.close();
            socket.close();
        } catch (IOException e) {
            ErrorManager.instance().error(getClass().getName(), e);
        }
    }

    public static void main(String[] args) throws IOException {
        String username = "usfca.cs601@gmail.com";
        String password = "usfcacs601";
        String host = "smtp.gmail.com";
        int port = 465;

        SMTPTest smtp = new SMTPTest(username, password, host, port);
    }
}
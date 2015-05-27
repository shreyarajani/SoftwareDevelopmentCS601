package cs601.webmail.protocols;

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

/**
 * Created by shreyarajani on 4/14/15.
 * Reference: http://inetjava.sourceforge.net/lectures/part1_sockets/InetJava-1.8-Email-Examples.html
 */

public class SMTP {

    private static final boolean DEBUG = true;

    private Socket socket;
    private BufferedReader in;
    private DataOutputStream out;
    private String host;
    private int port;
    private String userName;
    private String password;


    public SMTP(String userName, String password, String host, int port) throws IOException {
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
        System.out.println("Email: "+toEmail);
        System.out.println("Subject: " + subject);
        System.out.println("cc" + cc);
        System.out.println("bcc" + bcc);
        System.out.println("Body" + body);

        ArrayList<String> toList;
        ArrayList<String> ccList = null;
        ArrayList<String> bccList = null;

        toList = formatAddress(toEmail);
        if(!cc.equals("")) {
            ccList = formatAddress(cc);
        }
        if(!bcc.equals("")) {
            bccList = formatAddress(bcc);
        }

        System.out.println("toList" + toList);
        System.out.println("ccList" + ccList);
        System.out.println("bccList" + bccList);


        mailFrom();
        rcptTo(toList);
        if(!cc.equals("")) {
            ccList = formatAddress(cc);
            rcptCc(ccList);
        }
        if(!bcc.equals("")) {
            bccList = formatAddress(bcc);
            rcptBcc(bccList);
        }
        data();
        createHeaders(mail);
        setBody(body);
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
            createCommand("RCPT TO: " + toEmail);
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
            createCommand("RCPT TO: " + toEmail);
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
            createCommand("RCPT TO: " + toEmail);
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

    public void createHeaders(Mail mail) {
        String headers = "";
        headers = headers + "Subject: " + mail.getSubject() + "\r\n";
        headers = headers+ "To: " + mail.getTo() + "\r\n";
        headers = headers + "From: " + mail.getFrom() + "\r\n";
        headers = headers + "Sender: " + mail.getAccountID() + "\r\n";
        headers = headers + "cc: " + mail.getCc() + "\r\n";
        headers = headers + "Date: " + mail.getDate() + "\r\n";
        String encoding = "text/plain";
        headers += "Content-Type: " + encoding + "; charset=UTF-8" + "\r\n";
        try {
            createCommand(headers + "\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void verifyResponse(String response, String expected) throws IOException {
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
}
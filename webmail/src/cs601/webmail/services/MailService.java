package cs601.webmail.services;

import cs601.webmail.managers.MailManager;
import cs601.webmail.misc.Attachment;
import cs601.webmail.misc.Mail;
import cs601.webmail.protocols.POP;
import cs601.webmail.protocols.SMTP;
import cs601.webmail.protocols.SMTPwithAttachments;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by shreyarajani on 4/21/15.
 */

public class MailService {
    static final Logger logger = Logger.getLogger(MailService.class);

    HttpServletResponse response;
    HttpServletRequest request;
    String reqType;
    String email = "", password = "", popHost = "", smtpHost = "", userID = "";
    int popPort = 0, smtpPort = 0;
    ArrayList<Mail> mails;
    MailManager mailManager;
    Mail mail;

    public MailService(HttpServletRequest request, HttpServletResponse response, String reqType) {
        this.request = request;
        this.response = response;
        this.reqType = reqType;
    }

    public void checkMail() {
        getSession();

        mails = new ArrayList<>();
        POP pop = null;
        mailManager = new MailManager();
        try {
            pop = new POP(email, password, popHost, popPort);
            mails = pop.getAllMails(userID);
            for (Mail mail : mails) {
                mailManager.addMailToDB(mail);
            }
            pop.deleteAllEmail();
        } catch (IOException | MessagingException e) {
            logger.error(e);
        } finally {
            if (pop != null) {
                try {
                    pop.quit();
                } catch (IOException e) {
                    logger.error(e);
                }
            }
        }
        sendRedirect("/inbox");
    }

    public ArrayList<Mail> searchMails() {
        getSession();
        String tag = null, keyword = null;
        String search = request.getParameter("search");
        if (search.contains(":")) {
            int index = search.indexOf(':');
            tag = (search.substring(0, index)).toUpperCase();
            keyword = search.substring(index + 1, search.length());
        }
        if (tag != null) {
            if (tag.equals("FROM")) {
                tag = "FROMID";
            } else if (tag.equals("TO")) {
                tag = "TOID";
            }
        }

        mailManager = new MailManager();
        return mailManager.searchMailsInDB(userID, email, tag, keyword);
    }

    public void getSession() {
        HttpSession session = request.getSession();
        userID = session.getAttribute("SESSION_USER").toString();
        email = session.getAttribute("SESSION_ACCOUNT").toString();
        password = session.getAttribute("SESSION_PASSWORD").toString();
        popHost = session.getAttribute("SESSION_POP").toString();
        popPort = Integer.parseInt(session.getAttribute("SESSION_POPPORT").toString());
        smtpHost = session.getAttribute("SESSION_SMTP").toString();
        smtpPort = Integer.parseInt(session.getAttribute("SESSION_SMTPPORT").toString());
    }

    public ArrayList<Mail> getEmailList(String folder, String sortBy, int pageNo) {
        getSession();
        HttpSession session = request.getSession();
        session.setAttribute("SESSION_FOLDER", folder);

        ArrayList<Mail> mails;

        sortBy = sortBy.toUpperCase();

        mailManager = new MailManager();
        mails = mailManager.getMailsFromDB(userID, email, folder, sortBy, pageNo);
        return mails;
    }

    public Mail getEmail() {
        mail = new Mail();
        mailManager = new MailManager();
        String mailID = request.getParameter("id");
        mail = mailManager.getEmailfromDB(mailID);
        return mail;
    }

    public void sendMail() {
        mailManager = new MailManager();
        Attachment attachmentObj;
        getSession();
        Mail mail = new Mail();
        SMTP smtp = null;
        Date d = new Date();
        String date = d.toString();
        date = date.replaceAll(" ", "");
        date = date.replaceAll(":", "");
        String dateString = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(d);

        /**
         * Reference: http://stackoverflow.com/questions/19510656/how-to-upload-files-on-server-folder-using-jsp
         */
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        if (!isMultipart) {
            return;
        }

        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(1024 * 1024 * 2);
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setSizeMax(1024 * 1024);
        File attachment = null;
        String fileName = null;
        boolean att = false;
        String mailto = "", mailcc = "", mailbcc = "", mailsubject = "", mailbody = "";
        ArrayList<Attachment> attachments = new ArrayList<>();
        try {
            List items = upload.parseRequest(request);
            for (Object item1 : items) {
                FileItem item = (FileItem) item1;
                if (!item.isFormField()) {
                    fileName = new File(item.getName()).getName();
                    if (fileName != "") {
                        String dirName = "attachmentUpload" + "/" + userID + "/" + email + "/" + date;
                        File dir = new File(dirName);
                        dir.setExecutable(true, false);
                        dir.setReadable(true, false);
                        dir.setWritable(true, false);
                        if (!dir.exists() && !dir.mkdirs()) {
                            throw new IOException("Unable to create dir");
                        }
                        attachment = new File(dir, fileName);
                        item.write(attachment);
                        att = true;
                        attachmentObj = new Attachment();
                        attachmentObj.setFilepath(attachment.toString());
                        attachmentObj.setMailID(date);
                        attachmentObj.setAccountid(email);
                        attachmentObj.setUserid(userID);
                        attachmentObj.setFilename(fileName);
                        attachmentObj.setSize(fileName.length());
                        attachments.add(attachmentObj);
                    }

                } else {
                    if (item.getFieldName().equals("mailto")) {
                        mailto = item.getString();
                    }
                    if (item.getFieldName().equals("mailbcc")) {
                        mailbcc = item.getString();
                    }
                    if (item.getFieldName().equals("mailcc")) {
                        mailcc = item.getString();
                    }
                    if (item.getFieldName().equals("mailsubject")) {
                        mailsubject = item.getString();
                    }
                    if (item.getFieldName().equals("mailbody")) {
                        mailbody = item.getString();
                    }

                }
            }
        } catch (Exception ex) {
            try {
                throw new ServletException(ex);
            } catch (ServletException e) {
                logger.error(e);
            }
        }

        mail.setUserID(userID);
        mail.setAccountID(email);
        mail.setTo(mailto);
        mail.setFrom(email);
        mail.setFolder("DRAFTS");
        mail.setSubject(mailsubject);
        mail.setBody(mailbody);
        mail.setCc(mailcc);
        mail.setBcc(mailbcc);
        mail.setAttachment(att);
        mail.setHasread(1);
        mail.setSize(mail.getBody().length());
        mail.setDate(dateString);
        mail.setMailid(date);
        mail.setOutgoing(true);
        if (att) {
            mail.setAttachmentobject(attachments);
        }

        if (att) {
            try {
                SMTPwithAttachments s = new SMTPwithAttachments();
                s.withAttachments(email, password, smtpHost, mail);
                mail.setFolder("SENT");
                mailManager.addMailToDB(mail);
                sendRedirect("/inbox");
            } catch (IOException e) {
                logger.error(e);
            }
        } else {
            try {
                smtp = new SMTP(email, password, smtpHost, smtpPort);
                smtp.sendMail(mail);
                mail.setFolder("SENT");

            } catch (IOException e) {
                logger.error(e);
            } finally {
                if (smtp != null) {
                    try {
                        smtp.quit();
                        mailManager.addMailToDB(mail);
                        sendRedirect("/inbox");
                    } catch (IOException e) {
                        logger.error(e);
                    }
                }
            }
        }
    }

    public void toggleMail() {
        mailManager = new MailManager();
        String mailID = request.getParameter("id");
        String folder = (request.getParameter("f")).toLowerCase();
        mailManager.toggleMailinDB(mailID);
        if (folder.equals("inbox") || folder.equals("trash") || folder.equals("spam") || folder.equals("sent")) {
            sendRedirect("/" + folder);
            return;
        }
        sendRedirect("/showfolder");
    }

    private void sendRedirect(String uri) {
        try {
            response.sendRedirect(uri);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    public int getEmailCount(String folder) {
        getSession();
        mailManager = new MailManager();
        return mailManager.getEmailCountFromDB(userID, email, folder);
    }

    public ArrayList<Integer> getNoOfPages(int count) {
        int pageOffset = 10, noOfPages;
        ArrayList<Integer> noOfPagesList = new ArrayList<>();
        if (count % pageOffset == 0) {
            noOfPages = count / pageOffset;
        } else {
            noOfPages = (count / pageOffset) + 1;
        }
        for (int i = 1; i <= noOfPages; i++) {
            noOfPagesList.add(i);
        }

        return noOfPagesList;
    }
}
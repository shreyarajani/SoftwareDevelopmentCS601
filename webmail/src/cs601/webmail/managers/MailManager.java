package cs601.webmail.managers;

import cs601.webmail.misc.Attachment;
import cs601.webmail.misc.Folder;
import cs601.webmail.misc.Mail;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created by shreyarajani on 4/20/15.
 */
public class MailManager extends DBManager {
    static final Logger logger = Logger.getLogger(MailManager.class);
    private static boolean DEBUG = true;

    public void addMailToDB(Mail mail) throws IOException {
        ArrayList<String> contacts = new ArrayList<>();
        ArrayList<Attachment> attachmentArrayList = new ArrayList<>();
        boolean hasAttachment = false;

        PreparedStatement pstmt = null;
        try {
            String sql = DBConnectionManager.ADD_EMAILS;
            Connection con = DBConnectionManager.getConnection();
            con.setAutoCommit(false);
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, mail.getMailid());
            pstmt.setString(2, mail.getFrom());
            pstmt.setString(3, mail.getTo());
            pstmt.setString(4, mail.getDate());
            pstmt.setString(5, mail.getSubject());
            pstmt.setString(6, mail.getBody());
            pstmt.setInt(7, mail.getSize());//size
            pstmt.setBoolean(8, mail.getOutgoing());
            pstmt.setInt(9, mail.getHasread());
            pstmt.setString(10, mail.getCc());
            pstmt.setString(11, mail.getBcc());
            pstmt.setBoolean(12, mail.isAttachment());
            pstmt.setString(13, mail.getFolder());
            pstmt.setString(14, mail.getUserID());
            pstmt.setString(15, mail.getAccountID());
            pstmt.execute();
            pstmt.close();
            con.commit();
            con.setAutoCommit(true);
            hasAttachment = mail.isAttachment();
            if (hasAttachment) {
                attachmentArrayList = mail.getAttachmentobject();
            }
            if (mail.getBcc() != null) {
                contacts.add(mail.getBcc());
            } else if (mail.getCc() != null) {
                contacts.add(mail.getCc());
            }
            contacts.add(mail.getFrom());
            contacts.add(mail.getTo());
            ContactManager contactManager = new ContactManager();
            contactManager.parseContactToAddInDB(contacts, mail.getUserID(), mail.getAccountID());
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            if (pstmt != null) {
                try {
                    closeStatement();
                    AttachmentManager attachmentManager = new AttachmentManager();
                    for (Attachment a : attachmentArrayList) {
                        attachmentManager.addAttachmenttoDB(a);
                    }

                } catch (SQLException e) {
                    logger.error(e);
                }
            }
        }
    }

    public ArrayList<Mail> getMailsFromDB(String userID, String accountID, String folder, String sortBy, int pageNo) {
        int limit = 10;
        int offset = (pageNo * 10) - 10;
        ArrayList<Mail> mails = new ArrayList<>();
        String orderby = "DESC", field = "MAILDATE";
        Mail mail;
        boolean hasAttachment = false, isSent = false;
        Statement stmt;

        if (sortBy.contains("-")) {
            int index = sortBy.indexOf('-');
            orderby = sortBy.substring(0, index);
            field = sortBy.substring(index + 1, sortBy.length());
        }

        if (orderby.equals("A")) {
            orderby = "ASC";
        } else if (orderby.equals("D")) {
            orderby = "DESC";
        }

        switch (field) {
            case "FROM":
                field = "FROMID";
                break;
            case "SUBJECT":
                field = field.toUpperCase();
                break;
            case "DATE":
                field = "MAILDATE";
                break;
            case "SIZE":
                field = "MAILSIZE";
                break;
        }

        try {
            stmt = openTable(false);
            String query = "SELECT * FROM MAIL WHERE USERID = '" + userID + "' AND ACCOUNTID = '" + accountID + "' AND FOLDER = '" + folder + "' ORDER BY " + field + " " + orderby + " LIMIT " + offset + ", " + limit;
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String maildisplay = "<span class=\"glyphicon glyphicon-ok\"></span>";
                if (rs.getInt(9) == 0) { //unread
                    maildisplay = "<span class=\"glyphicon glyphicon-envelope\"></span>";
                }
                if (rs.getInt(8) == 1) {
                    isSent = true;
                }
                if (rs.getInt(12) == 1) {
                    hasAttachment = true;
                }
                mail = new Mail(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getInt(7), isSent, rs.getInt(9), rs.getString(10), rs.getString(11), hasAttachment, rs.getString(13), rs.getString(14), rs.getString(15), maildisplay);
                mails.add(mail);
            }

        } catch (SQLException e) {
            logger.error(e);
        } finally {
            try {
                closeStatement();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return mails;
    }

    public Mail getEmailfromDB(String mailID) {
        Mail mail = null;
        Statement stmt;
        boolean hasAttachment = false, isSent = false;

        try {
            stmt = openTable(false);
            String query = "SELECT * FROM MAIL WHERE MAILID = '" + mailID + "'";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                ArrayList<Attachment> attachments = null;
                if (rs.getInt(8) == 1) {
                    isSent = true;
                }
                if (rs.getInt(12) == 1) {
                    hasAttachment = true;
                    AttachmentManager aM = new AttachmentManager();
                    attachments = aM.getAllAttachments(mailID);
                }
                mail = new Mail(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getInt(7), isSent, rs.getInt(9), rs.getString(10), rs.getString(11), hasAttachment, rs.getString(13), rs.getString(14), rs.getString(15), "", attachments);
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            try {
                closeStatement();
                updateReadinDB(mailID);
            } catch (SQLException e) {
                logger.error(e);
            }
        }

        return mail;
    }

    private void updateReadinDB(String mailID) {
        PreparedStatement pstmt = null;
        Connection con = DBConnectionManager.getConnection();
        try {
            con.setAutoCommit(false);
            String sql = "UPDATE MAIL SET HASREAD = ? WHERE MAILID = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, 1);
            pstmt.setString(2, mailID);
            pstmt.executeUpdate();
            pstmt.close();
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            if (pstmt != null) {
                try {
                    closeStatement();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
        }
    }

    public void toggleMailinDB(String mailID) {
        Statement stmt;
        int value = 0, newValue = 0;

        try {
            stmt = openTable(false);
            String query = "SELECT * FROM MAIL WHERE MAILID = '" + mailID + "'";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                value = rs.getInt(9);
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            try {
                closeStatement();
            } catch (SQLException e) {
                logger.error(e);
            }
        }

        if (value == 0) {
            newValue = 1;
        }

        PreparedStatement pstmt = null;
        Connection con = DBConnectionManager.getConnection();
        try {
            con.setAutoCommit(false);
            String sql = "UPDATE MAIL SET HASREAD = ? WHERE MAILID = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, newValue);
            pstmt.setString(2, mailID);
            pstmt.executeUpdate();
            pstmt.close();
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            if (pstmt != null) {
                try {
                    closeStatement();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
        }
    }

    public int emptyTrashFromDB(String userid, String accountid) {
        String folder = "TRASH";
        int check = 0;
        PreparedStatement pstmt = null;
        Connection con = DBConnectionManager.getConnection();
        try {
            con.setAutoCommit(false);
            String sql = "DELETE FROM MAIL WHERE USERID = ? AND ACCOUNTID = ? AND FOLDER = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, userid);
            pstmt.setString(2, accountid);
            pstmt.setString(3, folder);
            check = pstmt.executeUpdate();
            pstmt.close();
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            if (pstmt != null) {
                try {
                    closeStatement();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
        }
        return check;
    }

    public ArrayList<Mail> searchMailsInDB(String userID, String email, String tag, String keyword) {
        Statement stmt;
        ArrayList<Mail> mails = new ArrayList<>();
        Mail mail;
        boolean hasAttachment = false, isSent = false;
        String query = "SELECT * FROM MAIL WHERE USERID = '" + userID + "' AND ACCOUNTID = '" + email + "' AND " + tag + " like '%" + keyword + "%'";
        try {
            stmt = openTable(false);
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String maildisplay = "<span class=\"glyphicon glyphicon-ok\"></span>";
                if (rs.getInt(9) == 0) { //unread
                    maildisplay = "<span class=\"glyphicon glyphicon-envelope\"></span>";
                }
                if (rs.getInt(8) == 1) {
                    isSent = true;
                }
                if (rs.getInt(12) == 1) {
                    hasAttachment = true;
                }
                mail = new Mail(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getInt(7), isSent, rs.getInt(9), rs.getString(10), rs.getString(11), hasAttachment, rs.getString(13), rs.getString(14), rs.getString(15), maildisplay);
                mails.add(mail);
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            try {
                closeStatement();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return mails;
    }

    public void deleteEmailFromDB(String mailid, String folder) {
        folder = folder.toUpperCase();
        PreparedStatement pstmt = null;
        Connection con = DBConnectionManager.getConnection();
        try {
            con.setAutoCommit(false);
            String sql = "DELETE FROM MAIL WHERE MAILID = ? AND FOLDER = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, mailid);
            pstmt.setString(2, folder);
            pstmt.executeUpdate();
            pstmt.close();
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            if (pstmt != null) {
                try {
                    closeStatement();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
        }
    }

    public int deleteEmailFromDBUsingFolder(Folder folder) {
        PreparedStatement pstmt = null;
        int check = 0;
        Connection con = DBConnectionManager.getConnection();
        try {
            con.setAutoCommit(false);
            String sql = "DELETE FROM MAIL WHERE ACCOUNTID = ? AND USERID = ? AND FOLDER = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, folder.getAccountid());
            pstmt.setString(2, folder.getUserid());
            pstmt.setString(3, folder.getFolder());
            check = pstmt.executeUpdate();
            pstmt.close();
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            if (pstmt != null) {
                try {
                    closeStatement();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
        }
        return check;
    }

    public int getEmailCountFromDB(String userID, String accountID, String folder) {
        Statement stmt = null;
        try {
            stmt = openTable(false);
            String query = "SELECT COUNT(MAILID) FROM MAIL WHERE FOLDER = '" + folder + "' AND USERID = '" + userID + "' AND ACCOUNTID = '" + accountID + "'";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            if (stmt != null) {
                try {
                    closeStatement();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
        }
        return 0;
    }
}

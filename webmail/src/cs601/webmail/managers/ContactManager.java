package cs601.webmail.managers;

import cs601.webmail.misc.Contact;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by shreyarajani on 5/4/15.
 */
public class ContactManager extends DBManager {
    static final Logger logger = Logger.getLogger(ContactManager.class);

    Contact contact;

    public void parseContactToAddInDB(ArrayList<String> contacts, String userid, String accountid) {
        String name = "", email = "";
        for (String c : contacts) {
            contact = new Contact();
            if (c.contains("<") || c.contains(">")) {
                int start = c.indexOf('<');
                int end = c.indexOf(">");
                name = c.substring(0, start);
                email = c.substring(start + 1, end);
                contact.setEmail(email);
                contact.setName(name);
                contact.setAccountid(accountid);
                contact.setUserid(userid);
                addContactToDB(contact);

            } else if (!(c.equals(""))) {
                contact.setEmail(c);
                contact.setUserid(userid);
                contact.setAccountid(accountid);
                contact.setName("");
                addContactToDB(contact);
            }

        }
    }

    public int addContactToDB(Contact contact) {
        int check = checkContactInDB(contact);
        if (check == 1) {
            return 1;
        }
        if (contact.getEmail() == null || contact.getEmail().equals("")) {
            return 0;
        }

        PreparedStatement pstmt = null;
        try {
            String sql = DBConnectionManager.INSERT_CONTACT;
            System.out.println(sql);
            Connection con = DBConnectionManager.getConnection();
            con.setAutoCommit(false);
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, contact.getName());
            pstmt.setString(2, contact.getEmail());
            pstmt.setString(3, contact.getUserid());
            pstmt.setString(4, contact.getAccountid());
            pstmt.execute();
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
        return 0;
    }

    private int checkContactInDB(Contact contact) {
        Statement stmt = null;
        try {
            stmt = openTable(false);
            String query = "SELECT * FROM CONTACT WHERE EMAIL = '" + contact.getEmail() + "' AND USERID = '" + contact.getUserid() + "' AND ACCOUNTID = '" + contact.getAccountid() + "'";

            ResultSet rs = stmt.executeQuery(query);
            if (!rs.next()) {
                return 0; // not present
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        return 1;
    }

    public ArrayList<Contact> getAllContactsFromDB(String userID, String accountID) {
        Statement stmt;
        ArrayList<Contact> contacts = new ArrayList<>();
        try {
            stmt = openTable(false);
            String query = "SELECT * FROM CONTACT WHERE USERID = '" + userID + "' AND ACCOUNTID = '" + accountID + "'";

            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                contact = new Contact();
                contact.setName(rs.getString(1));
                contact.setEmail(rs.getString(2));
                contact.setUserid(rs.getString(3));
                contact.setAccountid(rs.getString(4));
                contacts.add(contact);
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return contacts;
    }

    public Contact getContactFromDB(String userID, String accountID, String email) {
        Statement stmt;

        try {
            stmt = openTable(false);
            String query = "SELECT * FROM CONTACT WHERE USERID = '" + userID + "'" + "AND ACCOUNTID = '" + accountID + "' AND EMAIL = '" + email + "'";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                contact = new Contact(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
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

        return contact;
    }

    public int updateContactinDB(Contact contact) {
        PreparedStatement pstmt = null;
        Connection con = DBConnectionManager.getConnection();
        int update = 0;
        try {
            con.setAutoCommit(false);
            String sql = "UPDATE CONTACT SET CONTACTNAME = ? WHERE EMAIL = ? AND USERID = ? AND ACCOUNTID = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, contact.getName());
            pstmt.setString(2, contact.getEmail());
            pstmt.setString(3, contact.getUserid());
            pstmt.setString(4, contact.getAccountid());
            update = pstmt.executeUpdate();
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
        return update;
    }

    public int deleteContactFromDB(Contact contact) {
        PreparedStatement pstmt = null;
        Connection con = DBConnectionManager.getConnection();
        int update = 0;
        try {
            con.setAutoCommit(false);
            String sql = "DELETE FROM CONTACT WHERE EMAIL = ? AND USERID = ? AND ACCOUNTID = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, contact.getEmail());
            pstmt.setString(2, contact.getUserid());
            pstmt.setString(3, contact.getAccountid());
            update = pstmt.executeUpdate();
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
        return update;
    }
}

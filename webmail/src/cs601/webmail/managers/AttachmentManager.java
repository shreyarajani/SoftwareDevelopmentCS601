package cs601.webmail.managers;

import cs601.webmail.misc.Attachment;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by shreyarajani on 5/10/15.
 */
public class AttachmentManager extends DBManager {
    public void addAttachmenttoDB(Attachment a) {
        PreparedStatement pstmt = null;
        String sql = DBConnectionManager.INSERT_ATT;
        Connection con = DBConnectionManager.getConnection();
        try {
            con.setAutoCommit(false);
            pstmt = con.prepareStatement(sql);
            pstmt.setString(2, a.getFilename());
            pstmt.setInt(3, a.getSize());
            pstmt.setString(4, a.getFilepath());
            pstmt.setString(5, a.getMailID());
            pstmt.setString(6, a.getUserid());
            pstmt.setString(7, a.getAccountid());
            pstmt.execute();
            pstmt.close();
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pstmt != null) {
                try {
                    closeStatement();
                } catch (SQLException e) {
                    ErrorManager.instance().error(getClass().getName(), e);
                }
            }
        }
    }

    public ArrayList<Attachment> getAllAttachments(String mailID) {
        Statement stmt;
        Attachment a;
        ArrayList<Attachment> attachments = new ArrayList<>();
        try {
            stmt = openTable(false);
            String query = "SELECT * FROM ATTACHMENT WHERE MAILID = '" + mailID + "'";

            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                a = new Attachment();
                a.setFilename(rs.getString(2));
                a.setSize(rs.getInt(3));
                a.setFilepath(rs.getString(4));
                a.setMailID(mailID);
                attachments.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return attachments;
    }
}

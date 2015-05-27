package cs601.webmail.managers;

import cs601.webmail.misc.Folder;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by shreyarajani on 4/21/15.
 */
public class FolderManager extends DBManager {
    static final Logger logger = Logger.getLogger(FolderManager.class);

    public int addNewFolderToDB(Folder folder) {
        int present = checkFolderInDB(folder); //0 = not present

        if (present == 0) {
            PreparedStatement pstmt = null;
            try {
                Connection con = DBConnectionManager.getConnection();
                String sql = DBConnectionManager.ADD_FOLDER;
                pstmt = openTable(sql);
                pstmt.setString(1, folder.getFolder());
                pstmt.setString(2, folder.getUserid());
                pstmt.setString(3, folder.getAccountid());
                pstmt.addBatch();
                pstmt.executeBatch();
                con.commit();
                con.setAutoCommit(false);
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
        return present;
    }

    private int checkFolderInDB(Folder folder) {
        Statement stmt = null;
        try {
            stmt = openTable(false);
            String query = "SELECT * FROM FOLDER WHERE FOLDER = '" + folder.getFolder() + "' AND USERID = '" + folder.getUserid() + "' AND ACCOUNTID = '" + folder.getAccountid() + "'";

            ResultSet rs = stmt.executeQuery(query);
            if (!rs.next()) {
                return 0; // not present
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        return 1;
    }

    public ArrayList<Folder> getAllFOlderFromDB(String userid, String accountid) {
        Statement stmt;
        Folder folder;
        ArrayList<Folder> folderArrayList = new ArrayList<>();
        try {
            stmt = openTable(false);
            String query = "SELECT * FROM FOLDER WHERE USERID = '" + userid + "' AND ACCOUNTID = '" + accountid + "'";

            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                folder = new Folder();
                folder.setFolder(rs.getString(1));
                folder.setUserid(rs.getString(2));
                folder.setAccountid(rs.getString(3));
                folderArrayList.add(folder);
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return folderArrayList;
    }

    public void changeFolderinMailTable(String mailId, String folder) {
        folder = folder.toUpperCase();
        PreparedStatement pstmt = null;
        Connection con = DBConnectionManager.getConnection();
        try {
            con.setAutoCommit(false);
            String sql = "UPDATE MAIL SET FOLDER = ? WHERE MAILID = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, folder);
            pstmt.setString(2, mailId);
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


    public int deleteFolderFromDB(Folder folder) {
        String foldername = folder.getFolder().toUpperCase();
        int check = 0;
        PreparedStatement pstmt = null;
        Connection con = DBConnectionManager.getConnection();
        try {
            con.setAutoCommit(false);
            String sql = "DELETE FROM FOLDER WHERE ACCOUNTID = ? AND USERID = ? AND FOLDER = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, folder.getAccountid());
            pstmt.setString(2, folder.getUserid());
            pstmt.setString(3, foldername);
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
}

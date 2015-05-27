package cs601.webmail.managers;

import cs601.webmail.misc.DatabaseException;
import cs601.webmail.misc.User;
import cs601.webmail.protocols.AES;
import org.apache.log4j.Logger;

import java.sql.*;

/**
 * Created by shreyarajani on 4/18/15.
 */
public class UserManager extends DBManager {
    static final Logger logger = Logger.getLogger(UserManager.class);
    private static boolean DEBUG = true;

    public int addUserToDB(final User user) throws DatabaseException {
        boolean userList = checkDuplicateInDB(user.getEmail());

        if (!userList) { //false
            PreparedStatement pstmt = null;
            try {
                String sql = DBConnectionManager.REGISTER_USER;
                Connection con = DBConnectionManager.getConnection();
                con.setAutoCommit(false);
                pstmt = con.prepareStatement(sql);

                pstmt.setString(1, user.getEmail());
                String hashedpwd = AES.encrypt(user.getPassword());
                pstmt.setString(2, hashedpwd);
                pstmt.setString(3, user.getFirstName());
                pstmt.setString(4, user.getLastName());
                pstmt.execute();
                int id = 1;
                pstmt.close();
                con.commit();
                con.setAutoCommit(true);
                return id;
            } catch (SQLException e) {
                logger.error(e);
                throw new DatabaseException(DatabaseException.ErrorCode.INTERNAL_FAILURE);
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
        return 0;
    }

    public boolean checkDuplicateInDB(String userID) throws DatabaseException {

        Statement stmt = null;

        try {
            stmt = openTable(false);

            String select = "SELECT * FROM USER WHERE USERID =";

            if (userID != null) {
                select = select + " '" + userID + "'";
            }

            ResultSet rs = stmt.executeQuery(select);

            if (!rs.next()) {
                return false;
            } else {
                closeStatement();
                return true;
            }

        } catch (SQLException e) {
            logger.error(e);
            throw new DatabaseException(DatabaseException.ErrorCode.INTERNAL_FAILURE);
        } finally {
            if (stmt != null) {
                try {
                    closeStatement();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
        }
    }

    public boolean checkInDB(String userID, String password) throws DatabaseException {
        Statement stmt = null;
        try {
            stmt = openTable(false);
            String select = "SELECT * FROM USER ";
            String whereEmail = "WHERE USERID = ";
            String wherePassword = " AND PASSWORD = ";

            String hashedPassword = AES.encrypt(password);

            String q = select + whereEmail + "'" + userID + "'" + wherePassword + "'" + hashedPassword + "'";

            ResultSet rs = stmt.executeQuery(q);
            if (!rs.next()) {
                return false;

            } else {
                String e = rs.getString("USERID");
                String p = rs.getString("PASSWORD");
                return true;
            }

        } catch (SQLException e) {
            logger.error(e);
            throw new DatabaseException(DatabaseException.ErrorCode.INTERNAL_FAILURE);
        } finally {
            if (stmt != null) {
                try {
                    closeStatement();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
        }
    }

    public int updatePasswordinDB(String userid, String oldpwd, String newpwd) {
        PreparedStatement pstmt = null;
        Connection con = DBConnectionManager.getConnection();
        int update = 0;
        try {
            con.setAutoCommit(false);
            String sql = "UPDATE USER SET PASSWORD = ? WHERE USERID = ? AND PASSWORD = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, AES.encrypt(newpwd));
            pstmt.setString(2, userid);
            pstmt.setString(3, AES.encrypt(oldpwd));
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
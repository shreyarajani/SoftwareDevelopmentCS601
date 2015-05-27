package cs601.webmail.managers;

import cs601.webmail.misc.Account;
import cs601.webmail.protocols.AES;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by shreyarajani on 4/21/15.
 */
public class AccountManager extends DBManager {
    static final Logger logger = Logger.getLogger(MailManager.class);

    private static boolean DEBUG = true;
    Account account;

    public boolean checkAccountinDB(Account account) {

        Statement stmt;

        try {
            stmt = openTable(false);
            String select = "SELECT * FROM ACCOUNT WHERE ACCOUNTID = ";

            String email = account.getAccount();
            if (email != null) {
                select = select + "'" + email + "'";
            }

            ResultSet rs = stmt.executeQuery(select);

            if (!rs.next()) {
                return false;
            } else {
                closeStatement(); //data already present
                return true;
            }
        } catch (SQLException e) {
            logger.error(e);
        }

        return false;
    }

    public int addAccountinDB(Account account) {

        PreparedStatement pstmt = null;
        try {
            String sql = DBConnectionManager.REGISTER_ACCOUNT;
            System.out.println(sql);
            Connection con = DBConnectionManager.getConnection();
            con.setAutoCommit(false);
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, account.getAccount());
            String hashedpwd = AES.encrypt(account.getPassword());
            pstmt.setString(2, hashedpwd);
            pstmt.setString(3, account.getPopserver());
            pstmt.setInt(4, account.getPopport());
            pstmt.setString(5, account.getSmtpserver());
            pstmt.setInt(6, account.getSmtpport());
            pstmt.setString(7, account.getUserID());
            pstmt.execute();
            pstmt.close();
            con.commit();
            con.setAutoCommit(true);
            return 1;
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

    public ArrayList<Account> checkAccounts(String userid) {
        ArrayList<Account> accounts = new ArrayList<>();
        Statement stmt;

        try {
            stmt = openTable(false);

            String select = "SELECT * FROM ACCOUNT WHERE USERID = ";

            if (userid != null) {
                select = select + "'" + userid + "'";
            }

            ResultSet rs = stmt.executeQuery(select);
            while (rs.next()) {
                account = new Account(rs.getString(7), rs.getString(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getString(5), rs.getInt(6));
                account.setAccount(rs.getString(1));
                accounts.add(account);
            }
        } catch (SQLException e) {
            logger.error(e);
        }
        return accounts;
    }

    public Account getAccountfromDB(String accountID) {
        Account account = new Account();
        Statement stmt;

        try {
            stmt = openTable(false);
            String select = "SELECT * FROM ACCOUNT WHERE ACCOUNTID = ";

            if (accountID != null) {
                select = select + "'" + accountID + "'";
            }

            ResultSet rs = stmt.executeQuery(select);

            while (rs.next()) {
                account = new Account(rs.getString(7), rs.getString(1), AES.decrypt(rs.getString(2)), rs.getString(3), rs.getInt(4), rs.getString(5), rs.getInt(6));
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
        return account;
    }
}

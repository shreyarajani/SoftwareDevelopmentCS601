package cs601.webmail.test;

import java.sql.*;

public class ResultSetTest {
    public static void main(String args[]) throws SQLException, ClassNotFoundException {
        Connection c = null;
        Statement stmt = null;
        ResultSet rs = null;

        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:test.db");
        c.setAutoCommit(false);
        System.out.println("Opened database successfully");

        String USERTABLE = "CREATE TABLE IF NOT EXISTS USER " +
                "(USERID TEXT PRIMARY KEY," +
                " PASSWORD TEXT NOT NULL, " +
                " FNAME TEXT NOT NULL, " +
                " LNAME TEXT NOT NULL" + ")";

        String ACCOUNTTABLE = "CREATE TABLE IF NOT EXISTS ACCOUNT " +
                "(ACCOUNTID TEXT PRIMARY KEY," +
                " PASSWORD TEXT NOT NULL, " +
                " POP TEXT NOT NULL, " +
                " POPPORT INTEGER NOT NULL," +
                " SMTP TEXT NOT NULL," +
                " SMTPPORT INTEGER NOT NULL," +
                " USERID TEXT NOT NULL," +
                " FOREIGN KEY(USERID) REFERENCES USER(USERID)" + ")";


        String MAILTABLE = "CREATE TABLE IF NOT EXISTS MAIL " +
                "(MAILID TEXT PRIMARY KEY," +
                " FROMID    TEXT    NOT NULL, " +
                " TOID TEXT NOT NULL, " +
                " MAILDATE DATETIME NOT NULL, " +
                " SUBJECT TEXT, " +
                " BODY TEXT," +
                " MAILSIZE INTEGER DEFAULT 1, " +
                " ISSENT BOOLEAN NOT NULL, " +
                " HASREAD BOOLEAN DEFAULT 0, " +
                " CC BOOLEAN DEFAULT 0, " +
                " BCC BOOLEAN DEFAULT 0, " +
                " ATTACHMENT BOOLEAN DEFAULT 0, " +
                " FOLDER TEXT NOT NULL, " +
                " USERID TEXT NOT NULL," +
                " ACCOUNTID TEXT NOT NULL," +
                " FOREIGN KEY(FOLDER) REFERENCES FOLDER(FOLDER)" +
                " FOREIGN KEY(USERID) REFERENCES USER(USERID)" +
                " FOREIGN KEY(ACCOUNTID) REFERENCES ACCOUNT(ACCOUNTID)" + ")";

        String FOLDERTABLE = "CREATE TABLE IF NOT EXISTS FOLDER " +
                "(FOLDER TEXT PRIMARY KEY," +
                " USERID TEXT NOT NULL," +
                " ACCOUNTID TEXT NOT NULL," +
                " FOREIGN KEY(USERID) REFERENCES USER(USERID)" +
                " FOREIGN KEY(ACCOUNTID) REFERENCES ACCOUNT(ACCOUNTID)" + ")";

        stmt = c.createStatement();

        stmt.execute(USERTABLE);
        stmt.execute(ACCOUNTTABLE);
        stmt.execute(MAILTABLE);
        stmt.execute(FOLDERTABLE);
        c.setAutoCommit(true);
//        rs.close();
        stmt.close();
        c.close();

        System.out.println("Operation done successfully");
    }
}
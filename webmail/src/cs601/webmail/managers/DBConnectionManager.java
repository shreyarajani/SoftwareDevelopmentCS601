package cs601.webmail.managers;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by shreyarajani on 4/15/15.
 */
public class DBConnectionManager {
    static final Logger logger = Logger.getLogger(DBConnectionManager.class);
    public static Connection c = null;
    public static Statement stmt = null;
    public static String USERTABLE = "CREATE TABLE IF NOT EXISTS USER " +
            "(USERID TEXT PRIMARY KEY," +
            " PASSWORD TEXT NOT NULL, " +
            " FNAME TEXT NOT NULL, " +
            " LNAME TEXT NOT NULL" + ")";

    public static String MAILTABLE = "CREATE TABLE IF NOT EXISTS MAIL " +
            "(MAILID TEXT PRIMARY KEY," +
            " FROMID    TEXT    NOT NULL, " +
            " TOID TEXT NOT NULL, " +
            " MAILDATE DATETIME NOT NULL, " +
            " SUBJECT TEXT, " +
            " BODY TEXT," +
            " MAILSIZE INTEGER DEFAULT 1, " +
            " ISSENT BOOLEAN NOT NULL, " +
            " HASREAD BOOLEAN DEFAULT 0, " +
            " CC TEXT , " +
            " BCC TEXT, " +
            " ATTACHMENT BOOLEAN DEFAULT 0, " +
            " FOLDER TEXT NOT NULL, " +
            " USERID TEXT NOT NULL," +
            " ACCOUNTID TEXT NOT NULL," +
            " FOREIGN KEY(USERID) REFERENCES USER(USERID)" +
            " FOREIGN KEY(ACCOUNTID) REFERENCES ACCOUNT(ACCOUNTID)" + ")";

    public static String FOLDERTABLE = "CREATE TABLE IF NOT EXISTS FOLDER " +
            "(FOLDER TEXT ," +
            " USERID TEXT NOT NULL," +
            " ACCOUNTID TEXT NOT NULL," +
            " FOREIGN KEY(USERID) REFERENCES USER(USERID)" +
            " FOREIGN KEY(ACCOUNTID) REFERENCES ACCOUNT(ACCOUNTID)" + ")";

    public static String ATTACHMENTTABLE = "CREATE TABLE IF NOT EXISTS ATTACHMENT " +
            "(ATTID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " FILENAME TEXT NOT NULL, " +
            " ATTSIZE INTEGER DEFAULT 0, " +
            " FILEPATH TEXT," +
            " MAILID TEXT NOT NULL, " +
            " USERID TEXT NOT NULL, " +
            " ACCOUNTID TEXT NOT NULL, " +
            " FOREIGN KEY(MAILID) REFERENCES MAIL(MAILID)" +
            " FOREIGN KEY(USERID) REFERENCES USER(USERID)" +
            " FOREIGN KEY(ACCOUNTID) REFERENCES ACCOUNT(ACCOUNTID)" + ")";

    public static String CONTACTTABLE = "CREATE TABLE IF NOT EXISTS CONTACT " +
            "(CONTACTNAME TEXT ," +
            " EMAIL TEXT NOT NULL, " +
            " USERID TEXT NOT NULL," +
            " ACCOUNTID TEXT NOT NULL," +
            " FOREIGN KEY(USERID) REFERENCES USER(USERID)" +
            " FOREIGN KEY(ACCOUNTID) REFERENCES ACCOUNT(ACCOUNTID)" + ")";

    public static String ACCOUNTTABLE = "CREATE TABLE IF NOT EXISTS ACCOUNT " +
            "(ACCOUNTID TEXT PRIMARY KEY," +
            " PASSWORD TEXT NOT NULL, " +
            " POP TEXT NOT NULL, " +
            " POPPORT INTEGER NOT NULL," +
            " SMTP TEXT NOT NULL," +
            " SMTPPORT INTEGER NOT NULL," +
            " USERID TEXT NOT NULL," +
            " FOREIGN KEY(USERID) REFERENCES USER(USERID)" + ")";


    public static String REGISTER_USER = "INSERT INTO USER VALUES (?,?,?,?);";

    public static String REGISTER_ACCOUNT = "INSERT INTO ACCOUNT VALUES (?,?,?,?,?,?,?);";

    public static String ADD_EMAILS = "INSERT INTO MAIL VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

    public static String ADD_FOLDER = "INSERT INTO FOLDER VALUES (?,?,?);";

    public static String INSERT_CONTACT = "INSERT INTO CONTACT VALUES (?,?,?,?);";

    public static String INSERT_ATT = "INSERT INTO ATTACHMENT VALUES (?,?,?,?,?,?,?);";

    public static Connection getConnection() {
        try {
            if (c == null || c.isClosed()) {

                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:webmail.db");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return c;
    }

    private void makeConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:webmail.db");
        } catch (SQLException | ClassNotFoundException e) {
            ErrorManager.instance().error(getClass().getName(), e);
        }

        logger.info("Opened database successfully");
    }

    public static void closeConnection() throws SQLException {
        c.close();
        System.out.println("Closed database successfully");
    }

    public void createTables() throws SQLException {
        if (stmt == null || stmt.isClosed()) {
            stmt = c.createStatement();
        }

        stmt.executeUpdate(USERTABLE);
        stmt.executeUpdate(MAILTABLE);
        stmt.executeUpdate(FOLDERTABLE);
        stmt.executeUpdate(ACCOUNTTABLE);
        stmt.executeUpdate(CONTACTTABLE);
        stmt.executeUpdate(ATTACHMENTTABLE);
        stmt.close();
    }

    public void init() throws SQLException {
        makeConnection();
        createTables();
    }
}
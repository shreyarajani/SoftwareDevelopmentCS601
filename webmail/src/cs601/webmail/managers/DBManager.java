package cs601.webmail.managers;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by shreyarajani on 4/18/15.
 */
public abstract class DBManager {
    private PreparedStatement pStmt = null;
    private Statement stmt = null;
    private boolean shouldCloseOnCommit = true;

    public void disableAutoCommit() throws SQLException {
        DBConnectionManager.getConnection().setAutoCommit(false);
    }

    public void closeStatement() throws SQLException {

        if (this.pStmt != null) {
            this.pStmt.close();
            this.pStmt = null;
        }
        if (stmt != null) {
            this.stmt.close();
            this.stmt = null;
        }
        DBConnectionManager.getConnection().close();
    }

    public PreparedStatement openTable(String sql) throws SQLException {
        return openTable(sql, false, true);
    }

    public PreparedStatement openTable(String sql, boolean isAutoCommit, boolean shouldCloseOnCommit) throws SQLException {
        this.shouldCloseOnCommit = shouldCloseOnCommit;
        if (this.pStmt == null || this.pStmt.isClosed()) {
            this.pStmt = DBConnectionManager.getConnection().prepareStatement(sql);
        }
        if (!isAutoCommit) {
            disableAutoCommit();
        }
        return this.pStmt;
    }

    public Statement openTable(boolean isAutoCommit) throws SQLException {
        if (this.stmt == null || this.stmt.isClosed()) {
            this.stmt = DBConnectionManager.getConnection().createStatement();
        }
        if (!isAutoCommit) {
            disableAutoCommit();
        }
        return this.stmt;
    }
}
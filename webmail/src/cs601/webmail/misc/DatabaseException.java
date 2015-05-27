package cs601.webmail.misc;

/**
 * Created by shreyarajani on 4/18/15.
 */
public class DatabaseException extends Exception {

    public enum ErrorCode {
        INTERNAL_FAILURE,
        DUPLICATE,
        FAILURE
    }

    public ErrorCode code;

    public DatabaseException(ErrorCode code) {
        this.code = code;
    }

    public String getMessage() {
        switch (this.code) {
            case FAILURE:
                return "Failure!";
            case DUPLICATE:
                return "Duplicate!";
            case INTERNAL_FAILURE:
                return "Database error :(";
            default:
                return "Error! Try again later. Thank you!";
        }
    }
}
package cs601.webmail.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by shreyarajani on 5/10/15.
 */
public class SessionService {
    public void invalidateSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    public void removeAccountSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            session.removeAttribute("SESSION_ACCOUNT");
        }
    }

    public boolean isLoggedIn(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        return session.getAttribute("SESSION_USER") != null;
    }

    public boolean checkAccountSession(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        return (session.getAttribute("SESSION_USER") != null && session.getAttribute("SESSION_ACCOUNT") != null);
    }
}

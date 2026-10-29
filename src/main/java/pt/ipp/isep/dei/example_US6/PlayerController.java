package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.user.SessionManager;
import pt.ipp.isep.dei.user.User;

/**
 * Controller that manages the current game player session.
 */
public class PlayerController {

    private final SessionManager sessionManager = new SessionManager();

    /**
     * Returns the currently logged-in player, or null if nobody is logged in.
     */
    public User getCurrentPlayer() {
        return sessionManager.getCurrentUser();
    }

    /**
     * Ends the current player session (logs out).
     */
    public void logoutPlayer() {
        sessionManager.endSession();
    }
}

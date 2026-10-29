package pt.ipp.isep.dei.repository;

import pt.ipp.isep.dei.user.User;

/**
 * Singleton holding the current application user.
 */
public class ApplicationSession {
    private static ApplicationSession instance;
    private User currentUser;

    private ApplicationSession() { }

    public static synchronized ApplicationSession getInstance() {
        if (instance == null) {
            instance = new ApplicationSession();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
}

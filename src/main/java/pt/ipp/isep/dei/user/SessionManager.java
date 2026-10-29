package pt.ipp.isep.dei.user;

import pt.ipp.isep.dei.repository.ApplicationSession;
import java.util.Objects;

public class SessionManager {
    public void startSession(User user) {
        Objects.requireNonNull(user, "User must not be null");
        ApplicationSession.getInstance().setCurrentUser(user);
    }
    public void endSession() {
        ApplicationSession.getInstance().setCurrentUser(null);
    }
    public User getCurrentUser() {
        return ApplicationSession.getInstance().getCurrentUser();
    }
}

package pt.ipp.isep.dei.user;

public class UserSession {
    private User user;

    /**
     * Creates a UserSession for the given user.
     *
     * @param user the user associated with this session
     */
    public UserSession(User user) {
        this.user = user;
    }

    /**
     * Retrieves the username associated with this session.
     *
     * @return the username string
     */
    public String getUsername() {
        return user.getUsername();
    }
}


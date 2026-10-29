package pt.ipp.isep.dei.user;

import java.util.Objects;
import pt.ipp.isep.dei.ui.console.utils.CredentialManager;

public class User {
    private final String username;
    private final String id;
    private final String password;

    public User(String username, String password) {
        Objects.requireNonNull(username, "Username must not be null");
        if (username.isBlank()) {
            throw new IllegalArgumentException("Username must not be blank");
        }
        if (!CredentialManager.isValidPassword(password)) {
            throw new IllegalArgumentException(
                    "Password must be 7 alphanumeric chars, ≥3 uppercase, ≥2 digits"
            );
        }
        this.username = username;
        this.id = CredentialManager.generateUserId();
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getId()       { return id; }
    public String getPassword() { return password; }
}

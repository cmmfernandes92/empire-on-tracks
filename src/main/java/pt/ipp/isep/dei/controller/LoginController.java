package pt.ipp.isep.dei.controller;

import java.util.Objects;
import pt.ipp.isep.dei.repository.AuthenticationRepository;
import pt.ipp.isep.dei.user.User;

/**
 * Controller for user registration and login flows.
 */
public class LoginController {

    private final AuthenticationRepository repo = new AuthenticationRepository();

    /**
     * Registers a new user with a username and password.
     *
     * @param username the chosen username; must not be null or empty
     * @param password the chosen password; must satisfy the password policy
     * @return the created User
     * @throws NullPointerException     if username or password are null
     * @throws IllegalArgumentException if username is empty or password is invalid
     */
    public User register(String username, String password) {
        Objects.requireNonNull(username, "Username must not be null");
        Objects.requireNonNull(password, "Password must not be null");
        return repo.registerUser(username, password);
    }

    /**
     * Attempts to authenticate an existing user.
     *
     * @param id       the ID returned upon registration
     * @param password the provided password
     * @return the authenticated User, or null if credentials are invalid
     */
    public User login(String id, String password) {
        if (id == null || password == null) {
            return null;
        }
        return repo.authenticate(id, password);
    }
}

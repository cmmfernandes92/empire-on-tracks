package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.repository.AuthenticationRepository;
import pt.ipp.isep.dei.user.User;
import java.util.Objects;

public class AuthenticationController {
    private final AuthenticationRepository repo = new AuthenticationRepository();

    public User register(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
        return repo.registerUser(username, password);
    }

    public User login(String id, String password) {
        return repo.authenticate(id, password);
    }
}

package pt.ipp.isep.dei.repository;

import pt.ipp.isep.dei.user.User;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AuthenticationRepository {

    private final Map<String,User> users = new HashMap<>();

    public User registerUser(String username, String password) {
        Objects.requireNonNull(username, "Username must not be null");
        Objects.requireNonNull(password, "Password must not be null");
        User user = new User(username, password);
        users.put(user.getId(), user);
        return user;
    }

    public User authenticate(String id, String password) {
        if (id == null || password == null) return null;
        User u = users.get(id);
        return (u != null && u.getPassword().equals(password)) ? u : null;
    }
}

package pt.ipp.isep.dei.ui.console.authorization;

import pt.ipp.isep.dei.controller.AuthenticationController;
import pt.ipp.isep.dei.user.SessionManager;
import pt.ipp.isep.dei.user.User;
import pt.ipp.isep.dei.ui.console.utils.Console;

/**
 * Console-based user interface for authentication.
 * <p>
 * Provides an interactive menu for user registration and login,
 * managing user sessions via the console.
 */
public class AuthenticationUI implements Runnable {
    private final AuthenticationController ctrl = new AuthenticationController();
    private final SessionManager sessionMgr = new SessionManager();

    @Override
    public void run() {
        Console.println("\n--- AUTH MENU ---");
        Console.println("1) Register");
        Console.println("2) Login");
        String choice = Console.readLine("Select: ");
        if ("1".equals(choice)) handleRegister();
        else if ("2".equals(choice)) handleLogin();
        else Console.println("Exiting.");
    }

    private void handleRegister() {
        String user = Console.readLine("Username: ");
        String pass = Console.readLine("Password: ");
        try {
            User u = ctrl.register(user, pass);
            Console.println("Registered: ID=" + u.getId());
        } catch (Exception e) {
            Console.println("Fail: " + e.getMessage());
        }
    }

    private void handleLogin() {
        String id   = Console.readLine("ID: ");
        String pass = Console.readLine("Password: ");
        User u = ctrl.login(id, pass);
        if (u != null) {
            sessionMgr.startSession(u);
            Console.println("Welcome, " + u.getUsername());
        } else {
            Console.println("Invalid credentials.");
        }
    }
}

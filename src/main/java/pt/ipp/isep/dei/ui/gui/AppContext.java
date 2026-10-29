package pt.ipp.isep.dei.ui.gui;

import pt.ipp.isep.dei.controller.LoginController;
import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.user.User;

public class AppContext {

    // Singleton instance
    private static final AppContext instance = new AppContext();

    public static AppContext getInstance() {
        return instance;
    }

    // Estado global da aplicação
    private Map currentMap;
    private User currentUser; // ⬅️ novo campo para login

    // Controladores reutilizáveis
    public static final LoginController LOGIN_CTRL = new LoginController();

    // Getter e Setter para o mapa atual
    public Map getCurrentMap() {
        return currentMap;
    }

    public void setCurrentMap(Map map) {
        this.currentMap = map;
    }

    // ===== Login State =====
    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}



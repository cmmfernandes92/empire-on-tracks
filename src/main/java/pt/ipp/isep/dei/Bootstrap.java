package pt.ipp.isep.dei;

import pt.ipp.isep.dei.controller.AuthenticationController;
import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.repository.MapRepository;
import pt.ipp.isep.dei.repository.Repositories;
import pt.ipp.isep.dei.Serializer.MapSerializer;
import pt.ipp.isep.dei.user.User;

import java.io.File;

/**
 * Bootstraps application data: regista o jogador e carrega mapas salvos.
 */
public class Bootstrap implements Runnable {

    private final AuthenticationController authCtrl = new AuthenticationController();

    // Mesmo diretório usado no EditorMenuUI
    private static final String SAVE_DIR = "src/main/MapSave";

    @Override
    public void run() {
        addPlayerUser();
        loadSavedMaps();  // carrega tudo antes de arrancar o menu
    }

    private void addPlayerUser() {
        String username = "player1";
        String password = "ABC12XY"; // 7 chars: 3 uppercase, 2 digits
        User player = authCtrl.register(username, password);
        System.out.printf("Bootstrapped player → Username=%s%n", player.getUsername());
    }

    private void loadSavedMaps() {
        MapRepository repo = Repositories.getInstance().getMapRepository();
        File dir = new File(SAVE_DIR);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        // desserializa todos os .dat e adiciona com addMap()
        MapSerializer.loadAll(SAVE_DIR)
                .forEach(repo::addMap);
    }
}

package pt.ipp.isep.dei.ui.console.menu;

import pt.ipp.isep.dei.controller.CreateMapController;
import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.repository.Repositories;
import pt.ipp.isep.dei.repository.MapRepository;
import pt.ipp.isep.dei.ui.console.AddCityUI;
import pt.ipp.isep.dei.ui.console.AddIndustryUI;
import pt.ipp.isep.dei.ui.console.CreateScenarioUI;
import pt.ipp.isep.dei.ui.console.CreateMapUI;
import pt.ipp.isep.dei.ui.console.menu.PlayerMenuUI;
import pt.ipp.isep.dei.ui.console.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Menu de edição de mapas, com opção de guardar (serializar) usando o próprio nome do mapa.
 */
public class EditorMenuUI implements Runnable {

    // Diretório relativo do projeto onde os saves são gravados
    private static final String SAVE_DIR = "src/main/MapSave";

    @Override
    public void run() {
        List<MenuItem> options = new ArrayList<>();
        // Opções principais
        options.add(new MenuItem("UC1: Create a New Map", this::createNewMap));
        options.add(new MenuItem("Edit Existing Map", this::editExistingMap));
        options.add(new MenuItem("Switch to Player Menu", new PlayerMenuUI()));

        int selection;
        do {
            selection = Utils.showAndSelectIndex(options, "\n--- EDITOR MENU ------------------------");
            if (selection >= 0 && selection < options.size()) {
                options.get(selection).run();
            }
        } while (selection != -1);
    }

    /**
     * Inicia o fluxo de criação de um novo mapa.
     */
    private void createNewMap() {
        new CreateMapUI(new CreateMapController()).run();
    }

    /**
     * Exibe lista de mapas existentes e seleciona para edição.
     */
    private void editExistingMap() {
        MapRepository repo = Repositories.getInstance().getMapRepository();
        List<String> mapNames = repo.getMapsNames();

        if (mapNames.isEmpty()) {
            System.out.println("No maps available. Please create a map first.");
            return;
        }

        int selected = Utils.showAndSelectIndex(mapNames, "Select a map to edit:");
        if (selected == -1) {
            return;
        }

        String selectedMapName = mapNames.get(selected);
        Map currentMap = repo.getDetails(selectedMapName);
        repo.setCurrentMap(currentMap);
        System.out.println("Now editing: " + selectedMapName);

        showMapEditSubmenu();
    }

    /**
     * Mostra as opções de edição do mapa atual, incluindo salvar.
     */
    private void showMapEditSubmenu() {
        MapRepository repo = Repositories.getInstance().getMapRepository();
        List<MenuItem> editOptions = new ArrayList<>();

        editOptions.add(new MenuItem("UC2: Add an Industry", new AddIndustryUI(Repositories.getInstance(), repo.getCurrentMap())));
        editOptions.add(new MenuItem("UC3: Add a City", new AddCityUI(repo.getCurrentMap())));
        editOptions.add(new MenuItem("UC4: Create a Scenario", new CreateScenarioUI(repo.getCurrentMap())));
        editOptions.add(new MenuItem("Save Current Map (Serialize)", this::saveCurrentMap));
        editOptions.add(new MenuItem("Back to Editor Menu", this));

        int choice;
        do {
            choice = Utils.showAndSelectIndex(editOptions, "--- MAP EDIT MENU ------------------------");
            if (choice >= 0 && choice < editOptions.size()) {
                editOptions.get(choice).run();
            }
        } while (choice != -1);
    }

    /**
     * Serializa o mapa atual na pasta SAVE_DIR, usando o nome do mapa.
     */
    private void saveCurrentMap() {
        MapRepository repo = Repositories.getInstance().getMapRepository();
        Map currentMap = repo.getCurrentMap();

        // Garante existência do diretório de saves
        File dir = new File(SAVE_DIR);
        if (!dir.exists() && !dir.mkdirs()) {
            System.err.println("Não foi possível criar diretório de saves: " + SAVE_DIR);
            return;
        }

        // Usa o nome do mapa como nome de ficheiro e sanitiza
        String rawName = currentMap.getName();
        if (rawName == null || rawName.trim().isEmpty()) {
            System.err.println("O mapa não tem nome válido para salvar.");
            return;
        }
        String fileName = rawName.trim().replaceAll("[^a-zA-Z0-9_-]", "_");
        String path = SAVE_DIR + File.separator + fileName + ".dat";

        // Serialização Java
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(currentMap);
            System.out.println("Mapa '" + rawName + "' salvo em: " + path);
        } catch (IOException e) {
            System.err.println("Erro ao salvar o mapa: " + e.getMessage());
        }
    }
}

package pt.ipp.isep.dei.ui.console.menu;

import pt.ipp.isep.dei.controller.AssignTrainToRouteController;
import pt.ipp.isep.dei.controller.SimulatorController;
import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.repository.*;
import pt.ipp.isep.dei.ui.console.SimulatorUI;
import pt.ipp.isep.dei.ui.console.utils.Utils;
import pt.ipp.isep.dei.user.User;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerMenuUI implements Runnable {

    @Override
    public void run() {
        List<MenuItem> options = new ArrayList<>();

        options.add(new MenuItem("Select a Map & Start Simulation", this::selectMapAndLaunchSimulator));
        options.add(new MenuItem("Load Saved Simulation", this::loadSimulation));

        options.add(new MenuItem("Switch to Editor Menu", new EditorMenuUI()));

        int selection;
        do {
            selection = Utils.showAndSelectIndex(
                    options,
                    "\n--- PLAYER MENU ------------------------"
            );
            if (selection >= 0 && selection < options.size()) {
                options.get(selection).run();
            }
        } while (selection != -1);
    }

    private void selectMapAndLaunchSimulator() {
        User currentUser = ApplicationSession.getInstance().getCurrentUser();
        if (currentUser == null) {
            System.out.println("No user is currently logged in. Please log in first.");
            return;
        }

        // Cria o Player com dados do User (pode ajustar para ler atributos do User)
        Player player = new Player(5_000_000);

        // 1. Escolhe o mapa
        MapRepository mapRepo = Repositories.getInstance().getMapRepository();
        List<Map> maps = mapRepo.getAllMaps();
        if (maps.isEmpty()) {
            System.out.println("No maps available. Create one first.");
            return;
        }
        int mapIndex = Utils.showAndSelectIndex(
                maps.stream().map(Map::getName).collect(Collectors.toList()),
                "Select a map to simulate:"
        );
        if (mapIndex < 0) return;

        Map selectedMap = maps.get(mapIndex);
        mapRepo.setCurrentMap(selectedMap);

        // 2. Escolhe o cenário
        List<Scenario> scenarios = selectedMap.getScenarios();
        if (scenarios.isEmpty()) {
            System.out.println("No scenarios defined for this map.");
            return;
        }
        int scenIndex = Utils.showAndSelectIndex(
                scenarios.stream().map(Scenario::getName).collect(Collectors.toList()),
                "Select a scenario to run:"
        );
        if (scenIndex < 0) return;

        Scenario selectedScenario = scenarios.get(scenIndex);
        System.out.println("Scenario '" + selectedScenario.getName() + "' selected.");

        // 3. Datas de início e fim definidas pelo cenário
        LocalDateTime start = LocalDateTime.of(selectedScenario.getPeriod().getStartDate(), 1, 1, 0, 0);
        LocalDateTime end   = LocalDateTime.of(selectedScenario.getPeriod().getEndDate(), 12, 31, 23, 59);

        // 4. Dependências
        IndustryRepository indRepo = IndustryRepository.getInstance();
        LocomotiveRepository locoRepo = LocomotiveRepository.getInstance();

        // 5. Cria o relógio
        Time clock = new Time(start, end);

        // 6. Controladores
        SimulatorController simCtrl = new SimulatorController(
                selectedMap, clock, indRepo, locoRepo, player,selectedScenario
        );

        AssignTrainToRouteController routeCtrl = new AssignTrainToRouteController(
                player,
                selectedMap,
                selectedMap.getStations(),
                List.of(CargoType.values())
        );

        // 7. Inicia simulação automática (intervalo em segundos)
        simCtrl.startSimulation(30L);

        // 8. Lança a UI de simulação
        new SimulatorUI(simCtrl, player, routeCtrl).run();

        // 9. Termina o simulador
        simCtrl.shutdown();
    }
    // método de carregamento (substitui teu loadSimulation)
    private void loadSimulation() {
        File dir = new File(SAVE_DIR);
        String[] files = dir.list((d, f) -> f.endsWith(".sim"));
        if (files == null || files.length == 0) {
            System.out.println("Não há simulações guardadas em " + SAVE_DIR);
            return;
        }
        int idx = Utils.showAndSelectIndex(Arrays.asList(files), "Selecione a simulação:");
        if (idx < 0) return;
        String path = SAVE_DIR + File.separator + files[idx];
        SimulationSnapshot snap = SimulatorController.loadFromFile(path);

        // Reconstrói o estado da simulação
        Map map = snap.getMap();
        Time clock = snap.getClock();
        Player player = snap.getPlayer();
        Scenario scen = snap.getScenario();
        player.setOwnedTrains(snap.getTrains());

        SimulatorController ctl = new SimulatorController(
                map, clock,
                IndustryRepository.getInstance(),
                LocomotiveRepository.getInstance(),
                player,scen
        );
        ctl.setSelectedScenario(scen);

        AssignTrainToRouteController routeCtrl = new AssignTrainToRouteController(
                player, map, map.getStations(), List.of(CargoType.values())
        );

        new SimulatorUI(ctl, player, routeCtrl).run();
    }


    // no início da classe, após os imports:
    public static final String SAVE_DIR =
            System.getProperty("user.dir")
                    + File.separator
                    + "src" + File.separator
                    + "main" + File.separator
                    + "saved-simulations";


}
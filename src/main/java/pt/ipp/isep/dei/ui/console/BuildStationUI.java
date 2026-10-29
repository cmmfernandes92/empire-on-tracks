package pt.ipp.isep.dei.ui.console;


import pt.ipp.isep.dei.controller.BuildStationController;
import pt.ipp.isep.dei.domain.City;
import pt.ipp.isep.dei.domain.CityManager;
import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.domain.Station;
import pt.ipp.isep.dei.domain.StationType;
import pt.ipp.isep.dei.repository.Repositories;

import java.util.List;
import java.util.Scanner;

public class BuildStationUI implements Runnable {
    private final Scanner scanner = new Scanner(System.in);
    private final BuildStationController controller;
    private final Map map;

    public BuildStationUI() {
        this.map = Repositories.getInstance().getMapRepository().getCurrentMap();
        CityManager cityManager = new CityManager(map.getCities());
        this.controller = new BuildStationController(map, cityManager);
    }

    @Override
    public void run() {
        System.out.println("Total number of cities loaded: " + map.getCities().size());
        for (City c : map.getCities()) {
            System.out.println(" - " + c.getName() + " @ " + c.getCenter());
        }

        System.out.println("=== UC5: Build a Station ===");

        // 1. Seleção do tipo de estação
        StationType[] types = StationType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.printf("  %d - %s%n", i + 1, types[i]);
        }

        System.out.print("Select station type: ");
        int selected = Integer.parseInt(scanner.nextLine().trim()) - 1;
        if (selected < 0 || selected >= types.length) {
            System.out.println("❌ Invalid selection.");
            return;
        }

        // 2. Se for Station, escolher a direção de ancoragem
        String anchorDirection = "CENTER";
        if (types[selected] == StationType.Station_4x4) {
            System.out.println("Select anchor direction:");
            System.out.println("1 - NE (top-right)");
            System.out.println("2 - SE (bottom-right)");
            System.out.println("3 - SW (bottom-left)");
            System.out.println("4 - NW (top-left)");
            int anchorChoice = Integer.parseInt(scanner.nextLine().trim());
            anchorDirection = switch (anchorChoice) {
                case 1 -> "NE";
                case 2 -> "SE";
                case 3 -> "SW";
                case 4 -> "NW";
                default -> {
                    System.out.println("Invalid anchor selection. Using default CENTER.");
                    yield "CENTER";
                }
            };
        }

        // 3. Solicitar as coordenadas X e Y
        System.out.print("Enter X coordinate: ");
        int x = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Enter Y coordinate: ");
        int y = Integer.parseInt(scanner.nextLine().trim());

        // 4. Caso haja cidades, sugerir nome
        String suggestedName = null;
        City closestCity = controller.getClosestCity(x, y);
        if (closestCity != null) {
            System.out.println("Closest city detected: " + closestCity.getName());
            suggestedName = closestCity.getName() + " " + types[selected].getDisplayName();
            System.out.print("Do you want to name the station '" + suggestedName + "'? (yes/no): ");
            String answer = scanner.nextLine().trim().toLowerCase();
            if (answer.startsWith("y")) {
                // Aceitar o nome sugerido
                System.out.println("Station name accepted: " + suggestedName);
            } else {
                // Caso contrário, solicitar o nome customizado
                suggestedName = askForCustomName();
            }
        } else {
            System.out.println("No nearby cities found.");
            suggestedName = askForCustomName();
        }

        // 5. Chama o Controller para criar a estação e verificar se a localização está ocupada
        boolean result = controller.buildStation(suggestedName, x, y, types[selected], anchorDirection);

        // 6. Exibe a mensagem correta
        if (result) {
            System.out.println("✅ Station built successfully!");
        } else {
            System.out.println("❌ Error: Failed to build station.");
        }

        // 7. Exibe as estações no mapa
        System.out.println("\nCurrent stations on map:");
        for (Station s : map.getStations()) {
            System.out.print("- " + s.getStationName());
            if (s.getCityName() != null) {
                System.out.print(" (nearest city: " + s.getCityName() + ")");
            }
            System.out.println();
        }
    }

    private String askForCustomName() {
        System.out.print("Enter your desired station name: ");
        return scanner.nextLine().trim();
    }
}

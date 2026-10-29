package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.City;
import pt.ipp.isep.dei.domain.CityManager;
import pt.ipp.isep.dei.domain.Location;
import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.domain.Station;
import pt.ipp.isep.dei.domain.StationType;

import java.util.List;
import java.util.Objects;

public class BuildStationController {

    private final Map map;
    private final CityManager cityManager;

    public BuildStationController(Map map, CityManager cityManager) {
        this.map = Objects.requireNonNull(map);
        this.cityManager = Objects.requireNonNull(cityManager);
    }

    // Verifica se a localização está ocupada e, se não estiver, cria a estação
    public boolean buildStation(String name, int x, int y, StationType type, String orientation) {
        if (name == null || name.isBlank()) {
            return false; // Nome inválido
        }

        Location location = new Location(x, y);

        // Verifica se a localização já está ocupada
        if (map.isLocationOccupied(location)) {
            System.out.println("❌ Overbuild is not allowed: A station already exists at this location.");
            return false; // Local ocupado, não permite criar a estação
        }

        // Criação do ID da estação
        String id = "ST" + map.getStations().size();

        // Criar a estação com o novo construtor
        Station station = new Station(id, name, x, y, type, orientation);

        // Tenta adicionar a estação ao mapa
        map.addStation(station);

        return true; // Estação criada com sucesso
    }

    /**
     * Exposed for UI: devolve a cidade mais próxima via CityManager
     */
    public City getClosestCity(int x, int y) {
        return cityManager.getClosestCity(x, y);
    }

    /**
     * Finds the closest station to the center of the given city.
     */
    public Station getClosestStationToCity(City city) {
        List<Station> stations = map.getStations();
        if (stations.isEmpty()) {
            return null;
        }
        Station closest = null;
        double bestDist = Double.MAX_VALUE;
        Location cityCenter = city.getCenter();
        for (Station s : stations) {
            Location sp = s.getLocation();
            int dx = sp.getX() - cityCenter.getX();
            int dy = sp.getY() - cityCenter.getY();
            double dist = Math.sqrt(dx * dx + dy * dy);
            if (dist < bestDist) {
                bestDist = dist;
                closest = s;
            }
        }
        return closest;
    }
}


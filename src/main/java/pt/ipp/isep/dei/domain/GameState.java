package pt.ipp.isep.dei.domain;
import java.io.Serializable;
import pt.ipp.isep.dei.domain.ScenarioEvent;

public class GameState implements Serializable {
    private static GameState instance;

    private GameState() {
        // private constructor
    }

    public static GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }

    public void save() {
        System.out.println("[GameState] Game saved successfully.");
    }
}

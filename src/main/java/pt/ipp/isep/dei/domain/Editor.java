package pt.ipp.isep.dei.domain;

import pt.ipp.isep.dei.user.User;
import java.io.Serializable;
import pt.ipp.isep.dei.domain.ScenarioEvent;

public class Editor extends User implements Serializable {
    public Editor(String username, String password) {
        super(username, password);  // Call the constructor of the base class
    }

    // Additional methods specific to Editor can be added here
}

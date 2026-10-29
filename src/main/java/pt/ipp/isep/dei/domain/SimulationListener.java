
package pt.ipp.isep.dei.domain;

import java.io.Serializable;
import java.util.List;

@FunctionalInterface
public interface SimulationListener extends Serializable {

    void onTick(List<String> events);
}

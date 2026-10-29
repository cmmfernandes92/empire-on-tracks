package pt.ipp.isep.dei.domain;

import java.util.List;
import java.util.Objects;
import java.io.Serializable;
import pt.ipp.isep.dei.domain.ScenarioEvent;

/**
 * Scenario event that adds a new locomotive to the map as a template instance.
 * <p>
 * The locomotive is initially created with an empty route.
 */
public class AddTemplateLocomotiveEvent implements ScenarioEvent, Serializable {
    private final Locomotive template;

        public AddTemplateLocomotiveEvent(Locomotive template) {
            this.template = template;
        }

        @Override
        public void apply(Map map, LocomotiveCatalog catalog) {
            Locomotive clone = new Locomotive(
                    template.getId(),
                    template.getType(),
                    template.getPrice(),
                    template.requiresElectrification(),
                    template.getPower(),
                    template.getAcceleration(),
                    template.getTopSpeed(),
                    template.getStartYear(),
                    template.getFuelCostPerKm(),
                    template.getMaintenancePerYear()
            );
            catalog.addLocomotive(clone);
        }
    }
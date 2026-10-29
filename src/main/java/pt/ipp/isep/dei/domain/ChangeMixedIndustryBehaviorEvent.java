package pt.ipp.isep.dei.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Event that changes the behavior modes of a MixedIndustry on the map.
 * <p>
 * When applied, it updates the industry to one of IMPORT, EXPORT, or TRANSFORM modes,
 * adjusting input/output cargo types and quantities accordingly.
 * </p>
 *
 * @author Team
 * @version 1.0
 */
public class ChangeMixedIndustryBehaviorEvent implements ScenarioEvent, Serializable {

    private final int industryIndex;
    private final MixedBehavior newBehavior;
    private final CargoType inputCargo;
    private final CargoType outputCargo;

    /**
     * Constructs a ChangeMixedIndustryBehaviorEvent.
     *
     * @param industryIndex zero-based index of the MixedIndustry in the map's industry list
     * @param newBehavior   desired behavior mode (IMPORT, EXPORT, or TRANSFORM)
     * @param inputCargo    cargo type to use as input; may be null if not used
     * @param outputCargo   cargo type to use as output; may be null if not used
     */
    public ChangeMixedIndustryBehaviorEvent(int industryIndex,
                                            MixedBehavior newBehavior,
                                            CargoType inputCargo,
                                            CargoType outputCargo) {
        this.industryIndex = industryIndex;
        this.newBehavior = newBehavior;
        this.inputCargo = inputCargo;
        this.outputCargo = outputCargo;
    }

    /**
     * Applies the behavior change to the targeted MixedIndustry on the map.
     * <p>
     * If the industry index is invalid or the industry is not a MixedIndustry,
     * the event has no effect. For each behavior:
     * <ul>
     *   <li><b>IMPORT</b>: sets outputCargo, clears inputCargo and its quantity;</li>
     *   <li><b>EXPORT</b>: sets inputCargo, clears outputCargo;</li>
     *   <li><b>TRANSFORM</b>: sets both inputCargo and outputCargo;</li>
     * </ul>
     * </p>
     *
     * @param map     target simulation map containing industries
     * @param catalog locomotive catalog (unused by this event)
     */
    @Override
    public void apply(Map map, LocomotiveCatalog catalog) {
        List<Industry> industries = map.getIndustries();
        if (industryIndex < 0 || industryIndex >= industries.size()) {
            return;
        }

        Industry ind = industries.get(industryIndex);
        if (ind instanceof MixedIndustry mixed) {
            mixed.setBehavior(newBehavior);

            switch (newBehavior) {
                case IMPORT -> {
                    mixed.setOutputCargo(outputCargo);
                    mixed.setInputCargo(null);
                    mixed.setInputCargoQuantity(0.0);
                }
                case EXPORT -> {
                    mixed.setInputCargo(inputCargo);
                    mixed.setOutputCargo(null);
                }
                case TRANSFORM -> {
                    mixed.setInputCargo(inputCargo);
                    mixed.setOutputCargo(outputCargo);
                }
            }

            System.out.printf(
                    "MixedIndustry '%s' updated to %s (input: %s, output: %s)%n",
                    mixed.getIndustryName(), newBehavior,
                    inputCargo != null ? inputCargo : "None",
                    outputCargo != null ? outputCargo : "None"
            );
        }
    }
}

package pt.ipp.isep.dei.domain;

import java.io.Serializable;

/**
 * Event that changes the cargo types of an industry on the map as part of a scenario.
 * <p>
 * When applied, it updates the input and/or output cargo types of the specified industry.
 * </p>
 *
 * @author Team
 * @version 1.0
 */
public class ChangeIndustryCargoEvent implements ScenarioEvent, Serializable {

    private final int industryIndex;
    private final CargoType newInput;
    private final CargoType newOutput;

    /**
     * Constructs a ChangeIndustryCargoEvent.
     *
     * @param industryIndex zero-based index of the industry in the map's industry list
     * @param newInput      the new input cargo type; if {@code null}, input remains unchanged
     * @param newOutput     the new output cargo type; if {@code null}, output remains unchanged
     */
    public ChangeIndustryCargoEvent(int industryIndex,
                                    CargoType newInput,
                                    CargoType newOutput) {
        this.industryIndex = industryIndex;
        this.newInput = newInput;
        this.newOutput = newOutput;
    }

    /**
     * Applies the cargo type change to the targeted industry on the map.
     * <p>
     * If the industry index is out of bounds, the event has no effect.
     * Only industries of type {@link TransformingIndustry}, {@link MixedIndustry},
     * or {@link PrimaryIndustry} are updated.
     * </p>
     *
     * @param map         target simulation map containing industries
     * @param locomotives catalog of locomotives (unused by this event)
     */
    @Override
    public void apply(Map map, LocomotiveCatalog locomotives) {
        Industry industry;
        try {
            industry = map.getIndustries().get(industryIndex);
        } catch (IndexOutOfBoundsException e) {
            return;
        }

        if (industry instanceof TransformingIndustry ti) {
            if (newInput != null) ti.setInputCargo(newInput);
            if (newOutput != null) ti.setOutputCargo(newOutput);

        } else if (industry instanceof MixedIndustry mi) {
            if (newInput != null) mi.setInputCargo(newInput);
            if (newOutput != null) mi.setOutputCargo(newOutput);

        } else if (industry instanceof PrimaryIndustry pi) {
            if (newOutput != null) pi.setCargoType(newOutput);
        }
        // Other industry types are unaffected
    }
}

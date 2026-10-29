package pt.ipp.isep.dei.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Event that adjusts an industry's production rate by a specified percentage.
 * <p>
 * When applied, it increases or decreases the industry's current production
 * according to the provided percentage change.</p>
 *
 * @author Team
 * @version 1.0
 */
public class ChangeIndustryProductionRateEvent implements ScenarioEvent, Serializable {

    private final int industryIndex;
    private final double percentageChange;

    /**
     * Constructs a ChangeIndustryProductionRateEvent.
     *
     * @param industryIndex    zero-based index of the industry in the map's industry list
     * @param percentageChange percentage change to apply (e.g., 20 for +20%, -10 for -10%)
     */
    public ChangeIndustryProductionRateEvent(int industryIndex, double percentageChange) {
        this.industryIndex = industryIndex;
        this.percentageChange = percentageChange;
    }

    /**
     * Applies the production rate change to the targeted industry on the map.
     * <p>
     * If the industry index is out of range, the event has no effect.
     * Only industries of type PrimaryIndustry, TransformingIndustry, or MixedIndustry
     * will have their production updated.</p>
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

        Industry industry = industries.get(industryIndex);
        double currentRate = industry.getProduction();
        double newRate = currentRate * (1 + percentageChange / 100.0);

        if (industry instanceof PrimaryIndustry pi) {
            pi.setProduction(newRate);
        } else if (industry instanceof TransformingIndustry ti) {
            ti.setProduction(newRate);
        } else if (industry instanceof MixedIndustry mi) {
            mi.setProduction(newRate);
        }
    }
}
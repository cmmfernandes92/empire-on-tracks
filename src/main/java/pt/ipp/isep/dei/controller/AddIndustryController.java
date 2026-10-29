package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.mapper.dto.IndustryDTO;
import pt.ipp.isep.dei.mapper.IndustryMapper;
import pt.ipp.isep.dei.repository.Repositories;

import java.util.List;

/**
 * Controller for the “Add Industry” use-case.
 *
 * <p>Responsibilities:</p>
 * <ul>
 *   <li>Provide the UI with the list of industry templates
 *       (converted to {@link IndustryDTO});</li>
 *   <li>Validate the intended map coordinates;</li>
 *   <li>Create a concrete industry instance based on the selected
 *       template and user-supplied coordinates;</li>
 *   <li>Ask the {@link Map} to place the new industry.</li>
 * </ul>
 *
 * <p>It holds references to the shared {@link Repositories} aggregate
 * and to the {@link Map} that receives the new industry.</p>
 *
 * @author Team
 * @version 1.0
 */
public class AddIndustryController {

    private final Repositories repos;
    private final Map map;

    /**
     * Constructs the controller.
     *
     * @param repos shared repositories aggregate
     * @param map   target simulation map
     */
    public AddIndustryController(Repositories repos, Map map) {
        this.repos = repos;
        this.map   = map;
    }

    /* ------------------------------------------------------------------
     * 1. Expose industry templates as DTOs (UI → Controller)
     * ---------------------------------------------------------------- */
    /**
     * Retrieves all available industry templates as DTOs.
     *
     * @return list of {@link IndustryDTO} for presentation
     */
    public List<IndustryDTO> getIndustryTypeList() {
        List<Industry> models =
                repos.getIndustryRepository().getListOfIndustry();
        return IndustryMapper.toDTO(models);
    }

    /* ------------------------------------------------------------------
     * 2. Validate map position
     * ---------------------------------------------------------------- */
    /**
     * Checks whether the requested coordinates are positive and the cell is
     * currently empty on the map.
     *
     * @param x column index (0-based)
     * @param y row index (0-based)
     * @return {@code true} if the position is valid and free
     */
    public boolean isValidPosition(int x, int y) {
        ValueValidator v = new ValueValidator();
        return v.isPositive(x) && v.isPositive(y) && map.isEmpty(x, y);
    }

    /**
     * Deprecated alias kept for backwards compatibility with legacy code.
     * Delegates to {@link #isValidPosition(int, int)}.
     *
     * @param x column index (0-based)
     * @param y row index (0-based)
     * @return {@code true} if the position is valid and free
     * @deprecated Use {@link #isValidPosition(int, int)} instead.
     */
    @Deprecated
    public boolean iniciarAdicaoIndustria(int x, int y) {
        return isValidPosition(x, y);
    }

    /* ------------------------------------------------------------------
     * 3. Create and place the concrete industry (UI → Controller)
     * ---------------------------------------------------------------- */
    /**
     * Builds a concrete industry instance from the chosen template and
     * places it on the map.
     *
     * @param dto the DTO representing the chosen template
     * @param x   column (0-based)
     * @param y   row (0-based)
     * @return {@code true} if the industry was added successfully
     */
    public boolean addIndustry(IndustryDTO dto, int x, int y) {

        /* Defensive copy of the template */
        Industry model = repos.getIndustryRepository()
                .getIndustryTemplate(dto.getIndex());
        if (model == null) return false;

        /* Instantiate the correct concrete type */
        Location loc = new Location(x, y);
        Industry newInd;

        if (model instanceof PrimaryIndustry p) {
            newInd = new PrimaryIndustry(
                    p.getIndustryName(), p.getIndustryCost(),
                    p.getProduction(),   p.getDemand(),
                    loc,                 p.getCargoType());

        } else if (model instanceof TransformingIndustry t) {
            newInd = new TransformingIndustry(
                    t.getIndustryName(), t.getIndustryCost(),
                    t.getProduction(),   t.getDemand(),
                    loc,                 t.getInputCargoType(),
                    t.getOutputCargoType());

        } else if (model instanceof MixedIndustry m) {
            newInd = new MixedIndustry(
                    m.getIndustryName(), m.getIndustryCost(),
                    m.getProduction(),   m.getDemand(),
                    loc,                 m.getInputCargoType(),
                    m.getOutputCargoType());

        } else {
            return false; // unexpected subtype
        }

        /* Delegate placement to the map */
        return map.createIndustry(newInd, loc);
    }
}

package pt.ipp.isep.dei.controller;

import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.domain.Size;
import pt.ipp.isep.dei.domain.ValueValidator;
import pt.ipp.isep.dei.mapper.dto.SizeDTO;
import pt.ipp.isep.dei.repository.Repositories;
import pt.ipp.isep.dei.repository.MapRepository;
import pt.ipp.isep.dei.repository.SizeRepository;

import java.util.List;

/**
 * Controller for the “Create Map” use-case.
 *
 * <p>Responsibilities:</p>
 * <ul>
 *   <li>Validate map name and dimensions;</li>
 *   <li>Create a map draft with given parameters;</li>
 *   <li>Persist the map draft upon confirmation;</li>
 *   <li>Provide available predefined sizes for the UI.</li>
 * </ul>
 *
 * <p>It uses {@link ValueValidator} and delegates persistence to the
 * {@link MapRepository} and retrieves size templates from {@link SizeRepository}.</p>
 *
 * @author Team
 * @version 1.0
 */
public class CreateMapController {

    private final ValueValidator validator = new ValueValidator();
    private final MapRepository repository =
            Repositories.getInstance().getMapRepository();
    private final SizeRepository sizeRepository =
            Repositories.getInstance().getSizeRepository();

    /**
     * Validates that both width and height are strictly positive.
     *
     * @param width  the map width to validate
     * @param height the map height to validate
     * @return {@code true} if both width and height are positive
     */
    public boolean validateDimensions(int width, int height) {
        return validator.validateWidthLength(width, height);
    }

    /**
     * Validates the map name.
     *
     * <p>The name must not be null, must not be blank,
     * and must be unique among existing maps.</p>
     *
     * @param name the map name to validate
     * @return {@code true} if the name is valid and unique
     */
    public boolean validateMapName(String name) {
        return name != null
                && !name.trim().isEmpty()
                && repository.isMapNameUnique(name);
    }

    /**
     * Creates a map draft with the specified name and dimensions.
     *
     * <p>Performs validation and throws an exception if invalid.</p>
     *
     * @param name   the name of the map
     * @param width  the width of the map
     * @param height the height of the map
     * @return a new {@link Map} draft (not persisted)
     * @throws IllegalArgumentException if the name is invalid or dimensions are non-positive
     */
    public Map createMapDraft(String name, int width, int height) {
        if (!validateMapName(name)) {
            throw new IllegalArgumentException("Invalid or non-unique map name");
        }
        Size size = new Size(width, height);
        return repository.createMap(name, size.getWidth(), size.getHeight());
    }

    /**
     * Persists a previously created map draft.
     *
     * @param map the map draft to persist
     */
    public void saveMap(Map map) {
        repository.saveMap(map);
    }

    /**
     * Legacy single-step creation method for backward compatibility.
     *
     * <p>Creates and saves the map in one call using the modern two-step flow.</p>
     *
     * @param name   the map name
     * @param width  the map width
     * @param height the map height
     * @deprecated Use {@link #createMapDraft(String, int, int)}
     *             followed by {@link #saveMap(Map)} instead.
     */
    @Deprecated
    public void createMap(String name, int width, int height) {
        Map draft = createMapDraft(name, width, height);
        saveMap(draft);
    }

    /**
     * Retrieves predefined map sizes as DTOs for UI presentation.
     *
     * @return list of {@link SizeDTO} representing available sizes
     */
    public List<SizeDTO> getAvailableSizes() {
        return sizeRepository.getSizeMap().entrySet().stream()
                .map(entry -> new SizeDTO(
                        entry.getKey(),
                        entry.getValue().getWidth(),
                        entry.getValue().getHeight()))
                .toList();
    }
}
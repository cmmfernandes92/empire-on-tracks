package pt.ipp.isep.dei.mapper;

import pt.ipp.isep.dei.domain.Map;
import pt.ipp.isep.dei.mapper.dto.MapDTO;

/**
 * Converts Map domain objects to DTOs.
 */
public final class MapMapper {

    private MapMapper() {
        // utility class
    }

    /**
     * Converts a domain Map into a MapDTO.
     *
     * @param map the domain Map
     * @return the corresponding MapDTO
     */
    public static MapDTO toDTO(Map map) {
        return new MapDTO(
                map.getName(),
                map.getWidth(),
                map.getHeight()
        );
    }
}

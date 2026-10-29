package pt.ipp.isep.dei.mapper;

import pt.ipp.isep.dei.domain.Size;
import pt.ipp.isep.dei.mapper.dto.SizeDTO;

public final class SizeMapper {

    private SizeMapper() {
        // Utility class
    }

    /**
     * Converts a Size object and its ID into a SizeDTO.
     *
     * @param id   the unique ID assigned to the Size
     * @param size the Size object
     * @return the corresponding SizeDTO
     */
    public static SizeDTO toDTO(int id, Size size) {
        return new SizeDTO(id, size.getWidth(), size.getHeight());
    }
}

package pt.ipp.isep.dei.mapper;

import pt.ipp.isep.dei.domain.Locomotive;
import pt.ipp.isep.dei.mapper.dto.LocomotiveDTO;

import java.util.ArrayList;
import java.util.List;

public final class LocomotiveMapper {

    private LocomotiveMapper() {}

    public static LocomotiveDTO toDTO(Locomotive loco) {
        return new LocomotiveDTO(loco.getId(), loco.getType(), loco.getPrice());
    }
    public static List<LocomotiveDTO> toDTO (List<Locomotive> available) {
        List<LocomotiveDTO> dtoList = new ArrayList<>();
        for (Locomotive loco : available) {
            LocomotiveDTO dto = toDTO(loco);
            dtoList.add(dto);
        }
        return dtoList;
    }
}

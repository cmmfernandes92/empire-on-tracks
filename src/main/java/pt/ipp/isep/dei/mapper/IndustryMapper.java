package pt.ipp.isep.dei.mapper;

import pt.ipp.isep.dei.domain.*;
import pt.ipp.isep.dei.mapper.dto.IndustryDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts {@code List<Industry>} to {@code List<IndustryDTO>}
 * including cargo type and quantity.
 */
public final class IndustryMapper {

    private IndustryMapper() { }

    public static List<IndustryDTO> toDTO(List<Industry> models) {
        List<IndustryDTO> list = new ArrayList<>(models.size());

        for (int i = 0; i < models.size(); i++) {
            Industry m = models.get(i);

            String consumesType;
            double consumesQty;
            String producesType;
            double producesQty;

            if (m instanceof PrimaryIndustry p) {
                // A PrimaryIndustry não consome carga, mas produz uma carga
                consumesType = "–";
                consumesQty  = 0;
                producesType = p.getCargoType().name();  // Tipo de carga produzido
                producesQty  = p.getProduction();        // Quantidade produzida
            } else if (m instanceof TransformingIndustry t) {
                // A TransformingIndustry consome e produz cargas
                consumesType = t.getInputCargoType().name();  // Tipo de carga consumida
                consumesQty  = t.getDemand();                  // Quantidade consumida
                producesType = t.getOutputCargoType().name(); // Tipo de carga produzida
                producesQty  = t.getProduction();             // Quantidade produzida
            } else if (m instanceof MixedIndustry x) {
                // A MixedIndustry consome e produz cargas, podendo ter diferentes tipos
                consumesType = x.getInputCargoType().name();  // Tipo de carga consumida
                consumesQty  = x.getDemand();                  // Quantidade consumida
                producesType = x.getOutputCargoType().name(); // Tipo de carga produzida
                producesQty  = x.getProduction();             // Quantidade produzida
            } else {
                // Caso a indústria não seja nenhuma das anteriores, trata como desconhecida
                consumesType = "–";
                consumesQty  = 0;
                producesType = "UNKNOWN";
                producesQty  = 0;
            }

            // Adiciona o DTO à lista
            list.add(new IndustryDTO(
                    i,
                    m.getIndustryName(),
                    consumesType,        // Tipo de carga consumida
                    consumesQty,         // Quantidade de carga consumida
                    producesType,        // Tipo de carga produzida
                    producesQty          // Quantidade de carga produzida
            ));
        }
        return list;
    }
}

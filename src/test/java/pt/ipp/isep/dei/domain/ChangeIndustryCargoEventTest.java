package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChangeIndustryCargoEventTest {


    @Test
    void testApplyIgnoresInvalidIndex() {
        // Criando um mapa vazio (sem indústrias)
        Map map = new Map();

        // Criando o evento de mudança de carga
        ChangeIndustryCargoEvent event = new ChangeIndustryCargoEvent(1, CargoType.COAL, CargoType.STEEL);

        // O índice 1 não existe, então não deve lançar exceção, apenas não fazer nada
        assertDoesNotThrow(() -> event.apply(map, new LocomotiveCatalog()));
    }
}



package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChangeIndustryProductionRateEventTest {

    @Test
    void testApplyWithInvalidIndex() {
        // Criando um mapa com apenas uma indústria
        Map map = new Map();
        PrimaryIndustry industry = new PrimaryIndustry("Industry 4", 200, 20.0, 30.0, CargoType.COAL);
        map.getIndustries().add(industry);

        // Criando o evento de mudança de taxa de produção com um índice inválido
        ChangeIndustryProductionRateEvent event = new ChangeIndustryProductionRateEvent(1, 25); // Índice inválido
        event.apply(map, new LocomotiveCatalog());

        // Verificando que a produção não foi alterada
        assertEquals(20.0, industry.getProduction(), 0.01);  // Não houve alteração, pois o índice 1 não existe
    }

    @Test
    void testApplyWithZeroPercentageChange() {
        // Criando uma PrimaryIndustry
        PrimaryIndustry industry = new PrimaryIndustry("Industry 5", 250, 20.0, 30.0, CargoType.PLASTIC);
        Map map = new Map();
        map.getIndustries().add(industry);

        // Criando o evento de mudança de taxa de produção com 0% de mudança
        ChangeIndustryProductionRateEvent event = new ChangeIndustryProductionRateEvent(0, 0); // 0%
        event.apply(map, new LocomotiveCatalog());

        // Verificando que a produção não foi alterada
        assertEquals(20.0, industry.getProduction(), 0.01);  // Não houve alteração, pois a porcentagem foi 0
    }
}

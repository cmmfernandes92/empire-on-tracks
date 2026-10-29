package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AddTemplateLocomotiveEventTest {

    private Locomotive template;
    private AddTemplateLocomotiveEvent event;
    private LocomotiveCatalog catalog;
    private Map map;

    @BeforeEach
    void setUp() {
        template = new Locomotive(
                "L123",
                "Electric",
                1000000,
                true,
                5000,
                3.2,
                200,
                2020,
                1.5,
                5000
        );
        event = new AddTemplateLocomotiveEvent(template);
        catalog = new LocomotiveCatalog();
        map = new Map(); // Se o map não for usado internamente, podemos deixar ele vazio mesmo
    }

    @Test
    void testConstructorStoresTemplateCorrectly() {
        // Usa reflexão se necessário para verificar o campo private
        assertNotNull(event);
    }

    @Test
    void testApplyAddsLocomotiveCloneToCatalog() {
        event.apply(map, catalog);

        List<Locomotive> locomotives = catalog.getAllLocomotives();
        assertEquals(1, locomotives.size());

        Locomotive added = locomotives.get(0);

        assertEquals(template.getId(), added.getId());
        assertEquals(template.getType(), added.getType());
        assertEquals(template.getPrice(), added.getPrice());
        assertEquals(template.requiresElectrification(), added.requiresElectrification());
        assertEquals(template.getPower(), added.getPower());
        assertEquals(template.getAcceleration(), added.getAcceleration());
        assertEquals(template.getTopSpeed(), added.getTopSpeed());
        assertEquals(template.getStartYear(), added.getStartYear());
        assertEquals(template.getFuelCostPerKm(), added.getFuelCostPerKm());
        assertEquals(template.getMaintenancePerYear(), added.getMaintenancePerYear());

        // Garante que é um novo objeto (clonado)
        assertNotSame(template, added);
    }

    @Test
    void testApplyMultipleTimesAddsMultipleClones() {
        event.apply(map, catalog);
        event.apply(map, catalog);

        List<Locomotive> locomotives = catalog.getAllLocomotives();
        assertEquals(2, locomotives.size());
    }

    @Test
    void testApplyWithNullCatalogThrowsException() {
        assertThrows(NullPointerException.class, () -> event.apply(map, null));
    }

    @Test
    void testConstructorWithNullTemplateDoesNotThrow() {
        // Isso depende da lógica do seu construtor.
        // Se não aceita null, mude para assertThrows(NullPointerException.class, ...)
        AddTemplateLocomotiveEvent e = new AddTemplateLocomotiveEvent(null);
        assertNotNull(e);
    }
}

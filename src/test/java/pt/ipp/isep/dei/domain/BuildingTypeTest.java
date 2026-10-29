package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuildingTypeTest {

    @Test
    void testEnumValuesCoverage() {
        for (BuildingType type : BuildingType.values()) {
            assertNotNull(type);
        }
    }

    @Test
    void testEnumValueOf() {
        assertEquals(BuildingType.TELEGRAPH, BuildingType.valueOf("TELEGRAPH"));
        assertEquals(BuildingType.LIQUID_STORAGE, BuildingType.valueOf("LIQUID_STORAGE"));
    }
}

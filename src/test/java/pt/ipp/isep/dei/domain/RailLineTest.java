package pt.ipp.isep.dei.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RailLineTest {

    @Test
    void getStartAndEndReturnExactlyWhatWasPassed() {
        RailLine rl = new RailLine(null, null, false, false, 3.14);
        assertNull(rl.getStart(), "start should be exactly what was passed (null here)");
        assertNull(rl.getEnd(),   "end   should be exactly what was passed (null here)");
    }

    @Test
    void costAndMaintenance_whenNeitherElectrifiedNorDouble() {
        RailLine rl = new RailLine(null, null, false, false, 1.0);
        assertEquals(6000, rl.getCost(),           "base cost with no flags");
        assertEquals(60,   rl.getMaintenanceCost(), "base maintenance with no flags");
        assertFalse(rl.isElectrified());
        assertFalse(rl.isDouble());
    }

    @Test
    void costAndMaintenance_whenElectrifiedOnly() {
        RailLine rl = new RailLine(null, null, true, false, 1.0);
        assertEquals(6000 + 5000,    rl.getCost(),           "cost adds 5000 when electrified");
        assertEquals(60   + 12,      rl.getMaintenanceCost(), "maintenance adds 12 when electrified");
        assertTrue(rl.isElectrified());
        assertFalse(rl.isDouble());
    }

    @Test
    void costAndMaintenance_whenDoubleOnly() {
        RailLine rl = new RailLine(null, null, false, true, 1.0);
        assertEquals(6000 + 3000,    rl.getCost(),           "cost adds 3000 when double");
        assertEquals(60   + 24,      rl.getMaintenanceCost(), "maintenance adds 24 when double");
        assertFalse(rl.isElectrified());
        assertTrue(rl.isDouble());
    }

    @Test
    void costAndMaintenance_whenBothFlags() {
        RailLine rl = new RailLine(null, null, true, true, 1.0);
        assertEquals(6000 + 5000 + 3000, rl.getCost(),           "cost adds both electrified and double");
        assertEquals(60 + 12 + 24,       rl.getMaintenanceCost(), "maintenance adds both electrified and double");
        assertTrue(rl.isElectrified());
        assertTrue(rl.isDouble());
    }

    @Test
    void distanceIsStoredUnchanged() {
        double d = 123.456;
        RailLine rl = new RailLine(null, null, false, false, d);
        assertEquals(d, rl.getDistance(), 1e-9, "distance should be exactly the constructor argument");
    }

    @Test
    void differentFlagsReallyChangeCostAndMaintenance() {
        RailLine a = new RailLine(null, null, false, false, 1.0);
        RailLine b = new RailLine(null, null, true,  true,  1.0);
        assertNotEquals(a.getCost(),            b.getCost(),            "cost must differ");
        assertNotEquals(a.getMaintenanceCost(), b.getMaintenanceCost(), "maintenance cost must differ");
    }
}
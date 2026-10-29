package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PeriodTest {

    @Test
    void constructor_withValidRange_setsFields() {
        Period p = new Period(0, 10);
        assertEquals(0, p.getStartDate());
        assertEquals(10, p.getEndDate());
        assertEquals("Period[0 -> 10]", p.toString());
    }

    @Test
    void constructor_withEqualBounds_allowsZeroLengthPeriod() {
        Period p = new Period(5, 5);
        assertEquals(5, p.getStartDate());
        assertEquals(5, p.getEndDate());
        assertEquals("Period[5 -> 5]", p.toString());
    }

    @Test
    void constructor_withStartGreaterThanEnd_throwsException() {
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> new Period(10, 0)
        );
        assertTrue(e.getMessage().contains("startDate must be <= endDate"));
    }

    @Test
    void toString_formatMatchesExpected() {
        Period p = new Period(-3, 3);
        assertEquals("Period[-3 -> 3]", p.toString());
    }
}

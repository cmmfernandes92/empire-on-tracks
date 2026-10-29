package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionRecordTest {

    @Test
    void testConstructorAndGetters() {
        TransactionRecord.Type type = TransactionRecord.Type.TRAIN_MAINTENANCE;
        double amount = -500.0;
        int year = 2025;

        TransactionRecord record = new TransactionRecord(type, amount, year);

        assertEquals(type, record.getType());
        assertEquals(amount, record.getAmount());
        assertEquals(year, record.getYear());
    }

    @Test
    void testIsExpenseTrue() {
        TransactionRecord record = new TransactionRecord(TransactionRecord.Type.FUEL, -100.0, 2024);
        assertTrue(record.isExpense());
    }

    @Test
    void testIsExpenseFalse() {
        TransactionRecord record = new TransactionRecord(TransactionRecord.Type.REVENUE_DELIVERY, 200.0, 2023);
        assertFalse(record.isExpense());
    }

    @Test
    void testAllEnumValues() {
        TransactionRecord.Type[] values = TransactionRecord.Type.values();
        assertEquals(4, values.length);
        assertTrue(List.of(values).contains(TransactionRecord.Type.TRACK_MAINTENANCE));
        assertTrue(List.of(values).contains(TransactionRecord.Type.TRAIN_MAINTENANCE));
        assertTrue(List.of(values).contains(TransactionRecord.Type.FUEL));
        assertTrue(List.of(values).contains(TransactionRecord.Type.REVENUE_DELIVERY));
    }
}

package pt.ipp.isep.dei.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FinancialReportServiceTest {

    @Test
    void testGetYearlyFinancialSummaryWithData() {
        List<TransactionRecord> records = List.of(
                new TransactionRecord(TransactionRecord.Type.TRACK_MAINTENANCE, -1000.0, 2024),
                new TransactionRecord(TransactionRecord.Type.TRAIN_MAINTENANCE, -2000.0, 2024),
                new TransactionRecord(TransactionRecord.Type.FUEL, -500.0, 2024),
                new TransactionRecord(TransactionRecord.Type.REVENUE_DELIVERY, 8000.0, 2024),
                new TransactionRecord(TransactionRecord.Type.REVENUE_DELIVERY, 2000.0, 2024),
                new TransactionRecord(TransactionRecord.Type.FUEL, -150.0, 2023) // different year
        );

        List<String> summary = FinancialReportService.getYearlyFinancialSummary(records, 2024);

        assertEquals(7, summary.size());
        assertEquals("Year 2024 Financial Results:", summary.get(0));
        assertEquals("  Track Maintenance:     -1000.00", summary.get(1));
        assertEquals("  Train Maintenance:     -2000.00", summary.get(2));
        assertEquals("  Fuel:                 -500.00", summary.get(3));
        assertEquals("  Revenue (Deliveries): 10000.00", summary.get(4));
        assertEquals("  Total Expenses:       -3500.00", summary.get(5));
        assertEquals("  Net Result:           6500.00", summary.get(6));
    }

    @Test
    void testGetYearlyFinancialSummaryWithNoMatches() {
        List<TransactionRecord> records = List.of(
                new TransactionRecord(TransactionRecord.Type.FUEL, -100.0, 2023),
                new TransactionRecord(TransactionRecord.Type.REVENUE_DELIVERY, 200.0, 2023)
        );

        List<String> summary = FinancialReportService.getYearlyFinancialSummary(records, 2025);

        assertEquals(7, summary.size());
        assertTrue(summary.get(0).contains("2025"));
        assertTrue(summary.stream().allMatch(line -> line.matches(".*[-+]?0\\.00.*") || line.contains("Year")));
    }

    @Test
    void testGetYearlyFinancialSummaryWithEmptyList() {
        List<String> summary = FinancialReportService.getYearlyFinancialSummary(List.of(), 2022);

        assertEquals(7, summary.size());
        assertEquals("Year 2022 Financial Results:", summary.get(0));
        assertTrue(summary.stream().allMatch(line -> line.matches(".*[-+]?0\\.00.*") || line.contains("Year")));
    }
}

package pt.ipp.isep.dei.domain;

import java.util.List;
import java.util.ArrayList;

/**
 * Service for generating yearly financial summaries based on a list of transaction records.
 */
public class FinancialReportService {

    /**
     * Generates a formatted financial summary for a specific year from a list of transaction records.
     * <p>
     * The summary includes:
     * <ul>
     *     <li>Total track maintenance costs</li>
     *     <li>Total train maintenance costs</li>
     *     <li>Total fuel expenses</li>
     *     <li>Total revenue from deliveries</li>
     *     <li>Total expenses (sum of all costs)</li>
     *     <liNet financial result (revenue + expenses)</li>
     * </ul>
     *
     * @param transactions the list of transaction records
     * @param year the target year for the financial summary
     * @return a list of formatted strings representing the financial summary
     */
    public static List<String> getYearlyFinancialSummary(List<TransactionRecord> transactions, int year) {
        double trackMaintenance = 0;
        double trainMaintenance = 0;
        double fuel = 0;
        double revenueDelivery = 0;

        for (TransactionRecord tr : transactions) {
            if (tr.getYear() == year) {
                switch (tr.getType()) {
                    case TRACK_MAINTENANCE: trackMaintenance += tr.getAmount(); break;
                    case TRAIN_MAINTENANCE: trainMaintenance += tr.getAmount(); break;
                    case FUEL:              fuel += tr.getAmount(); break;
                    case REVENUE_DELIVERY:  revenueDelivery += tr.getAmount(); break;
                }
            }
        }

        List<String> summary = new ArrayList<>();
        summary.add(String.format("Year %d Financial Results:", year));
        summary.add(String.format("  Track Maintenance:     %.2f", trackMaintenance));
        summary.add(String.format("  Train Maintenance:     %.2f", trainMaintenance));
        summary.add(String.format("  Fuel:                 %.2f", fuel));
        summary.add(String.format("  Revenue (Deliveries): %.2f", revenueDelivery));
        double expenses = trackMaintenance + trainMaintenance + fuel;
        double net = revenueDelivery + expenses; // expenses are negative
        summary.add(String.format("  Total Expenses:       %.2f", expenses));
        summary.add(String.format("  Net Result:           %.2f", net));
        return summary;
    }
}

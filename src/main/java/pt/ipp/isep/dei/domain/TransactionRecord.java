package pt.ipp.isep.dei.domain;

import java.io.Serializable;

/**
 * Represents a financial transaction (either an expense or revenue)
 * that occurs during the simulation.
 * <p>
 * Each transaction stores its type, monetary value, and the year it occurred.
 * Expenses are represented as negative values, and revenues as positive.
 */
public class TransactionRecord implements Serializable {

    /**
     * Enumeration of possible transaction types.
     */
    public enum Type {
        /** Maintenance cost for railway tracks */
        TRACK_MAINTENANCE,
        /** Maintenance cost for trains */
        TRAIN_MAINTENANCE,
        /** Fuel consumption cost */
        FUEL,
        /** Revenue from cargo or passenger delivery */
        REVENUE_DELIVERY
    }

    private final Type type;
    private final double amount;
    private final int year;

    /**
     * Constructs a new {@code TransactionRecord} with the given type, amount, and year.
     *
     * @param type   the type of transaction
     * @param amount the monetary value of the transaction
     *               (negative for expenses, positive for revenue)
     * @param year   the year in which the transaction occurred
     */
    public TransactionRecord(Type type, double amount, int year) {
        this.type = type;
        this.amount = amount;
        this.year = year;
    }

    /**
     * Returns the type of this transaction.
     *
     * @return the transaction type
     */
    public Type getType() {
        return type;
    }

    /**
     * Returns the amount of this transaction.
     *
     * @return the transaction amount (negative for expenses, positive for revenue)
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Returns the year in which the transaction occurred.
     *
     * @return the transaction year
     */
    public int getYear() {
        return year;
    }

    /**
     * Determines whether this transaction represents an expense.
     *
     * @return {@code true} if the amount is negative, {@code false} otherwise
     */
    public boolean isExpense() {
        return amount < 0;
    }
}

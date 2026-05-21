package model.asset;

import interfaces.CSVSerializable;
import interfaces.Tradeable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a bond listed on a financial market.
 * <p>
 *     Extends {@link Asset} with bond-specific data (coupon rate, issue date, maturity date),
 *     and implements {@link Tradeable} for market operations and
 *     {@link CSVSerializable} for CSV persistence.
 * </p>
 */
public class Bond extends Asset implements Tradeable, CSVSerializable {
    private final double couponRate;
    private final LocalDate issueDate;
    private final LocalDate maturityDate;

    /**
     * Constructs a new {@code Bond} with the given market data.
     *
     * @param ticker        the unique ticker symbol
     * @param name          the full display name of the bond
     * @param price         the current market price
     * @param currency      the currency code the price is quoted in (e.g. {@code "DKK"})
     * @param couponRate    the annual coupon rate as a percentage
     * @param issueDate     the date the bond was issued
     * @param maturityDate  the date the bond matures
     * @param rating        the credit rating
     * @param market        the exchange this bond trades on
     * @param lastUpdated   the date the price data was last updated
     */
    public Bond(String ticker, String name, double price, String currency,
                double couponRate, LocalDate issueDate, LocalDate maturityDate,
                String rating, String market, LocalDate lastUpdated) {
        super(ticker, name, price, currency, rating, market, lastUpdated);
        this.couponRate    = couponRate;
        this.issueDate     = issueDate;
        this.maturityDate  = maturityDate;
    }

    public double getCouponRate()    { return couponRate; }
    public LocalDate getIssueDate()  { return issueDate; }
    public LocalDate getMaturityDate(){ return maturityDate; }

    /**
     * Returns the annual coupon payment in DKK based on the current price.
     * <p>
     *     Calculated as: {@code (couponRate / 100) * price}
     * </p>
     *
     * @return annual coupon payment in DKK
     */
    public double getAnnualCouponPayment() {
        return (couponRate / 100) * getPrice();
    }

    /**
     * Returns the number of years remaining until maturity.
     *
     * @return years to maturity as a {@code double}
     */
    public double getYearsToMaturity() {
        return (maturityDate.toEpochDay() - LocalDate.now().toEpochDay()) / 365.0;
    }

    @Override
    public String getTicker()   { return super.getTicker(); }
    @Override
    public double getPrice()    { return super.getPrice(); }
    @Override
    public String getCurrency() { return super.getCurrency(); }
    @Override
    public String getName()     { return super.getName(); }

    /**
     * Returns a semicolon-delimited CSV representation of this bond,
     * matching the column order of {@code bondMarket.csv}.
     * <p>
     *     Column order: {@code ticker;name;price;currency;coupon_rate;issue_date;maturity_date;rating;market;last_updated}
     * </p>
     *
     * @return a CSV-formatted String representing this bond
     */
    @Override
    public String toCSVLine() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return String.join(";",
                getTicker(),
                getName(),
                String.valueOf(getPrice()),
                getCurrency(),
                String.valueOf(couponRate),
                issueDate.format(fmt),
                maturityDate.format(fmt),
                getRating(),
                getMarket(),
                getLastUpdated().format(fmt));
    }
    /**
     * Returns whether this bond has passed its maturity date.
     *
     * @return {@code true} if the bond has matured
     */
    public boolean isMature() {
        return LocalDate.now().isAfter(maturityDate);
    }

    /**
     * Returns the number of days remaining until maturity.
     *
     * @return days to maturity as a {@code long}
     */
    public long getDaysToMaturity() {
        return maturityDate.toEpochDay() - LocalDate.now().toEpochDay();
    }
    /**
     * Returns a formatted one-line summary of this bond for console display.
     *
     * @return a formatted string showing ticker, name, coupon rate, price and currency
     */
    @Override
    public String toString() {
        return String.format("%-10s %-40s %8.2f%% %16.2f %8s (Matures: %s)",
                getTicker(), getName(), couponRate, getPrice(), getCurrency(),
                maturityDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }
}
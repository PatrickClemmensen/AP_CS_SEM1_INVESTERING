package model.portfolio;

import model.asset.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PositionTest {
    private Stock stock;
    private Position position;


    @BeforeEach
    void setUp(){
        stock = new Stock("NOVO-B", "Novozymes", "Health Care",
                710.0, "DKK", "AA", 1.6, "Nasdaq Copenhagen", LocalDate.parse("22-04-2025", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        position = new Position(stock, 20, 700.0);
    }


    @Test
    void getCurrentValue_shouldReturnPriceTimesQuantity() {
        assertEquals(14200.0, position.getCurrentValue());
    }

    @Test
    void getCostBasis_shouldReturnAverageBuyPriceTimesQuantity(){
        assertEquals(14000.0, position.getCostBasis());
    }

    @Test
    void getUnrealizedGain_shouldReturnDifferenceBetweenValueAndCost(){
        assertEquals(200, position.getUnrealizedGain());
    }

    @Test
    void getRankValue_shouldReturnCorrectPercentReturn(){
        // ROI = ((currentPrice - AverageBuyPrice)) / AverageBuyPrice)*100
        // = ((710-700)/700)*100 = 1.428

        assertEquals(1.4285, position.getRankValue(), 0.001);

    }


    @Test
    void increaseQuantity_shouldIncreaseQuantityCorrectly() {

        position.increaseQuantity(10, 100);
        // original quantity was 20, should now be 30
        assertEquals(30, position.getQuantity());
    }

    @Test
    void increaseQuantity_shouldUpdatePriceCorrectly() {
        Stock newStock = new Stock("NOVO-B", "Novozymes", "Health Care",
                750.0, "DKK", "AA", 1.6, "Nasdaq Copenhagen", LocalDate.parse("22-04-2025", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        Position newPosition = new Position(newStock, 20, 750);
        newPosition.increaseQuantity(newPosition.getQuantity(), newPosition.getAverageBuyPrice());
        assertEquals(750, newPosition.getAverageBuyPrice());
    }

    @Test
    void increaseQuantity_shouldRecalculateCorrectlyOnSecondPurchase() {
        // Simulates what PortfolioService.buy() does internally
        // Own: 20 shares @ 700 DKK, Buy: 20 more @ 710 DKK
        int newQuantity = 20;
        double newPrice = 710.0;

        position.increaseQuantity(newQuantity, newPrice);


        // (20*700 + 20*710) / 40 = 705.0
        assertEquals(705.0, position.getAverageBuyPrice());
        assertEquals(40, position.getQuantity());
    }

    @Test
    void decreaseQuantity_shouldDecreaseQuantityCorrectly(){
        position.decreaseQuantity(10);
        assertEquals(10, position.getQuantity());
    }

    @Test
    void decreaseQuantity_shouldNotBeNegative(){
        position.decreaseQuantity(1000);
        assertEquals(0, position.getQuantity());
    }



    @Test
    void testToCSVLine() {
        String csv = position.toCSVLine();
        String expected = "NOVO-B;20;700.0";
        assertEquals(expected, csv);
    }

    @Test
    void testToCSVLineContainsAllFields() {
        String csv = position.toCSVLine();
        assertTrue(csv.contains("NOVO-B"));
        assertTrue(csv.contains("20"));
        assertTrue(csv.contains("700.0"));
    }


    // --- toString ---

    @Test
    void testToStringContainsTicker() {
        assertTrue(position.toString().contains("NOVO-B"));
    }

    @Test
    void testToStringContainsQuantity() {
        assertTrue(position.toString().contains("20"));
    }

// --- toString fixes ---

    @Test
    void testToStringContainsAverageBuyPrice() {
        assertTrue(position.toString().contains("700,00"));
    }

    @Test
    void testToStringContainsCurrentPrice() {
        assertTrue(position.toString().contains("710,00"));
    }

    @Test
    void testToStringContainsName() {
        assertTrue(position.toString().contains("Novozymes"));
    }

// --- getUnrealizedGain ---

    @Test
    void getUnrealizedGain_shouldBeNegativeWhenPriceFalls() {
        Stock fallenStock = new Stock("NOVO-B", "Novozymes", "Health Care",
                650.0, "DKK", "AA", 1.6, "Nasdaq Copenhagen",
                LocalDate.parse("22-04-2025", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        Position fallenPosition = new Position(fallenStock, 20, 700.0);
        assertTrue(fallenPosition.getUnrealizedGain() < 0);
    }

    @Test
    void getUnrealizedGain_shouldBeZeroWhenPriceEqualsAverageBuyPrice() {
        Stock flatStock = new Stock("NOVO-B", "Novozymes", "Health Care",
                700.0, "DKK", "AA", 1.6, "Nasdaq Copenhagen",
                LocalDate.parse("22-04-2025", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        Position flatPosition = new Position(flatStock, 20, 700.0);
        assertEquals(0.0, flatPosition.getUnrealizedGain());
    }

// --- getRankValue ---

    @Test
    void getRankValue_shouldBeNegativeWhenPriceBelowAverageBuyPrice() {
        Stock fallenStock = new Stock("NOVO-B", "Novozymes", "Health Care",
                650.0, "DKK", "AA", 1.6, "Nasdaq Copenhagen",
                LocalDate.parse("22-04-2025", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        Position fallenPosition = new Position(fallenStock, 20, 700.0);
        assertTrue(fallenPosition.getRankValue() < 0);
    }

    @Test
    void getRankValue_shouldBeZeroWhenPriceEqualsAverageBuyPrice() {
        Stock flatStock = new Stock("NOVO-B", "Novozymes", "Health Care",
                700.0, "DKK", "AA", 1.6, "Nasdaq Copenhagen",
                LocalDate.parse("22-04-2025", DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        Position flatPosition = new Position(flatStock, 20, 700.0);
        assertEquals(0.0, flatPosition.getRankValue());
    }

// --- decreaseQuantity ---

    @Test
    void decreaseQuantity_shouldSetExactlyZeroWhenQtyMatches() {
        position.decreaseQuantity(20);
        assertEquals(0, position.getQuantity());
    }

// --- getCurrentValue ---

    @Test
    void getCurrentValue_shouldBeZeroWhenQuantityIsZero() {
        position.decreaseQuantity(20);
        assertEquals(0.0, position.getCurrentValue());
    }

// --- getCostBasis ---

    @Test
    void getCostBasis_shouldUpdateAfterIncreasingQuantity() {
        position.increaseQuantity(20, 710.0);
        // (20*700 + 20*710) / 40 = 705.0 avg, 40 qty → cost = 28200
        assertEquals(28200.0, position.getCostBasis());
    }
}

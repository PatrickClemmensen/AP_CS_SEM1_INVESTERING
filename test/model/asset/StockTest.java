package model.asset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class StockTest {

    private Stock stock;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @BeforeEach
    void setUp() {
        stock = new Stock(
                "NOVO-B",
                "Novozymes B",
                "Health Care",
                710.00,
                "DKK",
                "AA",
                1.6,
                "Nasdaq Copenhagen",
                LocalDate.parse("22-04-2025", FORMATTER)
        );
    }

    // --- Constructor / Getters ---

    @Test
    void getTicker_shouldReturnTicker() {
        assertEquals("NOVO-B", stock.getTicker());
    }

    @Test
    void getName_shouldReturnName() {
        assertEquals("Novozymes B", stock.getName());
    }

    @Test
    void getSector_shouldReturnSector() {
        assertEquals("Health Care", stock.getSector());
    }

    @Test
    void getPrice_shouldReturnPrice() {
        assertEquals(710.00, stock.getPrice());
    }

    @Test
    void getCurrency_shouldReturnCurrency() {
        assertEquals("DKK", stock.getCurrency());
    }

    @Test
    void getRating_shouldReturnRating() {
        assertEquals("AA", stock.getRating());
    }

    @Test
    void getDividendYield_shouldReturnDividendYield() {
        assertEquals(1.6, stock.getDividendYield());
    }

    @Test
    void getMarket_shouldReturnMarket() {
        assertEquals("Nasdaq Copenhagen", stock.getMarket());
    }

    @Test
    void getLastUpdated_shouldReturnLastUpdated() {
        assertEquals(LocalDate.parse("22-04-2025", FORMATTER), stock.getLastUpdated());
    }

    // --- Edge Cases ---

    @Test
    void dividendYield_shouldNotBeNegative() {
        assertThrows(IllegalArgumentException.class, () ->
                new Stock("NOVO-B", "Novozymes B", "Health Care",
                        710.00, "DKK", "AA", -1.5, "Nasdaq Copenhagen",
                        LocalDate.parse("22-04-2025", FORMATTER))
        );
    }

    @Test
    void price_shouldNotBeNegative() {
        assertThrows(IllegalArgumentException.class, () ->
                new Stock("NOVO-B", "Novozymes B", "Health Care",
                        -10, "DKK", "AA", -1.5, "Nasdaq Copenhagen",
                        LocalDate.parse("22-04-2025", FORMATTER))
        );
    }

    // --- toCSVLine ---

    @Test
    void testToCSVLine() {
        String csv = stock.toCSVLine();
        String expected = "NOVO-B;Novozymes B;Health Care;710.0;DKK;AA;1.6;Nasdaq Copenhagen;22-04-2025";
        assertEquals(expected, csv);
    }

    @Test
    void testToCSVLineContainsAllFields() {
        String csv = stock.toCSVLine();
        assertTrue(csv.contains("NOVO-B"));
        assertTrue(csv.contains("Health Care"));
        assertTrue(csv.contains("22-04-2025"));
    }

    // --- toString ---

    @Test
    void testToStringContainsTicker() {
        assertTrue(stock.toString().contains("NOVO-B"));
    }

    @Test
    void testToStringContainsSector() {
        assertTrue(stock.toString().contains("Health Care"));
    }

    @Test
    void testToStringContainsPrice() {
        assertTrue(stock.toString().contains("710"));
    }
}
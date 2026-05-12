package model.portfolio;

import model.asset.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

public class PortfolioTest {

    private Portfolio portfolio;
    private Stock stock1;
    private Stock stock2;
    private Position position1;
    private Position position2;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @BeforeEach
    void setUp() {
        portfolio = new Portfolio();

        stock1 = new Stock("NOVO-B", "Novozymes", "Health Care",
                710.0, "DKK", "AA", 1.6, "Nasdaq Copenhagen",
                LocalDate.parse("22-04-2025", FORMATTER));

        stock2 = new Stock("DANSKE", "Danske Bank", "Financials",
                192.0, "DKK", "BBB+", 4.5, "Nasdaq Copenhagen",
                LocalDate.parse("22-04-2025", FORMATTER));

        position1 = new Position(stock1, 20, 700.0);
        position2 = new Position(stock2, 10, 180.0);
    }

    // --- addPosition ---

    @Test
    void addPosition_shouldIncreasePortfolioSize() {
        portfolio.addPosition(position1);
        assertEquals(1, portfolio.getPositions().size());
    }

    @Test
    void addPosition_shouldContainAddedPosition() {
        portfolio.addPosition(position1);
        assertTrue(portfolio.getPositions().contains(position1));
    }

    @Test
    void addPosition_shouldSupportMultiplePositions() {
        portfolio.addPosition(position1);
        portfolio.addPosition(position2);
        assertEquals(2, portfolio.getPositions().size());
    }

    // --- removePosition ---

    @Test
    void removePosition_shouldDecreasePortfolioSize() {
        portfolio.addPosition(position1);
        portfolio.addPosition(position2);
        portfolio.removePosition(position1);
        assertEquals(1, portfolio.getPositions().size());
    }

    @Test
    void removePosition_shouldNoLongerContainRemovedPosition() {
        portfolio.addPosition(position1);
        portfolio.removePosition(position1);
        assertFalse(portfolio.getPositions().contains(position1));
    }

    @Test
    void removePosition_shouldNotAffectOtherPositions() {
        portfolio.addPosition(position1);
        portfolio.addPosition(position2);
        portfolio.removePosition(position1);
        assertTrue(portfolio.getPositions().contains(position2));
    }

    // --- findByTicker ---

    @Test
    void findByTicker_shouldReturnCorrectPosition() {
        portfolio.addPosition(position1);
        assertEquals(position1, portfolio.findByTicker("NOVO-B"));
    }

    @Test
    void findByTicker_shouldReturnNullIfNotFound() {
        portfolio.addPosition(position1);
        assertNull(portfolio.findByTicker("DSV"));
    }

    @Test
    void findByTicker_shouldReturnNullOnEmptyPortfolio() {
        assertNull(portfolio.findByTicker("NOVO-B"));
    }

    @Test
    void findByTicker_shouldDistinguishBetweenMultiplePositions() {
        portfolio.addPosition(position1);
        portfolio.addPosition(position2);
        assertEquals(position2, portfolio.findByTicker("DANSKE"));
    }

    // --- getTotalValue ---

    @Test
    void getTotalValue_shouldReturnZeroForEmptyPortfolio() {
        assertEquals(0.0, portfolio.getTotalValue());
    }

    @Test
    void getTotalValue_shouldReturnCorrectValueForSinglePosition() {
        portfolio.addPosition(position1);
        // 20 * 710.0 = 14200.0
        assertEquals(14200.0, portfolio.getTotalValue());
    }

    @Test
    void getTotalValue_shouldSumAllPositions() {
        portfolio.addPosition(position1);
        portfolio.addPosition(position2);
        // (20 * 710.0) + (10 * 192.0) = 14200 + 1920 = 16120
        assertEquals(16120.0, portfolio.getTotalValue());
    }

    @Test
    void getTotalValue_shouldUpdateAfterRemovingPosition() {
        portfolio.addPosition(position1);
        portfolio.addPosition(position2);
        portfolio.removePosition(position2);
        assertEquals(14200.0, portfolio.getTotalValue());
    }

    // --- getTotalGain ---

    @Test
    void getTotalGain_shouldReturnZeroForEmptyPortfolio() {
        assertEquals(0.0, portfolio.getTotalGain());
    }

    @Test
    void getTotalGain_shouldReturnCorrectGainForSinglePosition() {
        portfolio.addPosition(position1);
        // (710 - 700) * 20 = 200.0
        assertEquals(200.0, portfolio.getTotalGain());
    }

    @Test
    void getTotalGain_shouldSumGainsAcrossPositions() {
        portfolio.addPosition(position1);
        portfolio.addPosition(position2);
        // position1: (710-700)*20 = 200
        // position2: (192-180)*10 = 120
        assertEquals(320.0, portfolio.getTotalGain());
    }

    @Test
    void getTotalGain_shouldHandleNegativeGain() {
        Stock losingStock = new Stock("VWS", "Vestas", "Energy",
                150.0, "DKK", "A-", 0, "Nasdaq Copenhagen",
                LocalDate.parse("22-04-2025", FORMATTER));
        Position losingPosition = new Position(losingStock, 10, 200.0);
        portfolio.addPosition(losingPosition);
        // (150-200)*10 = -500
        assertTrue(portfolio.getTotalGain() < 0);
        assertEquals(-500.0, portfolio.getTotalGain());
    }

    @Test
    void getTotalGain_shouldUpdateAfterRemovingPosition() {
        portfolio.addPosition(position1);
        portfolio.addPosition(position2);
        portfolio.removePosition(position2);
        assertEquals(200.0, portfolio.getTotalGain());
    }
}
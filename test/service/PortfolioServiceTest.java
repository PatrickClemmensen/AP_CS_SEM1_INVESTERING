package service;

import model.asset.Bond;
import model.portfolio.Position;
import model.portfolio.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.AppConstants;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

public class PortfolioServiceTest {

    private PortfolioService portfolioService;
    private StockMarketService marketService;
    private BondMarketService bondService;
    private User user;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @BeforeEach
    void setUp() {
        AppConstants.TRANSACTIONS_FILE = "test/resources/transaction_test.csv";
        marketService = new StockMarketService("test/resources/stock_test.csv");
        bondService = new BondMarketService(AppConstants.BOND_MARKET_FILE);
        portfolioService = new PortfolioService(marketService, bondService);
        user = new User(1, "Test User", "test@test.com",
                LocalDate.parse("01-01-1990", FORMATTER),
                100000.0,
                LocalDate.parse("01-01-2024", FORMATTER),
                LocalDate.parse("22-04-2025", FORMATTER));
        portfolioService.loadPortfolio(user);
    }

    // --- positions loaded ---

    @Test
    void loadPortfolio_shouldAddPositionsForUser() {
        assertFalse(user.getPortfolio().getPositions().isEmpty());
    }

    @Test
    void loadPortfolio_shouldHaveThreePositions() {
        // NOVO-B, DANSKE, VWS — DSV fully sold
        assertEquals(3, user.getPortfolio().getPositions().size());
    }

    // --- BUY: single ---

    @Test
    void loadPortfolio_shouldFindDANSKE() {
        assertNotNull(user.getPortfolio().findByTicker("DANSKE"));
    }

    @Test
    void loadPortfolio_shouldHaveCorrectQuantityForDANSKE() {
        assertEquals(50, user.getPortfolio().findByTicker("DANSKE").getQuantity());
    }

    @Test
    void loadPortfolio_shouldHaveCorrectAverageBuyPriceForDANSKE() {
        assertEquals(180.0, user.getPortfolio().findByTicker("DANSKE").getAverageBuyPrice(), 0.001);
    }

    // --- BUY: multiple purchases, recalculated average ---

    @Test
    void loadPortfolio_shouldFindNOVOB() {
        assertNotNull(user.getPortfolio().findByTicker("NOVO-B"));
    }

    @Test
    void loadPortfolio_shouldHaveCorrectQuantityForNOVOB() {
        // 20 + 10 = 30
        assertEquals(30, user.getPortfolio().findByTicker("NOVO-B").getQuantity());
    }

    @Test
    void loadPortfolio_shouldRecalculateAverageBuyPriceForNOVOB() {
        // (20*700 + 10*720) / 30 = 706.67
        assertEquals(706.67, user.getPortfolio().findByTicker("NOVO-B").getAverageBuyPrice(), 0.01);
    }

    // --- SELL: partial ---

    @Test
    void loadPortfolio_shouldHaveCorrectQuantityForVWS() {
        assertEquals(15, user.getPortfolio().findByTicker("VWS").getQuantity());
    }

    // --- SELL: fully sold ---

    @Test
    void loadPortfolio_shouldRemoveFullySoldPosition() {
        assertNull(user.getPortfolio().findByTicker("DSV"));
    }

    // --- other users ---

    @Test
    void loadPortfolio_shouldNotLoadPositionsForOtherUsers() {
        User otherUser = new User(999, "Ghost User", "ghost@test.com",
                LocalDate.parse("01-01-1990", FORMATTER),
                50000.0,
                LocalDate.parse("01-01-2024", FORMATTER),
                LocalDate.parse("22-04-2025", FORMATTER));
        portfolioService.loadPortfolio(otherUser);
        assertTrue(otherUser.getPortfolio().getPositions().isEmpty());
    }

    // --- totals ---

    @Test
    void loadPortfolio_shouldCalculateCorrectTotalValue() {
        // (30*710) + (50*192) + (15*198) = 21300 + 9600 + 2970 = 33870
        assertEquals(33870.0, user.getPortfolio().getTotalValue(), 0.01);
    }

    @Test
    void loadPortfolio_shouldCalculateCorrectTotalGain() {
        // (710-706.67)*30 + (192-180)*50 + (198-190)*15 = 100 + 600 + 120 = 820
        assertEquals(820.0, user.getPortfolio().getTotalGain(), 0.01);
    }
}
package model.portfolio;

import model.asset.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PositionTest {
    private Stock stock;
    private Position position;


    @BeforeEach
    void setUp(){
        stock = new Stock("NOVO-B", "Novozymes", "Health Care",
                710.0, "DKK", "AA", 1.6, "Nasdaq Copenhagen", LocalDate.parse("22-04-2025"));
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
        assertEquals(200.0, position.getUnrealizedGain());
    }

    @Test
    void getRankValue_shouldReturnCorrectPercentReturn(){
        // ROI = ((currentPrice - AverageBuyPrice)) / AverageBuyPrice)*100
        // = ((710-700)/700)*100 = 1.428

        assertEquals(1.4285, position.getRankValue(), 0.001);

    }

    /*
    @Test
    void addQuantity_shouldIncreaseQuantityCorrectly() {
        position.addQuantity(10);
        // original quantity was 20, should now be 30
        assertEquals(30, position.getQuantity());
    }

    @Test
    void setAverageBuyPrice_shouldUpdateCorrectly() {
        position.setAverageBuyPrice(750.0);
        assertEquals(750.0, position.getAverageBuyPrice());
    }

    @Test
    void weightedAverage_shouldRecalculateCorrectlyOnSecondPurchase() {
        // Simulates what PortfolioService.buy() does internally
        // Own: 20 shares @ 700 DKK, Buy: 20 more @ 710 DKK
        int newQuantity = 20;
        double newPrice = 710.0;

        double newAverage = (position.getQuantity() * position.getAverageBuyPrice()
                + newQuantity * newPrice)
                / (position.getQuantity() + newQuantity);

        position.setAverageBuyPrice(newAverage);
        position.addQuantity(newQuantity);

        // (20*700 + 20*710) / 40 = 705.0
        assertEquals(705.0, position.getAverageBuyPrice());
        assertEquals(40, position.getQuantity());
    }
     */

}

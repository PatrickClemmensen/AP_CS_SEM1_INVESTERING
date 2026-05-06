package model.transaction;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderTypeTest {

    // fromString — happy path
    @Test
    void fromString_shouldParseLowercase() {
        assertEquals(OrderType.BUY, OrderType.fromString("buy"));
    }

    @Test
    void fromString_shouldParseUppercase() {
        assertEquals(OrderType.BUY, OrderType.fromString("BUY"));
    }

    @Test
    void fromString_shouldParseMixedCase() {
        assertEquals(OrderType.SELL, OrderType.fromString("sEll"));
    }

    @Test
    void fromString_shouldHandleLeadingAndTrailingWhitespace() {
        assertEquals(OrderType.BUY, OrderType.fromString("  buy  "));
    }

    // fromString — failure cases
    @Test
    void fromString_shouldThrowOnInvalidValue() {
        assertThrows(IllegalArgumentException.class,
                () -> OrderType.fromString("invalid"));
    }

    @Test
    void fromString_shouldThrowOnEmptyString() {
        assertThrows(IllegalArgumentException.class,
                () -> OrderType.fromString(""));
    }

    // enum identity
    @Test
    void buyAndSell_shouldBeDistinct() {
        assertNotEquals(OrderType.BUY, OrderType.SELL);
    }

    @Test
    void toString_shouldReturnEnumName() {
        assertEquals("BUY", OrderType.BUY.toString());
        assertEquals("SELL", OrderType.SELL.toString());
    }
}
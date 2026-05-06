package model.transaction;

public enum OrderType {
    BUY, SELL;

    public static OrderType fromString(String value){
        return OrderType.valueOf(value.trim().toUpperCase());
    }
}

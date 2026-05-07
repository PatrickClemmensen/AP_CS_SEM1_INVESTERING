package ui;

public enum MemberOption {

    OPTION_1(1, "View portfolio"),
    OPTION_2(2, "Buy stock"),
    OPTION_3(3, "Sell Stock"),
    OPTION_4(4, "View Market"),
    OPTION_5(5, "Logout"),
    EXIT(0, "Exit");

    private final int value;
    private final String label;

    MemberOption(int value, String label) {
        this.value = value;
        this.label = label;
    }

    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public static MemberOption fromChoice(int value) {
        for (MemberOption option : values()) {
            if (option.value == value) return option;
        }
        throw new IllegalArgumentException("Invalid menu choice: " + value);
    }
}
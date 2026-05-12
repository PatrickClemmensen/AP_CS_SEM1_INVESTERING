package ui.enums;


public enum LeaderOption {

    OPTION_1(1, "View all members"),
    EXIT(0, "Logout");

    private final int value;
    private final String label;

    /**
     * @param value the numeric choice the user types to select this option
     * @param label the description of the value that will be printed
     */
    LeaderOption(int value, String label) {
        this.value = value;
        this.label = label;
    }

    /**
     * Getter for the value attribute.
     * @return the numeric identifier shown in the menu
     */
    public int getValue() {
        return value;
    }
    /**
     * Getter for the label attribute.
     * @return the display text shown next to the menu number
     */
    public String getLabel() {
        return label;
    }

    /**
     * Returns the MemberOption whose value matches the given integer.
     * @param value the integer the user entered
     * @return the MemberOption matching the given value
     * @throws IllegalArgumentException if no option matches the given value
     */
    public static LeaderOption fromChoice(int value) {
        for (LeaderOption option : values()) {
            if (option.value == value) return option;
        }
        throw new IllegalArgumentException("Invalid menu choice: " + value);
    }

}

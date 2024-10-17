package lib.logy;

public enum LogyLevel {
    SUCCESS("\u001B[32m"),  // Green
    ERROR("\u001B[31m"),    // Red
    WARNING("\u001B[33m"),  // Yellow
    INFO("\u001B[37m");     // White

    public static final String RESET = "\u001B[0m";
    private final String colorCode;
    LogyLevel(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getColorCode() {
        return colorCode;
    }
}

package lib.logy;

public final class LogyMessenger {

    private LogyMessenger() {}

    public static void message(LogyTuple tuple) {
        System.out.println(tuple.level().getColorCode().concat(tuple.message()).concat(LogyLevel.RESET));
    }

    public static void messageF(LogyTuple tuple) {
        System.out.printf("LogLeve: %s, Message: %s\n", tuple.level().name(), tuple.message());
    }
}

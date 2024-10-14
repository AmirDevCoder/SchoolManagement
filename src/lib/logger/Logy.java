package lib.logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/// It can be inherited by others, but instantiation is not allowed.
public abstract class Logy {

    //TODO: store logs in a file
    private static final List<LogyTuple> logyStore = new ArrayList<>() {
        @Override
        public boolean add(LogyTuple logyTuple) {
            LogyMessenger.message(logyTuple);
            return super.add(logyTuple);
        }
    };

    public static void log(LogyLevel level, Object input) {
        logyStore.add(new LogyTuple(level, input.toString()));
    }

    public static void logOfMessages(LogyLevel logyLevel) {
        logyStore
                .stream()
                .filter(logyTuple -> logyTuple.level().equals(logyLevel))
                .forEach(LogyMessenger::message);
    }

    public static void logOfAll() {
        logyStore.forEach(LogyMessenger::messageF);
    }

    public static void clear() {
        logyStore.clear();
    }

}

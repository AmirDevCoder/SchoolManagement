package util;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

// todo: supporting for SuppressException
public final class ResultWrapper<T> {
    private final T value;
    private final Err error;

    private ResultWrapper(T value, Err error) {
        this.value = value;
        this.error = error;
    }

    public static <T> ResultWrapper<T> empty() {
        return new ResultWrapper<>(null, null);
    }

    public static <U> ResultWrapper<U> ok(U value) {
        return new ResultWrapper<>(value, null);
    }

    public static <U> ResultWrapper<U> err(String operation, String error) {
        return new ResultWrapper<>(null, Err.of(operation, error, null));
    }

    public static <U> ResultWrapper<U> err(String operation, String error, Pair<String> metaDate) {
        return new ResultWrapper<>(null, Err.of(operation, error, metaDate));
    }

    public boolean isSuccess() {
        return error == null;
    }

    public T getValue() {
        return value;
    }

    public String getError() {
        return (!isSuccess()) ? error.toString() : "null";
    }

    public void ifPresent(Consumer<T> consumer) {
        if (isSuccess()) {
            consumer.accept(value);
        }
    }

    public void ifPresentOrElse(Consumer<T> consumer, Runnable runnable) {
        if (isSuccess()) {
            consumer.accept(value);
        } else {
            runnable.run();
        }
    }

    /// It should be ues for somewhere that you can't return any type (void method or constructor)
    public T orElseThrow() {
        if (this.value == null) {
            throw new NoSuchElementException("No value present");
        } else {
            return this.value;
        }
    }

    /// It should be ues for somewhere that you can't return any type (void method or constructor)
    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (this.value != null) {
            return this.value;
        } else {
            throw (X) exceptionSupplier.get();
        }
    }

    public <U> ResultWrapper<U> map(Function<? super T, ? extends U> mapper) {
        if (this.isSuccess()) {
            U newValue = mapper.apply(getValue());
            return ResultWrapper.ok(newValue);
        } else {
            return ResultWrapper.err(getClass().getSimpleName().concat(".map"), getError());
        }
    }

    private static class Err {
        private final String operation;
        private final String message;
        private final Map<String, String> map = new HashMap<>();

        private Err(String operation, String message, Pair<String> metaDate) {
            this.operation = operation;
            this.message = message;
            if (metaDate != null) {
                map.put(metaDate.first(), metaDate.second());
            }
        }

        static Err of(String operation, String message, Pair<String> metaDate) {
            return new Err(operation, message, metaDate);
        }

        @Override
        public String toString() {
            return String.format("Operation: %s -> Message: %s, \nMetaData: %s\n", operation, message, map);
        }
    }

    @Override
    public String toString() {
        if (!isSuccess()) return getError();
        else return (getValue() != null) ? getValue().toString() : "null";
    }
}

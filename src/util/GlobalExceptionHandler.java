package util;

import lib.logy.Logy;
import lib.logy.LogyLevel;

/// It isn't GlobalException, it just captures exceptions for a thread that catch on it (usually the Main-Thread)
public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        Logy.log(LogyLevel.INFO, throwable.fillInStackTrace());
    }

}

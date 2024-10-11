package locator;

import lombok.experimental.UtilityClass;
import util.GlobalExceptionHandler;

import java.util.Map;

@UtilityClass
public class ServiceLocator {
    private static final Map<Class<? extends LocatableService>, LocatableService> services;

    static {
        try {
            Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler());
            services = ServiceFactory.getServices();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static <T extends LocatableService> T getService(Class<T> clazz) {
        LocatableService service = services.get(clazz);
        if (service == null) {
            throw new IllegalArgumentException("Unknown service: " + clazz.getName());
        }
        return clazz.cast(service);
    }

}

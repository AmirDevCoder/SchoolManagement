package presenter.aop;

import domain.service.LoggerService;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Proxy;

@RequiredArgsConstructor
public class ProxyFactory {
    private final LoggerService loggerSvc;

    @SuppressWarnings("unchecked")
    public <T> T createProxy(T target, Class<T> interfaceType) {
        return (T) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                new Class<?>[]{interfaceType},
                new LoggingInvocationHandler(target, loggerSvc)
        );
    }

}

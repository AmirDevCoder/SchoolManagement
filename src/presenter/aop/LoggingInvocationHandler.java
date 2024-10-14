package presenter.aop;

import domain.service.LoggerService;
import lib.logger.Logy;
import lib.logger.LogyLevel;
import lombok.RequiredArgsConstructor;
import util.ResultWrapper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@RequiredArgsConstructor
public class LoggingInvocationHandler implements InvocationHandler {
    private final Object target;
    private final LoggerService loggerSvc;

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        Logy.log(LogyLevel.INFO, "Executing: " + method.getName());
        try {
            Object result = method.invoke(target, args);
            if (result instanceof ResultWrapper<?> resWrapper) {
                if (resWrapper.isSuccess()) {
                    Logy.log(LogyLevel.SUCCESS, "Success: " + method.getName());
//                    loggerSvc.save();
                }
            }
            return result;
        } catch (Exception e) {
            Logy.log(LogyLevel.ERROR, "Failure: " + method.getName());
            throw e;
        }
    }

    /*
    *     @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long startTime = System.currentTimeMillis();

        // Invoke the method on the actual object
        Object result = method.invoke(target, args);

        long endTime = System.currentTimeMillis();
        System.out.println("Method " + method.getName() + " executed in " + (endTime - startTime) + " ms");

        return result;
    }
    * */

}

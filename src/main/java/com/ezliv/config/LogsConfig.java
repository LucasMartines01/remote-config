package com.ezliv.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.CompletionException;

@Configuration
@Aspect
@Slf4j
public class LogsConfig {

    @Around("execution(* com.ezliv.infra.gateways.ConfigGatewayImpl.*(..))")
    public Object logAllMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            log.info("Method: " + joinPoint.getSignature().getName() + " called");
            Object result = joinPoint.proceed();
            long timeTaken = System.currentTimeMillis() - startTime;
            log.info("Method: " + joinPoint.getSignature().getName() + " executed in " + timeTaken + "ms");
            return result;
        } catch (Exception ex) {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            Class<?>[] paramTypes = methodSignature.getParameterTypes();
            String[] paramNames = methodSignature.getParameterNames();
            Object[] paramValues = joinPoint.getArgs();
            Throwable rootCause = ex instanceof CompletionException ? ex.getCause() : ex;
            String exceptionName = rootCause.getClass().getSimpleName();
            String exceptionMessage = rootCause.getMessage();

            log.error(
                    "Exception in method {} -> | Params: {} | Values: {} | Types: {} | Exception Type: {} | Exception Message: {}",
                    joinPoint.getSignature().getName(),
                    Arrays.toString(paramNames),
                    Arrays.toString(paramValues),
                    Arrays.toString(paramTypes),
                    exceptionName,
                    exceptionMessage
            );
            throw ex;
        }
    }
}

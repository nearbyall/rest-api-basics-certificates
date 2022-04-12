package com.epam.esm.certificates.service.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Aspect for performing Log4j logging.
 */
@Aspect
@Component
@EnableAspectJAutoProxy
public class LoggingAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("@annotation(Loggable)")
    public void executeLogging() {
    }

    @Before(value = "executeLogging()")
    public void logMethodCallBefore(JoinPoint joinPoint) {
        StringBuilder sb = new StringBuilder();

        sb.append("BEFORE - ").append(joinPoint).append(": [");
        Arrays.stream(joinPoint.getArgs()).sequential().forEach(arg -> sb.append(arg).append(", "));
        sb.delete(sb.length() - 2, sb.length()).append("]");
        String message = sb.toString();

        LOGGER.info(message);
    }

    @AfterReturning(value = "executeLogging()", returning = "returnObject")
    public void logMethodCallAfterReturn(JoinPoint joinPoint, Object returnObject) {
        StringBuilder sb = new StringBuilder();

        sb.append("RETURNING - ").append(joinPoint).append(": [").append(returnObject);
        sb.delete(sb.length() - 2, sb.length()).append("]");
        String message = sb.toString();

        LOGGER.info(message);
    }

}

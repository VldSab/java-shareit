package ru.practicum.shareit.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Service;

@Aspect
@Service
@Slf4j
public class LoggingAspect {
    @Pointcut("within(ru.practicum.shareit.user.controller.UserController) " +
            "|| within(ru.practicum.shareit.item.ItemController)" +
            "|| within(ru.practicum.shareit.booking.controller.BookingController)" +
            "|| within(ru.practicum.shareit.exceptions.MainExceptionHandler)")
    public void logControllers() {
    }

    @AfterReturning(value = "logControllers()", returning = "result")
    public void logMethodCall(JoinPoint jp, Object result) {
        log.info("Method: " + jp.getSignature() + " returned " + result);
    }

}

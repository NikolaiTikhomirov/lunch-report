package com.gb.aspect;

import com.gb.LoggingProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class RecoverExceptionAspect {

    private final LoggingProperties properties;

    @Pointcut("@annotation(com.gb.aspect.RecoverException)")
    public void methodsAnnotatedWith() {
    }

    @Around(value = "methodsAnnotatedWith()")
    public Object recoverException (ProceedingJoinPoint joinPoint) throws Throwable{
        try {
            return joinPoint.proceed();
        }
        catch (Throwable e){
            Class<? extends RuntimeException> [] exceptionsToThrow = extractNoRecoverFor(joinPoint);
            for (Class<? extends RuntimeException> aClass : exceptionsToThrow) {
                if (aClass.isAssignableFrom(e.getClass())){
                    throw e;
                }
            }
        }
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        if (int.class.equals(signature.getMethod().getAnnotatedReturnType().getType())) {
            log.info("int was returned with 0");
            return 0;
        }
        if (double.class.equals(signature.getMethod().getAnnotatedReturnType().getType())) {
            log.info("double was returned with 0.0");return 0.0;
        }
        if (boolean.class.equals(signature.getMethod().getAnnotatedReturnType().getType())) {
            System.out.println("boolean was returned with false");
            return false;
        }
        else {
            log.info("null was returned");
            return null;
        }
    }

    private Class<? extends RuntimeException> [] extractNoRecoverFor(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RecoverException annotation = signature.getMethod().getAnnotation(RecoverException.class);
        if (annotation != null) {
            return annotation.noRecoverFor();
        }
        return joinPoint.getTarget().getClass().getAnnotation(RecoverException.class).noRecoverFor();
    }
}

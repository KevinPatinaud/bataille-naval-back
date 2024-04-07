package com.patinaud.batailleaspect.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LogAspect {
    @Before("execution(* com.patinaud.batailleplayer.ia.IaPlayerService.iaAttack(..))")
    public void adviceLogBefore(JoinPoint joinPoint) {

        System.out.println("Action effectuée avant la méthode : "
                + joinPoint.getSignature().getDeclaringTypeName()
                + "."
                + joinPoint.getSignature().getName()
                + " avec les arguments "
                + Arrays.toString(joinPoint.getArgs())
        );
    }

    @AfterReturning(pointcut = "execution(* com.patinaud.batailleplayer.ia.IaPlayerService.iaAttack(..))", returning = "result")
    public Object adviceLogAfterReturning(JoinPoint joinPoint, Object result) {

        System.out.println("La méthode : "
                + joinPoint.getSignature().getDeclaringTypeName()
                + "."
                + joinPoint.getSignature().getName()
                + " retourne : "
                + result
        );

        return result;
    }

}

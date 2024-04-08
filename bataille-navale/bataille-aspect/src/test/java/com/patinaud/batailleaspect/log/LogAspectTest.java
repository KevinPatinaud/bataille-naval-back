package com.patinaud.batailleaspect.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class LogAspectTest {

    @InjectMocks
    private LogAspect logAspect;

    @Mock
    private JoinPoint joinPoint;

    @Mock
    private Signature signature;

    @Test
    public void testAdviceLogBefore() {
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getName()).thenReturn("iaAttack");
        when(signature.getDeclaringTypeName()).thenReturn("com.patinaud.batailleplayer.ia.IaPlayerService");
        when(joinPoint.getArgs()).thenReturn(new Object[]{"arg1", "arg2"});

        logAspect.adviceLogBefore(joinPoint);

    }

    @Test
    public void testAdviceLogAfterReturning() {
        Object result = new Object();
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getName()).thenReturn("iaAttack");
        when(signature.getDeclaringTypeName()).thenReturn("com.patinaud.batailleplayer.ia.IaPlayerService");

        logAspect.adviceLogAfterReturning(joinPoint, result);

    }
}

package org.daigc.sharding;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class ShardingAspectSupport {

    @Around("@annotation(org.daigc.sharding.Sharding)")
    public void invoke(ProceedingJoinPoint pjp) {
        long e = System.currentTimeMillis();
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        Sharding sharding = AnnotationUtils.getAnnotation(method, Sharding.class);
        if (sharding.key().isEmpty()) {
            ShardingContext.bind(sharding.writing());
        } else {
            ParameterNameDiscoverer pnd = new StandardReflectionParameterNameDiscoverer();
            EvaluationContext context = new MethodBasedEvaluationContext(pjp.getTarget(), method, pjp.getArgs(), pnd);
            Object key = new SpelExpressionParser().parseExpression(sharding.key()).getValue(context);
            ShardingContext.bind(key, sharding.writing());
        }    
        Object r = null;
        try {
            r = pjp.proceed();
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        } finally {
            if (log.isDebugEnabled()) {
                e = System.currentTimeMillis() - e;
                String clazz = method.getDeclaringClass().getSimpleName();
                String p = Arrays.toString(pjp.getArgs());
                log.debug("It took {} ms for {}.{}({})={} from {}", e, clazz, method.getName(), p, r, ShardingContext.getCurrent());
            }
            ShardingContext.unbind();
        }
    }

}

package org.daigc.sharding;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Aspect
@Component
public class CrossingAspectSupport {

    @Around("@annotation(org.daigc.sharding.Crossing)")
    public List<Object> merge(ProceedingJoinPoint pjp) {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        if (log.isDebugEnabled()) {
            String clazz = method.getDeclaringClass().getName();
            log.debug("It start merging for {}.{}({})", clazz, method.getName(), Arrays.toString(pjp.getArgs()));
        }
        Crossing crossing = AnnotationUtils.getAnnotation(method, Crossing.class);
        List<Object> list = new LinkedList<>();
        ShardingContext.getShards().forEach(shard -> {
            long e = System.currentTimeMillis();
            ShardingContext.bind(shard, crossing.writing());
            Object r = null;
            try {
                r = pjp.proceed();
                list.add(r);
            } catch (Throwable ex) {
                if (crossing.throwable()) {
                    throw new RuntimeException(ex);
                }
                if (log.isWarnEnabled()) {
                    log.warn(ex.getMessage(), ex);
                }
            } finally {
                if (log.isDebugEnabled()) {
                    e = System.currentTimeMillis() - e;
                    log.debug("It took {} ms for {} from {}", e, r, ShardingContext.getCurrent());
                }
            }
        });
        ShardingContext.unbind();
        return list;
    }

}

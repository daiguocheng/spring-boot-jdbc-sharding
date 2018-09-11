package org.daigc.sharding;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

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
        Set<MasterSlaversShard> shards = ShardingContext.getShards();
        return new ForkJoinPool().invoke(new SharkTask(pjp, crossing, shards.toArray(new MasterSlaversShard[shards.size()])));
    }


    static class SharkTask extends RecursiveTask<List<Object>> {

        private ProceedingJoinPoint pjp;
        private Crossing crossing;
        private MasterSlaversShard[] shards;

        SharkTask(ProceedingJoinPoint pjp, Crossing crossing, MasterSlaversShard[] shards) {
            this.pjp = pjp;
            this.crossing = crossing;
            this.shards = shards;
        }

        @Override
        protected List<Object> compute() {
            if (shards.length == 1) {
                return compute(shards[0]);
            }
            int middle = shards.length / 2;
            SharkTask left = new SharkTask(pjp, crossing, Arrays.copyOfRange(shards, 0, middle));
            left.fork();
            List<Object> list = new SharkTask(pjp, crossing, Arrays.copyOfRange(shards, middle, shards.length)).compute();
            list.addAll(left.join());
            return list;
        }

        private List<Object> compute(MasterSlaversShard shard) {
            List<Object> list = new LinkedList<>();
            long e = System.currentTimeMillis();
            Object r = null;
            try {
                ShardingContext.bind(shard, crossing.writing());
                r = pjp.proceed();
                if (r instanceof Collection) {
                    list.addAll((Collection<?>) r);
                } else {
                    list.add(r);
                }
            } catch (Throwable ex) {
                if (log.isWarnEnabled()) {
                    log.warn(ex.getMessage(), ex);
                }
            } finally {
                if (log.isDebugEnabled()) {
                    e = System.currentTimeMillis() - e;
                    log.debug("It took {} ms for {} from {}", e, r, ShardingContext.getCurrent());
                }
                ShardingContext.unbind();
            }
            return list;
        }
    }

}

package org.daigc.sharding;

import java.lang.annotation.*;

/**
 * 跨分片模式————目标方法在每个分片各执行一次，须谨慎使用
 * */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Crossing {

    /**
     * 当前是否为写操作
     * */
    boolean writing() default false;

    /**
     * 是否抛出异常；默认记录警告级别日志
     * */
    boolean throwable() default false;
}

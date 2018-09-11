package org.daigc.sharding;

import java.lang.annotation.*;

/**
 * 跨分片模式————目标方法在每个分片各执行一次，须谨慎使用
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Crossing {

    /**
     * 当前是否为写操作
     */
    boolean writing() default true;
}

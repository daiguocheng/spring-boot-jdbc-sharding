package org.daigc.sharding;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;


/**
 * 单分片模式————目标方法仅在某个分片（key 不为空时）执行一次
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sharding {

    @AliasFor("key")
    String value() default "";

    /**
     * 分片关键字，参考 {@link Cacheable#key()}；默认不分片
     */
    @AliasFor("value")
    String key() default "";

    /**
     * 当前是否为写操作
     */
    boolean writing() default false;
}

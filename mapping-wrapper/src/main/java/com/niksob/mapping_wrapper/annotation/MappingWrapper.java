package com.niksob.mapping_wrapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface MappingWrapper {
    Class<?> source();
    Class<?>[] mapper();
    boolean isSpringComponentEnabled() default true;
}

package com.niksob.layer_connector.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface LayerConnector {
    Class<?> source();
    Class<?>[] mapper();
    boolean isSpringComponentEnabled() default true;
}

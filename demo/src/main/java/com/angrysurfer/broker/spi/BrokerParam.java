package com.angrysurfer.broker.spi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface BrokerParam {
    /** The name of the parameter in the incoming request's 'params' map. If blank, the method parameter name is used. */
    String value() default "";

    /** Whether the parameter is required. Defaults to true. */
    boolean required() default true;
}

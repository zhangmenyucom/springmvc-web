package com.taylor.annotation;

import java.lang.annotation.*;

/**
 * @author xiaolu.zhang
 * @desc:
 * @date: 2017/12/1 0:50
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface TestAnnotation {
    int type() default 1;

    String name() default "";
}

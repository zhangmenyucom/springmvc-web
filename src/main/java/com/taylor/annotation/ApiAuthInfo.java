package com.taylor.annotation;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiAuthInfo {
	public enum UidFrom {
		Cookie, Parameter, Path
	}
	
    UidFrom uidFrom() default UidFrom.Cookie;

	boolean required() default true;

	String name() default "uid";
}

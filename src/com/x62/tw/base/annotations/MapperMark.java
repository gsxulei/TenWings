package com.x62.tw.base.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;


/**
 * Mapper标记
 * 
 * @author GSXL
 */

@Retention(RUNTIME)
@Target(FIELD)
public @interface MapperMark
{
	String resource() default "";
}
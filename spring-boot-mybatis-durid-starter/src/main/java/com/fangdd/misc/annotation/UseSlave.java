package com.fangdd.misc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lantian
 */
@Target(ElementType.METHOD)    
@Retention(RetentionPolicy.RUNTIME)    
@Documented   
@Inherited
public @interface UseSlave {
	/**
	 * 指定使用的slave数据源，默认随机选择一个slave数据源
	 * @return
	 */
	public String key() default "";
}

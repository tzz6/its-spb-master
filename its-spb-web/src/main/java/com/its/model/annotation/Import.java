package com.its.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Import {
	
	/**列名称**/
    public String columnName() default "";
    
    /**列索引**/
    public int columnIndex() default -1;
    
    /**是否必填**/
    public boolean required() default false;
    
    /**是否可以重复**/
    public boolean duplicable() default true;
    
    /** 描述 **/
    public String description() default ""; 

}

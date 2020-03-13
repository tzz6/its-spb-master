package com.its.base.servers.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Import {

	/**列名称**/
    String columnName() default "";

    /**列索引**/
    int columnIndex() default -1;

    /**是否必填**/
    boolean required() default false;

    /**是否可以重复**/
    boolean duplicable() default true;

    /** 描述 **/
    String description() default "";

}

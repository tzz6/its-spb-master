package com.its.common.es.rhl.annotation;

import java.lang.annotation.*;

/**
  * Description: ES entity 标识ID的注解,在es entity field上添加
  * Company: tzz
  * @Author: tzz
  * Date: 2019/12/19 17:14
  */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
public @interface ESID {
}

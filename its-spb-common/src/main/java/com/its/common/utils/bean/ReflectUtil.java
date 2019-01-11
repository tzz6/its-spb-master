package com.its.common.utils.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * ReflectUtil
 *
 */
public class ReflectUtil {
	
	/** 方法--属性复制 */
	public static void fieldCopy(Object source, Object target) throws Exception {
		Method[] methods = source.getClass().getDeclaredMethods();
		for (Method method : methods) {
			String methodName = method.getName();
			System.out.println(methodName);
			if (methodName.startsWith("get")) {
				Object value = method.invoke(source, new Object[0]);
				System.out.println(value);
				String setMethodName = methodName.replaceFirst("(get)", "set");
				Method setMethod = target.getClass().getMethod(setMethodName, method.getReturnType());
				setMethod.invoke(target, value);
			}
		}
	}

	/** 属性字段名、值、数据类型 */
	public static void getFields(Object object) throws Exception {
		Field[] fields = object.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			String classType = field.getType().toString();
			int lastIndex = classType.lastIndexOf(".");
			classType = classType.substring(lastIndex + 1);
			System.out.println("fieldName：" + field.getName() + ",type:" + classType + ",value:" + field.get(object));
		}
	}
	
	/** 根据属性名设置值 */
	public static void setFieldValue(Object object, String fieldName, Object value) {
		try {
			Method[] methods = object.getClass().getDeclaredMethods();
			fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());
			for (Method method : methods) {
				String methodName = method.getName();
				if (methodName.equals("set" + fieldName)) {
					method.invoke(object, value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

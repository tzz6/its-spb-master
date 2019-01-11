package com.its.common.utils;

import java.net.URL;

/**
 * 资源加载
 *
 */
public class ResourceLoaderUtil {
	public static String CLASS_PATH_PREFIX = "classpath:";

	/** classpath中获取资源 */
	public static URL getResource(String resource) {
		ClassLoader classLoader = null;
		classLoader = Thread.currentThread().getContextClassLoader();
		return classLoader.getResource(resource);
	}

	/** classpath 中搜索路径 */
	public static String getPath(String resource) {
		if (resource != null) {
			if (resource.startsWith(CLASS_PATH_PREFIX)) {
				resource = getPath("") + resource.replaceAll(CLASS_PATH_PREFIX, "");
			}
		}

		URL url = getResource(resource);
		if (url == null)
			return null;
		return url.getPath().replaceAll("%20", " ");
	}

	public static String getPath(String resource, Class<?> clazz) {
		URL url = getResource(resource, clazz);
		if (url == null)
			return null;
		return url.getPath().replaceAll("%20", " ");
	}

	/** 指定class中获取资源 */
	public static URL getResource(String resource, Class<?> clazz) {
		return clazz.getResource(resource);
	}

	public static Class<?> loadClass(String clazz) throws ClassNotFoundException {
		return Class.forName(clazz);
	}
}

package com.its.common.utils.java;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 
 * @author tzz
 * @date 2019/02/22
 * @Introduce: Write describe here
 */
public class MapUtil {

	/**
	 * 方法一 在for-each循环中使用entries来遍历 这是最常见的并且在大多数情况下也是最可取的遍历方式。在键值都需要时使用
	 */
	public static void forEachMap(Map<String, Object> map) {
		Set<String> keys = map.keySet();
		for (String key : keys) {
			System.out.println(key + "---" + map.get(key));
		}
	}

	/**
	 * 方法一 :如果只需要map中的键或者值，你可以通过keySet或values来实现遍历 比entrySet性能
	 */
	public static void forEach(Map<String, Object> map) {

		Set<String> keys = map.keySet();
		for (String key : keys) {
			System.out.println("key:" + map.get(key));
		}
		for (Object value : map.values()) {
			System.out.println("value:" + value);
		}
	}

	/**
	 * 方法三：使用Iterators
	 */
	public static void iteratorsMap(Map<String, Object> map) {
		Iterator<Entry<String, Object>> iterators = map.entrySet().iterator();
		while (iterators.hasNext()) {
			Entry<String, Object> entry = iterators.next();
			System.out.println(entry.getKey() + "---" + entry.getValue());
		}
	}

	/**
	 * 方法四 :通过键找值遍历(效率低)
	 */
	public static void entryMap(Map<String, Object> map) {
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			System.out.println(entry.getKey() + "--" + entry.getValue());
		}
	}
}

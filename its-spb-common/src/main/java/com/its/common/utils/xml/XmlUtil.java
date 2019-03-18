package com.its.common.utils.xml;

import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * 
 * @author tzz
 */
public class XmlUtil {

	/**
	 * 解析xml生成Map--支持N层子节点
	 * 
	 * @param xml
	 * @return
	 * @throws DocumentException
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> xmlToMap(String xml) {
		Map<String, Object> map = null;
		try {
			if (xml != null && !"".equals(xml)) {
				map = new HashMap<String, Object>(16);
				Element root = DocumentHelper.parseText(xml).getRootElement();
				List<Element> elements = root.elements();
				for (Element element : elements) {
					List<Element> childElements = element.elements();
					if (childElements.size() > 0) {
						map.put(element.getName(), xmlToMap(element));
					} else{
					    map.put(element.getName(), element.getText());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> xmlToMap(Element element) {
		Map<String, Object> xmlMap = new HashMap<String, Object>(16);
		List<Element> elements = element.elements();
		int size = elements.size();
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				Element ele = (Element) elements.get(i);
				List<Object> listMap = null;
				if (ele.elements().size() > 0) {
					Map<String, Object> map = xmlToMap(ele);
					Object object = xmlMap.get(ele.getName());
					if (object != null) {
						if (!(object instanceof List)) {
							listMap = new ArrayList<Object>();
							listMap.add(object);
							listMap.add(map);
						}
						if (object instanceof List) {
							listMap = (List<Object>) object;
							listMap.add(map);
						}
						xmlMap.put(ele.getName(), listMap);
					} else {
						xmlMap.put(ele.getName(), map);
					}
				} else {
					Object object = xmlMap.get(ele.getName());
					if (object != null) {
						if (!(object instanceof List)) {
							listMap = new ArrayList<Object>();
							listMap.add(object);
							listMap.add(ele.getText());
						}
						if (object instanceof List) {
							listMap = (List<Object>) object;
							listMap.add(ele.getText());
						}
						xmlMap.put(ele.getName(), listMap);
					} else {
						xmlMap.put(ele.getName(), ele.getText());
					}
				}
			}
		} else {
			xmlMap.put(element.getName(), element.getText());
		}
		return xmlMap;
	}

	@SuppressWarnings("unchecked")
	public static Object getMapValue(Map<String, Object> map, String key) {
		Set<String> keySet = map.keySet();
		for (String mapKey : keySet) {
			Object object = map.get(mapKey);
			if (key != null && key.equals(mapKey)) {
				return object;
			} else {
				if (object instanceof Map) {
					return getMapValue((Map<String, Object>) object, key);
				}
			}
		}
		return null;
	}

	/**
	 * 扩展xstream使其支持CDATA
	 */
	@SuppressWarnings("unused")
	private static XStream xstream = new XStream(new XppDriver() {
	    @Override
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				// 对所有xml节点的转换都增加CDATA标记
				boolean cdata = true;

				@Override
				@SuppressWarnings("rawtypes")
				public void startNode(String name, Class clazz) {
					super.startNode(name, clazz);
				}

				@Override
				protected void writeText(QuickWriter writer, String text) {
					if (cdata) {
						writer.write("<![CDATA[");
						writer.write(text);
						writer.write("]]>");
					} else {
						writer.write(text);
					}
				}
			};
		}
	});
}

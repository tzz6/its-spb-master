package com.its.common.utils.xml;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * Jaxb 2.0
 *
 */
public class JaxbXmlUtil {

	@SuppressWarnings("unchecked")
	public static <T> T xmlToBean(String xml, Class<T> beanClass) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(beanClass);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			T user = (T) unmarshaller.unmarshal(new StringReader(xml));
			return user;
		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**Bean转XML*/
	public static String beanToXml(Object obj) {
		String result = null;
		try {
			JAXBContext context = JAXBContext.newInstance(obj.getClass());
			Marshaller marshaller = context.createMarshaller();
			//决定是否在转换成xml时同时进行格式化（即按标签自动换行，否则即是一行的xml）
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			//Marshaller.JAXB_ENCODING xml的编码方式
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "utf-8");
			StringWriter writer = new StringWriter();
			marshaller.marshal(obj, writer);
			result = writer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
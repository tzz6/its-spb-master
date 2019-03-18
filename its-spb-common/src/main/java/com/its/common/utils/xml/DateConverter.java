package com.its.common.utils.xml;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * 
 * @author tzz
 */
@SuppressWarnings("rawtypes")
public class DateConverter implements Converter {

	/**判断字段是否属于要转换的类型*/
	@Override
	public boolean canConvert(Class paramClass) {
		return Date.class.isAssignableFrom(paramClass);
	}

	/** 对象转化为xml */
	@Override
	public void marshal(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		writer.setValue(format.format(object));
	}

	/** xml转化为对象 */
	@Override
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return dateFormat.parse(reader.getValue());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

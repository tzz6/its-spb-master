package com.its.common.utils.xml;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * 
 * @author tzz
 */
public class JaxbDateSerializer extends XmlAdapter<String, Date> {
    /** SimpleDateFormat 是线程不安全的类，一般不要定义为static变量，如果定义为static，必须加锁，或者使用DateUtils工具类。 */
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    /**
     * 说明：如果是JDK8的应用，可以使用Instant代替Date，LocalDateTime代替Calendar，DateTimeFormatter代替SimpleDateFormat，官方给出的解释：simple
     * beautiful strong immutable thread-safe。
     */
    private static final ThreadLocal<DateFormat> DF = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        }
    };

    @Override
    public String marshal(Date date) throws Exception {
        return DF.get().format(date);
    }

    @Override
    public Date unmarshal(String date) throws Exception {
        return dateFormat.parse(date);
    }
}

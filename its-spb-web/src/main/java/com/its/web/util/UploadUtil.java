package com.its.web.util;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.its.common.utils.Constants;
import com.its.common.utils.ImportError;
import com.its.model.annotation.Import;

public class UploadUtil<T> {

	public static Logger logger = LogManager.getLogger(UploadUtil.class);

	/**
	 * 检查Excel表头
	 * 
	 * @param imptClass
	 * @param row
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	public ImportError checkHead(Class<T> imptClass, Row row, String lang) throws Exception {
		ImportError errorForm = new ImportError();
		String description = "";
		errorForm.setRowNum(1 + "");
		StringBuffer buffer = new StringBuffer();
		Field[] fields = imptClass.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			Import annotation = field.getAnnotation(Import.class);
			if (annotation == null)
				continue;
			if (!StringUtils.isEmpty(annotation.description())) {
				description = ResourceBundleHelper.get(lang, annotation.description());
			}

			Cell data = row.getCell(annotation.columnIndex());
			String cellString = data.getStringCellValue();
			if (StringUtils.isBlank(cellString) || !cellString.equalsIgnoreCase(description)) {
				buffer.append(ResourceBundleHelper.get(lang, Constants.Excel.EXCEL_HEADER_COLUMN)
						+ (annotation.columnIndex() + 1)
						+ ResourceBundleHelper.get(lang, Constants.Excel.EXCEL_HEADER_SHOULD) + ":" + description);
			}
			field.setAccessible(false);
		}

		if (StringUtils.isBlank(buffer.toString())) {
			return null;
		}
		errorForm.setErrorInfo(buffer.toString());
		return errorForm;
	}

	/**
	 * 检查Excel表头
	 * 
	 * @param imptClass
	 * @param headers
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	public ImportError checkHead(Class<T> imptClass, List<String> headers, String lang) throws Exception {
		ImportError errorForm = new ImportError();
		String description = "";
		errorForm.setRowNum(1 + "");
		StringBuffer buffer = new StringBuffer();
		Field[] fields = imptClass.getDeclaredFields();
		int index = 0;
		for (Field field : fields) {
			field.setAccessible(true);
			Import annotation = field.getAnnotation(Import.class);
			if (annotation == null)
				continue;
			String anDescription = annotation.description();
			if (!StringUtils.isEmpty(anDescription)) {
				description = ResourceBundleHelper.get(lang, anDescription);
			}

			if (StringUtils.isBlank(headers.get(index)) || !headers.get(index).equalsIgnoreCase(description)) {
				buffer.append(ResourceBundleHelper.get(lang, Constants.Excel.EXCEL_HEADER_COLUMN)
						+ (annotation.columnIndex() + 1)
						+ ResourceBundleHelper.get(lang, Constants.Excel.EXCEL_HEADER_SHOULD) + ":" + description);
			}
			field.setAccessible(false);
			index++;
		}

		if (StringUtils.isBlank(buffer.toString())) {
			return null;
		}
		errorForm.setErrorInfo(buffer.toString());
		return errorForm;
	}

}
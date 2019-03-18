package com.its.common.utils.poi;

import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Excel超大数据读取，抽象Excel2007读取器 excel2007的底层数据结构是xml文件，采用SAX的事件驱动的方法解析
 * xml，需要继承DefaultHandler，在遇到文件内容时，事件会触发，这种做法可以大大降低 内存的耗费，特别使用于大数据量的文件。
 * @author tzz
 */
public abstract class AbstractExcelHandler extends DefaultHandler {

	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(AbstractExcelHandler.class);

	/** 共享字符串表*/
	private SharedStringsTable sst;
	/** 上一次的内容*/
	private String lastContents;
	private boolean nextIsString;

	private int sheetIndex = -1;
	private List<String> rowList = new ArrayList<String>();

	/** 当前行*/
	private int curRow = 0;
	/** 当前列*/
	private int curCol = 0;
	/** 日期标志*/
	private boolean dateFlag;
	/** 数字标志*/
	private boolean numberFlag;

	private boolean isTElement;

	private boolean isEmpty = false;

	/**
	 * 遍历Sheet中所有的电子表格
	 * 
	 * @param filename
	 * @throws Exception
	 */
	public void process(String filename) throws Exception {
		OPCPackage pkg = OPCPackage.open(filename);
		XSSFReader r = new XSSFReader(pkg);
		SharedStringsTable sst = r.getSharedStringsTable();
		XMLReader parser = fetchSheetParser(sst);
		Iterator<InputStream> sheets = r.getSheetsData();
		while (sheets.hasNext()) {
			curRow = 0;
			sheetIndex++;
			InputStream sheet = sheets.next();
			InputSource sheetSource = new InputSource(sheet);
			parser.parse(sheetSource);
			sheet.close();
			pkg.close();
		}
	}

	/**
	 * 只遍历一个Sheet，其中sheetId为要遍历的sheet索引，从1开始，1-3
	 * 
	 * @param filename
	 * @param sheetId
	 * @throws Exception
	 */
	public void process(String filename, int sheetId) throws Exception {
		OPCPackage pkg = OPCPackage.open(filename);
		XSSFReader r = new XSSFReader(pkg);
		SharedStringsTable sst = r.getSharedStringsTable();
		XMLReader parser = fetchSheetParser(sst);
		// 根据 rId# 或 rSheet# 查找sheet
		InputStream sheet2 = r.getSheet("rId" + sheetId);
		sheetIndex++;
		InputSource sheetSource = new InputSource(sheet2);
		parser.parse(sheetSource);
		sheet2.close();
		pkg.close();
	}

	public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException {
		XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
		this.sst = sst;
		parser.setContentHandler(this);
		return parser;
	}

	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
//		logger.info("startElement: " + localName + ", " + name + ", " + attributes);
	    // c:单元格
	    String s = "s";
	    String c = "c";
	    String date = "1";
	    String type = "2";
		if (c.equals(name)) {
			// 如果下一个元素是 SST 的索引，则将nextIsString标记为true
			String cellType = attributes.getValue("t");
			if (s.equals(cellType)) {
				nextIsString = true;
			} else {
				nextIsString = false;
				isEmpty = true;
			}
			String cellDateType = attributes.getValue(s);
			// 日期格式
			if (date.equals(cellDateType)) {
				dateFlag = true;
			} else {
				dateFlag = false;
			}
			// 数字类型处理
			if (type.equals(cellDateType)) {
				numberFlag = true;
			} else {
				numberFlag = false;
			}
		}

		// 当元素为t时
		String t = "t";
		if (t.equals(name)) {
			isTElement = true;
		} else {
			isTElement = false;
		}
		// 置空
		lastContents = "";
	}

	@Override
	@SuppressWarnings("unused")
	public void endElement(String uri, String localName, String name) throws SAXException {
		// 根据SST的索引值的到单元格的真正要存储的字符串
		// 这时characters()方法可能会被调用多次
		if (nextIsString) {
			try {
				int idx = Integer.parseInt(lastContents);
				lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
			} catch (Exception e) {
//				logger.info(nextIsString);
			}
		}
		// t元素也包含字符串
		String v = "v";
		String c = "c";
		String row = "row";
		if (isTElement) {
			String value = lastContents.trim();
			rowList.add(curCol, value);
			curCol++;
			isTElement = false;
		} else if (v.equals(name)) {
			// v => 单元格的值，如果单元格是字符串则v标签的值为该字符串在SST中的索引
			// 将单元格内容加入rowlist中，在这之前先去掉字符串前后的空白符
			String value = lastContents.trim();
			value = "".equals(value) ? " " : value;
			try {
			    // 日期格式处理
				if (dateFlag) {
					Date date = HSSFDateUtil.getJavaDate(Double.valueOf(value));
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//					value = dateFormat.format(date);
				}
				// 数字类型处理
				if (numberFlag) {
					BigDecimal bd = new BigDecimal(value);
//					value = bd.setScale(3, BigDecimal.ROUND_UP).toString();
				}
			} catch (Exception e) {
//				logger.info("转换失败仍用读出来的值:" + value);
			}
			rowList.add(curCol, value);
			curCol++;
		} else if (isEmpty && c.equals(name)) {
			rowList.add(curCol, null);
			curCol++;
		} else {
			// 如果标签名称为 row ，这说明已到行尾，调用 optRows() 方法
			if (row.equals(name)) {
				getRows(sheetIndex + 1, curRow, rowList);
				rowList.clear();
				curRow++;
				curCol = 0;
			}
		}
		isEmpty = false;
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		// 得到单元格内容的值
		lastContents += new String(ch, start, length);
	}

	/**
	 * 获取行数据回调
	 * 
	 * @param sheetIndex
	 * @param curRow
	 * @param rowList
	 */
	public abstract void getRows(int sheetIndex, int curRow, List<String> rowList);

}
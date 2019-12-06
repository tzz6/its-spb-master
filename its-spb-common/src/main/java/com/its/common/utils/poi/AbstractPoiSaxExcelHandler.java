package com.its.common.utils.poi;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
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
public abstract class AbstractPoiSaxExcelHandler extends DefaultHandler {

    private int sheetIndex = -1;
    private StylesTable stylesTable;
    /** 共享字符串表 */
    private SharedStringsTable sst;
    /** 上一次的内容 */
    private String lastContents;
    private boolean nextIsString;

    private List<String> rowlist = new ArrayList<String>();
    /** 当前行 */
    private int curRow = 0;
    /** 当前列 */
    private int curCol = 0;

    /** 定义前一个元素和当前元素的位置，用来计算其中空的单元格数量，如A6和A8等 */
    private String preRef = null, ref = null;
    /** 定义该文档一行最大的单元格数，用来补全一行最后可能缺失的单元格 */
    private String maxRef = null;

    private CellDataType nextDataType = CellDataType.SSTINDEX;
    private final DataFormatter formatter = new DataFormatter();
    private short formatIndex;
	private String formatString;

	enum CellDataType {
	    // 用一个enum表示单元格可能的数据类型
		BOOL, ERROR, FORMULA, INLINESTR, SSTINDEX, NUMBER, DATE, NULL
	}

	/**
	 * 遍历Sheet中所有的电子表格
	 * 
	 * @param filename
	 * @throws Exception
	 */
	public void process(String filename) throws Exception {
		OPCPackage pkg = OPCPackage.open(filename);
		XSSFReader r = new XSSFReader(pkg);
		stylesTable = r.getStylesTable();
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
	 * @param path
	 *            文件路径
	 * @param sheetId
	 * @throws Exception
	 */
	public void process(String path, int sheetId) throws Exception {
		OPCPackage pkg = OPCPackage.open(path);
		XSSFReader r = new XSSFReader(pkg);
		stylesTable = r.getStylesTable();
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

	/**
	 * 解析一个element的开始时触发事件
	 */
	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {

		// c => cell
	    String c = "c";
	    String s = "s";
		if (c.equals(name)) {
			// 前一个单元格的位置
			if (preRef == null) {
				preRef = attributes.getValue("r");
			} else {
				preRef = ref;
			}
			// 当前单元格的位置
			ref = attributes.getValue("r");

			this.setNextDataType(attributes);

			// Figure out if the value is an index in the SST
			String cellType = attributes.getValue("t");
			if (cellType != null && s.equals(cellType)) {
				nextIsString = true;
			} else {
				nextIsString = false;
			}

		}
		// Clear contents cache
		lastContents = "";
	}

	/**
	 * 根据element属性设置数据类型
	 * 
	 * @param attributes
	 */
	public void setNextDataType(Attributes attributes) {

		nextDataType = CellDataType.NUMBER;
		formatIndex = -1;
		formatString = null;
		String cellType = attributes.getValue("t");
		String cellStyleStr = attributes.getValue("s");
		String b = "b";
		String e = "e";
		String inlineStr = "inlineStr";
		String s = "s";
		String str = "str";
		if (b.equals(cellType)) {
			nextDataType = CellDataType.BOOL;
		} else if (e.equals(cellType)) {
			nextDataType = CellDataType.ERROR;
		} else if (inlineStr.equals(cellType)) {
			nextDataType = CellDataType.INLINESTR;
		} else if (s.equals(cellType)) {
			nextDataType = CellDataType.SSTINDEX;
		} else if (str.equals(cellType)) {
			nextDataType = CellDataType.FORMULA;
		}
		String mdyy = "m/d/yy";
		if (cellStyleStr != null) {
			int styleIndex = Integer.parseInt(cellStyleStr);
			XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
			formatIndex = style.getDataFormat();
			formatString = style.getDataFormatString();
			if (mdyy == formatString) {
				nextDataType = CellDataType.DATE;
				// full format is "yyyy-MM-dd hh:mm:ss.SSS";
				formatString = "yyyy-MM-dd";
			}
			if (formatString == null) {
				nextDataType = CellDataType.NULL;
				formatString = BuiltinFormats.getBuiltinFormat(formatIndex);
			}
		}
	}

	/**
	 * 解析一个element元素结束时触发事件
	 */
	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {
		// Process the last contents as required.
		// Do now, as characters() may be called more than once
		if (nextIsString) {
			try {
				int idx = Integer.parseInt(lastContents);
				lastContents = sst.getItemAt(idx).toString();
				nextIsString = false;
			} catch (Exception e) {
				// logger.info(nextIsString);
			}
		}

		// v => contents of a cell
		String v = "v";
		String row = "row";
		if (v.equals(name)) {
			String value = this.getDataValue(lastContents.trim(), "");
			// 补全单元格之间的空单元格
			if (!ref.equals(preRef)) {
				int len = countNullCell(ref, preRef);
				for (int i = 0; i < len; i++) {
					rowlist.add(curCol, "");
					curCol++;
				}
			}
			rowlist.add(curCol, value);
			curCol++;
		} else {
			// 如果标签名称为 row，这说明已到行尾，调用 optRows() 方法
			if (row.equals(name)) {
				// 默认第一行为表头，以该行单元格数目为最大数目
				if (curRow == 0) {
					maxRef = ref;
				}
				// 补全一行尾部可能缺失的单元格
				if (maxRef != null) {
					int len = countNullCell(maxRef, ref);
					for (int i = 0; i <= len; i++) {
						rowlist.add(curCol, "");
						curCol++;
					}
				}

				// 一行的末尾重置一些数据
				getRows(sheetIndex + 1, curRow, rowlist);
				rowlist.clear();
				curRow++;
				curCol = 0;
				preRef = null;
				ref = null;
			}
		}
	}

	/**
	 * 根据数据类型获取数据
	 * 
	 * @param value
	 * @param thisStr
	 * @return
	 */
	public String getDataValue(String value, String thisStr)

	{
		switch (nextDataType) {
		// 这几个的顺序不能随便交换，交换了很可能会导致数据错误
		case BOOL:
			char first = value.charAt(0);
			thisStr = first == '0' ? "FALSE" : "TRUE";
			break;
		case ERROR:
			thisStr = "\"ERROR:" + value.toString() + '"';
			break;
		case FORMULA:
			thisStr = '"' + value.toString() + '"';
			break;
		case INLINESTR:
			XSSFRichTextString rtsi = new XSSFRichTextString(value.toString());
			thisStr = rtsi.toString();
			rtsi = null;
			break;
		case SSTINDEX:
			thisStr = value.toString();
			break;
		case NUMBER:
			if (formatString != null) {
				thisStr = formatter.formatRawCellContents(Double.parseDouble(value), formatIndex, formatString).trim();
			} else {
				thisStr = value;
			}
			thisStr = thisStr.replace("_", "").trim();
			break;
		case DATE:
			try {
				thisStr = formatter.formatRawCellContents(Double.parseDouble(value), formatIndex, formatString);
			} catch (NumberFormatException ex) {
				thisStr = value.toString();
			}
			thisStr = thisStr.replace(" ", "");
			break;
		default:
			thisStr = "";
			break;
		}
		return thisStr;
	}

	/**
	 * 获取element的文本数据
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		lastContents += new String(ch, start, length);
	}

	/**
	 * 计算两个单元格之间的单元格数目(同一行)
	 * 
	 * @param ref
	 * @param preRef
	 * @return
	 */
	public int countNullCell(String ref, String preRef) {
		// excel2007最大行数是1048576，最大列数是16384，最后一列列名是XFD
		String xfd = ref.replaceAll("\\d+", "");
		String xfd1 = preRef.replaceAll("\\d+", "");

		xfd = fillChar(xfd, 3, '@', true);
		xfd1 = fillChar(xfd1, 3, '@', true);

		char[] letter = xfd.toCharArray();
		char[] letter1 = xfd1.toCharArray();
		int res = (letter[0] - letter1[0]) * 26 * 26 + (letter[1] - letter1[1]) * 26 + (letter[2] - letter1[2]);
		return res - 1;
	}

	/**
	 * 字符串的填充
	 * 
	 * @param str
	 * @param len
	 * @param let
	 * @param isPre
	 * @return
	 */
	String fillChar(String str, int len, char let, boolean isPre) {
		int len1 = str.length();
		if (len1 < len) {
			if (isPre) {
				for (int i = 0; i < (len - len1); i++) {
					str = let + str;
				}
			} else {
				for (int i = 0; i < (len - len1); i++) {
					str = str + let;
				}
			}
		}
		return str;
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
package com.its.common.utils.poi;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
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
 * 1. Office2007与Office Open XML
 * 
 * 在Office
 * 2007之前，Office一直都是以二进制位的方式存储，但这种格式不易被其它软件拿来使用，在各界的压力下，MicroSoft于2005年发布了基于XML的ooxml开放文档标准。ooxml的xml
 * schema强调减少load time，增快parsing speed，将child elements分开存储，而不是multiple
 * attributes一起存，这有点类似于HTML的结构。ooxml
 * 使用XML和ZIP技术结合进行文件存储，因为XML是一个基于文本的格式，而且ZIP容器支持内容的压缩，所以其一大优势就是可以大大减小文件的尺寸。其它特点这里不再叙述。
 * 
 * 2. SAX方式解析XML
 * 
 * SAX全称Simple API for
 * XML，它是一个接口，也是一个软件包。它是一种XML解析的替代方法，不同于DOM解析XML文档时把所有内容一次性加载到内存中的方式，它逐行扫描文档，一边扫描，一边解析。所以那些只需要单遍读取内容的应用程序就可以从SAX解析中受益，这对大型文档的解析是个巨大优势。另外，SAX
 * “推" 模型可用于广播环境，能够同时注册多个ContentHandler，并行接收事件，而不是在一个管道中一个接一个地进行处理。一些支持 SAX
 * 的语法分析器包括 Xerces，Apache parser（以前的 IBM 语法分析器）、MSXML（Microsoft 语法分析器）和
 * XDK（Oracle 语法分析器）。这些语法分析器是最灵活的，因为它们还支持 DOM。
 * 
 * 3. POI以SAX解析excel2007文件
 * 
 *
 */
public class SAXExcelHandler {

	private static StylesTable stylesTable;

	/**
	 * 处理一个sheet
	 * 
	 * @param filename
	 * @throws Exception
	 */
	public void processOneSheet(String filename) throws Exception {

		OPCPackage pkg = OPCPackage.open(filename);
		XSSFReader r = new XSSFReader(pkg);
		stylesTable = r.getStylesTable();
		SharedStringsTable sst = r.getSharedStringsTable();

		XMLReader parser = fetchSheetParser(sst);

		// Seems to either be rId# or rSheet#
		InputStream sheet2 = r.getSheet("rId1");
		InputSource sheetSource = new InputSource(sheet2);
		parser.parse(sheetSource);
		sheet2.close();
	}

	/**
	 * 处理所有sheet
	 * 
	 * @param filename
	 * @throws Exception
	 */
	public void processAllSheets(String filename) throws Exception {

		OPCPackage pkg = OPCPackage.open(filename);
		XSSFReader r = new XSSFReader(pkg);
		SharedStringsTable sst = r.getSharedStringsTable();

		XMLReader parser = fetchSheetParser(sst);

		Iterator<InputStream> sheets = r.getSheetsData();
		while (sheets.hasNext()) {
			System.out.println("Processing new sheet:\n");
			InputStream sheet = sheets.next();
			InputSource sheetSource = new InputSource(sheet);
			parser.parse(sheetSource);
			sheet.close();
			System.out.println("");
		}
	}

	/**
	 * 获取解析器
	 * 
	 * @param sst
	 * @return
	 * @throws SAXException
	 */
	public XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException {
		XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
		SheetHandler handler = new SheetHandler(sst);
		parser.setContentHandler(handler);
		return parser;
	}

	/**
	 * 自定义解析处理器 See org.xml.sax.helpers.DefaultHandler javadocs
	 */
	private static class SheetHandler extends DefaultHandler {

		private SharedStringsTable sst;
		private String lastContents;
		private boolean nextIsString;

		private List<String> rowlist = new ArrayList<String>();
		private int curRow = 0;
		private int curCol = 0;

		// 定义前一个元素和当前元素的位置，用来计算其中空的单元格数量，如A6和A8等
		private String preRef = null, ref = null;
		// 定义该文档一行最大的单元格数，用来补全一行最后可能缺失的单元格
		private String maxRef = null;

		private CellDataType nextDataType = CellDataType.SSTINDEX;
		private final DataFormatter formatter = new DataFormatter();
		private short formatIndex;
		private String formatString;

		// 用一个enum表示单元格可能的数据类型
		enum CellDataType {
			BOOL, ERROR, FORMULA, INLINESTR, SSTINDEX, NUMBER, DATE, NULL
		}

		private SheetHandler(SharedStringsTable sst) {
			this.sst = sst;
		}

		/**
		 * 解析一个element的开始时触发事件
		 */
		public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {

			// c => cell
			if (name.equals("c")) {
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
				if (cellType != null && cellType.equals("s")) {
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
			if ("b".equals(cellType)) {
				nextDataType = CellDataType.BOOL;
			} else if ("e".equals(cellType)) {
				nextDataType = CellDataType.ERROR;
			} else if ("inlineStr".equals(cellType)) {
				nextDataType = CellDataType.INLINESTR;
			} else if ("s".equals(cellType)) {
				nextDataType = CellDataType.SSTINDEX;
			} else if ("str".equals(cellType)) {
				nextDataType = CellDataType.FORMULA;
			}
			if (cellStyleStr != null) {
				int styleIndex = Integer.parseInt(cellStyleStr);
				XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
				formatIndex = style.getDataFormat();
				formatString = style.getDataFormatString();
				if ("m/d/yy" == formatString) {
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
		public void endElement(String uri, String localName, String name) throws SAXException {
			// Process the last contents as required.
			// Do now, as characters() may be called more than once
			if (nextIsString) {
				int idx = Integer.parseInt(lastContents);
				lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
				nextIsString = false;
			}

			// v => contents of a cell
			// Output after we've seen the string contents
			if (name.equals("v")) {
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
				if (name.equals("row")) {
					String value = "";
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
					// 拼接一行的数据
					for (int i = 0; i < rowlist.size(); i++) {
						if (rowlist.get(i).contains(",")) {
							value += "\"" + rowlist.get(i) + "\",";
						} else {
							value += rowlist.get(i) + ",";
						}
					}
					// 加换行符
					value += "\n";
					try {
						writer.write(value);
					} catch (IOException e) {
						e.printStackTrace();
					}
					curRow++;
					// 一行的末尾重置一些数据
					rowlist.clear();
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
					thisStr = formatter.formatRawCellContents(Double.parseDouble(value), formatIndex, formatString)
							.trim();
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
			String xfd_1 = preRef.replaceAll("\\d+", "");

			xfd = fillChar(xfd, 3, '@', true);
			xfd_1 = fillChar(xfd_1, 3, '@', true);

			char[] letter = xfd.toCharArray();
			char[] letter_1 = xfd_1.toCharArray();
			int res = (letter[0] - letter_1[0]) * 26 * 26 + (letter[1] - letter_1[1]) * 26 + (letter[2] - letter_1[2]);
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
			int len_1 = str.length();
			if (len_1 < len) {
				if (isPre) {
					for (int i = 0; i < (len - len_1); i++) {
						str = let + str;
					}
				} else {
					for (int i = 0; i < (len - len_1); i++) {
						str = str + let;
					}
				}
			}
			return str;
		}
	}

	static BufferedWriter writer = null;

	public static void main(String[] args) throws Exception {
		SAXExcelHandler example = new SAXExcelHandler();
		String str = "templete_1.1.1_正确_zh";
		// String str = "templete_500000_正确_zh";
		String filename = "E:\\MCS\\test\\" + str + ".xlsx ";
		System.out.println("-- 程序开始 --");
		long time_1 = System.currentTimeMillis();
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("E:\\MCS\\test\\" + str + ".csv")));
			example.processOneSheet(filename);
		} finally {
			writer.close();
		}
		long time_2 = System.currentTimeMillis();
		System.out.println("-- 程序结束 --");
		System.out.println("-- 耗时 --" + (time_2 - time_1) + "ms");
	}

}

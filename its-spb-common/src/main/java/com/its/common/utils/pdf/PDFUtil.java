package com.its.common.utils.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.its.common.utils.DateUtil;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * PDF
 */
public class PDFUtil {

	private static Font headfont;// 设置字体大小
	private static Font headfont2;// 设置字体大小
	private static Font keyfont;// 设置字体大小
	private static Font keyfontNomal;// 设置字体大小
	private static Font textfont;// 设置字体大小
	private static int maxWidth = 520;

	static {
		BaseFont bfChinese = null;
		try {
			// bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
			// BaseFont.NOT_EMBEDDED);
			bfChinese = BaseFont.createFont("C:/Windows/Fonts/arialuni.ttf", BaseFont.IDENTITY_H,
					BaseFont.NOT_EMBEDDED);
			headfont = new Font(bfChinese, 12, Font.BOLD);// 设置字体大小
			headfont2 = new Font(bfChinese, 10, Font.BOLD);// 设置字体大小
			keyfont = new Font(bfChinese, 8, Font.BOLD);// 设置字体大小
			keyfontNomal = new Font(bfChinese, 8, Font.NORMAL);// 设置字体大小
			textfont = new Font(bfChinese, 8, Font.NORMAL);// 设置字体大小
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static PdfPCell createCell(String value, Font font, int align) {
		PdfPCell cell = new PdfPCell();
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(align);
		cell.setPhrase(new Phrase(value, font));
		return cell;
	}

	public static PdfPCell createCell(String value, Font font) {
		PdfPCell cell = new PdfPCell();
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setPhrase(new Phrase(value, font));
		return cell;
	}

	public static PdfPCell createCell(String value, Font font, int align, int colspan) {
		PdfPCell cell = new PdfPCell();
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(align);
		cell.setColspan(colspan);
		cell.setPhrase(new Phrase(value, font));
		return cell;
	}

	public static PdfPCell createCell(String value, Font font, int align, int colspan, boolean boderFlag) {
		PdfPCell cell = new PdfPCell();
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(align);
		cell.setColspan(colspan);
		cell.setPhrase(new Phrase(value, font));
		cell.setPadding(3.0f);
		if (!boderFlag) {
			cell.setBorder(0);
			cell.setPaddingTop(15.0f);
			cell.setPaddingBottom(8.0f);
		}
		return cell;
	}

	public static PdfPTable createTable(int colNumber) {
		PdfPTable table = new PdfPTable(colNumber);
		try {
			table.setTotalWidth(maxWidth);
			table.setLockedWidth(true);
			table.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.getDefaultCell().setBorder(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return table;
	}

	public static PdfPTable createTable(float[] widths) {
		PdfPTable table = new PdfPTable(widths);
		try {
			table.setTotalWidth(maxWidth);
			table.setLockedWidth(true);
			table.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.getDefaultCell().setBorder(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return table;
	}

	public static PdfPTable createBlankTable() {
		PdfPTable table = new PdfPTable(1);
		table.getDefaultCell().setBorder(0);
		table.addCell(createCell("", keyfont));
		table.setSpacingAfter(20.0f);
		table.setSpacingBefore(20.0f);
		return table;
	}

	/**
	 * 使用IText生成PDF
	 * 
	 * @param path
	 */
	public static String generatePDF(String path) {
		String savePath = null;
		try {
			// 构造PdfPTable
			PdfPTable pdfPTable = createTable(6);
			pdfPTable.addCell(createCell("测试生成PDF：", headfont, Element.ALIGN_CENTER, 6, false));
			pdfPTable.addCell(createCell("使用Itext生成PDF附件", headfont, Element.ALIGN_CENTER, 6, false));
			pdfPTable.addCell(createCell("姓名（地址）：", headfont2, Element.ALIGN_LEFT, 3, false));
			pdfPTable.addCell(createCell("日期：2016-01-04", headfont2, Element.ALIGN_RIGHT, 3, false));

			pdfPTable.addCell(createCell("序号", keyfont, Element.ALIGN_CENTER));
			pdfPTable.addCell(createCell("品名", keyfont, Element.ALIGN_CENTER));
			pdfPTable.addCell(createCell("数量", keyfont, Element.ALIGN_CENTER));
			pdfPTable.addCell(createCell("完税价格", keyfont, Element.ALIGN_CENTER));
			pdfPTable.addCell(createCell("税率%", keyfont, Element.ALIGN_CENTER));
			pdfPTable.addCell(createCell("进口税金额", keyfont, Element.ALIGN_CENTER));
			for (int i = 0; i < 200; i++) {
				pdfPTable.addCell(createCell((i + 1) + "", textfont));
				pdfPTable.addCell(createCell("品名" + (i + 1), textfont));
				pdfPTable.addCell(createCell("10", textfont));
				pdfPTable.addCell(createCell("150", textfont));
				pdfPTable.addCell(createCell("2", textfont));
				pdfPTable.addCell(createCell("30", textfont));
			}
			pdfPTable.addCell(createCell("进口税金合计：人民币（大写）" + 30000, keyfont, Element.ALIGN_LEFT, 6, false));

			pdfPTable.addCell(createCell(" 说明:", keyfontNomal, Element.ALIGN_LEFT, 6, false));
			pdfPTable.addCell(createCell("1.税金计算公式：进口税金=完税价格*税率 ", keyfontNomal, Element.ALIGN_LEFT, 6, false));
			pdfPTable.addCell(createCell("2.税金计算公式：进口税金=完税价格*税率 ", keyfontNomal, Element.ALIGN_LEFT, 6, false));
			// 生成File
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			savePath = path + DateUtil.getDateyyyyMMddHHmmss() + ".pdf";
			File file = new File(savePath);
			Document document = new Document();// 建立一个Document对象
			document.setPageSize(PageSize.A4);// 设置页面大小
			PdfWriter.getInstance(document, new FileOutputStream(file));
			document.open();
			document.add(pdfPTable);
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return savePath;
	}

	/**
	 * HTML转PDF
	 * 
	 * @param htmlPath
	 *            HTML路径
	 * @param imagePath
	 *            图片路径
	 * @param path
	 *            保存目录
	 * @return
	 */

	public static String htmlToPDF(String htmlPath, String imagePath, String path) {
		String savePath = null;
		try {
			String url = new File(htmlPath).toURI().toURL().toString();
			// 生成File
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			savePath = path + DateUtil.getDateyyyyMMddHHmmss() + ".pdf";

			OutputStream os = new FileOutputStream(savePath);
			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocument(url);

			// 解决中文支持问题
			ITextFontResolver fontResolver = renderer.getFontResolver();
			fontResolver.addFont("C:/Windows/Fonts/arialuni.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			// 解决图片的相对路径问题
			renderer.getSharedContext().setBaseURL(imagePath);

			renderer.layout();
			renderer.createPDF(os);
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return savePath;
	}

	/**
	 * HTML转PDF
	 * 
	 * @param htmlStr
	 *            HTML字符串
	 * @param imagePath
	 *            图片路径
	 * @param path
	 *            保存目录
	 * @return
	 */

	public static String htmlStrToPDF(String htmlStr, String imagePath, String path) {
		String savePath = null;
		try {
			// 生成File
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			savePath = path + DateUtil.getDateyyyyMMddHHmmss() + ".pdf";

			OutputStream os = new FileOutputStream(savePath);
			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocumentFromString(htmlStr);

			// 解决中文支持问题
			ITextFontResolver fontResolver = renderer.getFontResolver();
			fontResolver.addFont("C:/Windows/Fonts/arialuni.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			// 解决图片的相对路径问题
			renderer.getSharedContext().setBaseURL(imagePath);

			renderer.layout();
			renderer.createPDF(os);
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return savePath;
	}

}
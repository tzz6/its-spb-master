package com.its.common.utils.pdf;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

/***
 * 使用PDFBox将PDF转Image
 * @author tzz
 */
public class PdfBoxUtils {

	private static Logger logger = Logger.getLogger(PdfBoxUtils.class);

	/**
	 * 将PDF转为Image(支持多页-多页转化为多张图片)
	 * @param pdfUrl
	 * @param imageFormat
	 * @return
	 */
	public static List<byte[]> convertPdfToImage(String pdfUrl, String imageFormat) {
		List<byte[]> list = new ArrayList<byte[]>();
		ByteArrayOutputStream byteOutput = null;
		try {
			// InputStream in = url.openStream();
			// PDDocument doc = PDDocument.load(new URL(pdfUrl));
			PDDocument doc = PDDocument.load(new File(pdfUrl));
			int pageCount = doc.getNumberOfPages();
			logger.info("----------PDF Page Number:" + pageCount);
			@SuppressWarnings("unchecked")
			List<PDPage> pages = doc.getDocumentCatalog().getAllPages();
			for (int i = 0; i < pages.size(); i++) {
				byteOutput = new ByteArrayOutputStream();
				PDPage page = pages.get(i);
				int width = new Float(page.getTrimBox().getWidth()).intValue();
				int height = new Float(page.getTrimBox().getHeight()).intValue();
				logger.info("----------PDF Page width:" + width + "---height:" + height);
				BufferedImage image = page.convertToImage();
				ImageIO.write(image, imageFormat, byteOutput);
				byte[] byteArray = byteOutput.toByteArray();
				list.add(byteArray);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}

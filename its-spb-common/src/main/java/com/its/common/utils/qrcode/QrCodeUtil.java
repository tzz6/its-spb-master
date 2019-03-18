package com.its.common.utils.qrcode;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

/**
 * 二维码生成、解析
 * @author tzz
 */
public class QrCodeUtil {

	protected static final Logger log = Logger.getLogger(QrCodeUtil.class);

	/** 生成二维码图片 */
	public static void generateQRCode(String url, String path) {
		try {
			MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
			Map<EncodeHintType, String> hints = new HashMap<EncodeHintType, String>(16);
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			BitMatrix bitMatrix = multiFormatWriter.encode(url, BarcodeFormat.QR_CODE, 400, 400, hints);
			File file = new File(path);
			MatrixToImageWriter.writeToFile(bitMatrix, "jpg", file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 解析二维码图片 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String analysisQRCode(String path) {
		try {
			MultiFormatReader formatReader = new MultiFormatReader();
			File file = new File(path);
			BufferedImage image = ImageIO.read(file);
			LuminanceSource source = new BufferedImageLuminanceSource(image);
			Binarizer binarizer = new HybridBinarizer(source);
			BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
			Map hints = new HashMap(16);
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
			Result result = formatReader.decode(binaryBitmap, hints);
			log.info("result = " + result.toString());
			log.info("resultFormat = " + result.getBarcodeFormat());
			log.info("resultText = " + result.getText());
			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}

package com.its.common.utils;

import java.awt.AWTException;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * 图片工具类， 图片水印，文字水印，缩放
 */
public final class ImageUtils {
	/** 图片格式：JPG */
	private static final String PICTRUE_FORMATE_JPG = "jpg";

	/**
	 * 添加图片水印
	 * 
	 * @param targetImg
	 *            目标图片路径
	 * @param waterImg
	 *            水印图片路径
	 * @param x
	 *            水印图片距离目标图片左侧的偏移量，如果x<0, 则在正中间
	 * @param y
	 *            水印图片距离目标图片上侧的偏移量，如果y<0, 则在正中间
	 * @param alpha
	 *            透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明)
	 */
	public final static void addImageWeatermark(String targetImg, String waterImg, int x, int y, float alpha) {
		try {
			File file = new File(targetImg);
			Image image = ImageIO.read(file);
			int width = image.getWidth(null);
			int height = image.getHeight(null);
			BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = bufferedImage.createGraphics();
			g.drawImage(image, 0, 0, width, height, null);

			Image waterImage = ImageIO.read(new File(waterImg)); // 水印文件
			int width_1 = waterImage.getWidth(null);
			int height_1 = waterImage.getHeight(null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));

			int widthDiff = width - width_1;
			int heightDiff = height - height_1;
			if (x < 0) {
				x = widthDiff / 2;
			} else if (x > widthDiff) {
				x = widthDiff;
			}
			if (y < 0) {
				y = heightDiff / 2;
			} else if (y > heightDiff) {
				y = heightDiff;
			}
			g.drawImage(waterImage, x, y, width_1, height_1, null); // 水印文件结束
			g.dispose();
			ImageIO.write(bufferedImage, PICTRUE_FORMATE_JPG, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 添加文字水印
	 * 
	 * @param targetImg
	 *            目标图片路径
	 * @param pressText
	 *            水印文字， 如：中国证券网
	 * @param fontName
	 *            字体名称， 如：宋体
	 * @param fontStyle
	 *            字体样式，如：粗体和斜体(Font.BOLD|Font.ITALIC)
	 * @param fontSize
	 *            字体大小，单位为像素
	 * @param color
	 *            字体颜色
	 * @param x
	 *            水印文字距离目标图片左侧的偏移量，如果x<0, 则在正中间
	 * @param y
	 *            水印文字距离目标图片上侧的偏移量，如果y<0, 则在正中间
	 * @param alpha
	 *            透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明)
	 */
	public static void addTextWeatermark(String targetImg, String pressText, String fontName, int fontStyle,
			int fontSize, Color color, int x, int y, float alpha) {
		try {
			File file = new File(targetImg);

			Image image = ImageIO.read(file);
			int width = image.getWidth(null);
			int height = image.getHeight(null);
			BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = bufferedImage.createGraphics();
			g.drawImage(image, 0, 0, width, height, null);
			g.setFont(new Font(fontName, fontStyle, fontSize));
			g.setColor(color);
			// g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
			// alpha));

			int width_1 = fontSize * getLength(pressText);
			int height_1 = fontSize;
			int widthDiff = width - width_1;
			int heightDiff = height - height_1;
			if (x < 0) {
				x = widthDiff / 2;
			} else if (x > widthDiff) {
				x = widthDiff;
			}
			if (y < 0) {
				y = heightDiff / 2;
			} else if (y > heightDiff) {
				y = heightDiff;
			}

			g.drawString(pressText, x, y + height_1);
			g.dispose();
			ImageIO.write(bufferedImage, PICTRUE_FORMATE_JPG, file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取字符长度，一个汉字作为 1 个字符, 一个英文字母作为 0.5 个字符
	 * 
	 * @param text
	 * @return 字符长度，如：text="中国",返回 2；text="test",返回 2；text="中国ABC",返回 4.
	 */
	public static int getLength(String text) {
		int textLength = text.length();
		int length = textLength;
		for (int i = 0; i < textLength; i++) {
			if (String.valueOf(text.charAt(i)).getBytes().length > 1) {
				length++;
			}
		}
		return (length % 2 == 0) ? length / 2 : length / 2 + 1;
	}

	/**
	 * 图片缩放
	 * 
	 * @param filePath
	 *            图片路径
	 * @param height
	 *            高度
	 * @param width
	 *            宽度
	 * @param bb
	 *            比例不对时是否需要补白
	 */
	public static void resize(String filePath, int height, int width, boolean bb) {
		try {
			double ratio = 0; // 缩放比例
			File f = new File(filePath);
			BufferedImage bi = ImageIO.read(f);
			Image itemp = bi.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
			// 计算比例
			if ((bi.getHeight() > height) || (bi.getWidth() > width)) {
				if (bi.getHeight() > bi.getWidth()) {
					ratio = (new Integer(height)).doubleValue() / bi.getHeight();
				} else {
					ratio = (new Integer(width)).doubleValue() / bi.getWidth();
				}
				AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
				itemp = op.filter(bi, null);
			}
			if (bb) {
				BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				Graphics2D g = image.createGraphics();
				g.setColor(Color.white);
				g.fillRect(0, 0, width, height);
				if (width == itemp.getWidth(null))
					g.drawImage(itemp, 0, (height - itemp.getHeight(null)) / 2, itemp.getWidth(null),
							itemp.getHeight(null), Color.white, null);
				else
					g.drawImage(itemp, (width - itemp.getWidth(null)) / 2, 0, itemp.getWidth(null),
							itemp.getHeight(null), Color.white, null);
				g.dispose();
				itemp = image;
			}
			ImageIO.write((BufferedImage) itemp, "jpg", f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 图片合并(将两张图片合并为一张图片)
	 * 
	 * @param oneSrc
	 * @param twoSrc
	 * @param mergeSrc
	 *            合并后的图片路径
	 */
	public static void mergeImage(String oneSrc, String twoSrc, String mergeSrc) {
		String postFix = mergeSrc.substring(mergeSrc.lastIndexOf(".") + 1, mergeSrc.length());
		try {
			File fileOne = new File(oneSrc);// 读取第一张图片
			Image src = ImageIO.read(fileOne);
			int width = src.getWidth(null);
			int height = src.getHeight(null);
			if (width > 900 || height > 900) {
				int num = (int) Math.ceil((double) width / 900);
				int num2 = (int) Math.ceil((double) height / 900);
				num = num > num2 ? num : num2;
				width = width / num;
				height = height / num;
			}
			BufferedImage bufferedImageOne = null;
			if ("png".equalsIgnoreCase(postFix.toLowerCase())) {
				bufferedImageOne = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
			} else {
				bufferedImageOne = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			}
			bufferedImageOne.getGraphics().drawImage(src.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0,
					null);
			int[] imageArrayOne = new int[width * height];// 从图片中读取RGB
			imageArrayOne = bufferedImageOne.getRGB(0, 0, width, height, imageArrayOne, 0, width);

			File fileTwo = new File(twoSrc);// 读取第二张图片
			src = ImageIO.read(fileTwo);
			BufferedImage bufferedImageTwo = null;
			if ("png".equalsIgnoreCase(postFix.toLowerCase())) {
				bufferedImageTwo = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
			} else {
				bufferedImageTwo = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			}
			bufferedImageTwo.getGraphics().drawImage(src.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0,
					null);
			int[] imageArrayTwo = new int[width * height];// 从图片中读取RGB
			imageArrayTwo = bufferedImageTwo.getRGB(0, 0, width, height, imageArrayTwo, 0, width);

			// 生成新图片
			BufferedImage imageNew = new BufferedImage(width * 2, height, BufferedImage.TYPE_INT_RGB);
			imageNew.setRGB(0, 0, width, height, imageArrayOne, 0, width); // 设置左半部分的RGB
			imageNew.setRGB(width, 0, width, height, imageArrayTwo, 0, width); // 设置右半部分的RGB
			File outFile = new File(mergeSrc);
			// 写图片
			ImageIO.write(imageNew, postFix, outFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将网页转化为图片
	 * @param url
	 *            网页地址
	 * @param savePath
	 *            图片保存路径
	 */
	public static void htmlToImage(String url, String savePath) {

		try {
			// 此方法仅适用于JdK1.6及以上版本
			Desktop.getDesktop().browse(new URL(url).toURI());
			Robot robot = new Robot();
			robot.delay(10000);
			Dimension d = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
			int width = (int) d.getWidth();
			int height = (int) d.getHeight();
			// 最大化浏览器
			robot.keyRelease(KeyEvent.VK_F11);
			robot.delay(2000);
			Image image = robot.createScreenCapture(new Rectangle(0, 0, width, height));
			BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics g = bi.createGraphics();
			g.drawImage(image, 0, 0, width, height, null);
			// 保存图片
			ImageIO.write(bi, "jpg", new File(savePath));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
}
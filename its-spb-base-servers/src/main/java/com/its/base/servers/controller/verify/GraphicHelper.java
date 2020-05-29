package com.its.base.servers.controller.verify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.*;


/**
 * Description: 图片验证码
 * Company: tzz
 *
 * @Author: tzz
 * Date: 2020/4/30 19:36
 */
public final class GraphicHelper {

    private static final Logger logger = LoggerFactory.getLogger(GraphicHelper.class);

    /**
     * 生成汉字的个数
     */
    private static Integer[] arr = new Integer[]{1, 2, 3, 4, 5};
    /**
     * 汉字颜色随机范围
     */
    private static Color[] colors = {Color.BLUE, Color.MAGENTA, Color.ORANGE};

    /**
     * 随机实体
     */
    private static final Random RANDOM = new Random();

    private GraphicHelper() {
    }

    /**
     * 生成背景图片
     */
    private static BufferedImage getBackGround() {
        // 指定生成验证码的宽度
        int width = 240;
        // 指定生成验证码的高度
        int height = 120;

        int len50 = 50;
        int len100 = 100;
        int len120 = 120;
        int len150 = 150;
        int len200 = 200;
        int len240 = 240;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // 创建Graphics2D对象
        Graphics2D g2d = (Graphics2D) g;
        // 背景色
        g.setColor(getRandColor(len200, len240));
        // 绘制背景
        g.fillRect(0, 0, width, height);
        // 线条色
        g.setColor(getRandColor(len150, len200));
        // 绘制88根位置和颜色全部为随机产生的线条，该线条为2f
        for (int i = 0; i < len50; i++) {
            int x = RANDOM.nextInt(width - 1);
            int y = RANDOM.nextInt(height - 1);
            int x1 = RANDOM.nextInt(len100) + 1;
            int y1 = RANDOM.nextInt(len120) + 1;
            BasicStroke bs = new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL);
            Line2D line = new Line2D.Double(x, y, x + x1, y + y1);
            g2d.setStroke(bs);
            // 绘制直线
            g2d.draw(line);
            // g2d.setColor(getRandColor(random, 30, 150)); // 随机每条线条的颜色
        }
        // 输出生成的验证码图片
        g.dispose();
        return image;
    }

    private static String getRandomChineseChar() {
        String str = null;
        int hs;
        int ls;
        hs = 176 + Math.abs(RANDOM.nextInt(39));
        ls = 161 + Math.abs(RANDOM.nextInt(93));
        byte[] b = new byte[2];

        b[0] = (byte) hs;
        b[1] = (byte) ls;
        try {
            // 转成中文
            str = new String(b, "GBk");
        } catch (UnsupportedEncodingException ex) {
            logger.error("转成中文字符出错", ex);
        }
        return str;
    }

    /**
     * 生成验证码
     */
    public static VerifyData buildImage() {
        VerifyData data = new VerifyData();
        // 生成背景图片
        BufferedImage image = getBackGround();
        Graphics graphics = image.getGraphics();
        // 设置颜色
        graphics.setColor(Color.red);
        graphics.setFont(new Font("宋体", Font.BOLD, 30));

        StringBuilder sb = new StringBuilder();
        StringBuilder realPointsOffset = new StringBuilder();

        // 转成集合
        List<Integer> intList = Arrays.asList(arr);
        // 重新随机排序
        Collections.shuffle(intList);
        // 定义随机1到arr.length某一个字不参与校验
        int num = RANDOM.nextInt(arr.length) + 1;
        // 5个汉字,只点4个
        for (int i = 0; i < arr.length; i++) {
            String ch = getRandomChineseChar();
            final int maxRandom = 30;
            int randomX = RANDOM.nextInt(maxRandom);
            int randomY = RANDOM.nextInt(maxRandom);

            int place = intList.get(i);
            int offsetX = getOffsetX(place);
            int offsetY = getOffsetY(place);

            int x = randomX + offsetX;
            int y = randomY + offsetY;
            // 字体颜色
            graphics.setColor(colors[RANDOM.nextInt(colors.length)]);
            graphics.drawString(ch, x, y);
            if (place != num) {
                sb.append(ch);
                realPointsOffset.append(x + "_" + y);
                realPointsOffset.append(",");
            }
        }

        data.setChineseChars(sb);
        data.setRealPointsOffset(realPointsOffset);

        BufferedImage combined = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        // 合并
        Graphics g = combined.getGraphics();
        g.drawImage(image, 0, 0, null);
        String imgcode = encodeImgageToBase64(combined);
        imgcode = imgcode.replaceAll("\r\n", "");
        data.setCombined(imgcode);
        return data;
    }

    /**
     * 获取X轴方向偏移量
     *
     * @param place 位置
     * @return X轴方向偏移量
     */
    private static int getOffsetX(int place) {
        int offsetX;

        int len2 = 2;
        int len3 = 3;
        int len4 = 4;
        int len5 = 5;
        if (place == 1) {
            offsetX = 20;
        } else if (place == len2 || place == len5) {
            offsetX = 100;
        } else if (place == len3) {
            offsetX = 150;
        } else if (place == len4) {
            offsetX = 20;
        } else {
            offsetX = 0;
        }

        return offsetX;
    }

    /**
     * 获取Y轴方向偏移量
     *
     * @param place 位置
     * @return Y轴方向偏移量
     */
    private static int getOffsetY(int place) {
        int offsetY;
        int len1 = 1;
        int len2 = 2;
        int len3 = 3;
        int len4 = 4;
        int len5 = 5;
        if (place == len1 || place == len2) {
            offsetY = 30;
        } else if (place == len3) {
            offsetY = 20;
        } else if (place == len4 || place == len5) {
            offsetY = 90;
        } else {
            offsetY = 0;
        }

        return offsetY;
    }

    /**
     * 将本地图片进行Base64位编码
     * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
     *
     * @param bufferedImage 图片文件流
     * @return
     */
    public static String encodeImgageToBase64(BufferedImage bufferedImage) {
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", outputStream);
            // 对字节数组Base64编码
            byte[] bytes = Base64.getEncoder().encode(outputStream.toByteArray());
            // 返回Base64编码过的字节数组字符串
            return new String(bytes);
        } catch (MalformedURLException e1) {
            logger.error("地址格式不正确", e1);
        } catch (IOException e) {
            logger.error("打开地址出错", e);
        }
        return null;
    }

    private static Color getRandColor(int is, int ie) {
        int s = is;
        int e = ie;
        int len255 = 255;
        if (is > len255) {
            s = 255;
        }
        if (ie > len255) {
            e = 255;
        }
        int r = s + RANDOM.nextInt(e - s);
        int g = s + RANDOM.nextInt(e - s);
        int b = s + RANDOM.nextInt(e - s);
        return new Color(r, g, b);
    }
}

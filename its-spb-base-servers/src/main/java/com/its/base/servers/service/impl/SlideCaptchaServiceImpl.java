package com.its.base.servers.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Objects;
import com.its.base.servers.controller.verify.CaptchaConfiguration;
import com.its.base.servers.controller.verify.GraphicHelper;
import com.its.base.servers.controller.verify.SlideCaptcha;
import com.its.base.servers.controller.verify.VerifyData;
import com.its.base.servers.service.CaptchaService;
import com.its.common.redis.service.RedisService;
import com.jhlabs.image.GaussianFilter;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Random;

/**
 * Description: 滑动验证码
 * Company: tzz
 *
 * @Author: tzz
 * Date: 2020/5/20 14:34
 */
@Service
public class SlideCaptchaServiceImpl implements CaptchaService {

    private static final Log log = LogFactory.getLog(SlideCaptchaServiceImpl.class);
    /**
     * 滑动验证码类型
     */
    private static final String CAPTCHA_TYPE_SLIDE = "slide";
    /**
     * 生成图片后缀
     */
    private static final String PIC_SUFFIX = ".png";

    private static final String SLIDE_CAPTCHA = "SLIDE_CAPTCHA:";

    private static final String PIC_CAPTCHA = "PIC_CAPTCHA:";
    /**
     * 生成滑块验证码的原始图片的路径
     */
    private String imagePath;

    /**
     * 背景图片的宽度
     */
    private int backgroundWidth;

    /**
     * 背景图片的高度
     */
    private int backgroundHeight;

    /**
     * 滑块左侧边界值，避免生成的滑块的位置太靠近图片的左边
     */
    private int sliderLeft;

    /**
     * 滑块右侧边界值，避免生成的滑块太靠近图片的右边
     */
    private int sliderRight;

    /**
     * 滑块顶部边界值，避免生成的滑块太靠近图片的顶部
     */
    private int sliderTop;

    /**
     * 滑块底部边界值，避免生成的滑块太靠近图片的底部
     */
    private int sliderBottom;

    /**
     * 验证验证位置时，坐标偏差
     */
    private int offset;

    /**
     * 滑块方块边长
     */
    private int sliderSide;

    /**
     * 滑块上小圆半径
     */
    private int sliderRadius;

    /**
     * 滑块上方块和小圆重叠部分距离
     */
    private int sliderOverlap;

    /**
     * 高斯模糊半径
     */
    private int filterRadius;

    /**
     * 滑块水印对比度
     */
    private float sliderAlpha;

    /**
     * 滑动验证码过期时间，单位秒
     */
    private long timeout;

    /**
     * 边框宽度
     */
    private int borderWidth;

    /**
     * 边框对比度
     */
    private float borderAlpha;

    /**
     * 可用背景图个数
     */
    private int size;

    private RedisService redisService;
    private CaptchaConfiguration captchaConfiguration;

    @Autowired
    public SlideCaptchaServiceImpl(RedisService redisService, CaptchaConfiguration captchaConfiguration) {
        this.redisService = redisService;
        this.captchaConfiguration = captchaConfiguration;
    }

    private static final Random RANDOM = new Random();

    @PostConstruct
    public void init() {
        imagePath = defaultIfNull(
                captchaConfiguration.getSlideImagePath(),
                "/static/img/captcha/");
        log.debug("获取到滑动图片的路劲debug：" + imagePath);
        log.info("获取到滑动图片的路劲info：" + imagePath);

        //可用背景图大小
        size = defaultIfNull(
                captchaConfiguration.getSlideImageSize(),
                5);

        backgroundWidth = defaultIfNull(
                captchaConfiguration.getSlideBackgroundWidth(), 400);
        backgroundHeight = defaultIfNull(
                captchaConfiguration.getSlideBackgroundHeight(), 300);

        sliderLeft = defaultIfNull(captchaConfiguration.getSlideSliderLeft(),
                backgroundWidth / 10);
        sliderRight = defaultIfNull(captchaConfiguration.getSlideSliderRight(),
                backgroundWidth - sliderLeft);
        if (sliderRight > backgroundWidth) {
            sliderRight = backgroundWidth;
        }

        sliderTop = defaultIfNull(captchaConfiguration.getSlideSliderTop(),
                backgroundHeight / 10);
        sliderBottom = defaultIfNull(
                captchaConfiguration.getSlideSliderBottom(), backgroundHeight
                        - sliderTop);
        if (sliderBottom > backgroundHeight) {
            sliderBottom = backgroundHeight;
        }

        offset = defaultIfNull(captchaConfiguration.getSlideOffset(), 3);
        sliderSide = defaultIfNull(captchaConfiguration.getSlideSliderSide(),
                53);
        sliderRadius = defaultIfNull(
                captchaConfiguration.getSlideSliderRadius(), 7);
        sliderOverlap = defaultIfNull(
                captchaConfiguration.getSlideSliderOverlap(), 1);
        filterRadius = defaultIfNull(
                captchaConfiguration.getSlideFilterRadius(), sliderSide / 5);
        sliderAlpha = defaultIfNull(captchaConfiguration.getSlideSliderAlpha(),
                0.8F);
        timeout = defaultIfNull(captchaConfiguration.getSlideTimeout(), 300L);

        borderWidth = defaultIfNull(captchaConfiguration.getSlideBorderWidth(),
                2);
        borderAlpha = defaultIfNull(captchaConfiguration.getSlideBorderAlpha(),
                0.8F);
    }

    /**
     * 根据原图文件路径去获取对应的文件流
     *
     * @return 文件流
     */
    private InputStream getSourceImageInputStream() {
        String sourceImageName = getSourceImageName();
        return getClass().getResourceAsStream(sourceImageName);
    }

    /**
     * 随机获取一个文件名称
     *
     * @return 随机文件
     */
    private String getSourceImageName() {
        // 获取原始图片的完整路径，随机采用一张
        int sourceSize = RANDOM.nextInt(size);
        return StrUtil.join("", imagePath, sourceSize, PIC_SUFFIX);
    }

    /**
     * 生成验证码
     *
     * @param requestId 请求标识
     * @return 返回返回验证码数据
     * @throws IOException 读取原始图片异常或生成图片异常
     */
    @Override
    public SlideCaptcha create(String requestId) throws IOException {
        SlideCaptcha slideCaptcha;
        try (InputStream is = getSourceImageInputStream()) {
            if (is == null) {
                throw new IOException("读取原始图片异常");
            }
            BufferedImage backgroundImage = Thumbnails.of(is)
                    .sourceRegion(Positions.TOP_LEFT, backgroundWidth, backgroundHeight)
                    .size(backgroundWidth, backgroundHeight).asBufferedImage();
            Shape slider = createSlider();
            drawOutline(backgroundImage, slider);

            BufferedImage sliderImage = createBlankImage(backgroundImage);
            transparent(sliderImage);
            Composite composite = AlphaComposite.Src;
            watermark(sliderImage, backgroundImage, slider, composite);

            Rectangle bounds = slider.getBounds();
            sliderImage = sliderImage.getSubimage(bounds.x, 0, bounds.width, backgroundHeight);

            BufferedImage filteredImage = gaussianFilter(backgroundImage, filterRadius);
            composite = AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, sliderAlpha);
            watermark(backgroundImage, filteredImage, slider, composite);

            slideCaptcha = new SlideCaptcha();
            String backgroundImageData = "data:image/png;base64," + encodeImage(backgroundImage);
            slideCaptcha.setBackgroundImage(backgroundImageData);
            String sliderImageData = "data:image/png;base64," + encodeImage(sliderImage);
            slideCaptcha.setSliderImage(sliderImageData);

            String cacheKey = getCacheKey(requestId);
            Object value = bounds.x;
            slideCaptcha.setBackgroundWidth(backgroundWidth);
            redisService.set(cacheKey, String.valueOf(value), (int) timeout);
        }
        return slideCaptcha;
    }

    /**
     * 验证码校验
     *
     * @param requestId      请求标识
     * @param code           验证码
     * @param clearIfSuccess 验证通过，是否清除验证码
     * @return 验证结果，验证成功，返回null，验证失败，返回重新生成的验证码数据
     * @throws IOException 读取原始图片异常或生成图片异常
     */
    @Override
    public SlideCaptcha verify(String requestId, String code, Boolean clearIfSuccess) throws IOException {
        String cacheKey = getCacheKey(requestId);
        SlideCaptcha slideCaptcha = null;
        boolean verifyPass = true;
        try {
            String strValue = (String) redisService.get(cacheKey);
            Integer value = Integer.valueOf(strValue);
            if (StringUtils.isEmpty(code) || value == null) {
                verifyPass = false;
            } else {
                Integer x = Integer.valueOf(code);
                Integer min = value - offset;
                Integer max = value + offset;
                if (x < min || x > max) {
                    verifyPass = false;
                }
            }
        } catch (Exception exception) {
            log.error("验证滑动验证码出错", exception);
            verifyPass = false;
        }
        if (!verifyPass || clearIfSuccess) {
            redisService.del(cacheKey);
        }
        if (!verifyPass) {
            slideCaptcha = create(requestId);
        }
        return slideCaptcha;
    }

    /**
     * 如果参数值不为空，则返回参数值，否则返回默认值
     *
     * @param value        参数值
     * @param defaultValue 默认值
     * @return 返回结果
     */
    private static <T> T defaultIfNull(T value, T defaultValue) {    // NOSONAR 误判，跳过扫描
        return value != null ? value : defaultValue;
    }

    /**
     * 生成滑块
     *
     * @return 滑块形状信息
     */
    private Shape createSlider() {
        // 小圆直径
        int sliderDiameter = 2 * sliderRadius;
        // 小圆切割后的宽度
        int cutWidth = sliderDiameter - sliderOverlap;
        // 滑块的宽度=方块的边长+小圆切割后的宽度
        int sliderWidth = sliderSide + cutWidth;
        // 滑块高度=滑块宽度
        int sliderHeight = sliderWidth;
        // X轴滑动范围=滑块右边界-滑块左边界-滑块宽度
        int slideDeltaX = sliderRight - sliderLeft - sliderWidth;
        // Y轴滑动范围=滑块下边界-滑块上边界-滑块高度
        int slideDeltaY = sliderBottom - sliderTop - sliderHeight;

        // 滑块左上角X坐标
        int x = sliderLeft + RANDOM.nextInt(slideDeltaX);
        // 滑块左上角Y坐标
        int y = sliderTop + RANDOM.nextInt(slideDeltaY);

        // 方块的左上角X坐标
        int rx = x;
        // 方块的左上角Y坐标
        int ry = y + cutWidth;

        // 滑块左侧小圆（A圆）左上角X坐标
        int ax = x - sliderOverlap;
        // 滑块左侧小圆（A圆）左上角Y坐标
        int ay = y + cutWidth + sliderSide / 2 - sliderRadius;

        // 滑块上方小圆（B圆）左上角X坐标
        int bx = x + sliderSide / 2 - sliderRadius;
        // 滑块上方小圆（B圆）左上角Y坐标
        int by = y;

        // 滑块右侧小圆（C圆）左上角X坐标
        int cx = x + sliderWidth - sliderDiameter;
        // 滑块右侧小圆（C圆）左上角Y坐标，左侧小圆与右侧小圆在同一水平线上
        int cy = ay;

        Area area = new Area(new Rectangle(rx, ry, sliderSide, sliderSide));
        area.subtract(new Area(new Ellipse2D.Double(ax, ay, sliderDiameter, sliderDiameter)));
        area.add(new Area(new Ellipse2D.Double(bx, by, sliderDiameter, sliderDiameter)));
        area.add(new Area(new Ellipse2D.Double(cx, cy, sliderDiameter, sliderDiameter)));
        return area;
    }

    /**
     * 绘制轮廓
     *
     * @param sourceImage 原始图像
     * @param outline     轮廓
     */
    private void drawOutline(BufferedImage sourceImage, Shape outline) {
        Graphics2D graphics = sourceImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setStroke(new BasicStroke(borderWidth));
        graphics.setPaint(Color.WHITE);
        Rectangle bounds = outline.getBounds();
        graphics.setPaint(new GradientPaint(bounds.x, bounds.y, Color.WHITE,
                bounds.x + bounds.width, bounds.y + bounds.height, Color.BLACK));
        Composite composite = AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, borderAlpha);
        graphics.setComposite(composite);
        graphics.draw(outline);
        graphics.dispose();
    }

    /**
     * 给图片加水印
     *
     * @param sourceImage    原始图片
     * @param watermarkImage 水印图片
     * @param watermarkShape 水印形状
     */
    private static void watermark(BufferedImage sourceImage, BufferedImage watermarkImage,
                                  Shape watermarkShape, Composite composite) {
        Graphics2D graphics = sourceImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setComposite(composite);
        graphics.setClip(watermarkShape);
        graphics.drawImage(watermarkImage, 0, 0, null);
        graphics.dispose();
    }

    /**
     * 高斯过滤
     *
     * @param sourceImage 原始图片
     * @param radius      过滤半径
     * @return 高斯过滤后的图片
     */
    private static BufferedImage gaussianFilter(BufferedImage sourceImage, float radius) {
        BufferedImage destinationImage = createBlankImage(sourceImage);
        GaussianFilter filter = new GaussianFilter();
        filter.setRadius(radius);
        filter.filter(sourceImage, destinationImage);

        return destinationImage;
    }

    /**
     * 生成原始图片同等尺寸的空白图片
     *
     * @param sourceImage 原始图片
     * @return 同等尺寸的空白图片
     */
    private static BufferedImage createBlankImage(BufferedImage sourceImage) {
        int width = sourceImage.getWidth();
        int height = sourceImage.getHeight();
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * 对图片进行透明化处理
     *
     * @param image 图片
     */
    private static void transparent(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        Graphics2D graphics = image.createGraphics();
        graphics.setComposite(AlphaComposite.Clear);
        graphics.fillRect(0, 0, width, height);
        graphics.dispose();
    }

    /**
     * 对图片进行Base64位编码
     *
     * @param image 图片
     * @return Base64编码的文本
     */
    private static String encodeImage(BufferedImage image) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", outputStream);
            byte[] data = outputStream.toByteArray();
            return Base64.getEncoder().encodeToString(data);
        }
    }

    /**
     * 获取缓存键
     *
     * @param requestId 请求标识
     * @return 缓存键
     */
    private String getCacheKey(String requestId) {
        String captchaType = captchaConfiguration.getCaptchaType();
        if (Objects.equal(captchaType, CAPTCHA_TYPE_SLIDE)) {
            return SLIDE_CAPTCHA + requestId;
        } else {
            return PIC_CAPTCHA + requestId;
        }
    }

    /**
     * <p>从redis获取文字型验证码图片缓存</p>
     *
     * @param reqid 请求id
     * @return 验证码
     * @author 89003389
     * @since 1.9
     */
    @Override
    public VerifyData getPicCache(String reqid) {
        String cacheKey = getCacheKey(reqid);
        Object obj = redisService.get(cacheKey);
        if (obj != null) {
            String str = (String) obj;
            if (StringUtils.isNotBlank(str)) {
                return JSON.parseObject(str, VerifyData.class);
            }
        }
        return null;
    }

    /**
     * <p>设置文字型验证码图片并存入redis缓存</p>
     *
     * @param reqid 请求id
     * @return 验证码
     * @author 89003389
     * @since 1.9
     */
    @Override
    public VerifyData setPicCache(String reqid) {
        String cacheKey = getCacheKey(reqid);
        VerifyData data = GraphicHelper.buildImage();
        if (data != null) {
            redisService.set(cacheKey, JSON.toJSONString(data), 120);
        }
        return data;
    }

    /**
     * 校验验证码
     *
     * @param reqid     请求id
     * @param resultSet 结果值
     * @return 校验通过返回true，否则返回false
     */
    @Override
    public boolean verifyCode(String reqid, String resultSet) {
        VerifyData data = getPicCache(reqid);
        if (data != null) {
            String[] notCheck = resultSet.split(",");
            String[] really = data.getRealPointsOffset().toString().split(",");
            for (int i = 0; i < really.length; i++) {
                String[] checkPoint = notCheck[i].split("_");
                String[] realPoint = really[i].split("_");
                if (!checkPoint(checkPoint, realPoint)) {
                    // 只要有一个字校验不通过就算不通过
                    return false;
                }
            }
            return true;
        }
        return false;

    }

    /**
     * 校验一个字与另一个字的偏移量是否超过12像素
     *
     * @param checkPoint checkPoint
     * @param realPoint  realPoint
     */
    private boolean checkPoint(String[] checkPoint, String[] realPoint) {
        // 绘制的时候是以左下角为起点坐标,
        Integer x = Integer.parseInt(realPoint[0]) + 15;
        Integer y = Integer.parseInt(realPoint[1]) - 15;

        Integer x1 = Integer.valueOf(checkPoint[0]);
        Integer y1 = Integer.valueOf(checkPoint[1]);
        return Math.abs(x - x1) < 12 && Math.abs(y - y1) < 12;
    }
}

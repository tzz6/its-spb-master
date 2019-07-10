package com.its.web.controller.login;

import com.its.common.utils.Constants;
import com.its.web.util.IpUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * 图形验证码
 * @author tzz
 */
public class VerifyCodeServlet extends HttpServlet {

	private static final long serialVersionUID = -726272150926117174L;

	protected final transient Logger log = LogManager.getLogger(VerifyCodeServlet.class);

	public static Color getRandColor(int fc, int bc) {
		Random random = new Random();
		int end = 255;
		if (fc > end){
		    fc = 255;
		}
		if (bc > end){
		    bc = 255;
		}
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String num = this.getInitParameter("num");
		String content = this.getInitParameter("content");
		char[] chars = content.toCharArray();
		int n = 5;
		try {
			n = Integer.valueOf(num);
		} catch (NumberFormatException e) {
		}
		// 设置输出类型
		response.setContentType("image/jpeg"); 
		// 在内存中创建图象
		int width = 80, height = 20;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// 获取图形上下文
		Graphics g = image.getGraphics();

		// 生成随机类
		Random random = new Random();

		// 设定背景色
		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);

		// 画边框
		g.setColor(new Color(0, 0, 0));
		g.drawRect(0, 0, width - 1, height - 1);

		// 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
		g.setColor(getRandColor(160, 200));
		int end = 6;
		for (int i = 0; i < end; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(96);
			int yl = random.nextInt(95);
			g.setColor(new Color(random.nextInt(80), random.nextInt(80), random.nextInt(80)));
			g.drawLine(x, y, x + xl, y + yl);
		}
		// 取随机产生的认证码(4位数字)
		String sRand = "";
		for (int i = 0; i < n; i++) {
			String rand = null;
			// 随机生成数字或者字母
			int i1 = random.nextInt(chars.length);
			rand = String.valueOf(chars[i1]);
			if (random.nextInt(10) > 5) {
				rand = String.valueOf((char) (random.nextInt(10) + 48));
			} else {
				rand = String.valueOf((char) (random.nextInt(26) + 65));
			}
			if ("0".equals(rand) || "1".equals(rand) || "l".equalsIgnoreCase(rand) || "q".equalsIgnoreCase(rand)
					|| "e".equalsIgnoreCase(rand) || "f".equalsIgnoreCase(rand) || "u".equalsIgnoreCase(rand)
					|| "o".equalsIgnoreCase(rand) || "i".equalsIgnoreCase(rand)) {
				--i;
				continue;
			}
			// 设定字体
			g.setFont(getRandomFont());
			sRand += rand;
			// 将认证码显示到图象中
			g.setColor(new Color(random.nextInt(80), random.nextInt(80), random.nextInt(80)));
			// 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
			g.drawString(rand, 15 * i + 10, 16);
		}

		// 将认证码存入SESSION
		log.info("请求IP" + IpUtil.getIpAddr(request) + "  sessionID" + request.getSession().getId() + "  生成验证码：  ---> "
				+ sRand);
		request.getSession().setAttribute(Constants.SessionKey.VERIFY_CODE, sRand);
		// 图象生效
		g.dispose();
		// 输出图象到页面
		ImageIO.write(image, "JPEG", response.getOutputStream());
	}

	/**
	 * 随机生成字体、文字大小
	 * 
	 * @return
	 */
	public static Font getRandomFont() {
		String[] fonts = { "Georgia", "Arial Black", "Snap ITC", "Poor Richard", "Modern No. 20", "Tahoma", "Verdana",
				"Arial", "Quantzite", "Time News Roman", "Courier New" };
		int fontIndex = (int) Math.round(Math.random() * (fonts.length - 1));
		int fontSize = (int) Math.round(Math.random() * 12 + 10);
		return new Font(fonts[fontIndex], Font.PLAIN, fontSize);
	}
}
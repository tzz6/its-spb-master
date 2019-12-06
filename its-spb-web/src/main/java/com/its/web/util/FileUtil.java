package com.its.web.util;

import com.its.common.utils.DateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * 
 * @author tzz
 * @工号: 
 * @date 2019/07/10
 * @Introduce: FileUtil
 */
public class FileUtil {
	
	private static Logger logger = LogManager.getLogger(FileUtil.class);
	
	public static final String PDF_DIR = "/WEB-INF/file/pdf";
	public static final String IMAGE_DIR = "/WEB-INF/file/image";
	public static final String FILE_UPLOAD_DIR = "/WEB-INF/file/upload";
	public static final String FILE_UPLOAD_TEMP_DIR = "/WEB-INF/file/temp";
	public static final String FILE_TEMPLATE_DIR = "/WEB-INF/file/template";
	
	/**
	 * 获取项目中目录的实际路径
	 * 
	 * @param request
	 * @param dir:目录
	 * @return
	 */
	public static String getPath(HttpServletRequest request, String dir) {
		return request.getSession().getServletContext().getRealPath(dir);
	}

	/**
	 * @Method: makeFileName
	 * @param postfix:后缀
	 * @return
	 * @Description: 生成上传文件的文件名，文件名以：uuid+"_"+时间戳+"."+后缀
	 */
	public static String makeFileName(String postfix) {
		return UUID.randomUUID().toString() + "_" + DateUtil.getDateyyyyMMddHHmmss() + "." + postfix;
	}

	/**
	 * (创建目录)为防止一个目录下面出现太多文件，要使用hash算法打散存储目录
	 * 
	 * @param filename:文件名，要根据文件名生成存储目录
	 * @param savePath:文件存储路径
	 * @return:返回生成的存储路径
	 */
	public static String makePath(String filename, String savePath) {
		// 得到文件名的hashCode的值，得到的就是filename这个字符串对象在内存中的地址
		int hashcode = filename.hashCode();
		// 0--15
		int dir1 = hashcode & 0xf; 
		// 0-15
		int dir2 = (hashcode & 0xf0) >> 4; 
		String dir = savePath + File.separator + DateUtil.getDateyyyyMMdd() + File.separator + dir1 + File.separator
				+ dir2;
		File file = new File(dir);
		if (!file.exists()) {
			file.mkdirs();
		}
		return dir + File.separator;
	}

	/**
	 * 下载附件
	 * 
	 * @param response
	 * @param fileName
	 * @param path
	 */
	public static void downloadFile(HttpServletResponse response, String fileName, String path) {
		File file = new File(path);
		if (!file.exists()) {
			logger.info("您要下载的资源已被删除！！");
		}
		FileInputStream in = null;
		OutputStream out = null;
		try {
			response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
			in = new FileInputStream(path);
			out = response.getOutputStream();
			byte [] buffer = new byte[1024];
			int len = 0;
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("UnsupportedEncodingException",e);
		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundException",e);
		} catch (IOException e) {
			logger.error("IOException",e);
		}finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				logger.error("Exception", e);
			}
		}
	}
	
	/**
	 * 保存文件到磁盘
	 * 
	 * @param path：保存目录
	 * @param fileFormat:文件格式
	 * @param bytes
	 * @return
	 */
	public static String saveFile(String path, String fileFormat, byte[] bytes) {
		String savePath = null;
		FileOutputStream fos = null;
		try {
			final String fileName = DateUtil.getDateyyyyMMddHHmmss();
			File dir = new File(path);
			if (!dir.exists()) {
				logger.info(dir.getPath() + "目录不存在,创建目录..");
				if (!dir.mkdirs()) {
					logger.error("创建目录" + dir.getPath() + "失败");
				}
			}
			String index = "";
			File[] oldfiles = dir.listFiles(new FileFilter() {
			    // 文件过滤器，查看目录下是否有同名文件
				@Override
				public boolean accept(File file) {
					String oldFileName = file.getName();
					logger.info("-------------oldFileName:" + oldFileName);
					boolean flag = false;
					if (oldFileName.startsWith(fileName)) {
						flag = true;
					}
					return flag;
				}
			});
			if (oldfiles != null && oldfiles.length > 0) {
			    // 文件名已存在
				index = (oldfiles.length + 1) + "";
			}
			savePath = path + DateUtil.getDateyyyyMMddHHmmss() + index + "." + fileFormat;
			fos = new FileOutputStream(savePath);
			fos.write(bytes);
		} catch (Exception e) {
			logger.error("Exception",e);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					logger.error("IOException",e);
				}
			}
		}
		return savePath;
	}
}

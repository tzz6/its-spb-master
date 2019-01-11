package com.its.common.utils.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import org.apache.log4j.Logger;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import com.its.common.utils.Constants;

/**
 * ZIP工具包(支持中文)
 * 依赖：ant-1.9.6.jar
 */
public class CHZipUtils {
	
	private static final Logger logger = Logger.getLogger(CHZipUtils.class);

	/**使用GBK编码可以避免压缩中文文件名乱码*/
	private static final String CHINESE_CHARSET = "GBK";
	/**文件读取缓冲区大小*/
	private static final int CACHE_SIZE = 1024;

	/**
	 * 压缩文件
	 * @param sourceFolder 压缩文件夹
	 * @param zipFilePath 压缩文件输出路径
	 */
	public static void zip(String sourceFolder, String zipFilePath) {
		OutputStream os = null;
		BufferedOutputStream bos = null;
		ZipOutputStream zos = null;
		try {
			os = new FileOutputStream(zipFilePath);
			bos = new BufferedOutputStream(os);
			zos = new ZipOutputStream(bos);
			// 解决中文文件名乱码
			zos.setEncoding(CHINESE_CHARSET);
			File file = new File(sourceFolder);
			String basePath = null;
			if (file.isDirectory()) {//压缩文件夹
				basePath = file.getPath();
			} else {
				basePath = file.getParent();
			}
			zipFile(file, basePath, zos);
			
		} catch (Exception e) {
			logger.error(Constants.EXCEPTION, e);
		} finally{
			try {
				if (zos != null) {
					zos.closeEntry();
					zos.close();
				}
				if (bos != null) {
					bos.close();
				}
				if (os != null) {
					os.close();
				}
			} catch (IOException e) {
				logger.error(Constants.EXCEPTION, e);
			}
		}
	}

	/**
	 * 递归压缩文件
	 * @param parentFile
	 * @param basePath
	 * @param zos
	 * @throws Exception
	 */
	private static void zipFile(File parentFile, String basePath, ZipOutputStream zos) throws Exception {
		File[] files = null;
		if (parentFile.isDirectory()) {
			files = parentFile.listFiles();
		} else {
			files = new File[1];
			files[0] = parentFile;
		}
		String pathName;
		InputStream is;
		BufferedInputStream bis;
		byte[] cache = new byte[CACHE_SIZE];
		for (File file : files) {
			if (file.isDirectory()) {
				pathName = file.getPath().substring(basePath.length() + 1) + File.separator;
				zos.putNextEntry(new ZipEntry(pathName));
				zipFile(file, basePath, zos);
			} else {
				pathName = file.getPath().substring(basePath.length() + 1);
				is = new FileInputStream(file);
				bis = new BufferedInputStream(is);
				zos.putNextEntry(new ZipEntry(pathName));
				int nRead = 0;
				while ((nRead = bis.read(cache, 0, CACHE_SIZE)) != -1) {
					zos.write(cache, 0, nRead);
				}
				bis.close();
				is.close();
			}
		}
	}

	/**
	 * 解压压缩包
	 * @param zipFilePath 压缩文件路径
	 * @param destDir 解压目录
	 */
	public static void unZip(String zipFilePath, String destDir) {
		ZipFile zipFile = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		try {
			zipFile = new ZipFile(zipFilePath, CHINESE_CHARSET);
			Enumeration<ZipEntry> zipEntries = zipFile.getEntries();
			File file, parentFile;
			ZipEntry entry;
			byte[] cache = new byte[CACHE_SIZE];
			while (zipEntries.hasMoreElements()) {
				entry = (ZipEntry) zipEntries.nextElement();
				if (entry.isDirectory()) {
					new File(destDir + entry.getName()).mkdirs();
					continue;
				}
				bis = new BufferedInputStream(zipFile.getInputStream(entry));
				file = new File(destDir + entry.getName());
				parentFile = file.getParentFile();
				if (parentFile != null && (!parentFile.exists())) {
					parentFile.mkdirs();
				}
				fos = new FileOutputStream(file);
				bos = new BufferedOutputStream(fos, CACHE_SIZE);
				int readIndex = 0;
				while ((readIndex = bis.read(cache, 0, CACHE_SIZE)) != -1) {
					fos.write(cache, 0, readIndex);
				}
				bos.flush();
				bos.close();
				fos.close();
				bis.close();
			}
		} catch (IOException e) {
			logger.error(Constants.EXCEPTION, e);
		}finally{
			try {
				zipFile.close();
			} catch (IOException e) {
				logger.error(Constants.EXCEPTION, e);
			}
		}
	}

}

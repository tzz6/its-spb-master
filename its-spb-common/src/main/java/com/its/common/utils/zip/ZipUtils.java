package com.its.common.utils.zip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author tzz
 */
public class ZipUtils {

	/**
	 * 文件压缩（支持一次压缩多个文件，文件存放至一个文件夹中） 中文名乱码，采用Apache的zip包可解决
	 * 
	 * @param filepath
	 *            要被压缩的文件路径
	 * @param zippath
	 *            压缩后存放的路径
	 */
	public static void zipFile(String filepath, String zippath) {
	    // 要被压缩的文件夹
		File file = null;
		File zipFile = null;
		ZipOutputStream zipOut = null;
		try {
			file = new File(filepath);
			zipFile = new File(zippath);
			zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
			zipFile(file, zipFile, zipOut);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void zipFile(File file, File zipFile, ZipOutputStream zipOut) {
		InputStream input = null;
		try {
			if (file.isDirectory()) {
			    // 压缩文件夹
				File[] files = file.listFiles();
				for (int i = 0; i < files.length; ++i) {
					File f = files[i];
					input = new FileInputStream(f);
					zipOut.putNextEntry(new ZipEntry(file.getName() + File.separator + f.getName()));
					int temp = 0;
					while ((temp = input.read()) != -1) {
						zipOut.write(temp);
					}
					input.close();
				}
				zipOut.close();
			} else {
			    // 压缩单个文件
				input = new FileInputStream(file);
				String fileName = new String(file.getName().getBytes(), "UTF-8");
				zipOut.putNextEntry(new ZipEntry(fileName));
				int temp = 0;
				while ((temp = input.read()) != -1) {
					zipOut.write(temp);
				}
				input.close();
				zipOut.close();
			}
			zipOut.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 解压缩（解压缩单个文件） */
	public static void zipContraFile(String zippath, String outfilepath, String filename) {
		ZipFile zipFile = null;
		// 压缩文件路径和文件名
		File file = null;
		// 解压后路径和文件名
		File outFile = null;
		try {
			file = new File(zippath);
			outFile = new File(outfilepath);
			zipFile = new ZipFile(file);
			// 所解压的文件名
			ZipEntry entry = zipFile.getEntry(filename);
			InputStream input = zipFile.getInputStream(entry);
			OutputStream output = new FileOutputStream(outFile);
			int temp = 0;
			while ((temp = input.read()) != -1) {
				output.write(temp);
			}
			input.close();
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解压缩（压缩文件中包含多个文件）可代替上面的方法使用。 ZipInputStream类
	 * 当我们需要解压缩多个文件的时候，ZipEntry就无法使用了， 如果想操作更加复杂的压缩文件，我们就必须使用ZipInputStream类
	 */
	public static void zipContraMultiFile(String zippath, String outzippath) {
		File file = null;
		ZipFile zipFile = null;
		ZipInputStream zipInput = null;
		ZipEntry entry = null;
		InputStream input = null;
		OutputStream output = null;
		File outFile = null;
		try {
			if (!new File(outzippath).exists()) {
				new File(outzippath).mkdirs();
			}
			file = new File(zippath);
			zipFile = new ZipFile(file);
			zipInput = new ZipInputStream(new FileInputStream(file));
			while ((entry = zipInput.getNextEntry()) != null) {
				System.out.println("解压缩" + entry.getName() + "文件");
				outFile = new File(outzippath + File.separator + entry.getName());
				if (!outFile.getParentFile().exists()) {
					outFile.getParentFile().mkdir();
				}
				if (!outFile.exists()) {
					outFile.createNewFile();
				}
				input = zipFile.getInputStream(entry);
				output = new FileOutputStream(outFile);
				int temp = 0;
				while ((temp = input.read()) != -1) {
					output.write(temp);
				}
				input.close();
				output.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

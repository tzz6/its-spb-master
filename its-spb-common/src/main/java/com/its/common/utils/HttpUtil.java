package com.its.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * HttpUtil
 *
 */
public final class HttpUtil {


	/**
	 * 
	 * 方法说明：<br>
	 * 
	 * @param url
	 *            请求URL
	 * @param content
	 *            post参数 key1=val1&key2=val2&key3=val3
	 * @return
	 * @throws Exception
	 */
	public static String sendByPost(String url, String content) {
		try {
			StringBuilder result = new StringBuilder();
			URL u = new URL(url);
			HttpURLConnection con = (HttpURLConnection) u.openConnection();

			con.setDoInput(true);
			con.setDoOutput(true);
			con.setAllowUserInteraction(false);
			con.setUseCaches(false);
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			con.setConnectTimeout(10000);
			con.setReadTimeout(20000);
			OutputStream out = null;
			BufferedReader reader = null;
			String line = null;
			try {
				out = con.getOutputStream();
				out.write(content.getBytes("UTF-8"));
				out.flush();

				reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
				while ((line = reader.readLine()) != null)
					result.append(line);
			} finally {
				if (out != null) {
					out.close();
				}
				if (reader != null) {
					reader.close();
				}
			}
			return result.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * 方法说明：<br>
	 * 
	 * @param url
	 *            请求URL
	 * @param content
	 *            get参数 key1=val1&key2=val2&key3=val3
	 * @return
	 * @throws IOException
	 */
	public static String sendByGet(String url, String content) throws IOException {
		URL u = new URL(url + "?" + content);
		HttpURLConnection con = (HttpURLConnection) u.openConnection();
		con.setUseCaches(false);
		StringBuilder result = new StringBuilder();
		BufferedReader reader = null;
		String line = null;
		reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
		while (null != (line = reader.readLine())) {
			result.append(line);
		}
		if (reader != null) {
			reader.close();
		}
		return result.toString();
	}

	public static String getStreamContent(InputStream in) {
		StringBuilder result = new StringBuilder();
		String line = null;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			while (null != (line = reader.readLine())) {
				result.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	/**
	 * 
	 * @param url
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public static InputStream getInputStreamBySendPost(String url, String content) throws Exception {
		System.out.println("URL: " + url);
		System.out.println("Content: " + content);
		URL u = new URL(url);
		HttpURLConnection con = (HttpURLConnection) u.openConnection();

		con.setDoInput(true);
		con.setDoOutput(true);
		con.setAllowUserInteraction(false);
		con.setUseCaches(false);
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
		con.setConnectTimeout(10000);
		con.setReadTimeout(20000);
		OutputStream out = null;
		try {
			out = con.getOutputStream();
			out.write(content.getBytes("UTF-8"));
			out.flush();
			return con.getInputStream();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

}

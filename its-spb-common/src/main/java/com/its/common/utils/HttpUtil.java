package com.its.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * HttpUtil
 *
 * @author tzz
 */
public final class HttpUtil {

    public static void main(String[] args) {
        String url = "http://sfbuy-admin.sf-express.com/package1/arrangeList";
//        String url = "http://127.0.0.1:8085/package1/arrangeList";
//        String content = "startDate=2019-2-27%2000:00:00&endDate=2019-3-29%2023:59:59&abordWhCode=JFK03A&source=CN&arrangeStatus=7&statusType=0&keyWord=&ports=&idType=";

        String content = "startDate=2019-2-27%2000:00:00&endDate=2019-3-29%2023:59:59&abordWhCode=JFK03A&source=CN&arrangeStatus=9&statusType=0&keyWord=&ports=&idType=";
        String json = sendByPost(url, content);
        System.out.println(json);
    }


    /**
     * 方法说明：<br>
     *
     * @param url     请求URL
     * @param content post参数 key1=val1&key2=val2&key3=val3
     * @return str
     */
    private static String sendByPost(String url, String content) {
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
            String line;
            try {
                out = con.getOutputStream();
                out.write(content.getBytes(StandardCharsets.UTF_8));
                out.flush();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
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
     * 方法说明：<br>
     *
     * @param url     请求URL
     * @param content get参数 key1=val1&key2=val2&key3=val3
	 */
    public static void sendByGet(String url, String content) throws IOException {
        URL u = new URL(url + "?" + content);
        HttpURLConnection con = (HttpURLConnection) u.openConnection();
        con.setUseCaches(false);
        StringBuilder result = new StringBuilder();
        BufferedReader reader;
        String line;
        reader = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        while (null != (line = reader.readLine())) {
            result.append(line);
        }
		reader.close();
	}
    /**
     * sendByRequest
     * @param url url
     * @param method method
     * @param jsessionid jsessionid
     * @throws IOException
     */
    public static void sendByRequest(String url, String method, String jsessionid) throws IOException {
        URL u = new URL(url);
        HttpURLConnection con = (HttpURLConnection) u.openConnection();
        con.setRequestMethod(method);
        con.setDoOutput(true);
        con.addRequestProperty("Cookie", "JSESSIONID=" + jsessionid);
        con.connect();
        con.getInputStream();
        con.disconnect();
	}

    public static String getStreamContent(InputStream in) {
        StringBuilder result = new StringBuilder();
        String line;
        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            while (null != (line = reader.readLine())) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    /**
     * @param url url
     * @param content content
     * @return InputStream
     * @throws Exception ex
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
		try (OutputStream out = con.getOutputStream()) {
			out.write(content.getBytes(StandardCharsets.UTF_8));
			out.flush();
			return con.getInputStream();
		}
    }

}

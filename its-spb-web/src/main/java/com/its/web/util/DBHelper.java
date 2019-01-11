package com.its.web.util;

import com.its.common.crypto.des.DESUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBHelper {

	private static final Logger logger = LogManager.getLogger(DBHelper.class);
	
	public static String url;
	public static String name;
	public static String user;
	public static String password;

	static {
		String prop = "/jdbc.properties";
		url = PropertiesUtil.getValue(prop, "jdbc.url");
		name = PropertiesUtil.getValue(prop, "jdbc.driver");
		user = PropertiesUtil.getValue(prop, "jdbc.username");
		password = PropertiesUtil.getValue(prop, "jdbc.password");
		// 解密
		url = DESUtil.decrypt(url, DESUtil.KEY);
		user = DESUtil.decrypt(user, DESUtil.KEY);
		password = DESUtil.decrypt(password, DESUtil.KEY);
	}

	private Connection connection;

	private PreparedStatement prepareStatement;

	public Connection getConnection() {
		return connection;
	}

	public PreparedStatement getPrepareStatement() {
		return prepareStatement;
	}

	public DBHelper(String sql, boolean autoCommit) {
		try {
			Class.forName(name);// 指定连接类型
			connection = DriverManager.getConnection(url, user, password);
			connection.setAutoCommit(autoCommit);// 设置事务为非自动提交
			prepareStatement = connection.prepareStatement(sql);
		} catch (Exception e) {
			logger.error("Exception", e);
		}
	}

	public void close() {
		try {
			if (null != this.connection) {
				this.connection.close();
			}
			if (null != this.prepareStatement) {
				this.prepareStatement.close();
			}

		} catch (SQLException e) {
			logger.error("Exception", e);
		}
	}
}
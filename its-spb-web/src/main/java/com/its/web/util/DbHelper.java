package com.its.web.util;

import com.its.common.crypto.des.DesUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 
 * @author tzz
 * @工号: 
 * @date 2019/07/10
 * @Introduce: DBHelper
 */
public class DbHelper {

	private static final Logger logger = LogManager.getLogger(DbHelper.class);
	
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
		url = DesUtil.decrypt(url, DesUtil.KEY);
		user = DesUtil.decrypt(user, DesUtil.KEY);
		password = DesUtil.decrypt(password, DesUtil.KEY);
	}

	private Connection connection;

	private PreparedStatement prepareStatement;

	public Connection getConnection() {
		return connection;
	}

	public PreparedStatement getPrepareStatement() {
		return prepareStatement;
	}

	public DbHelper(String sql, boolean autoCommit) {
		try {
		    // 指定连接类型
			Class.forName(name);
			connection = DriverManager.getConnection(url, user, password);
			// 设置事务为非自动提交
			connection.setAutoCommit(autoCommit);
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
package com.its.model.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Datagrid
 * 
 *
 * @param <T>
 */
public class Datagrid<T> implements Serializable {
	private static final long serialVersionUID = 2352904300281575563L;
	
	/** 总记录数 */
	private long total;
	/** 动态列 */
	private List<Column> columns = new ArrayList<Column>();
	/** 列表行 */
	private List<T> rows;
	/** 脚列表 */
	private List<Map<String, Object>> footer;

	public Datagrid() {
		super();
	}

	/**
	 * 
	 * @param total
	 *            总记录数
	 * @param rows
	 *            列表行
	 */
	public Datagrid(long total, List<T> rows) {
		this(total, null, rows, null);
	}

	/**
	 * 
	 * @param total
	 *            总记录数
	 * @param columns
	 *            动态列
	 * @param rows
	 *            列表行
	 * @param footer
	 *            脚列表
	 */
	public Datagrid(long total, List<Column> columns, List<T> rows, List<Map<String, Object>> footer) {
		super();
		this.total = total;
		this.columns = columns;
		this.rows = rows;
		this.footer = footer;
	}

	/**
	 * 总记录数
	 */
	public long getTotal() {
		return total;
	}

	/**
	 * 设置总记录数
	 * 
	 * @param total
	 *            总记录数
	 */
	public Datagrid<T> setTotal(long total) {
		this.total = total;
		return this;
	}

	/**
	 * 列表行
	 */
	public List<T> getRows() {
		return rows;
	}

	/**
	 * 设置列表行
	 * 
	 * @param rows
	 *            列表行
	 */
	public Datagrid<T> setRows(List<T> rows) {
		this.rows = rows;
		return this;
	}

	/**
	 * 脚列表
	 */
	public List<Map<String, Object>> getFooter() {
		return footer;
	}

	/**
	 * 设置脚列表
	 * 
	 * @param footer
	 *            脚列表
	 */
	public Datagrid<T> setFooter(List<Map<String, Object>> footer) {
		this.footer = footer;
		return this;
	}

	/**
	 * 动态列
	 */
	public List<Column> getColumns() {
		return columns;
	}

	/**
	 * 设置动态列
	 * 
	 * @param columns
	 *            动态列
	 */
	public Datagrid<T> setColumns(List<Column> columns) {
		this.columns = columns;
		return this;
	}

}

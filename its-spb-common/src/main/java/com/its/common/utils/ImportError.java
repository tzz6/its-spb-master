package com.its.common.utils;

import java.io.Serializable;

/**
 * 导入文件上传错误
 * 
 *
 */
public class ImportError implements Comparable<ImportError> ,Serializable {

	private static final long serialVersionUID = 3624021298626500301L;

	/** 行号 **/
	private String rowNum;

	/** 错误原因 **/
	private String errorInfo;

	public ImportError() {
		super();
	}

	public ImportError(String rowNum, String errorInfo) {
		super();
		this.rowNum = rowNum;
		this.errorInfo = errorInfo;
	}

	public String getRowNum() {
		return rowNum;
	}

	public void setRowNum(String rowNum) {
		this.rowNum = rowNum;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	@Override
	public int compareTo(ImportError obj) {
		return this.rowNum.compareTo(obj.getRowNum());
	}

}

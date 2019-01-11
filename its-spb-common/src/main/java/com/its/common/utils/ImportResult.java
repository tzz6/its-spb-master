package com.its.common.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 导入后的返回信息
 * 
 *
 */
public class ImportResult implements Serializable{

	private static final long serialVersionUID = -4248984575118256548L;

	private boolean success = false;

	/*** 单条错误信息 ***/
	private String singleMsg = null;

	/**** 成功数量 ****/
	private int count = 0;

	/** 包含错误行号的错误提示 ***/
	private List<ImportError> errors = new ArrayList<ImportError>();

	public ImportResult() {
		super();
	}

	public ImportResult(boolean success, List<ImportError> errors, int count) {
		super();
		this.success = success;
		this.errors = errors;
		this.count = count;
	}

	public ImportResult(boolean succ, int count) {
		this.success = succ;
		this.count = count;
	}

	public ImportResult(boolean success, String singleMsg, int count) {
		super();
		this.success = success;
		this.singleMsg = singleMsg;
		this.count = count;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getSingleMsg() {
		return singleMsg;
	}

	public void setSingleMsg(String singleMsg) {
		this.singleMsg = singleMsg;
	}

	public List<ImportError> getErrors() {
		return errors;
	}

	public void setErrors(List<ImportError> errors) {
		this.errors = errors;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}

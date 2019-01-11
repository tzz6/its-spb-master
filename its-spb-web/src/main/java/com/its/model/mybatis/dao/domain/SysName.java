package com.its.model.mybatis.dao.domain;

import java.io.Serializable;

/**
 * 系统名称
 * 
 *
 */
public class SysName implements Serializable {

	private static final long serialVersionUID = 8172084302013491935L;

	/** 系统CODE */
	private String sysNameCode;
	/** 名称 */
	private String name;
	/** 英文名称 */
	private String enName;
	/** 语言CODE */
	private String bldCode;
	/** 状态（1：有效、0：无效） */
	private String snStatus;

	public String getSysNameCode() {
		return sysNameCode;
	}

	public void setSysNameCode(String sysNameCode) {
		this.sysNameCode = sysNameCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public String getBldCode() {
		return bldCode;
	}

	public void setBldCode(String bldCode) {
		this.bldCode = bldCode;
	}

	public String getSnStatus() {
		return snStatus;
	}

	public void setSnStatus(String snStatus) {
		this.snStatus = snStatus;
	}

}

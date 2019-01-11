package com.its.model.mybatis.dao.domain;

import java.io.Serializable;
import java.util.Date;

import com.its.model.annotation.Import;


/**
 * SysUser
 *
 */
public class SysUser implements Serializable {

	private static final long serialVersionUID = 7523417995488977087L;

	/** ID */
	private String stId;
	/** 用户名 */
	@Import(columnIndex = 0, description = "st_code", required = true)
	private String stCode;
	/** 用户姓名 */
	@Import(columnIndex = 1, description = "st_name", required = true)
	private String stName;
	/** 密码 */
	@Import(columnIndex = 2, description = "st_password", required = true)
	private String stPassword;
	/** 语言 */
	private String language;
	/** 创建人 */
	private String createBy;
	/** 编辑人 */
	private String updateBy;
	/** 创建时间 */
	private Date createTm;
	/** 修改时间 */
	private Date updateTm;
	
	public String getStId() {
		return stId;
	}
	public void setStId(String stId) {
		this.stId = stId;
	}
	public String getStCode() {
		return stCode;
	}
	public void setStCode(String stCode) {
		this.stCode = stCode;
	}
	public String getStName() {
		return stName;
	}
	public void setStName(String stName) {
		this.stName = stName;
	}
	public String getStPassword() {
		return stPassword;
	}
	public void setStPassword(String stPassword) {
		this.stPassword = stPassword;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public Date getCreateTm() {
		return createTm;
	}
	public void setCreateTm(Date createTm) {
		this.createTm = createTm;
	}
	public Date getUpdateTm() {
		return updateTm;
	}
	public void setUpdateTm(Date updateTm) {
		this.updateTm = updateTm;
	}
	
}
package com.its.model.mybatis.dao.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * SysRole
 * 
 */
public class SysRole implements Serializable {

	private static final long serialVersionUID = -5137156121850070587L;

	/** ID */
	private String roleId;
	/** 名称 */
	private String roleName;
	/** 系统CODE */
	private String sysNameCode;
	/** 系统名称 */
	private String sysName;
	/** 创建人 */
	private String createBy;
	/** 编辑人 */
	private String updateBy;
	/** 创建时间 */
	private Date createTm;
	/** 修改时间 */
	private Date updateTm;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getSysNameCode() {
		return sysNameCode;
	}

	public void setSysNameCode(String sysNameCode) {
		this.sysNameCode = sysNameCode;
	}

	public String getCreateBy() {
		return createBy;
	}

	public String getSysName() {
		return sysName;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
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

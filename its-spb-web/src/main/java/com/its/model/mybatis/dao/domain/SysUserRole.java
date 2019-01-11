package com.its.model.mybatis.dao.domain;

import java.io.Serializable;

/**
 * 用户角色
 * 
 *
 */
public class SysUserRole implements Serializable {

	private static final long serialVersionUID = -797641210509539164L;
	
	/** 用户ID */
	private String stId;
	/** 角色ID */
	private String roleId;
	public String getStId() {
		return stId;
	}
	public void setStId(String stId) {
		this.stId = stId;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

}
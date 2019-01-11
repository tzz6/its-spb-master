package com.its.model.mybatis.dao.domain;

import java.io.Serializable;

/**
 * 角色菜单
 * 
 *
 */
public class SysRoleMenu implements Serializable {

	private static final long serialVersionUID = -5362050803667838008L;
	/** 角色ID */
	private String roleId;
	/** 菜单ID */
	private String menuId;
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

}
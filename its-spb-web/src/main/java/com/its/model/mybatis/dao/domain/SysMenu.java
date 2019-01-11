package com.its.model.mybatis.dao.domain;

import java.io.Serializable;

/**
 * 菜单
 *
 */
public class SysMenu implements Serializable {

	private static final long serialVersionUID = 6492822046025899115L;
	
	/** ID */
	private String menuId;
	/** 上级菜单ID */
	private String parentMenuId;
	/** 上级菜单名 */
	private String parentMenuName;
	/** 菜单名*/
	private String menuName;
	/** 排序*/
	private Integer menuSort;
	/** URL */
	private String menuUrl;
	/** 菜单类型（M:菜单、B:按钮） */
	private String menuType;
	/** 权限编码 */
	private String permissionCode;
	/** 权限URL */
	private String permissionUrl;
	/** 系统CODE */
	private String sysNameCode;
	/** 系统名称 */
	private String sysName;
	/** 语言CODE */
	private String bldCode;
	
	
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getParentMenuId() {
		return parentMenuId;
	}
	public String getParentMenuName() {
		return parentMenuName;
	}
	public void setParentMenuName(String parentMenuName) {
		this.parentMenuName = parentMenuName;
	}
	public void setParentMenuId(String parentMenuId) {
		this.parentMenuId = parentMenuId;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public Integer getMenuSort() {
		return menuSort;
	}
	public void setMenuSort(Integer menuSort) {
		this.menuSort = menuSort;
	}
	public String getMenuUrl() {
		return menuUrl;
	}
	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}
	public String getMenuType() {
		return menuType;
	}
	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}
	public String getPermissionCode() {
		return permissionCode;
	}
	public void setPermissionCode(String permissionCode) {
		this.permissionCode = permissionCode;
	}
	public String getPermissionUrl() {
		return permissionUrl;
	}
	public void setPermissionUrl(String permissionUrl) {
		this.permissionUrl = permissionUrl;
	}
	public String getSysNameCode() {
		return sysNameCode;
	}
	public void setSysNameCode(String sysNameCode) {
		this.sysNameCode = sysNameCode;
	}
	public String getSysName() {
		return sysName;
	}
	public void setSysName(String sysName) {
		this.sysName = sysName;
	}
	public String getBldCode() {
		return bldCode;
	}
	public void setBldCode(String bldCode) {
		this.bldCode = bldCode;
	}
}
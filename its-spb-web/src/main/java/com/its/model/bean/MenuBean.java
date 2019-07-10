package com.its.model.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author tzz
 */
public class MenuBean{

	private String menuId;
	
	private String menuname;
	
	private String icon;
	
	private String miParameter;
	
	private String miHierarchicalstructure;
	
	private List<MenuBean> menus = new ArrayList<MenuBean>();
	
	private String url;

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getMenuname() {
		return menuname;
	}

	public void setMenuname(String menuname) {
		this.menuname = menuname;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}


	public String getMiParameter() {
		return miParameter;
	}

	public void setMiParameter(String miParameter) {
		this.miParameter = miParameter;
	}

	public String getMiHierarchicalstructure() {
		return miHierarchicalstructure;
	}

	public void setMiHierarchicalstructure(String miHierarchicalstructure) {
		this.miHierarchicalstructure = miHierarchicalstructure;
	}

	public List<MenuBean> getMenus() {
		return menus;
	}

	public void setMenus(List<MenuBean> menus) {
		this.menus = menus;
	}
	
}

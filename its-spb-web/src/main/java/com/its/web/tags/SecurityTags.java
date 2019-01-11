package com.its.web.tags;

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.its.model.mybatis.dao.domain.SysMenu;
import com.its.web.util.UserSession;


/**
 * 自定义权限控制标签
 *
 */
public class SecurityTags extends TagSupport {

	private static final long serialVersionUID = 3355675361846153593L;
	// <sec:authentication property=""></sec:authentication>
	private String property;

	@Override
	public int doStartTag() throws JspException {
		boolean flag = false;
		String permissionCode = getProperty();
		if (permissionCode != null) {
			List<SysMenu> sysMenus = UserSession.getSysMenu();
			for (SysMenu sysMenu : sysMenus) {
				if (permissionCode.equals(sysMenu.getPermissionCode())) {
					flag = true;
				}
			}
		}
		if (flag) {
			return EVAL_BODY_INCLUDE;
		} else {
			return SKIP_BODY;
		}
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

}

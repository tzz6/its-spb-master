package com.its.core.mybatis.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.its.model.mybatis.dao.domain.SysRoleMenu;


@Repository("sysRoleMenuMapper")
public interface SysRoleMenuMapper {

	List<SysRoleMenu> getSysRoleMenuByRoleId(@Param("roleId") String roleId);

	void saveSysRoleMenu(List<SysRoleMenu> list);

	void deleteSysRoleMenuByRoleId(@Param("roleId") String roleId);

}

package com.its.base.servers.api.sys.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.its.common.model.ResponseBase;

/**
 *
 * @author tzz
 * @Introduce: SysRoleService
 */
public interface SysRoleService {

    /**
     * 根据id获取SysRole
     *
     * @param id id
     * @return String
     */
    @GetMapping("/getSysRoleById")
    String getSysRoleById(@RequestParam(value = "id") String id);

    /**
     * 获取SysRol
     * @return ResponseBase
     */
    @PostMapping("/getSysRole")
    ResponseBase getSysRole();

}

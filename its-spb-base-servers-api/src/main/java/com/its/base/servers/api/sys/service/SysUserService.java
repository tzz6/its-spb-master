package com.its.base.servers.api.sys.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.its.base.servers.api.sys.domain.SysUser;
import com.its.common.model.ResponseBase;

/**
 * 
 * @author tzz
 * @Introduce: SysUserService
 */
public interface SysUserService {

    /**
     * 根据id获取SysUser
     * 
     * @param stId
     * @return
     */
    @GetMapping("/getSysUserByStId")
    public SysUser getSysUserByStId(@RequestParam(value = "stId") String stId);

    /**
     * 获取SysUser
     * 
     * @param sysUser
     * @return
     */
    @PostMapping("/getSysUser")
    public ResponseBase getSysUser(@RequestBody SysUser sysUser);

}

package com.its.core.mybatis.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.its.model.mybatis.dao.domain.SysName;


@Repository("sysNameMapper")
public interface SysNameMapper {

	public List<SysName> getSysNameByLang(@Param("lang") String lang);

}

package com.its.web.druid;

import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.its.common.mybatis.config.DruidConfig;
import com.its.core.mybatis.dao.mapper.SysMenuMapper;
import com.its.model.mybatis.dao.domain.SysMenu;

@RunWith(SpringRunner.class)
//@ComponentScan(basePackages = {"com.its.core"})//增加自动扫描的包
@Import(DruidConfig.class)
@SpringBootTest
/**
 * 
 * @author tzz
 * @工号: 
 * @date 2019/07/11
 * @Introduce: DruidTest
 */
public class DruidTest {

	@Autowired
	private DataSource dataSource;

	@Resource
	private SysMenuMapper sysMenuMapper;

	@Test
	public void contextLoads() {
		System.out.println(dataSource);
	}
	@Test
	public void testMybatis() {
		List<SysMenu> sysMenus = sysMenuMapper.getSysMenus();
		System.out.println(sysMenus);
	}

}

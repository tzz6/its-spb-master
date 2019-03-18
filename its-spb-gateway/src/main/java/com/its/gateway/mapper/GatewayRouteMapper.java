package com.its.gateway.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.JdbcType;

import com.its.gateway.domain.GatewayRouter;

/**
 * 
 * @author tzz
 * @Introduce: Gateway动态路由
 */
public interface GatewayRouteMapper {

    /**
     * 获取动态路由配置
     * 
     * @return
     */
    @Select("select gateway_route_id,gateway_route_key,gateway_route,status from its_spb_gateway_route where status = '1'")
    @Results(id = "GatewayRoute", value = {
        @Result(column = "gateway_route_id", property = "id", javaType = Integer.class, jdbcType = JdbcType.BIGINT),
        @Result(column = "gateway_route_key", property = "key", javaType = String.class, jdbcType = JdbcType.VARCHAR),
        @Result(column = "gateway_route", property = "gatewayRoute", javaType = String.class,
            jdbcType = JdbcType.VARCHAR),
        @Result(column = "status", property = "status", javaType = String.class, jdbcType = JdbcType.VARCHAR)})
    public List<GatewayRouter> getGatewayRouteList();

    /**
     * 新增
     * 
     * @param gatewayRouter
     * @throws Exception
     */
    @Insert("insert into its_spb_gateway_route(gateway_route, gateway_route_key, status)values(#{gatewayRoute}, #{key}, #{status})")
    public void insertGatewayRoute(GatewayRouter gatewayRouter) throws Exception;

    /**
     * 修改
     * 
     * @param gatewayRouter
     * @throws Exception
     */
    @Update("update its_spb_gateway_route set gateway_route = #{gatewayRoute}, status = #{status} where gateway_route_key = #{key}")
    public void updateGatewayRouteByKey(GatewayRouter gatewayRouter) throws Exception;
    /**
     * 修改
     * 
     * @param gatewayRouter
     * @throws Exception
     */
    @Update("update its_spb_gateway_route set gateway_route = #{gatewayRoute} where gateway_route_id = #{id}")
    public void updateGatewayRoute(GatewayRouter gatewayRouter) throws Exception;

    /**
     * 根据id删除
     * 
     * @param id
     * @throws Exception
     */
    @Delete("delete from its_spb_gateway_route where gateway_route_id = #{id}")
    public void deleteGatewayRoute(Integer id) throws Exception;

    /**
     * 删除全部
     * 
     * @param id
     * @throws Exception
     */
    @Delete("delete from its_spb_gateway_route")
    public void deleteGatewayRouteAll() throws Exception;

}

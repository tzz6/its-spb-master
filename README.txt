1、project说明

2、URL
	 http://127.0.0.1:8101/ 可访问注册中心
	 http://127.0.0.1:8401/ 访问配置中心服务端  如：http://127.0.0.1:8401/its-spb-zuul-dev.yml
	 http://localhost:8301/actuator/refresh 通过post请求如下地址实现Client端手动刷新配置文件(its-spb-order-servers)
	 http://localhost:80/actuator/refresh 通过post请求如下地址实现Client端手动刷新配置文件(its-spb-zull)
	 http://127.0.0.1/api-order/getOrderToBaseGetSysRoleHystrix/1 通过网关访问its-spb-order-servers服务
	 http://127.0.0.1/api-base/getMember 通过网关访问its-spb-base-servers服务
	 http://127.0.0.1:8301/getOrderToBaseGetSysRoleHystrix/1 直接访问its-spb-order-servers服务
	 http://127.0.0.1:8201/getMember 直接访问its-spb-base-servers服务
	 http://127.0.0.1:80/swagger-ui.html  查看swagger API
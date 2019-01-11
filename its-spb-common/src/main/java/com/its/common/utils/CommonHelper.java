package com.its.common.utils;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Set;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class CommonHelper {

	private static Log log = LogFactory.getLog(CommonHelper.class);

	private static final String SERVER_ID = InitialServerID.getServerId();
	
	private static class InitialServerID {
		private static String getServerId() {
			String serverid = null;
			try {
				serverid = getTomcatServerHttpServerid();
			} catch (Exception ex) {
			}
			log.info("SERVERID=" + serverid);
			//尝试多种方案获取服务器ip
			if(serverid==null){
				try {
					serverid = InetAddress.getLocalHost().getHostAddress();
				} catch (UnknownHostException e) {
				}
			}
			return serverid;
		}
	}

	// 获取Serverid+port的字符串,JBOSS
	public static String getServerId() {
		return SERVER_ID;
	}

	static final String TOMCAT_OBJECTNAME = "jboss.web:type=Connector,*";
	static final String TOMCAT_HTTP_PROTOCOL = "HTTP/1.1";

	public static String getTomcatServerHttpServerid() throws Exception {

		MBeanServer server = null;
		List<MBeanServer> mbeanServers = MBeanServerFactory
				.findMBeanServer(null);
		for (MBeanServer _mbs : mbeanServers) {
			if ("jboss".equals(_mbs.getDefaultDomain())) {
				server = _mbs;
				break;
			}
		}
		
		//加入JMX无法获得端口号，可以加入下面的代码
		if (server == null) {
			server = ManagementFactory.getPlatformMBeanServer();
		}
		
		if (server == null)
			throw new Exception("没有命名为jboss的域");

		Set<ObjectInstance> instances = server.queryMBeans(ObjectName
				.getInstance(TOMCAT_OBJECTNAME), null);

		for (ObjectInstance _ins : instances)
			System.out
					.println(_ins.getObjectName() + " " + _ins.getClassName());

		Set<ObjectName> names = server.queryNames(ObjectName
				.getInstance(TOMCAT_OBJECTNAME), null);
		for (ObjectName name : names) {
			String protocol = (String) server.getAttribute(name, "protocol");
			if (TOMCAT_HTTP_PROTOCOL.equals(protocol)) {
				Object o = server.getAttribute(name, "address");
				String ip = o.toString().substring(1);
//				int port = (Integer)server.getAttribute(name, "port");
				return ip;
			}

		}

		throw new Exception("获取http端口出错");
	}
	
}

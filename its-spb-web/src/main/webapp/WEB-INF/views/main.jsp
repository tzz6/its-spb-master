<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/commons/common.jsp"%>
<script type="text/javascript" src="${ctx}/js/outlook.js"> </script>
<%@ include file="/commons/language.jsp"%>

<fmt:bundle basename="com.its.resource.lang">
<!DOCTYPE html>
<html>
<head>
<title><fmt:message key="mcs.index" /></title>
<style>
#mainPage .panel {
	float: left;
	margin-right: 20px;
	width: 50%;
}

#mainPage .panel #pDate {
	border: none;
	padding: 0 !important;
}
</style>

<script type="text/javascript"> 
$(function(){
    $('#loginOut').click(function() {
		$.messager.confirm(Msg.sys_remaind1, Msg.sys_logout, function(r) {
	    if (r) {
	    	window.location.href = $("#ctx").val() + '/logout';
	    }
		});
	});
});
//菜单
 var _menus = { basic : <%=request.getAttribute("menuJson")%> };
</script>
</head>
<body id="cc" class="easyui-layout" style="overflow-y: hidden"
	scroll="no">
	<noscript>
		<div
			style="position: absolute; z-index: 100000; height: 2046px; top: 0px; left: 0px; width: 100%; background: white; text-align: center;">
			<img src="images/noscript.gif" alt='抱歉，请开启脚本支持！' />
		</div>
	</noscript>
	<input type="hidden" name="ctx" id="ctx" value="${ctx }">
	<div region="north" split="true" border="false"
		style="overflow: hidden; height: 45px; background: #6C98D1; line-height: 15px; color: #fff; font-family: Verdana, 微软雅黑, 黑体">
		<span style="float: right; padding-right: 20px; margin-top: 12px;"
			class="head"><fmt:message key="main.welcome" />&nbsp; ${ITS_USER_SESSION.stCode } <!-- 登出 --> &nbsp;&nbsp;<a
			href="###" id="loginOut"> <fmt:message key="main.logout" /></a>
		</span> 
<!-- 		<span -->
<!-- 			style="padding-left: 10px; font-size: 16px; float: left; margin-top: 5px;"><img -->
<!-- 			src="images/logo.png" width="80" height="30" align="absmiddle" /> -->
<%-- 			&nbsp;&nbsp;${requestScope.rb.cbtb_title_txt} </span> --%>
	</div>
	<div region="south" split="true"
		style="height: 40px; background: #D2E0F2;">
		<div class="footer">&copy;2017&nbsp;&nbsp;<fmt:message key="main.sf" />&nbsp;&nbsp;<fmt:message key="main.arr" /></div>
		
	</div>
	<div id="mm" class="easyui-menu" style="width: 150px;">
		<div id="mm-tabupdate"><fmt:message key="main.refresh" /></div>
		<div class="menu-sep"></div>
		<div id="mm-tabclose"><fmt:message key="main.close" /></div>
		<div id="mm-tabcloseall"><fmt:message key="main.close.all" /></div>
		<div id="mm-tabcloseother"><fmt:message key="main.close.other" /></div>
		<div class="menu-sep"></div>
		<div id="mm-tabcloseright"><fmt:message key="main.close.right" /></div>
		<div id="mm-tabcloseleft"><fmt:message key="main.close.left" /></div>
<!-- 		<div class="menu-sep"></div> -->
<%-- 		<div id="mm-exit"><fmt:message key="main.exit" /></div> --%>
	</div>
	<div region="west" hide="true" split="true" title="<fmt:message key='navigat.meun' />"
		style="width: 180px;" id="west">
		<!--  导航内容 -->
		<div id='wnav' class="easyui-accordion" fit="true" border="false"></div>
	</div>
	<div data-options="region:'center'"
		style="padding: 5px; background: #eee;">
		<div id="tabs" class="easyui-tabs" fit="true" border="false">
			<div title="<fmt:message key='main.index' />" style="padding: 20px;" id="mainPage">
				<div id="p" class="easyui-panel" title="<fmt:message key='main.my.info' />"
					style="width: 400px; height: 200px; padding: 10px; float: left;">
					<div>
						<span style="font-size: 14px"><fmt:message key="main.howdy" />，${ITS_USER_SESSION.stCode }<br /></span>
						<span style="font-size: 14px">EhCache页面缓存
							<%  
								SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							    out.print(format.format(new Date()));  
							    System.out.println(System.currentTimeMillis());  
							%>  
						</span>
					</div>
				</div>
				<div id="pDate" class="easyui-panel"
					style="width: 400px; height: 300px; padding: 10px; float: right;">
					<div id="cc" class="easyui-calendar"
						style="width: 280px; height: 280px;"></div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
</fmt:bundle>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:choose>
   <c:when test="${!(empty cookie.lang.value)}"> 
   <c:set var="lang" value="${cookie.lang.value}"/> 
   </c:when>
   <c:otherwise>
   <%
   	String language = request.getLocale().toString();
   	if (language.compareToIgnoreCase("zh_HK")==0 || language.compareToIgnoreCase("zh_TW")==0 || language.compareToIgnoreCase("zh_CN")==0){
   		language = "zh";
   	}
   %>
   <c:set var="lang" value="<%=language%>"/>
   </c:otherwise>
</c:choose>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<fmt:setLocale value="${lang}" />
<fmt:bundle basename="com.its.resource.lang">
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key="login.button" /></title>
<script type="text/javascript" src="${ctx}/js/common/jquery-2.1.4.js"></script>
<script type="text/javascript" src="${ctx}/js/common/jquery.i18n.min.js"></script>
<script type="text/javascript" src="${ctx}/js/common/jquery.form.js"></script>
<script type="text/javascript" src="${ctx}/js/common/language.js?v=20171220001"></script>
<script type="text/javascript" src="${ctx}/js/jsencrypt.min.js?v=201801040001"></script>
<script type="text/javascript" src="${ctx}/js/login/login.js?v=201801040001"></script>
<link href="${ctx}/css/css.css?v=20171220001" rel="stylesheet" />
<style>
body{background:#fff;}
.login-brand-box{background:#fff;}
.logo-mod{background:#fff;}
.header{width:888px;height:422px;margin:0 auto;}
.footer{position:absolute;left:0;bottom:0;width:100%;}
.login{border:1px solid #ccc;padding:0 20px;background:#F9F9F9;}
.login h2 {margin:20px 5px;padding-bottom: 5px;color: #333;font-size: 18px;}
.login table{width:300px;}
.login table td{padding:5px\9;margin:5px\9;}
.login-type label{display:inline-block;margin-right:15px;margin-left:5px;}
br {display: block;margin: 1px 0;line-height:1px;}
</style>
</head>
<body>
<input type="hidden" id="ctx" name="ctx" value="${ctx }">
<div class="header">
	<div class="brand-box login-brand-box">
		<div class="brand login-brand">
		<div class="login">
		<h2>ITS后台管理平台</h2>
		<form id="login_form" action="${ctx }/login" method="post">
		<input type="hidden" id="lang" name="lang" value="${lang}" />
		<table>
			<tr>
			<td>
			<input class="input_cls" type="text" id="username" name="username" value="${username }" placeholder="<fmt:message key="login.username" />"/>
			<br/><span class="error" id="usernameInfo"></span>
			</td>
			</tr>
			<tr>
			<td>
			<input class="input_cls" type="password" id="password" name="password" value="${password}" placeholder="<fmt:message key="login.password" />"/>
			<br/><span class="error" id="loginPsw-msg"></span>
			</td>
			</tr>
			<tr>
			<td>
			<input class="input_cls" style="width: 90px;" type="text" id="verifyCode" name="verifyCode" placeholder="验证码"/> 
			<img height="26" src="${ctx }/verifyCodeServlet" width="88" height="32" style="vertical-align: middle;" id="securityCodeImg">
			</td>
			</tr>
			<tr>
			<td>
			<select class="input_cls" style="width: 90px;" name="language" id="language" onchange="changeLanguage();">
			<option value="" <c:if test="${lang==''}">selected</c:if>><fmt:message key="login.language.opt" /></option>
			<option value="zh" <c:if test="${lang=='zh'}">selected</c:if>><fmt:message key="login.language.cn" /></option>
			<option value="en" <c:if test="${lang=='en'}">selected</c:if>><fmt:message key="login.language.en" /></option>
			</select>
			</td>
			</tr>
			<tr>
			<td>
			<input type="checkbox" id="savePassword" name="savePassword" value="1" 
			<c:if test="${savePassword == '1' }">checked="checked"</c:if>> <fmt:message key="login.savePassword" />
			</td>
			</tr>
			<tr>
			<td>
<%-- 			<button id="btn_login" class="login_btn"> <fmt:message key="login.button" /></button> --%>
			<input id="btn_login" type="button"  class="login_btn" value="<fmt:message key="login.button" />" onclick="ajaxSubmit()"/>
			</td>
			</tr>
			<tr>
			<td id="errorInfo" style="color: red;"></td>
			</tr>
		</table>
		</form>
		</div>
		<div class="people"></div>
	</div>
	</div>
</div>
<div class="footer">
	<div class="footer-content"><ul><li>&copy;2018 ITS 版权所有</li></ul></div>
</div>
</body>
</html>
</fmt:bundle>
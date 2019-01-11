<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>  
<link rel="stylesheet" type="text/css" href="<%=basePath %>css/easyui/themes/metro/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=basePath %>css/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="<%=basePath %>css/easyui/demo/demo.css">
<link rel="stylesheet" type="text/css" href="<%=basePath %>css/public.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath %>css/default.css" />
<script type="text/javascript" src="<%=basePath %>js/jquery-1.8.0.min.js"></script>
<script type="text/javascript" src="<%=basePath %>js/jquery-extend.js"></script>
<script type="text/javascript" src="<%=basePath %>js/easyui/jquery.easyui.min.js"></script> 
<script type="text/javascript" src="<%=basePath %>js/common.js?v=201409281118"> </script>
<link rel="stylesheet" type="text/css" href="<%=basePath %>js/My97DatePicker/my97.css">
<script type="text/javascript" src="<%=basePath %>js/My97DatePicker/jquery.my97.js"></script>
<script type="text/javascript" src="<%=basePath %>js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="<%=basePath %>js/jquery.form.js?v=201409281118"> </script>
<script type="text/javascript" src="<%=basePath %>js/json2.js"></script>
<script type="text/javascript" src="<%=basePath %>js/jquery.blockUI.js"></script>
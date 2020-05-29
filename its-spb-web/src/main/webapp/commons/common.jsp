<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<c:set var="lang" value="${ITS_USER_SESSION.language}"/>
<!-- 服务网关 start-->
<!-- 跨域访问 -->
<c:set var="apibase" value="http://localhost/api-base"></c:set>
<c:set var="apiorder" value="http://localhost/api-order"></c:set>
<!-- 同域访问nginx配置为同域-->
<%--<c:set var="apibase" value="/api-base"></c:set>--%>
<%--<c:set var="apiorder" value="/api-order"></c:set>--%>
<!-- 服务网关 end-->
<link rel="stylesheet" type="text/css" href="${ctx}/easyui-1.5.2/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${ctx}/easyui-1.5.2/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${ctx}/easyui-1.5.2/themes/color.css">
<link rel="stylesheet" type="text/css" href="${ctx}/easyui-1.5.2/demo/demo.css">
<link rel="stylesheet" type="text/css" href="${ctx}/css/public.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/css/default.css" />
<link rel="stylesheet" type="text/css" href="${ctx}/js/My97DatePicker/my97.css">
<script type="text/javascript" src="${ctx}/easyui-1.5.2/jquery.min.js?v=201703201200"></script>
<script type="text/javascript" src="${ctx}/easyui-1.5.2/jquery.easyui.min.js?v=201703201200"></script>
<script type="text/javascript" src="${ctx}/easyui-1.5.2/locale/easyui-lang-${lang}.js?v=201703201200"></script>
<script type="text/javascript" src="${ctx}/js/jquery-extend.js?v=201703201200"></script>
<script type="text/javascript" src="${ctx}/js/common.js?v=201703201200"> </script>
<script type="text/javascript" src="${ctx}/js/My97DatePicker/jquery.my97.js?v=201703201200"></script>
<script type="text/javascript" src="${ctx}/js/My97DatePicker/WdatePicker.js?v=201703201200"></script>
<script type="text/javascript" src="${ctx}/js/jquery.form.js?v=201703201200"> </script>
<script type="text/javascript" src="${ctx}/js/json2.js?v=201703201200"></script>
<script type="text/javascript" src="${ctx}/js/jquery.blockUI.js?v=201703201200"></script>
<script type="text/javascript" src="${ctx}/js/ajaxExt.js?v=201908262100"> </script>

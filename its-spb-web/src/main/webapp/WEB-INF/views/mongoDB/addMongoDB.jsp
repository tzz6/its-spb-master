<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<script type="text/javascript" src="${ctx}/js/ajaxExt.js?v=201908262100"> </script>
<c:set var="apibase" value="http://localhost/api-base"></c:set>
<c:set var="apiorder" value="http://localhost/api-order"></c:set>
<c:set var="lang" value="${ITS_USER_SESSION.language}" />
<input type="hidden" id="lang" value="${lang}" />
<fmt:setLocale value="${lang}" />
<fmt:bundle basename="com.its.resource.lang">
	<form id="mongodb_dialog_form" action="##" method="post">
		<input type="hidden" id="id" name="id" value="">
		<table cellpadding="0" cellspacing="5" class="form-table">
			<tr>
				<td class="agent-service-td">城市中文名:</td>
				<td colspan="3"><input id="name" name="name"
					style="width: 113px;" class="easyui-validatebox"
					data-options="required:true,validType:['length[1,20]']" /></td>
			</tr>
			<tr>
				<td class="agent-service-td">城市英文名:</td>
				<td colspan="3"><input id="enName" name="enName"
					style="width: 113px;" class="easyui-validatebox"
					data-options="required:true,validType:['length[1,20]']" /></td>
			</tr>
			<tr>
				<td class="agent-service-td">城市编码:</td>
				<td colspan="3"><input id="code" name="code"
					style="width: 113px;" class="easyui-validatebox"
					data-options="required:true,validType:['length[1,20]']" /></td>
			</tr>
		</table>
	</form>
	<div id="mongodb_dialog_button_div" style="text-align: center;">
		<a href="#" id="mongodb_dialog_linkbutton_save"
			class="easyui-linkbutton" data-options="iconCls:'icon-save'">保存</a>
		&nbsp;&nbsp;&nbsp;&nbsp; <a href="#"
			id="mongodb_dialog_linkbutton_cancel" class="easyui-linkbutton"
			data-options="iconCls:'icon-cancel'">取消</a>
	</div>




</fmt:bundle>
<script type="text/javascript">
	$(function() {
		$('#mongodb_dialog_linkbutton_cancel').click(function() {
			$("#mongodb_dialog_div").dialog('close');
		});

		$('#mongodb_dialog_linkbutton_save').click(function() {
			if ($(this).linkbutton("options").disabled) { //已禁用按钮
				return;
			}
			addMongoDB();
		});

	})

	//保存
	function addMongoDB() {
		$("#mongodb_dialog_linkbutton_save").linkbutton('enable');
		ajaxBaseExt('${apibase}/mongoDB/save', "POST", false, "JSON", $('#mongodb_dialog_form').serialize(),
				successFunction, errorFunction);
	}

	function successFunction(result) {
		$("#mongodb_dialog_linkbutton_save").linkbutton("enable");
		$("#mongodb_dialog_div").dialog("close");
		$('#mongodb_table').datagrid("clearChecked").datagrid("reload");
	}
	function errorFunction() {
		$.messager.show({
			title: Msg.sys_remaind1,
			msg: Msg.sys_err
		});
	}

</script>
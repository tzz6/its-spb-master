<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/commons/common.jsp"%>
<%@ include file="/commons/language.jsp"%>
<%@ taglib uri="http://www.its.web.com" prefix="sc"%>
<fmt:bundle basename="com.its.resource.lang">
<!DOCTYPE html>
<html>
<head>
</head>
<body class="easyui-layout">
	<div id="tb" style="padding:5px;"> 
		<!-- 	查询条件 -->
		<form id="search_form">
			<table style="cellspacing: 0">
				<tr>
					<td class="content_title">&nbsp;<fmt:message key="user.employee.number" />:</td>
					<td class="content_input"><input type="text" name="stCode"
						id="stCode" /></td>
					<td class="content_linkbutton" style="padding-left: 10px;"><a
						href="#" id="search_linkbutton" class="easyui-linkbutton"
						data-options="iconCls:'icon-search'"> <fmt:message
								key="btn.search" /></a></td>
					<td style="padding-left: 10px;"><a href="#"
						class="easyui-linkbutton" data-options="iconCls:'icon-reload'"
						id="reset_linkbutton"> <fmt:message key="btn.reset" /></a></td>
				</tr>
			</table>
		</form>

	</div>
	<!-- 用户列表 -->
	<table id="user_table" cellspacing="0" cellpadding="0" data-options="rownumbers:true"  ></table>
</body>
<script type="text/javascript">
	$(function() {
		// 初始化系统列表
		$('#user_table').datagrid(
				{
					url : '${ctx}${url}?random='
							+ new Date().getTime(),
					pageList : [ 50, 100, 200 ],
					pageSize : 50,
					rownumbers : true,
					pagination : true,
					remoteSort : true,
					checkOnSelect : false,
					selectOnCheck : false,
					singleSelect : true,
					toolbar : '#tb',
					columns : [ [ {
						field : 'rowCheckBox',
						title : '${requestScope.rb.query_customerMsg_txt_14}',
						width : 10,
						checkbox : true,
						align : 'center'
					}, {
						field : 'stCode',
						title : Msg.sys_user_code,
						width : 200,
						align : 'center'
					}, {
						field : 'stName',
						title : Msg.sys_user_name,
						width : 200,
						align : 'center'
					}, {
						field : 'createTm',
						title : Msg.sys_user_create_tm,
						width : 200,
						align : 'center',
						formatter: function(value, row){
							return $.fn.timestampFormat(value,'yyyy-MM-dd HH:mm:ss');
						}
					}, {
						field : 'stId',
						hidden : true,
						width : 350,
						align : 'center'
					} ] ],
					onLoadSuccess : function(data) {
						if (data.total == '0') { // 查询无记录时提醒
							$("#not_exist").show();
						} else {
							$("#not_exist").hide();
						}
					}
				});
		_doResize($('#user_table'));
	});
	
	//控制页面大小不随展示行数变化
	$(window).resize(function(){
		 _doResize($('#user_table'));
	});
	     
	// 查询按钮点击事件 		
	$('#search_linkbutton').click(function() {
		querySysUserList();
	});

	// 重置按钮点击事件
	$('#reset_linkbutton').click(function() {
		resetSearchForm();
	});
	// 重置按钮点击事件
	$('#reset_linkbutton_set').click(function() {
		 $('#role_form').form('clear');  
	});
	// 按条件查询角色信息列表
	function querySysUserList() {
		$('#user_table').datagrid('load',
				$.serializeObject($('#search_form').form()));
	}
	// 重置查询列表框
	function resetSearchForm() {
		$('#stCode').val('');
	}
		
</script>
</html>
</fmt:bundle>
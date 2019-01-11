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
					<td class="content_title">&nbsp;文件名:</td>
					<td class="content_input"><input type="text" name="name" id="name" /></td>
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

		<table>
			<tr>
				<td style="padding: 2px 2px 2px 5px;"><a href="#" id="add_linkbutton" class="easyui-linkbutton"
					data-options="iconCls:'icon-add'"><fmt:message key="btn.create" /></a></td>
				<td class="content_linkbutton"><a href="#" id="delete_linkbutton" class="easyui-linkbutton"
					data-options="iconCls:'icon-cancel'"><fmt:message
							key="btn.delete" /></a></td>
				<td id="not_exist" style="display: none;"><span style="color: red;"><fmt:message key="search.no.record" />！</span></td>
			</tr>
		</table>
	</div>
	<!-- 用户列表 -->
	<table id="file_table" cellspacing="0" cellpadding="0" data-options="rownumbers:true"  ></table>
	<!-- 新增 -->
	<div id="upload_dialog_div" class="easyui-dialog" style="width: 500px;height: 600px;"
		data-options="modal:true, shadow:false, closed:true, buttons:'#add_dialog_button_div', title:'文件上传'">
		<jsp:include page="uploadFile.jsp"></jsp:include>
	</div>
	
</body>
<script type="text/javascript">
	$(function() {
		// 初始化系统列表
		$('#file_table').datagrid(
				{
					url : '${ctx}/file/fileManage?random='
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
						field : 'name',
						title : '文件名',
						width : 200,
						align : 'center'
					}, {
						field : 'savePath',
						title : '保存路径',
						width : 200,
						align : 'center'
					}, {
						field : 'createDate',
						title : '创建时间',
						width : 200,
						align : 'center',
						formatter: function(value, row){
							return $.fn.timestampFormat(value,'yyyy-MM-dd HH:mm:ss');
						}
					}, {
						field : '0',
						title : '操作',
						width : 200,
						align : 'center',
						formatter: function(value, row){
							return '<a href="${ctx}/file/download/'+row.id+'">下载</a>'
						}
					}, {
						field : 'id',
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
		_doResize($('#file_table'));
	});
	
	//控制页面大小不随展示行数变化
	$(window).resize(function(){
		 _doResize($('#file_table'));
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
		$('#file_table').datagrid('load',
				$.serializeObject($('#search_form').form()));
	}
	// 重置查询列表框
	function resetSearchForm() {
		$('#name').val('');
	}

	// 新增按钮点击事件
	$('#add_linkbutton').click(function() {
		openAddDialog();
	});

	// 打开新增弹出框
	function openAddDialog() {
		$('#upload_dialog_div').dialog('open');
		$("#upload_dialog_linkbutton_save").linkbutton('enable');
	}
 	
	// 删除按钮点击事件
	  $('#delete_linkbutton').click(function(){
	  var id = "";  // ID
  	  var rows = $('#file_table').datagrid('getChecked'); // 获取选中的行数据
  	  if(rows.length > 0){ 
  		  $.messager.confirm( Msg.sys_confirm2,Msg.sys_deleate,function(flag){
  			  if(flag){
  				  $.each(rows, function(index, row){
  	                  if(index+1 == rows.length){
  	                	id = id + row.id;
  	                  }else{
  	                	id = id + row.id + ",";
  	                  }
  	              });
  	              $.ajax({
  	                  url:'${ctx}/file/deleteFileModel?random=' + new Date().getTime(),
  	                  type: "POST",
  	                  data: {'ids':id},
  	                  async: false,
  	                  success: function (resultData){
  	                	  resultData = eval('(' + resultData + ')');
  	                	  if (resultData == 'SUCCESS') {
  	                		  $.messager.show({
  	                			  title: Msg.sys_remaind1,
  	                			  msg: Msg.sys_delete_txt1
  	                		  });
  	                		  querySysUserList(); // 重新查询用户列表
  	                		  $('#file_table').datagrid('uncheckAll');
  	                	  } else if(resultData == 'FAIL'){
  	                		 $.messager.show({
                                 title: Msg.sys_remaind1,
                                 msg:Msg.sys_delete_txt2
                             });
  	                	  }else {
  	                		if (resultData != null && resultData != undefined) {
	  	      					var index = resultData.indexOf("/logout");
	  	      					if (index != -1) {
		  	      					$.messager.show({
	 	                                 title: Msg.sys_remaind1,
	 	                                 msg:Msg.sys_no_permissions_txt1
	 	                             });
			  	      				setTimeout(function () { 
			      						top.location.href = resultData;
			      				    }, 3000);
	  	      					}
	  	      				}
  	                	 }
  	                  }
  	              });
  			  }
  		  });
  	  }else{
  		  $.messager.show({
  			  title:Msg.sys_remaind1,
  			  msg: Msg.frequency_10
  		  });
  	  }
	  });
	  
</script>
</html>
</fmt:bundle>
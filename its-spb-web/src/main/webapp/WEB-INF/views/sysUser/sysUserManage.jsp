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

		<table>
			<tr>
			<sc:security property="SYS_USER_ADD">
				<td style="padding: 2px 2px 2px 5px;"><a href="#" id="add_linkbutton" class="easyui-linkbutton"
					data-options="iconCls:'icon-add'"><fmt:message key="btn.create" /></a></td>
			</sc:security>
			<sc:security property="SYS_USER_UPDATE">
				<td class="content_linkbutton"><a href="#" id="update_linkbutton" class="easyui-linkbutton" data-options="iconCls:'icon-edit'"><fmt:message
							key="btn.modify" /></a></td>
			</sc:security>
			<sc:security property="SYS_USER_DELETE">
				<td class="content_linkbutton"><a href="#" id="delete_linkbutton" class="easyui-linkbutton"
					data-options="iconCls:'icon-cancel'"><fmt:message
							key="btn.delete" /></a></td>
			</sc:security>
			<sc:security property="SYS_USER_ADD">
				<td style="padding: 2px 2px 2px 5px;"><a href="#" id="import_linkbutton" class="easyui-linkbutton"
					data-options="iconCls:'icon-page_excel'"><fmt:message key="btn.import" /></a></td>
				<td style="padding: 2px 2px 2px 5px;"><a href="#" id="export_linkbutton" class="easyui-linkbutton"
					data-options="iconCls:'icon-page_excel'"><fmt:message key="btn.export" /></a></td>
			</sc:security>
			<sc:security property="SYS_USER_ROLE">
				<td class="content_linkbutton"><a id="setRole" href="javascript:setRole()" class="easyui-linkbutton" data-options="iconCls:'icon-search'"><fmt:message
							key="btn.user.set.role" /></a></td>
			</sc:security>
				<td id="not_exist" style="display: none;"><span style="color: red;"><fmt:message key="search.no.record" />！</span></td>
			</tr>
		</table>
	</div>
	<!-- 用户列表 -->
	<table id="user_table" cellspacing="0" cellpadding="0" data-options="rownumbers:true"  ></table>
	<!-- 新增 -->
	<div id="add_dialog_div" class="easyui-dialog" style="width: 500px;"
		data-options="modal:true, shadow:false, closed:true, buttons:'#add_dialog_button_div', title:'<fmt:message key="user.new.users" />'">
		<form id="add_dialog_form" method="post">
			<input name="stName" id="stName_add_h" type="hidden" value="">
			<div style="padding: 20px">
				<table cellpadding="0" cellspacing="0" class="form-table">
					<tr style="height: 30px;">
						<td style="width: auto; font-size: 14px;"><fmt:message key="user.employee.number" /></td>
						<td style="padding-left: 20px;"><input id="stCode" name="stCode" style="width: 200px; font-size: 14px;" class="easyui-validatebox"
							data-options="required:true,validType:'length[1,16]'"></td>
					</tr>

					<tr style="height: 30px;">
						<td style="width: auto; font-size: 14px;"><fmt:message key="user.employee.name" /></td>
						<td style="padding-left: 20px;"><input name="stName_add" id="stName_add" class="easyui-validatebox" style="width: 200px; font-size: 14px;"
							data-options="required:true,validType:'length[1,20]'"></td>
					</tr>

				</table>
			</div>
		</form>
		<div id="add_dialog_button_div" style="text-align: center;">
			<a href="#" id="add_dialog_linkbutton_save" class="easyui-linkbutton"
				data-options="iconCls:'icon-save'"><fmt:message key="btn.save" /></a> &nbsp;&nbsp;&nbsp;&nbsp; <a
				href="#" id="add_dialog_linkbutton_cancel" class="easyui-linkbutton"
				data-options="iconCls:'icon-cancel'"><fmt:message key="btn.close" /></a>
		</div>
	</div>
	<!-- 修改 -->
	<div id="update_dialog_div" class="easyui-dialog" style="width: 500px;"
		data-options="modal:true, shadow:false, closed:true, buttons:'#update_dialog_button_div', title:'<fmt:message key="user.modify.users" />'">
		<form id="update_dialog_form" method="post">
		<input id="stId" name="stId" value="" type="hidden">
		<input id="stName_update_h" name="stName" value="" type="hidden">
			<div style="padding: 20px">
				<table cellpadding="0" cellspacing="0" class="form-table">
					<tr style="height: 30px;">
						<td style="width: auto; font-size: 14px;"><fmt:message key="user.employee.number" /></td>
						<td style="padding-left: 20px;"><input id="stCode_update" name="stCode" style="width: 200px; font-size: 14px;" class="easyui-validatebox"
							data-options="required:true,validType:'length[1,16]'"></td>
					</tr>

					<tr style="height: 30px;">
						<td style="width: auto; font-size: 14px;"><fmt:message key="user.employee.name" /></td>
						<td style="padding-left: 20px;"><input name="stName_update" id="stName_update" class="easyui-validatebox" style="width: 200px; font-size: 14px;"
							data-options="required:true,validType:'length[1,20]'"></td>
					</tr>

				</table>
			</div>
		</form>
		<div id="update_dialog_button_div" style="text-align: center;">
			<a href="#" id="update_dialog_linkbutton_save" class="easyui-linkbutton" data-options="iconCls:'icon-save'"><fmt:message key="btn.save" /></a>
			&nbsp;&nbsp;&nbsp;&nbsp; 
			<a href="#" id="update_dialog_linkbutton_cancel" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"><fmt:message key="btn.close" /></a>
		</div>
	</div>
	
	<!--导入-->
	<div id="import_dialog_div" class="easyui-dialog" style="width:900px;height:600px" data-options="modal:true, shadow:false, closed:true, 
		buttons:'#upload_dialog_button_div', title:'<fmt:message key="btn.import" />'">
	      <div>
		        <form id="upload_dialog_form" method="post" accept-charset="utf-8" enctype="multipart/form-data">
		        	<table>
		        		<tr>
							<td>解析方式:</td>
							<td>
								<label><input name="imptType" type="radio" value="SAX"  checked="checked"/>POI(SAX)</label> 
								<label><input name="imptType" type="radio" value="POI" />POI</label> 
							</td>
		        		</tr>
		        		<tr>
							<td>保存方式:</td>
							<td>
								<label><input name="saveType" type="radio" value="Mysql"  checked="checked"/>Mysql</label> 
								<label><input name="saveType" type="radio" value="MongDB" />MongDB</label> 
							</td>
		        		</tr>
					    <tr>
					       <td><fmt:message key="user.select.file" />：</td>
					       <td><input type="file" name="imptFile" id="uploadFile" style="height:24px;width:300px; border:1px solid #a5c3e0;border-top:1px solid #89accd;border-right:1px solid #89accd;padding:0px;"></td>
					       <td>
					          <a class="easyui-linkbutton" href="javascript:void(0)" id="import_dialog_linkbutton_save" data-options="iconCls:'icon-save'" ><fmt:message key="btn.upload" /></a>
					       </td>
					       <td style="padding-left: 10px;">
					          <a class="easyui-linkbutton" href="${ctx}/file/downloadPath?path=/WEB-INF/file/template&fileName=user_template.xlsx" data-options="iconCls:'icon-page_white_excel'"><fmt:message key="user.export.templet" /></a>
					       </td>
					       <td style="padding-left: 10px;">
					          <span id="msg_uploading" style="color: red;"></span>
					       </td>
					   </tr>
					</table>
		        </form>
		        <div id="resultTable" title="<fmt:message key="user.import.failure.record" />" style="padding:0px; margin: 0px;"></div>
		</div>
	</div>
	<!-- 导入 end -->
	
	<!-- 设置用户角色    start -->
    <div id="tbRole" style="padding: 5px; display: none;">
		<div>
		 <form id="role_form" >
			<table>
				<tr>
					<td><fmt:message key="role.name" />:</td>
					<td><input id="roleName" name="roleName" type="text" style="width:110px" /></td>
					<td><fmt:message key="sys.name.code" />:</td>
					<td>
					<input name="sysNameCode" id="sysNameCode_search" class="easyui-combobox" style="width:110px" data-options="panelHeight:'auto',valueField:'sysNameCode',textField:'name',url:'${apibase}/sysRole/getSysNameList'"/>
					</td>
					<td><a href="javascript:searchRole()" id="btnsearchPrice"
						class="easyui-linkbutton" data-options="iconCls:'icon-search'"><fmt:message key="btn.search" /></a>
					</td>
					<td><a href="#"
						class="easyui-linkbutton" data-options="iconCls:'icon-reload'"
						id="reset_linkbutton_set"> <fmt:message key="btn.reset" /></a></td>
				</tr>
			</table>
			</form>
		</div>
	</div>

	<div id="setRolePage" class="easyui-dialog" style="width: 550px;height: 500px;"
		closed="true" modal="true" buttons="#dlg-buttonsRole" shadow="false" title="<fmt:message key='btn.user.set.role' />">
		<div id="roleDg"></div>
		<div id="dlg-buttonsRole" style="text-align: center">
			<a href="#" id="btnSaveRole" class="easyui-linkbutton" iconCls="icon-save" onclick="saveRole()"><fmt:message key="btn.save" /></a>
			 <a href="#" style="text-align: center" class="easyui-linkbutton" iconCls="icon-cancel" onclick="closeMenu()"><fmt:message key="btn.close" /></a>
		</div>
	</div>
	<!-- 设置用户角色 end -->
</body>
<script type="text/javascript">
	
	var cols;
	var dragCols = [ {
		field : 'rowCheckBox',
		hidden : false,
		title : '${requestScope.rb.query_customerMsg_txt_14}',
		width : 10,
		checkbox : true,
		align : 'center'
	}, {
		field : 'stCode',
		hidden : false,
		title : Msg.sys_user_code,
		width : 200,
		align : 'center',
		formatter : 'formatter-stCode'
	}, {
		field : 'stName',
		hidden : false,
		title : Msg.sys_user_name,
		width : 200,
		align : 'center'
	}, {
		field : 'createTm',
		hidden : false,
		title : Msg.sys_user_create_tm,
		width : 200,
		align : 'center'
	}, {
		field : 'stId',
		hidden : true,
		width : 350,
		align : 'center'
	} ];
	$(function() {
		delCookie("cols");
		cols = getCookie("cols");
		if (cols == null) {
			//将数组转为JSON，解决Cookie保存数组问题
			setCookie("cols", JSON.stringify(dragCols));
			cols = dragCols;
		}else{
			cols = JSON.parse(cols);
		}
		for(var i = 0;i<cols.length;i++){
			if(cols[i].formatter=='formatter-stCode'){
				cols[i].formatter = function(value, row) {return "<input name='stCode_update' id='"+row.stId+"' value='"+value+"' onchange='updateStName(this)' style='width: 190px;height:20px; font-size: 12px;'>";}
				
			}
			if(cols[i].formatter=='formatter-createTm'){
				cols[i].formatter = function(value, row) {return $.fn.timestampFormat(value, 'yyyy-MM-dd HH:mm:ss');}
			}
		}
		init();
		drag();//绑定datagrid，绑定拖拽
	});
	

	function updateStName(obj) {
		$.ajax({url : '${apibase}/sysUser/updateSysUser?random='+ new Date().getTime(),
			type : "POST",
			data : {
				'stId' : obj.id,
				'stCode' : obj.value
			},
			async : false,
			success : function(resultData) {
				resultData = eval('(' + resultData + ')');
				if (resultData == 'SUCCESS') {
					$.messager.show({
						title : Msg.sys_remaind1,
						msg : Msg.sys_save_txt
					});
					$('#update_dialog_div').dialog('close'); // 关闭新增窗口
					querySysUserList(); // 重新查询用户列表
				} else if (resultData == 'IS_REPEAT') {
					$.messager.show({
						title : Msg.sys_remaind1,
						msg : Msg.sys_userMgr_04
					});
				} else if (resultData == 'FAIL') {
					$.messager.show({
						title : Msg.sys_remaind1,
						msg : Msg.sys_save_txt3
					});
				} else {
					if (resultData != null && resultData != undefined) {
						var index = resultData.indexOf("/logout");
						if (index != -1) {
							$.messager.show({
								title : Msg.sys_remaind1,
								msg : Msg.sys_no_permissions_txt1
							});
							setTimeout(function() {
								top.location.href = resultData;
							}, 3000);
						}
					}
				}
			}
		});
		
	}
	//设置cookies
	function setCookie(name, value) {
		var Days = 30;
		var exp = new Date();
		exp.setTime(exp.getTime() + Days*24*60*60*1000);
		document.cookie = name + "=" + escape(value) + ";expires="
				+ exp.toGMTString();
	}
	//获取cookies
	function getCookie(name) {
		var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
		if (arr = document.cookie.match(reg))
			return unescape(arr[2]);
		else
			return null;
	}
	//删除cookies
	function delCookie(name) {
		var exp = new Date();
		exp.setTime(exp.getTime() - 1);
		var cval = getCookie(name);
		if (cval != null)
			document.cookie = name + "=" + cval + ";expires="
					+ exp.toGMTString();
	}

	function init() {
// 		debugger;
		// 初始化系统列表
		$('#user_table').datagrid(
				{
					url : '${apibase}/sysUser/sysUserManage?random='
							+ new Date().getTime(),
					pageList : [ 50, 100, 200,10000,50000,100000,1000000 ],
					pageSize : 50,
					nowrap : true,
					rownumbers : true,
					pagination : true,
					remoteSort : true,
					checkOnSelect : false,
					selectOnCheck : false,
					singleSelect : true,
					toolbar : '#tb',
					columns : [ cols ],
					onLoadSuccess : function(data) {
						drag();
						if (data.total == '0') { // 查询无记录时提醒
							$("#not_exist").show();
						} else {
							$("#not_exist").hide();
						}
					}
				});
		_doResize($('#user_table'));
	}

	//拖动drag和drop都是datagrid的头的datagrid-cell
	function drag() {
		$('.datagrid-header-inner .datagrid-cell').draggable({
			revert : true,
			proxy : 'clone'
		}).droppable({
			accept : '.datagrid-header-inner .datagrid-cell',
			onDrop : function(e, source) {
// 				debugger;
				//取得拖动源的html值
				var src = $(e.currentTarget.innerHTML).html();
				//取得拖动目标的html值
				var sou = $(source.innerHTML).html();
				var tempcolsrc;//拖动后源和目标列交换
				var tempcolsou;
				var tempcols = [];
				var tempcolsFMT = [];
				for (var i = 0; i < cols.length; i++) {
					if (cols[i].title == sou) {
						tempcolsrc = cols[i];//循环读一遍列把源和目标列都记下来
					} else if (cols[i].title == src) {
						tempcolsou = cols[i];
					}
				}
				for (var i = 0; i < cols.length; i++) {
					//再循环一遍，把源和目标的列对换
					var col = {
						field : cols[i].field,
						hidden : cols[i].hidden,
						title : cols[i].title,
						align : cols[i].align,
						checkbox : cols[i].checkbox,
						width : cols[i].width,
						formatter : cols[i].formatter
					};
					var colFMT; 
					if(cols[i].formatter!=null){
						colFMT = {
							field : cols[i].field,
							hidden : cols[i].hidden,
							title : cols[i].title,
							align : cols[i].align,
							checkbox : cols[i].checkbox,
							width : cols[i].width,
							formatter : 'formatter-'+cols[i].field
						};
					}else{
						colFMT = {
								field : cols[i].field,
								hidden : cols[i].hidden,
								title : cols[i].title,
								align : cols[i].align,
								checkbox : cols[i].checkbox,
								width : cols[i].width,
								formatter : cols[i].formatter
							};
					}
					if (cols[i].title == sou) {
						col = tempcolsou;
					} else if (cols[i].title == src) {
						col = tempcolsrc;
					}
					tempcols.push(col);
					tempcolsFMT.push(colFMT);
				}
				cols = tempcols;
				//延时执行重绑定datagrid操作。revert需要时间,避免没有做延时就直接重绑会出错
				timeid = setTimeout("init()", 100);
				setCookie("cols", JSON.stringify(tempcolsFMT));
			}
		});
	}

	//控制页面大小不随展示行数变化
	$(window).resize(function() {
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
		$('#user_table').datagrid('load', $.serializeObject($('#search_form').form()));
	}
	// 重置查询列表框
	function resetSearchForm() {
		$('#stCode').val('');
	}

	// 新增按钮点击事件
	$('#add_linkbutton').click(function() {
		openAddDialog();
	});

	// 打开新增弹出框
	function openAddDialog() {
		$('#add_dialog_div').dialog('open');
		$('#add_dialog_form').form('clear');
	}

	// 新增用户窗口保存按钮点击事件
	$('#add_dialog_linkbutton_save').click(function() {
		saveSysUser();
	});

	// 新增用户窗口关闭按钮点击事件
	$('#add_dialog_linkbutton_cancel').click(function() {
		$('#add_dialog_div').dialog('close');
	});

	// 提交保存录入的新增用户信息
	function saveSysUser() {
		$('#add_dialog_form').form('submit', {
			url : '${apibase}/sysUser/addSysUser?random=' + new Date().getTime(),
			contentType: 'application/x-www-form-urlencoded;charset=UTF-8', 
			type: 'post', 
			dataType: 'json', 
			onSubmit : function() {
				$("#stName_add_h").val($("#stName_add").val());
				return $(this).form('validate');
			},
			success : function(resultData) {
				alert("easyUI form提交,因跨域请求后，无法获取返回的response,手动刷新列表");
				if (resultData == 'SUCCESS') {
					$.messager.show({
						title : Msg.sys_remaind1,
						msg : Msg.sys_save_txt
					});
					$('#add_dialog_div').dialog('close'); // 关闭新增窗口
					querySysUserList(); // 重新查询用户列表
				} else if (resultData == 'IS_REPEAT') {
					$.messager.show({
						title : Msg.sys_remaind1,
						msg : Msg.sys_userMgr_04
					});
				} else if(resultData == 'FAIL'){
             		 $.messager.show({
                         title: Msg.sys_remaind1,
                         msg:Msg.sys_save_txt3
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

	// 修改按钮点击事件
	$('#update_linkbutton').click(function() {
		var rows = $('#user_table').datagrid('getChecked'); // 获取选中的行数据
		if (rows.length == 1) {
			var stId = rows[0].stId;
			openUpdateDialog(stId);
		} else if (rows.length > 1) {
			$.messager.show({
				title : Msg.sys_remaind1,
				msg : Msg.sys_only_select_one
			});
		} else {
			$.messager.show({
				title : Msg.sys_remaind1,
				msg : Msg.frequency_11
			});
		}
	});

	// 打开修改用户弹出框
	function openUpdateDialog(stId) {
		$.ajax({
			url : '${apibase}/sysUser/getSysUserById?random='+ new Date().getTime(),
			type : "post",
			data : {
				"stId" : stId
			},
			async : true,
			dataType : "json",
			type:"POST",
			success : function(data) {
				var uform = $('#update_dialog_form');
				$('#update_dialog_div').dialog('open');
				$('#update_dialog_form').form('clear');
				$('#update_dialog_form').form('load', {
					stId : data.stId,
					stCode : data.stCode,
					stName_update : data.stName
				});
			}
		});
	}

	// 修改用户窗口保存按钮点击事件
	$('#update_dialog_linkbutton_save').click(function() {
		updateSysUser();
	});

	// 修改用户窗口关闭按钮点击事件
	$('#update_dialog_linkbutton_cancel').click(function() {
		$('#update_dialog_div').dialog('close');
	});
	
	// 打开导入弹出框
	$('#import_linkbutton').click(function() {
		if($(this).linkbutton("options").disabled){	//已禁用按钮
			return;
		}
		$('#import_dialog_div').dialog('open');
		$('#upload_dialog_form').form('clear');
		$('input[name="imptType"][value="SAX"]').prop("checked",true);
		$('input[name="saveType"][value="Mysql"]').prop("checked",true);
		$("#import_dialog_linkbutton_save").linkbutton('enable');
		$('#resultTable').datagrid('loadData',{'total':0,'rows':[]}); 
// 		var row = $("#agentservice_table").datagrid("getSelected");
// 		if (row) {
// 			var agentServiceId = row.agentServiceId;
// 			$("#import_area_agentServiceId").val(agentServiceId);
// 		}
	});
	
	$('#import_dialog_linkbutton_save').click(function() {
		if($(this).linkbutton("options").disabled){	//已禁用按钮
			return;
		}
		uploadSaveFile();
	});
	// 从本地加载数据的分页方法
	initResultTable();

	// 提交保存录入的修改用户信息
	function updateSysUser() {
		debugger
		$('#update_dialog_form').form('submit',{
		url : '${apibase}/sysUser/updateSysUser?random='+ new Date().getTime(),
		onSubmit : function() {
// 			$("#stName_update_h").val(encodeURI($("#stName_update").val()));
			$("#stName_update_h").val($("#stName_update").val());
						return $(this).form('validate');
		},
		success : function(resultData) {
			if (resultData == 'SUCCESS') {
				$.messager.show({title : Msg.sys_remaind1,msg : Msg.sys_save_txt});
				$('#update_dialog_div').dialog('close'); // 关闭新增窗口
				querySysUserList(); // 重新查询用户列表
			} else if (resultData == 'IS_REPEAT') {
				$.messager.show({title : Msg.sys_remaind1,msg : Msg.sys_userMgr_04});
			} else if (resultData == 'FAIL') {
				$.messager.show({title : Msg.sys_remaind1,msg : Msg.sys_save_txt3});
			} else {
				if (resultData != null && resultData != undefined) {
						var index = resultData.indexOf("/logout");
						if (index != -1) {
							$.messager.show({title : Msg.sys_remaind1,msg : Msg.sys_no_permissions_txt1});
							setTimeout(function() {
								top.location.href = resultData;
							}, 3000);
						}
				}
			}
		}
		});
	}

	// 删除按钮点击事件
	$('#delete_linkbutton').click(
		function() {
		var stId = ""; // 用户ID
		var rows = $('#user_table').datagrid('getChecked'); // 获取选中的行数据
		if (rows.length > 0) {
			$.messager.confirm(Msg.sys_confirm2,Msg.sys_deleate,
			function(flag) {
				if (flag) {$.each(rows,function(index,row) {
						if (index + 1 == rows.length) {
							stId = stId+ row.stId;
						} else {
							stId = stId+ row.stId+ ",";
						}
				});
				$.ajax({url : '${apibase}/sysUser/deleteSysUser?random='+ new Date().getTime(),
						type : "POST",
						data : {'stId' : stId},
						async : false,
						success : function(resultData) {
							if (resultData == 'SUCCESS') {
								$.messager.show({title : Msg.sys_remaind1,msg : Msg.sys_delete_txt1});
								querySysUserList(); // 重新查询用户列表
								$('#user_table').datagrid('uncheckAll');
							} else if (resultData == 'FAIL') {
								$.messager.show({title : Msg.sys_remaind1,msg : Msg.sys_delete_txt2});
							} else {
								if (resultData != null&& resultData != undefined) {
									var index = resultData.indexOf("/logout");
									if (index != -1) {
										$.messager.show({title : Msg.sys_remaind1,msg : Msg.sys_no_permissions_txt1});
											setTimeout(function() {top.location.href = resultData;},3000);
									}
								}
							}
						}
				});
				}
			});
		} else {
		$.messager.show({title : Msg.sys_remaind1,msg : Msg.frequency_10});}
	});
	
	// Excel导出
	$('#export_linkbutton').click(function() {
		window.location.href = "${apibase}/excel/export";
	});

	//--------------关联菜单---------------
	var userRoles;
	var addMenus = new Array();
	var removeMenus = new Array();
	//----------菜单设置按钮点击事件----------
	function setRole() {
		userRoles = new Array();
		addMenus = new Array();
		removeMenus = new Array();
		var items = $("#user_table").datagrid('getChecked');
		if (items.length > 1) {
			$.messager.show({
				title : Msg.sys_remaind1,
				msg : Msg.sys_only_select_one
			});
			return;
		} else if (items.length == 0) {
			$.messager.show({
				title : Msg.sys_remaind1,
				msg : Msg.frequency_11
			});
			return;
		}

		$('#role_form').form('clear');
		//加载菜单信息
		role_form = $('#role_form').form();
		roleDg = $('#roleDg').datagrid({
			url : '${apibase}/sysRole/sysRoleManage?random='+ new Date().getTime(),
			queryParams : {
				"roleName" : $('#roleName').val(),
				"sysNameCode" : $('#sysNameCode_search').val()
			},
			toolbar : '#tbRole',
			height : 400,
			nowrap : false,
			idField : 'roleId',
			pageList : [ 50, 100, 200 ],
			pageSize : 50,
			singleSelect : false, //是否单选 
			pagination : true,//分页控件 
			rownumbers : true, //行号
			columns : [ [ {
				field : "ck",
				title : '选中',
				checkbox : true
			}, {
				field : "roleId",
				hidden : true
			}, {
				field : "roleName",
				title : Msg.sys_role_name
			}, {
				field : "sysName",
				title : Msg.sys_name_code
			} ] ],
			onLoadSuccess : function() {
				//$("#roleDg").datagrid("clearChecked");
				$.post('${apibase}/sysUser/getSysUserRoleList?random='+ new Date().getTime(),{stId : items[0].stId},
				function(data) {
					if (data != null&& data.length > 0) {
						userRoles = data;
						for (var i = 0; i < userRoles.length; i++) {
							var rows = $('#roleDg').datagrid("getRows");
							for (var j = 0; j < rows.length; j++) {
								if (rows[j].roleId == userRoles[i].roleId) {
									var rowIndex = $('#roleDg').datagrid("getRowIndex",rows[j].roleId);
									$('#roleDg').datagrid("checkRow",rowIndex).datagrid("refreshRow",rowIndex);//刷新行
								}
							}
						}
					}
				}, "json");
			}
		});

		$('#setRolePage').dialog('open');
		// 			$('#setRolePage').dialog({
		// 				title:Msg.set_associated_roles,
		// 				height:500,
		// 				width:550,
		// 				 onOpen:function(){   
		// 			          //dialog原始left  
		// 			          default_left=$('#setRolePage').panel('options').left;   
		// 			          //dialog原始top  
		// 			          default_top=$('#setRolePage').panel('options').top;  
		// 			        },  
		// 			        onMove:function(left,top){  //鼠标拖动时事件  
		// 			           var body_width=document.body.offsetWidth;//body的宽度  
		// 			           var body_height=document.body.offsetHeight;//body的高度  
		// 			           var dd_width= $('#setRolePage').panel('options').width;//dialog的宽度  
		// 			           var dd_height= $('#setRolePage').panel('options').height;//dialog的高度                     
		// 			           if(left<1||left>(body_width-dd_width)||top<1||top>(body_height-dd_height)){  
		// 			              $('#setRolePage').dialog('move',{      
		// 			                    //left:default_left,      
		// 			                    //top:default_top      
		// 			              });  
		// 			          }  
		// 			        }

		// 			}).dialog('open');
	}

	function searchRole() {
		roleDg.datagrid('reload', $.serializeObject(role_form));
	}

	function closeMenu() {
		$("#roleDg").datagrid("clearChecked");
		$('#setRolePage').dialog('close')
	}

	//----------保存角色设置按钮点击事件----------
	function saveRole() {
		var stIds = "";
		var roleIds = "";
		var users = $("#user_table").datagrid('getChecked');
		var addRoles = $("#roleDg").datagrid("getChecked");
		stIds = users[0].stId;
		if (addRoles.length > 0) {
			roleIds = addRoles[0].roleId;
			if (addRoles.length > 1) {
				for (var i = 1; i < addRoles.length; i++) {
					roleIds += "," + addRoles[i].roleId;
				}
			}
		}
		var data = {
			stId : stIds,
			roleId : roleIds
		};
		$.post("${apibase}/sysUser/saveSysUserRole", data, function(resultData) {
			if (resultData == 'SUCCESS') {
				$.messager.show({
					title : Msg.sys_remaind1,
					msg : Msg.sys_success
				});
				$("#setRolePage").dialog("close");
				$("#roleDg").datagrid("clearChecked");
				$('#user_table').datagrid("clearChecked").datagrid("reload");
			} else if (resultData == 'FAIL') {
				$.messager.show({
					title : Msg.sys_remaind1,
					msg : Msg.sys_save_txt3
				});
			} else {
				if (resultData != null && resultData != undefined) {
					var index = resultData.indexOf("/logout");
					if (index != -1) {
						$.messager.show({
							title : Msg.sys_remaind1,
							msg : Msg.sys_no_permissions_txt1
						});
						setTimeout(function() {
							top.location.href = resultData;
						}, 3000);
					}
				}
			}
		});
	}
	
	//导入上传保存
	function uploadSaveFile() {
		var filepath = $("#uploadFile").val();
		if (!filepath) {
			$.messager.alert(Msg.sys_remaind1, Msg.msg_upload_selectFileFirst);
			return;
		}
		var fileType = filepath.substring(filepath.lastIndexOf(".")).toUpperCase();
		if ('.XLSX' != fileType && '.XLS' != fileType) {
			$.messager.alert(Msg.sys_remaind1, Msg.msg_upload_fileFormatWrong);
			return;
		}
		$("#import_dialog_linkbutton_save").linkbutton('disable');
		$("#msg_uploading").html(Msg.msg_uploading);
		//清空datagrid
// 		$('#resultTable').datagrid('loadData',{'total':0,'rows':[]}); 
		$("#upload_dialog_form").form('submit',{
			url : '${apibase}/excel/import?random='+ new Date().getTime(),
			method : "post",
			success : function(result) {
			$("#msg_uploading").html("");
			$("#import_dialog_linkbutton_save").linkbutton('enable');
			$("#upload_dialog_form")[0].reset();
			if (checkResultJson.success) {
				$.messager.show({title : Msg.sys_remaind1,msg : Msg.upload_success + "[" + checkResultJson.count+"]"+ Msg.records});
				$('#import_dialog_div').dialog('close');
				querySysUserList();
			} else {
				if (checkResultJson.singleMsg) {
					$.messager.show({title : Msg.sys_remaind1,msg : checkResultJson.singleMsg});
				} else {
						var ajaxData = getAjaxData(1, 10);
						$('#resultTable').datagrid('loadData', ajaxData);
						}
					}
				}
			});
	}
	
	function initResultTable() {
		$('#resultTable').datagrid({
			pagination : true,
			width : 880,
			height : 400,
			pageNumber : 1,
			pageSize : 10,
			pageList : [ 50, 100, 200 ],
			fitColumns : true,
			columns : [ [
					{
						field : 'rowNum',
						width : 25,
						align : 'center',
						halign : 'center',
						title : Msg.title_upload_lineNo
					},
					{
						field : 'errorInfo',
						width : 260,
						align : 'left',
						halign : 'center',
						title : Msg.title_upload_failReason
					} ] ],
			onLoadSuccess : function() {
				 $(this).datagrid("fixRownumber");
// 				_resize($('#resultTable'));
			}
		});

		$('#resultTable').datagrid('getPager').pagination({
			onSelectPage : function(pageIndex, pageSize) {
				var gridOpts = $('#resultTable').datagrid('options');
				gridOpts.pageNumber = pageIndex;
				gridOpts.pageSize = pageSize;
				var ajaxData = getAjaxData(pageIndex, pageSize);
				$('#resultTable').datagrid('loadData', ajaxData);
			}
		});
	}
//获取分页数据
function getAjaxData(pageIndex, pageSize) {
	var ajaxData = new Object();
	ajaxData.total = checkResultJson.errors.length;
	var rowArray = new Array();
	var firstIndex = (pageIndex - 1) * pageSize;
	var lastIndex = firstIndex + pageSize;
	lastIndex = lastIndex > ajaxData.total ? ajaxData.total : lastIndex;
	for (var i = firstIndex, j = 0; i < lastIndex; i++, j++) {
		rowArray[j] = checkResultJson.errors[i];
	}
	ajaxData.rows = rowArray;
	return ajaxData;
}
</script>
</html>
</fmt:bundle>
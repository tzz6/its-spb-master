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
					<input name="sysNameCode" id="sysNameCode_search" class="easyui-combobox" style="width:110px" data-options="panelHeight:'auto',valueField:'sysNameCode',textField:'name',url:'${ctx}/sysRole/getSysNameList'"/>
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

	<div id="setRolePage" class="easyui-dialog" style="width: 500px;"
		closed="true" modal="true" buttons="#dlg-buttonsRole" shadow="false">
		<div id="roleDg"></div>
		<div id="dlg-buttonsRole" style="text-align: center">
			<a href="#" id="btnSaveRole" class="easyui-linkbutton" iconCls="icon-save" onclick="saveRole()"><fmt:message key="btn.save" /></a>
			 <a href="#" style="text-align: center" class="easyui-linkbutton" iconCls="icon-cancel" onclick="closeMenu()"><fmt:message key="btn.close" /></a>
		</div>
	</div>
	<!-- 设置用户角色 end -->
</body>
<script type="text/javascript">
	$(function() {
		// 初始化系统列表
		$('#user_table').datagrid(
				{
					url : '${ctx}/sysUser/sysUserManage?random='
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
			url : '${ctx}/sysUser/addSysUser?random=' + new Date().getTime(),
			contentType: 'application/x-www-form-urlencoded;charset=UTF-8', 
			type: 'post', 
			dataType: 'json', 
			onSubmit : function() {
				$("#stName_add_h").val($("#stName_add").val());
				return $(this).form('validate');
			},
			success : function(resultData) {
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
			url : '${ctx}/sysUser/getSysUserById?random=' + new Date().getTime(),
			type : "post",
			data : {
				"stId" : stId
			},
			async : true,
			dataType : "json",
			success : function(data) {
				var uform = $('#update_dialog_form');
				$('#update_dialog_div').dialog('open');
				$('#update_dialog_form').form('clear');
				$('#update_dialog_form').form('load', {stId:data.stId,stCode:data.stCode, stName_update:data.stName});
			}
		});
	}
	
	  // 修改用户窗口保存按钮点击事件
    $('#update_dialog_linkbutton_save').click(function (){
        updateSysUser();
    });
    
    // 修改用户窗口关闭按钮点击事件
    $('#update_dialog_linkbutton_cancel').click(function (){
        $('#update_dialog_div').dialog('close');
    });
    
 	// 提交保存录入的修改用户信息
	function updateSysUser() {
		$('#update_dialog_form').form('submit', {
			url : '${ctx}/sysUser/updateSysUser?random=' + new Date().getTime(),
			onSubmit : function() {
				$("#stName_update_h").val($("#stName_update").val());
				return $(this).form('validate');
			},
			success : function(resultData) {
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
				} else if(resultData == 'FAIL'){
					$.messager.show({
						title : Msg.sys_remaind1,
						msg : Msg.sys_save_txt3
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
 	
	// 删除按钮点击事件
	  $('#delete_linkbutton').click(function(){
		  var stId = "";  // 用户ID
  	  var rows = $('#user_table').datagrid('getChecked'); // 获取选中的行数据
  	  if(rows.length > 0){ 
  		  $.messager.confirm( Msg.sys_confirm2,Msg.sys_deleate,function(flag){
  			  if(flag){
  				  $.each(rows, function(index, row){
  	                  if(index+1 == rows.length){
  	                	stId = stId + row.stId;
  	                  }else{
  	                	stId = stId + row.stId + ",";
  	                  }
  	              });
  	              $.ajax({
  	                  url:'${ctx}/sysUser/deleteSysUser?random=' + new Date().getTime(),
  	                  type: "POST",
  	                  data: {'stId':stId},
  	                  async: false,
  	                  success: function (resultData){
  	                	  if (resultData == 'SUCCESS') {
  	                		  $.messager.show({
  	                			  title: Msg.sys_remaind1,
  	                			  msg: Msg.sys_delete_txt1
  	                		  });
  	                		  querySysUserList(); // 重新查询用户列表
  	                		  $('#user_table').datagrid('uncheckAll');
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
	  
	   //--------------关联菜单---------------
	    var userRoles;
		var addMenus=new Array();
		var removeMenus=new Array();
		//----------菜单设置按钮点击事件----------
		function setRole(){
			userRoles=new Array();
			addMenus=new Array();
			removeMenus=new Array();
			var items = $("#user_table").datagrid('getChecked');
			if(items.length>1){ 
				$.messager.show({
					title:Msg.sys_remaind1,
					msg:Msg.sys_only_select_one
				});
				return;
			} else if(items.length==0){
				$.messager.show({
					title:Msg.sys_remaind1,
					msg:Msg.frequency_11
				});
				return;
			}		

			$('#role_form').form('clear');
			//加载菜单信息
			role_form=$('#role_form').form();	
			roleDg=$('#roleDg').datagrid({
				url :'${ctx}/sysRole/sysRoleManage?random='+new Date().getTime(),  		
				queryParams: {"roleName":$('#roleName').val(),"sysNameCode":$('#sysNameCode_search').val()},
				    toolbar : '#tbRole',
				    nowrap : false,
				    idField : 'roleId',
				    pageList : [ 50, 100, 200 ],
					pageSize : 50,
				    singleSelect : false, //是否单选 
					pagination : true,//分页控件 
					rownumbers : true, //行号
				    columns:[[
				        {field:"ck",title:'选中',checkbox:true},
				        {field:"roleId",hidden:true},
				        {field:"roleName",title:Msg.sys_role_name},
				        {field:"sysName",title:Msg.sys_name_code}
				    ]],
				    onLoadSuccess:function(){
				    	//$("#roleDg").datagrid("clearChecked");
				    	$.post('${ctx}/sysUser/getSysUserRoleList?random='+new Date().getTime(),{stId:items[0].stId},function(data){
			    			if(data != null && data.length>0){
			    				userRoles=data;
				    			for(var i=0;i<userRoles.length;i++){
				    				var rows = $('#roleDg').datagrid("getRows");
				    				for(var j=0;j<rows.length;j++){
				    					if(rows[j].roleId==userRoles[i].roleId){
				    						var rowIndex = $('#roleDg').datagrid("getRowIndex",rows[j].roleId);
				    						$('#roleDg').datagrid("checkRow",rowIndex)//选中行
				    									.datagrid("refreshRow",rowIndex);//刷新行
				    						
				    					}
				    				}
				    			}
			    			}
			    		},"json");
				    }
			}); 
			
			$('#setRolePage').dialog({
				title:Msg.set_associated_roles,
				height:500,
				width:550,
				 onOpen:function(){   
			          //dialog原始left  
			          default_left=$('#setRolePage').panel('options').left;   
			          //dialog原始top  
			          default_top=$('#setRolePage').panel('options').top;  
			        },  
			        onMove:function(left,top){  //鼠标拖动时事件  
			           var body_width=document.body.offsetWidth;//body的宽度  
			           var body_height=document.body.offsetHeight;//body的高度  
			           var dd_width= $('#setRolePage').panel('options').width;//dialog的宽度  
			           var dd_height= $('#setRolePage').panel('options').height;//dialog的高度                     
			           if(left<1||left>(body_width-dd_width)||top<1||top>(body_height-dd_height)){  
			              $('#setRolePage').dialog('move',{      
			                    //left:default_left,      
			                    //top:default_top      
			              });  
			          }  
			        }
				
			}).dialog('open');
		}
		
		
		function searchRole(){		
			roleDg.datagrid('reload', $.serializeObject(role_form));
	    }
		
		 function closeMenu(){
		    	$("#roleDg").datagrid("clearChecked");
		    	$('#setRolePage').dialog('close')
		    }
		    
		//----------保存角色设置按钮点击事件----------
		function saveRole(){
			var stIds="";
			var roleIds="";
			var users = $("#user_table").datagrid('getChecked');
			var addRoles = $("#roleDg").datagrid("getChecked");
			stIds = users[0].stId;
			if(addRoles.length>0){
				roleIds = addRoles[0].roleId;
				if(addRoles.length >1){
					for(var i=1;i<addRoles.length;i++){
						roleIds+=","+addRoles[i].roleId;
					}
				}
			}
			var data = {stId:stIds,roleId:roleIds};
			$.post("${ctx}/sysUser/saveSysUserRole", data, function(resultData) {
				if (resultData == 'SUCCESS') {
					$.messager.show({
						title : Msg.sys_remaind1,
						msg : Msg.sys_success
					});
					$("#setRolePage").dialog("close");
					$("#roleDg").datagrid("clearChecked");
					$('#user_table').datagrid("clearChecked").datagrid("reload");
				}  else if(resultData == 'FAIL'){
					$.messager.show({
						title : Msg.sys_remaind1,
						msg : Msg.sys_save_txt3
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
		});
	}
</script>
</html>
</fmt:bundle>
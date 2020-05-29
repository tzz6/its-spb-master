<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/commons/common.jsp"%>
<%@ include file="/commons/language.jsp"%>
<fmt:bundle basename="com.its.resource.lang">
<%@ taglib uri="http://www.its.web.com" prefix="sc"%>
<!DOCTYPE html>
<html>
<head>
</head>
<body class="easyui-layout">
	<div id="tb" style="padding:5px;">
		<form id="search_form">
			<table style="cellspacing: 0">
				<tr>
					<td class="content_title">&nbsp;<fmt:message key="role.name" />:</td>
					<td class="content_input"><input type="text" name="roleName" id="roleName" /></td>
					<td class="content_title">&nbsp;<fmt:message key="sys.name.code" />:</td>
					<td class="content_input">
					<input name="sysNameCode" id="sysNameCode" class="easyui-combobox" style="width:154px" data-options="panelHeight:'auto',valueField:'sysNameCode',textField:'name',url:'${apibase}/sysRole/getSysNameList'"/>
					</td>
					<td class="content_linkbutton" style="padding-left: 10px;"><a href="#" id="search_linkbutton" class="easyui-linkbutton" data-options="iconCls:'icon-search'"> <fmt:message
								key="btn.search" /></a></td>
					<td style="padding-left: 10px;"><a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-reload'"
						id="reset_linkbutton"> <fmt:message key="btn.reset" /></a></td>
				</tr>
			</table>
		</form>

		<table>
			<tr>
			<sc:security property="SYS_ROLE_ADD">
				<td style="padding: 2px 2px 2px 5px;"><a href="#" id="add_linkbutton" class="easyui-linkbutton" data-options="iconCls:'icon-add'"><fmt:message key="btn.create" /></a></td>
			</sc:security>
			<sc:security property="SYS_ROLE_UPDATE">
				<td class="content_linkbutton"><a href="#" id="update_linkbutton" class="easyui-linkbutton" data-options="iconCls:'icon-edit'"><fmt:message
							key="btn.modify" /></a></td>
			</sc:security>
			<sc:security property="SYS_ROLE_DELETE">
				<td class="content_linkbutton"><a href="#" id="delete_linkbutton" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"><fmt:message
							key="btn.delete" /></a></td>
			</sc:security>
			<sc:security property="SYS_ROLE_MENU">
				<td class="content_linkbutton"><a id="setMenu" href="javascript:setMenu()" class="easyui-linkbutton" data-options="iconCls:'icon-search'"><fmt:message
							key="btn.role.set.menu" /> </a></td>
			</sc:security>
				<td id="not_exist" style="display: none;"><span style="color: red;"><fmt:message key="search.no.record" />！</span></td>
			</tr>
		</table>
	</div>
	<!-- 角色列表 -->
	<table id="role_table"  cellspacing="0" cellpadding="0" data-options="rownumbers:true" ></table>
	<!-- 新增 -->
	<div id="add_dialog_div" class="easyui-dialog" style="width:500px;" data-options="modal:true, shadow:false, closed:true, buttons:'#add_dialog_button_div', title:'<fmt:message key="role.new" />'">
        <form id="add_dialog_form" method="post">
        	<input id="roleName_add_h" name="roleName" type="hidden">
            <div style="padding:20px">
				<table cellpadding="0" cellspacing="0" class="form-table">
					<tr style="height:30px;">
						<td style="width:90px;font-size:14px;"><fmt:message key="sys.name.code" /></td>
						<td>
							<input name="sysNameCode" id="sys_name_add" class="easyui-combobox" style="width:200px;font-size:14px;"  data-options="panelHeight:'auto',required:true" >
						</td>
					</tr>
					<tr style="height:30px;">
						<td style="width:90px;font-size:14px;"><fmt:message key="role.name" /></td>
						<td>
							<input id="roleName_add" name="roleName_add" style="width:200px;font-size:14px;" class="easyui-validatebox" data-options="required:true,validType:'length[1,16]'">
						</td>
					</tr>

				</table>
			</div>
        </form>
        <div id="add_dialog_button_div" style="text-align: center;">
            <a href="#" id="add_dialog_linkbutton_save" class="easyui-linkbutton" data-options="iconCls:'icon-save'"><fmt:message key="btn.save" /></a>
            &nbsp;&nbsp;&nbsp;&nbsp;
            <a href="#" id="add_dialog_linkbutton_cancel" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"><fmt:message key="btn.close" /></a>
        </div>
    </div>
	<!-- 修改 -->
	<div id="update_dialog_div" class="easyui-dialog" style="width: 500px;"
		data-options="modal:true, shadow:false, closed:true, buttons:'#update_dialog_button_div', title:'<fmt:message key="role.modify" />'">
		<form id="update_dialog_form" method="post">
			<input id="roleId_update" name="roleId" value="" type="hidden">
			<input id="roleName_update_h" name="roleName" type="hidden">
			<div style="padding:20px">
				<table cellpadding="0" cellspacing="0" class="form-table">
					<tr style="height:30px;">
						<td style="width:90px;font-size:14px;"><fmt:message key="sys.name.code" /></td>
						<td>
							<input name="sysNameCode" id="sys_name_update" class="easyui-combobox" style="width:200px;font-size:14px;"  data-options="panelHeight:'auto',required:true" >
						</td>
					</tr>
					<tr style="height:30px;">
						<td style="width:90px;font-size:14px;"><fmt:message key="role.name" /></td>
						<td>
							<input id="roleName_update" name="roleName_update" style="width:200px;font-size:14px;" class="easyui-validatebox" data-options="required:true,validType:'length[1,16]'">
						</td>
					</tr>

				</table>
		</form>
		<div id="update_dialog_button_div" style="text-align: center;">
			<a href="#" id="update_dialog_linkbutton_save" class="easyui-linkbutton" data-options="iconCls:'icon-save'"><fmt:message key="btn.save" /></a>
			&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="#" id="update_dialog_linkbutton_cancel" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"><fmt:message key="btn.close" /></a>
		</div>
	</div>

	<!-- 设置角色菜单    start -->
    <div id="tbMenu" style="padding: 5px; display: none;">
		<div>
		 <form id="menu_form" >
		 	<input type="hidden" id="sysNameCode_menu" name="sysNameCode" value="">
			<table>
				<tr>
					<td><fmt:message key="menu.name" />:</td>
					<td><input id="menuName" name="menuName" type="text" style="width:120px" /></td>
					<td><fmt:message key="menu.type" />:</td>
					<td>
					<select class="easyui-combobox" name="menuType" id="menuType" style="width:100%;" data-options="panelHeight:'auto'">
		        			<option value="" selected="selected"></option>
		        			<option value="M"><fmt:message key="menu.type.menu" /></option>
		        			<option value="B"><fmt:message key="menu.type.button" /></option>
	        		</select>
					</td>
					<td><a href="javascript:searchMenu()" id="btnsearchPrice"
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

	<div id="setMenuPage" class="easyui-dialog" style="width: 550px;height: 500px;"
		closed="true" modal="true" buttons="#dlg-buttonsRole" shadow="false" title="<fmt:message key='btn.role.set.menu' />">
		<div id="menuDg"></div>
		<div id="dlg-buttonsRole" style="text-align: center">
			<a href="#" id="btnSaveMenu" class="easyui-linkbutton"
				iconCls="icon-save" onclick="saveMenu()"><fmt:message key="btn.save" /></a>
			 <a href="#" style="text-align: center"
				class="easyui-linkbutton" iconCls="icon-cancel"
				onclick="closeMenu()"><fmt:message key="btn.close" /></a>
		</div>
	</div>
	<!-- 设置角色菜单 end -->
</body>
<script type="text/javascript">
	$(function() {
		// 初始化系统列表 重写jquery.easyui.min.js 11515 14335行 支持设置headers
		$('#role_table').datagrid(
				{
					url : '${apibase}/sysRole/sysRoleManage?random=' + new Date().getTime(),
					pageList : [ 50, 100, 200 ],
					pageSize : 50,
					rownumbers : true,
					pagination : true,
					remoteSort : true,
					checkOnSelect : false,
					selectOnCheck : false,
					singleSelect : true,
					headers:{'Content-Type':'application/json;charset=utf8','its-token':'1333333333'},
					toolbar : '#tb',
					columns : [ [ {
						field : 'rowCheckBox',
						title : '${requestScope.rb.query_customerMsg_txt_14}',
						width : 10,
						checkbox : true,
						align : 'center'
					}, {
						field : 'roleName',
						title : Msg.sys_role_name,
						width : 200,
						align : 'center'
					}, {
						field : 'sysName',
						title : Msg.sys_name_code,
						width : 200,
						align : 'center'
					}, {
						field : 'createTm',
						title : Msg.sys_user_create_tm,
						width : 200,
						align : 'center'
					}, {
						field : 'sysNameCode',
						hidden : true,
						width : 150,
						align : 'center'
					}, {
						field : 'roleId',
						hidden : true,
						width : 200,
						align : 'center'
					} ] ],
					onBeforeLoad : function(xhr) {
// 						alert("abc");
// 						xhr.setRequestHeader("its-username","admin");
// 		            	xhr.setRequestHeader("its-language","en");
					},
					onLoadSuccess : function(data) {
						if (data.total == '0') { // 查询无记录时提醒
							$("#not_exist").show();
						} else {
							$("#not_exist").hide();
						}
					}
		});
		//初始化角色所属系统
		$(function(){
			$.ajaxext('${apibase}/sysRole/getSysNameList?random='+new Date().getTime(), "post",true,"json","",
	        	function(data){//success
					if(data){
						$('#sys_name_add').combobox({
				    		data:data,
						    valueField:'sysNameCode',
						    textField:'name',
						    editable:false,
						    required:true
						});

						$('#sys_name_update').combobox({
				    		data:data,
				    		valueField:'sysNameCode',
							textField:'name',
						    editable:false,
						    required:true
						});

		  			}
	            },
	            function(){//error
	            	alert("error");
	            }
			);
		});
		_doResize($('#role_table'));
	});
	//控制页面大小不随展示行数变化
	$(window).resize(function(){
		 _doResize($('#role_table'));
	});
	// 查询按钮点击事件
	$('#search_linkbutton').click(function() {
		querySysRoleList();
	});

	// 重置按钮点击事件
	$('#reset_linkbutton').click(function() {
		resetSearchForm();
	});
	// 按条件查询角色信息列表
	function querySysRoleList() {
		$('#role_table').datagrid('load', $.serializeObject($('#search_form').form()));
	}
	// 重置查询列表框
	function resetSearchForm() {
		 $('#search_form').form('clear');
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
		saveSysRole();
	});

	// 新增用户窗口关闭按钮点击事件
	$('#add_dialog_linkbutton_cancel').click(function() {
		$('#add_dialog_div').dialog('close');
	});

	// 提交保存录入的新增用户信息
	function saveSysRole() {
		$('#add_dialog_form').form('submit', {
			url : '${apibase}/sysRole/addSysRole?random=' + new Date().getTime(),
			onSubmit : function() {
				$("#roleName_add_h").val($("#roleName_add").val());
				return $(this).form('validate');
			},
			success : function(resultData) {
				alert("easyUI form提交,因跨域请求后，无法获取返回的response,手动刷新列表,可修改为ajax解决,如用户管理:"+resultData);
				if (resultData == 'SUCCESS') {
					$.messager.show({
						title : Msg.sys_remaind1,
						msg : Msg.sys_save_txt
					});
					$('#add_dialog_div').dialog('close'); // 关闭新增窗口
					querySysRoleList(); // 重新查询用户列表
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
		var rows = $('#role_table').datagrid('getChecked'); // 获取选中的行数据
		if (rows.length == 1) {
			var roleId = rows[0].roleId;
			openUpdateDialog(roleId);
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
	function openUpdateDialog(roleId) {
		$(function(){
			$.ajaxext('${apibase}/sysRole/getSysRoleById?random=' + new Date().getTime(), "post",true,"json",{"roleId" : roleId},
	        	function(data){//success
					var uform = $('#update_dialog_form');
					$('#update_dialog_div').dialog('open');
					$('#update_dialog_form').form('clear');
					$('#update_dialog_form').form('load', {roleId:data.roleId,roleName_update:data.roleName, sysNameCode:data.sysNameCode});
	            },
	            function(){//error
	            	alert("error");
	            }
			);
		});
	}

	  // 修改用户窗口保存按钮点击事件
    $('#update_dialog_linkbutton_save').click(function (){
        updateSysRole();
    });

    // 修改用户窗口关闭按钮点击事件
    $('#update_dialog_linkbutton_cancel').click(function (){
        $('#update_dialog_div').dialog('close');
    });

 	// 提交保存录入的修改用户信息
	function updateSysRole() {
		$('#update_dialog_form').form('submit', {
			url : '${apibase}/sysRole/updateSysRole?random=' + new Date().getTime(),
			onSubmit : function() {
				$("#roleName_update_h").val($("#roleName_update").val());
				return $(this).form('validate');
			},
			success : function(resultData) {
				if (resultData == 'SUCCESS') {
					$.messager.show({
						title : Msg.sys_remaind1,
						msg : Msg.sys_save_txt
					});
					$('#update_dialog_div').dialog('close'); // 关闭新增窗口
					querySysRoleList(); // 重新查询列表
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
	 var roleId = "";  // ID
  	 var rows = $('#role_table').datagrid('getChecked'); // 获取选中的行数据
  	 if(rows.length > 0){
  		  $.messager.confirm( Msg.sys_confirm2,Msg.sys_deleate,function(flag){
  			  if(flag){
  				  $.each(rows, function(index, row){
  	                  if(index+1 == rows.length){
  	                	roleId = roleId + row.roleId;
  	                  }else{
  	                	roleId = roleId + row.roleId + ",";
  	                  }
  	              });
  	              $.ajax({
  	                  url:'${apibase}/sysRole/deleteSysRole?random=' + new Date().getTime(),
  	                  type: "POST",
  	                  data: {'roleId':roleId},
  	                  async: false,
  	                  success: function (resultData){
  	                	  if (resultData == 'SUCCESS') {
  	                		  $.messager.show({
  	                			  title: Msg.sys_remaind1,
  	                			  msg: Msg.sys_delete_txt1
  	                		  });
  	                		  querySysRoleList(); // 重新查询列表
  	                		  $('#role_table').datagrid('uncheckAll');
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
	    var roleMenus;
		var addMenus=new Array();
		var removeMenus=new Array();
		var menu_form;
		var menuDg;
		//----------菜单设置按钮点击事件----------
		function setMenu(){
			roleMenus=new Array();
			addMenus=new Array();
			removeMenus=new Array();
			var items = $("#role_table").datagrid('getChecked');
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
			$('#setMenuPage').dialog('open');
			$('#menu_form').form('clear');
			$("#sysNameCode_menu").val(items[0].sysNameCode);
			//加载菜单信息
			menu_form=$('#menu_form').form();
			menuDg=$('#menuDg').datagrid({
				url :'${apibase}/sysRole/getSysMenuList?random='+new Date().getTime(),
				queryParams: {"menuName":$('#menuName').val(),"sysNameCode":$('#sysNameCode_menu').val(),"menuType":$('#menuType').val()},
				    toolbar : '#tbMenu',
				    height : 400,
				    nowrap : false,
				    idField : 'menuId',
				    pageList : [ 50, 100, 200 ],
					pageSize : 50,
				    singleSelect : false, //是否单选
					pagination : true,//分页控件
					rownumbers : true, //行号
				    columns:[[
				        {field:"ck",title:'选中',checkbox:true},
				        {field:"menuId",hidden:true},
				        {field:"menuName",title:Msg.sys_menu_name},
				        {field:"parentMenuName",title:Msg.sys_menu_parent_menu},
				        {field:"sysName",title:Msg.sys_name_code},
				        {field:"menuType",title:Msg.sys_menu_type,formatter: function(value, row){
							if(value=='M'){
								return Msg.sys_menu_type_menu;
							}else{
								return Msg.sys_menu_type_button;
							}
						}}
				    ]],
				    onLoadSuccess:function(){
				    	//$("#menuDg").datagrid("clearChecked");
				    	$.post('${apibase}/sysRole/getSysRoleMenuList?random='+new Date().getTime(),{roleId:items[0].roleId},function(data){
			    			if(data != null && data.length>0){
			    				roleMenus=data;
				    			for(var i=0;i<roleMenus.length;i++){
				    				var rows = $('#menuDg').datagrid("getRows");
				    				for(var j=0;j<rows.length;j++){
				    					if(rows[j].menuId==roleMenus[i].menuId){
				    						var rowIndex = $('#menuDg').datagrid("getRowIndex",rows[j].menuId);
				    						$('#menuDg').datagrid("checkRow",rowIndex)//选中行
				    									.datagrid("refreshRow",rowIndex);//刷新行

				    					}
				    				}
				    			}
			    			}
			    		},"json");
				    }
			});

// 			$('#setMenuPage').dialog({
// 				title:Msg.set_associated_roles,
// 				height:500,
// 				width:550,
// 				 onOpen:function(){
// 			          //dialog原始left
// 			          default_left=$('#setMenuPage').panel('options').left;
// 			          //dialog原始top
// 			          default_top=$('#setMenuPage').panel('options').top;
// 			        },
// 			        onMove:function(left,top){  //鼠标拖动时事件
// 			           var body_width=document.body.offsetWidth;//body的宽度
// 			           var body_height=document.body.offsetHeight;//body的高度
// 			           var dd_width= $('#setMenuPage').panel('options').width;//dialog的宽度
// 			           var dd_height= $('#setMenuPage').panel('options').height;//dialog的高度
// 			           if(left<1||left>(body_width-dd_width)||top<1||top>(body_height-dd_height)){
// 			              $('#setMenuPage').dialog('move',{
// 			                    //left:default_left,
// 			                    //top:default_top
// 			              });
// 			          }
// 			        }

// 			}).dialog('open');
		}

		// 查询
		function searchMenu(){
			menuDg.datagrid('reload', $.serializeObject(menu_form));
	    }

		// 重置
		$('#reset_linkbutton_set').click(function() {
			 $('#menu_form').form('clear');
		});

		 function closeMenu(){
		    	$("#menuDg").datagrid("clearChecked");
		    	$('#setMenuPage').dialog('close')
		    }

		//----------保存角色设置按钮点击事件----------
		function saveMenu(){
			var roleIds="";
			var menuIds="";
			var roles = $("#role_table").datagrid('getChecked');
			var addMenus = $("#menuDg").datagrid("getChecked");
			roleIds = roles[0].roleId;
			if(addMenus.length>0){
				menuIds = addMenus[0].menuId;
				if(addMenus.length >1){
					for(var i=1;i<addMenus.length;i++){
						menuIds+=","+addMenus[i].menuId;
					}
				}
			}
			var data = {roleId:roleIds,menuId:menuIds};
			$.post("${apibase}/sysRole/saveSysRoleMenu", data, function(resultData) {
			if (resultData == 'SUCCESS') {
				$.messager.show({
					title : Msg.sys_remaind1,
					msg : Msg.sys_success
				});
				$("#setMenuPage").dialog("close");
				$("#menuDg").datagrid("clearChecked");
				$('#role_table').datagrid("clearChecked").datagrid("reload");
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
		});
	}

</script>
</html>
</fmt:bundle>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<c:set var="lang" value="${ITS_USER_SESSION.language}" />
<input type="hidden" id="lang" value="${lang}" />
<fmt:setLocale value="${lang}" />
<fmt:bundle basename="com.its.resource.lang">
<div>commons.fileupload方式</div>
<p style="color: red;">需要去掉spring-servlet中 支持上传文件的配置,修改jsp页面的form submit的form id</p>
<br>
	<form id="commons_upload_dialog_form" action="${ctx }/file/saveFile" method="post" enctype="multipart/form-data">
		<div style="padding: 0px;" align="left">
			<table  border="1" cellpadding="0" cellspacing="5" class="form-table">
				<tr id="productTr">
					<td class="agent-service-td" valign="top" style="width: 160px;">commons.fileupload方式:</td>
					<td valign="top" " id="productTr_Td" colspan="3">
					<div>文件1：<input type="file" name="file" id="file1"></div>
    			    <div>文件2：<input type="file" name="file" id="file2"></div>
					</td>
				</tr>
			</table>
		</div>
	</form>
	<form id="springmvc_upload_dialog_form" action="${ctx }/file/saveFileSM" method="post" enctype="multipart/form-data">
		<div style="padding: 0px;" align="left">
			<table  border="1" cellpadding="0" cellspacing="5" class="form-table">
				<tr id="productTr">
					<td class="agent-service-td" valign="top" style="width: 160px;">SpringMVC方式:</td>
					<td valign="top" " id="productTr_Td" colspan="3">
					<div>文件1：<input type="file" name="file" id="file1"></div>
    			    <div>文件2：<input type="file" name="file" id="file2"></div>
					</td>
				</tr>
			</table>
		</div>
	</form>
	<div id="attribute_dialog_button_div" style="text-align: center;">
		<a href="#" id="upload_dialog_linkbutton_save"
			class="easyui-linkbutton" data-options="iconCls:'icon-save'"><fmt:message
				key="btn.save" /></a> &nbsp;&nbsp;&nbsp;&nbsp; <a href="#"
			id="attribute_dialog_linkbutton_cancel" class="easyui-linkbutton"
			data-options="iconCls:'icon-cancel'"><fmt:message key="btn.close" /></a>
	</div>

</fmt:bundle>
<script type="text/javascript">
	$(function() {
		$('#attribute_dialog_linkbutton_cancel').click(function() {
			$("#upload_dialog_div").dialog('close');
		});
		
		$('#upload_dialog_linkbutton_save').click(function() {
			if($(this).linkbutton("options").disabled){	//已禁用按钮
				return;
			}
			uploadSaveFile();
		});

	})
	
	//上传保存
function uploadSaveFile() {
	$("#upload_dialog_linkbutton_save").linkbutton('disable');
// 	$("#commons_upload_dialog_form").form('submit',{
	$("#springmvc_upload_dialog_form").form('submit',{
// 		url : '${ctx}/file/saveFileSM?random='+ new Date().getTime(),
		method : "post",
		success : function(result) {
			resultData = eval('(' + result + ')');
			if (resultData == 'SUCCESS') {
				$.messager.show({
					title : Msg.sys_remaind1,
					msg : Msg.sys_success
				});
				$("#upload_dialog_div").dialog("close");
				$('#file_table').datagrid("clearChecked").datagrid("reload");
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
			}
		});
}
</script>
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
		<table cellspacing="10">
		        <tr>       
			        <td class="content_input">
			        <div style="width: 168px;">
                        <input id="st_code" name="u.st_code" class="easyui-textbox easyui-validatebox" label="<fmt:message key='user.employee.number' />:" style="width:160px;" 
                            labelPosition='top' data-options="prompt:'<fmt:message key='user.employee.number' />',validType:'maxLength[32]'"/></div>
		            </td>
	            	<td class="content_input">
	            	<div style="width: 165px;">
<!-- 		            	<select id="sr.sys_name_code" name="sr.sys_name_code" class="easyui-combobox" label="系统名:" labelPosition='top' style="width:160px;" data-options="editable:false">   -->
<!-- 			            	<option value="" selected="selected">&nbsp;</option>   -->
<!-- 			            	<option value="IOP-MCS">IOP-MCS</option>   -->
<!-- 						    <option value="IOP-AMS">IOP-AMS</option>  -->
<!-- 					    </select>   -->
					    <input id="sr.sys_name_code" name="sr.sys_name_code" class="easyui-combobox" label="系统名:" labelPosition='top' 
		            	style="width:160px;" data-options="panelHeight:'auto',valueField:'EN_NAME',textField:'BLD_NAME',url:'${ctx}/sysReport/sysReportPageValue?sqlKey=SYS_NAME'"/>
					 </div>
		            </td>
		            <td class="content_input" colspan="2">
                    <input id="u.create_tm" name="u.create_tm" type="text" style="width:159px" class="create_date_start easyui-datetimebox" label="创建时间:" labelPosition='top' required="required" editable="false"/>
                                        至
                    <input id="u.create_tm_to" name="u.create_tm_to" type="text"  style="width:159px" class="create_date_end easyui-datetimebox" required="required" data-options="validType:['md','ld']" editable="false"/>
		            </td>
               </tr>
               <tr>
                 <td colspan="2">
	                 <div id="not_exist" style="display: none;"><span style="color: red;"><fmt:message key="search.no.record" />！</span></div>
	             </td>
                 <td valign="bottom" colspan="2" style="" align="right">         
                 	<a href="#" id="search_linkbutton" class="easyui-linkbutton" style="min-width:80px;" data-options="iconCls:'icon-search'"> <fmt:message key="btn.search" /></a>
					<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-page_excel'" id="export_linkbutton"> <fmt:message key="btn.export" /></a>
					<a href="#" class="easyui-linkbutton" style="min-width:80px;" data-options="iconCls:'icon-reload'" id="reset_linkbutton"> <fmt:message key="btn.reset" /></a>
	             </td>
                </tr>
	        </table>
	        <input type="hidden" id="u.st_code_where" name="u.st_code_where" value="and u.st_code like '%<where>u.st_code</where>%'"/>
	        <input type="hidden" id="u.create_tm_where" name="u.create_tm_where" value="and u.create_tm >= '<where>u.create_tm</where>'"/>
	        <input type="hidden" id="u.create_tm_to_where" name="u.create_tm_to_where" value="and u.create_tm <= '<where>u.create_tm_to</where>'"/>
	        <input type="hidden" id="sr.sys_name_code_where" name="sr.sys_name_code_where" value="and sr.sys_name_code  = '<where>sr.sys_name_code</where>'"/>
		</form>

		<input id="tableHeart" value="${tableHeart}" hidden="true"/>
	</div>
	<!-- 用户列表 -->
	<table id="user_table" cellspacing="0" cellpadding="0" data-options="rownumbers:true"  ></table>
</body>
<script type="text/javascript">
	var tableHeart = $('#tableHeart').val();
	 tableHeart = tableHeart.replace(/(\[)|(\])/g, "");
	 var tableHeartArr = new Array();
	 tableHeartArr = tableHeart.split(",");
	 
	 //生成columns
     var columns = [];
//      var col = {};
//      col.field = 'rowCheckBox';
//      col.hidden = false;
//      col.title = '${requestScope.rb.query_customerMsg_txt_14}';
//      col.checkbox = true;
//      col.align = 'center';
//      columns.push(col);
     for (var i = 0; i < tableHeartArr.length; i++) {
         var column = {};
         column.field = $.trim(tableHeartArr[i]);
         column.hidden = false;
         column.title = $.trim(tableHeartArr[i]);
         column.align = 'center';
         columns.push(column);
     }
	
	$(function() {
		init();
		initTime();
	});
	
	$.extend($.fn.validatebox.defaults.rules, {
        md: {
            validator: function(value, param){
                var d1 = $(".create_date_start").datetimebox("getValue");
                var d2 = value;
                d1 = d1.replace(/-/g,"/");
                var date1 = new Date(d1);                
                d2 = d2.replace(/-/g,"/");
                var date2 = new Date(d2);
                return date2>=date1;
            },
            message: '结束时间小于起始时间'
        }
    });
	
	$.extend($.fn.validatebox.defaults.rules, {
        ld: {
            validator: function(value, param){
                var d1 = $(".create_date_start").datetimebox("getValue");
                var d2 = value;
                d1 = d1.replace(/-/g,"/");
                var date1 = new Date(d1);                
                d2 = d2.replace(/-/g,"/");
                var date2 = new Date(d2);                
                var timeslong = date2.getTime() - date1.getTime();               
                var diff = timeslong/(1000*60*60*24);
                return diff <= 30;
                
            },
            message: '时间段大于30天'
        }
    });

	function init() {
		// 初始化系统列表
		$('#user_table').datagrid(
				{
					url : '',
// 					url : '${ctx}/sysReport/sysReportManage?random='
// 							+ new Date().getTime(),
					pageList : [ 50, 100, 200 ],
					pageSize : 50,
					nowrap : true,
					rownumbers : true,
					pagination : true,
					remoteSort : true,
					checkOnSelect : false,
					selectOnCheck : false,
					singleSelect : true,
					toolbar : '#tb',
					columns : [ columns ],
					onLoadSuccess : function(data) {
						if (data.total == '0') { // 查询无记录时提醒
							$("#not_exist").show();
						} else {
							$("#not_exist").hide();
						}
					}
				});
		_doResize($('#user_table'));
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
	// 按条件查询角色信息列表
	function querySysUserList() {
		$('#user_table').datagrid("options").url = '${ctx}/sysReport/sysReportManage?random='+ new Date().getTime();
		$('#user_table').datagrid('load', $.serializeObject($('#search_form').form()));
	}
	// 重置查询列表框
	function resetSearchForm() {
		$("#search_form").form("clear");
// 		 $('#bcCode').combobox('select', '');  
		// 时间
	    var endDate = new Date();
	    var startTime = endDate.getTime()-1000*60*60*24*29;
		var startDate = new Date(startTime);
	    $('.create_date_start').datetimebox('setValue', parsedate(startDate, 0));
	    $('.create_date_end').datetimebox('setValue', parsedate(endDate, 1));
	}

	// Excel导出
	$('#export_linkbutton').click(function() {
		window.location.href = "${ctx}/excel/export";
	});



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

function initTime(){
	 $(".create_date_start").datetimebox({
        editable: false
    });
  	
    $(".create_date_end").datetimebox({	      
        editable: false
    });
    
    var endDate = new Date();
    var startTime = endDate.getTime()-1000*60*60*24*29;
	 var startDate = new Date(startTime);
    $('.create_date_start').datetimebox('setValue', parsedate(startDate, 0));
    $('.create_date_end').datetimebox('setValue', parsedate(endDate, 1));
}
function parsedate(value,type){  
    var date = new Date(value);  
    var year = date.getFullYear();  
    var month = date.getMonth()+1; //月份+1     
    var day = date.getDate();
    var language = "zh";
    if(language == "en"){
	    if(type == 0){
	    	return month+"/"+day+"/"+year+" 00:00"; 
	    }
	    return month+"/"+day+"/"+year+" 23:59:59";  	    	
    }else{
	    if(type == 0){
	    	return year+"-"+month+"-"+day+" 00:00"; 
	    }
	    return year+"-"+month+"-"+day+" 23:59:59";  	    	
    }

} 
</script>
</html>
</fmt:bundle>
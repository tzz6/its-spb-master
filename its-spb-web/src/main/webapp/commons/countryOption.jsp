<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/commons/language.jsp"%>
<%-- <%@ include file="/commons/common.jsp"%> --%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>
<!-- create by 89003459-->
<!-- 添加国家页面    使用说明-->
<!-- 用法： -->
<!-- 	1、父页面需提供getCountryOption()方法 ，向国家页面传递已添加的国家的国家编码  -->
<!-- 	例： -->
<!-- 		function getCountryOption(){ -->
<!-- 			var countrys = new Array(); -->
<!-- 			countrys.push('CN'); -->
<!-- 			countrys.push('HK'); -->
<!-- 			return countrys; -->
<!-- 		} -->
<!-- 	2、父页面需提供countryCallback(rows)方法,在选取国家页面选取完国家后点击确定按钮会调用此方法向父窗体返回选取的数据 -->
<!-- 	       参数说明  rows: 向父窗体返回用户选取的数据；例：Object {total: 2, rows: Array[2]}rows: Array[2] -->
<%-- 如使用包含  ：<%@ include file="/commons/countryOption.jsp"%> ，需在在调用时调用initCountry()方法 实现数据每次初始化最新数据--%>
<!-- 打开方式：$('#country_dialog_div').dialog('open'); -->

<!-- 	<div id="country_dialog_div" class="easyui-dialog" -->
<!-- 		style="width: 900px; top: 100px;" -->
<!-- 		data-options="modal:true, shadow:false, closed:true, buttons:'#country_dialog_button_div',title:'国家'"> -->
<div id="country_option_div">
		<form id="country_form">
			<table>
				<tr>
					<td style="width:400px; padding-left: 30px">
							<input class="easyui-textbox easyui-validatebox" label="<fmt:message key='country.option.added.country'/>:" labelPosition='top' 
		       				id="countryOptionName" name="countryOptionName" style="width:70%;" placeholder="<fmt:message key="country.option.query.title" />"/>
						<a href="#" id="countryOptionQue_btn" class="easyui-linkbutton"
							data-options="iconCls:'icon-search'"><fmt:message key="country.option.retrieval" /></a>
					</td>
					<td></td>
					<td style="width:400px; padding-left: 30px">
					<input class="easyui-textbox easyui-validatebox" label="<fmt:message key='country.option.country.be.added'/>:" labelPosition='top' 
		       				id="countryName" name="countryName" style="width:70%;" placeholder="<fmt:message key="country.option.query.title" />"/>
						<a href="#" id="countryQue_btn" class="easyui-linkbutton"
							data-options="iconCls:'icon-search'"><fmt:message key="country.option.retrieval" /></a>
					</td>
				</tr>
				<tr>
					<td style="width:400px; text-align: center"><table id="countryTb_option"></table></td>
					<td style="width:50px; text-align: center;">
						<p><a href="#" id="addCountry" class="easyui-linkbutton" style="width: 80px;"><<</a></p>
						<p><a href="#" id="delCountry" class="easyui-linkbutton" style="width: 80px;">>></a></p>
						<p><a href="#" id="impCountry" class="easyui-linkbutton" style="width: 80px;"><fmt:message key="country.option.import" /></a></p>
					</td>
					<td style="width:400px; text-align: center"><table id="countryTb"></table></td>
				</tr>
			</table>
		</form>
		<div id="country_dialog_button_div" style="text-align: center;">
			<a href="javascript:void(0)" id="save_country_btn" class="easyui-linkbutton" data-options="iconCls:'icon-save'"><fmt:message key="exchangeRate.system.confirm" /></a> &nbsp;&nbsp;&nbsp;&nbsp;
			<a href="#" id="cancel_country_btn" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"><fmt:message key="exchangeRate.system.cancel" /></a>
		</div>
	</div>
	<div id="imp_country_dialog_div" class="easyui-dialog"
		style="width: 500px; top: 200px; height: 300px"
		data-options="modal:true, shadow:false, closed:true, buttons:'#imp_dialog_button_div',title:'<fmt:message key="countru.option.code.import" />'">
		<table style="margin-top: 20px;">
			<tr>
				<td><fmt:message key="country.option.country.code" />：</td>
				<td><textarea id="imp_country" cols="60" rows="12" placeholder="<fmt:message key="country.option.code.imp.title" />"></textarea></td>
			</tr>
		</table>
		<div id="imp_dialog_button_div" style="text-align: center;">
			<a href="javascript:void(0)" id="imp_country_btn" class="easyui-linkbutton" data-options="iconCls:'icon-save'"><fmt:message key="country.option.import" /></a> &nbsp;&nbsp;&nbsp;&nbsp;
			<a href="#" id="cancel_country_imp_btn" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'"><fmt:message key="exchangeRate.system.cancel" /></a>
		</div>
	</div>	
</body>
<script type="text/javascript">	
var arrayObj = new Array();
var arrayObj_option = new Array();
var optionTotal = 0;
var impArray = [];
var impArrayCheck = [];
var isImp = false;
//initCountry();
function initCountry(){
	// 父窗体传过来已选取的数据
	var countryInfo = getCountryOption();
	if(impArray != ''){
		countryInfo = impArray;
	}else{
		impArray = countryInfo;
	}
	optionTotal = countryInfo.length
	$('#country_form').form('clear');
	$('#countryTb').datagrid({
		url : '${ctx}/dictionary/get/countries',
		height : 430,
		rownumbers : true,
		remoteSort : true,
		fitColumns : true,
		checkOnSelect : true,
		selectOnCheck : true,
		singleSelect : false,
		toolbar : '#tb',
		columns : [ [ {
			field : 'rowCheckBox',
			title : '${requestScope.rb.query_customerMsg_txt_14}',
			width : 10,
			checkbox : true,
			align : 'center'
		}, {
			field : 'bcCode',
			title : '<fmt:message key="country.option.country.code" />',
			width : 50,
			align : 'center'
		}, {
			field : 'bldName',
			title : '<fmt:message key="country.option.cunotry.name" />',
			width : 60,
			align : 'center'
		} ] ],
		onLoadSuccess : function(data) {
			//$('#countryTb_option').datagrid('loadData', { total: 0, rows: [] });
			var impCount = 0;
			// 通过父窗体传递的数据countryInfo获取已选择的项
			var columns = $(this).datagrid('getColumnFields');
			if(countryInfo != ''){
				// 循环遍历每一行做对比
				for(var i=0;i<countryInfo.length;i++){
					var rows = $(this).datagrid('getData').rows;
					for(var j=0;j<rows.length;j++){
						if(rows[j].bcCode == countryInfo[i]){
							var addData = rows[j];
							$('#countryTb').datagrid('deleteRow',j);
							$('#countryTb_option').datagrid('insertRow',{ row:addData});
							for(var k=0;k<impArrayCheck.length;k++){
								if(impArrayCheck[k] == addData.bcCode){
									$('#countryTb_option').datagrid('selectRow',$('#countryTb_option').datagrid('getData').total-1);
									if(isImp){
										impCount++;
									}
								}
							}
							impArray.push(addData.bcCode);
							continue;
						}
					}
				}
			}
			if(isImp){
				$.messager.alert('<fmt:message key="exchangeRate.system.prompt" />','<fmt:message key="country.option.import.title01" />'+impCount+'<fmt:message key="country.option.import.title02" />','warning');
				isImp = false;
			}
			impArrayCheck = [];
		},
		onSelect : function(index){
			arrayObj.push(index);
		},
		onUnselect : function(index){
			for(var i=0;i<arrayObj.length;i++){
				if(arrayObj[i] == index){
					arrayObj.splice(i,1)
				}
			}
		},
		onCheckAll:function(rows){
			// 全选时将数据下标全部存储起来
			arrayObj.length = 0;
			for(var i=0;i<rows.length;i++){
				arrayObj[i] = i;
			}
			
		},
		onUncheckAll:function(rows){
			// 取消全选时清空下标存储
			arrayObj.length = 0;
		}
	});
	$('#countryTb_option').datagrid({
		height : 430,
		rownumbers : true,
		remoteSort : true,
		fitColumns : true,
		checkOnSelect : true,
		selectOnCheck : true,
		singleSelect : false,
		columns:[[
			{field : 'rowCheckBox',title : '${requestScope.rb.query_customerMsg_txt_14}',
			 width : 10,checkbox : true,align : 'center'},
			{field:'bcCode',title:'<fmt:message key="country.option.country.code" />',width:50,align : 'center'},
			{field:'bldName',title:'<fmt:message key="country.option.cunotry.name" />',width:60,align : 'center'}

		]],
		onLoadSuccess : function(data) {
			arrayObj_option.length = 0;
			// 通过父窗体传递的数据countryInfo获取已选择的项
			var columns = $(this).datagrid('getColumnFields');
			if(countryInfo != ''){
				// 循环遍历每一行做对比
				for(var i=0;i<countryInfo.length;i++){
					var rows = $(this).datagrid('getData').rows;
					for(var j=0;j<rows.length;j++){
						if(rows[j].bcCode == countryInfo[i]){
							var addData = rows[j];
							$('#countryTb').datagrid('deleteRow',j);
							$('#countryTb_option').datagrid('insertRow',{ row:addData});
							continue;
						}
					}
				}
			}
		},
		onSelect : function(index){
			arrayObj_option.push(index);
		},
		onUnselect : function(index){
			for(var i=0;i<arrayObj_option.length;i++){
				if(arrayObj_option[i] == index){
					arrayObj_option.splice(i,1)
				}
			}
		},
		onCheckAll:function(rows){
			// 全选时将数据下标全部存储起来
			arrayObj_option.length = 0;
			for(var i=0;i<rows.length;i++){
				arrayObj_option[i] = i;
			}
			
		},
		onUncheckAll:function(rows){
			// 取消全选时清空下标存储
			arrayObj_option.length = 0;
		}
	});
}

	
$("#addCountry").click(function(){
	if ($(this).linkbutton("options").disabled) {
		return;
	}
	var rowData = $('#countryTb').datagrid('getSelections'); // 获取选中的行数据
	$('#countryTb_option').datagrid("unselectAll");
	for(var i=0;i<rowData.length;i++){
		$('#countryTb_option').datagrid('insertRow',{ row:rowData[i]});
		$('#countryTb_option').datagrid('selectRow',$('#countryTb_option').datagrid('getData').total-1);
		impArray.push(rowData[i].bcCode);
	}	
	// 根据下标删除选中的data
	arrayObj = bubbleSort(arrayObj);
	for(var i=arrayObj.length-1;i>=0;i--){
		$('#countryTb').datagrid('deleteRow',arrayObj[i]);
	}
	arrayObj.length = 0;
});
$("#delCountry").click(function(){
	if ($(this).linkbutton("options").disabled) {
		return;
	}
	var rowData = $('#countryTb_option').datagrid('getSelections'); // 获取选中的行数据
	for(var i=0;i<rowData.length;i++){
		$('#countryTb').datagrid('insertRow',{ row:rowData[i]});
	}
	arrayObj_option = unique(bubbleSort(arrayObj_option));
	for(var i=(arrayObj_option.length-1);i>=0;i--){
		$('#countryTb_option').datagrid('deleteRow',arrayObj_option[i]);
	}
	arrayObj_option.length = 0;
});

//数组排序 
function bubbleSort(array){
    /*给每个未确定的位置做循环*/
    for(var unfix=array.length-1; unfix>0; unfix--){
      /*给进度做个记录，比到未确定位置*/
      for(var i=0; i<unfix;i++){
        if(array[i]>array[i+1]){
          var temp = array[i];
          array.splice(i,1,array[i+1]);
          array.splice(i+1,1,temp);
        }
      }
    }
    return array;
}
// 选取国家后点击保存按钮函数
$("#save_country_btn").click(function(){
	if ($(this).linkbutton("options").disabled) {
			return;
	} 
	var rows = $('#countryTb_option').datagrid('getData'); // 获取选中的行数据
	if(rows.total == 0)
		return;
	optionTotal = rows.total;
	// 调用父窗体方法返回选取的国家信息
	countryCallback(rows);
	$('#country_dialog_div').dialog('close');
});
$("#cancel_country_btn").click(function(){
	if ($(this).linkbutton("options").disabled) {
			return;
	} 
	$('#country_dialog_div').dialog('close');
});

$("#country_dialog_div").dialog({
	onClose: function () {
	   //如果用户没有点击确认按钮，直接关闭了，清空已添加的国家
	   $("#countryTb_option").datagrid('loadData', { total: 0, rows: [] });
	   impArray = [];
	   $("#country_dialog_button_div").show();
	   $("#addCountry").linkbutton("enable");
	   $("#delCountry").linkbutton("enable");
	   $("#impCountry").linkbutton("enable");
	} 
});

// 用于存储匹配成功的记录行数，连续点击查询按钮跳过这些行
var tempIndex=[];
// 查询函数
function searchText(dg,t){
	dg.datagrid("unselectAll");
	var rows = dg.datagrid("getData").rows;
	var columns = dg.datagrid('getColumnFields');
	var searchVal = t.val().toUpperCase();
	for(var i=0;i<rows.length;i++){
		for(var j=1;j<columns.length;j++){
			if(rows[i][columns[j]].indexOf(searchVal)>=0){
				var flag = false;
				for(var m=0;m<tempIndex.length;m++){
					if(tempIndex[m] == i){
						flag = true;
						break;
					}
				}
				if(!flag){
					dg.datagrid("selectRow",i);
					tempIndex.push(i);
					return;
				}
			}
		}
		if(i==(rows.length-1)){
			tempIndex=[];
		}
	}
}
$("#countryOptionQue_btn").click(function(){
	if($("#countryName").val()!='')
		searchText($("#countryTb_option"),$("#countryOptionName"));
});
$("#countryQue_btn").click(function(){
	if($("#countryName").val()!='')
		searchText($("#countryTb"),$("#countryName"));
});
$("#impCountry").click(function(){
	if ($(this).linkbutton("options").disabled) {
		return;
	}
	$("#imp_country_dialog_div").dialog('open');
});
$("#cancel_country_imp_btn").click(function(){
	$('#imp_country_dialog_div').dialog('close');
});
// 国家二字码导入
$("#imp_country_btn").click(function(){
	var impCountry = $("#imp_country").val().toUpperCase().replace(/(^\s*)|(\s*$)/g, "");
	if(impCountry!=""){
		var countrysArry = impCountry.split(",");
		for(var i=0;i<countrysArry.length;i++){
			if(countrysArry[i]!=''){
				impArray.push(countrysArry[i]);
				impArrayCheck.push(countrysArry[i]);
			}
		}
		$("#countryTb_option").datagrid('loadData', { total: 0, rows: [] });
		impArray = unique(impArray);
		impArrayCheck = unique(impArrayCheck);
		isImp = true;
		initCountry();
		$("#imp_country").val("");
		$('#imp_country_dialog_div').dialog('close');
	}
	
});
// 去除数组中重复元素
function unique(arr) {
    var result = [], hash = {};
    for (var i = 0, elem; (elem = arr[i]) != null; i++) {
        if (!hash[elem]) {
            result.push(elem);
            hash[elem] = true;
        }
    }
    return result;
}
</script>
</html>
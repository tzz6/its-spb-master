if ($.fn.pagination){
	$.fn.pagination.defaults.beforePageText = '第';
	$.fn.pagination.defaults.afterPageText = '共{pages}頁';
	$.fn.pagination.defaults.displayMsg = '顯示{from}到{to},共{total}記錄';
}
if ($.fn.datagrid){
	$.fn.datagrid.defaults.loadMsg = '正在處理，請稍待。。。';
}
if ($.fn.treegrid && $.fn.datagrid){
	$.fn.treegrid.defaults.loadMsg = $.fn.datagrid.defaults.loadMsg;
}
if ($.messager){
	$.messager.defaults.ok = '確定';
	$.messager.defaults.cancel = '取消';
}
$.map(['validatebox','textbox','filebox','searchbox',
		'combo','combobox','combogrid','combotree',
		'datebox','datetimebox','numberbox',
		'spinner','numberspinner','timespinner','datetimespinner'], function(plugin){
	if ($.fn[plugin]){
		$.fn[plugin].defaults.missingMessage = '該輸入項為必輸項';
	}
});
if ($.fn.validatebox){
	$.fn.validatebox.defaults.rules.email.message = '請輸入有效的電子郵件地址';
	$.fn.validatebox.defaults.rules.url.message = '請輸入有效的URL地址';
	$.fn.validatebox.defaults.rules.length.message = '輸入內容長度必須介於{0}和{1}之間';
	$.fn.validatebox.defaults.rules.remote.message = '請修正此欄位';
}
if ($.fn.calendar){
	$.fn.calendar.defaults.weeks = ['日','一','二','三','四','五','六'];
	$.fn.calendar.defaults.months = ['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月'];
}
if ($.fn.datebox){
	$.fn.datebox.defaults.currentText = '今天';
	$.fn.datebox.defaults.closeText = '關閉';
	$.fn.datebox.defaults.okText = '確定';
}
if ($.fn.datetimebox && $.fn.datebox){
	$.extend($.fn.datetimebox.defaults,{
		currentText: $.fn.datebox.defaults.currentText,
		closeText: $.fn.datebox.defaults.closeText,
		okText: $.fn.datebox.defaults.okText
	});
}
if ($.fn.datetimespinner){
	$.fn.datetimespinner.defaults.selections = [[0,4],[5,7],[8,10],[11,13],[14,16],[17,19]]
}

var Msg = {
		//系统提示
		sys_remaind:'系统提示',
		sys_remaind1:'提示',
		sys_success:'操作成功！',
		sys_err:'操作失败，请重试！',
		sys_confirm:'确认',
		sys_deleate:'您确认想要删除记录吗？',
		sys_warn:'警告',
		sys_add_success:'新增成功!',
		sys_opreat:'操作提示',
		sys_errmeage:'错误提示',
		sys_select_one:'请选中一行！',
		sys_confirm1:'确认对话框',
		sys_confirm2:'确认提示',
		sys_save_success: '保存成功',
		sys_delete_success: '删除成功',
		sys_only_select_one: '只能选择一条数据',
		sys_pls_select: '请选择',
		weightTemplate_eq_weight:'开始重量必须小于等于结束重量',
		weightTemplate_status_invalid: '失效',
		weightTemplate_status_draft: '草稿',
		weightTemplate_status_valid: '生效',
		//账单模板
		accountTemplate_setDefaultValue: "设置默认值",
		accountTemplate_cannot_setDefaultValue: "不能设置默认值",
		accountTemplate_not_null_defaultValue: "不能设置默认值",
		accountTemplate_not_null_defaultValue_mappingField: "映射字段与默认值不能同时为空",
		accountTemplate_not_null_file: "文件不能为空",
		accountTemplate_file_max_out: "文件大小不能超过2MB"
	}


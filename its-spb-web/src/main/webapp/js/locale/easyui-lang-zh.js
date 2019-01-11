if ($.fn.pagination){
	$.fn.pagination.defaults.beforePageText = '第';
	$.fn.pagination.defaults.afterPageText = '共{pages}页';
	$.fn.pagination.defaults.displayMsg = '显示{from}到{to},共{total}记录';
}
if ($.fn.datagrid){
	$.fn.datagrid.defaults.loadMsg = '正在处理，请稍待。。。';
}
if ($.fn.treegrid && $.fn.datagrid){
	$.fn.treegrid.defaults.loadMsg = $.fn.datagrid.defaults.loadMsg;
}
if ($.messager){
	$.messager.defaults.ok = '确定';
	$.messager.defaults.cancel = '取消';
}
if ($.fn.validatebox){
	$.fn.validatebox.defaults.missingMessage = '该输入项为必输项';
	$.fn.validatebox.defaults.rules.email.message = '请输入有效的电子邮件地址';
	$.fn.validatebox.defaults.rules.url.message = '请输入有效的URL地址';
	$.fn.validatebox.defaults.rules.length.message = '输入内容长度必须介于{0}和{1}之间';
	$.fn.validatebox.defaults.rules.remote.message = '请修正该字段';
	$.fn.validatebox.defaults.rules.maxLength.message = '输入内容长度必须小于等于{0}个字符';
}
if ($.fn.numberbox){
	$.fn.numberbox.defaults.missingMessage = '该输入项为必输项';
}
if ($.fn.combobox){
	$.fn.combobox.defaults.missingMessage = '该输入项为必输项';
}
if ($.fn.combotree){
	$.fn.combotree.defaults.missingMessage = '该输入项为必输项';
}
if ($.fn.combogrid){
	$.fn.combogrid.defaults.missingMessage = '该输入项为必输项';
}
if ($.fn.calendar){
	$.fn.calendar.defaults.weeks = ['日','一','二','三','四','五','六'];
	$.fn.calendar.defaults.months = ['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月'];
}
if ($.fn.datebox){
	$.fn.datebox.defaults.currentText = '今天';
	$.fn.datebox.defaults.cleanText = '清空';
	$.fn.datebox.defaults.closeText = '关闭';
	$.fn.datebox.defaults.okText = '确定';
	$.fn.datebox.defaults.missingMessage = '该输入项为必输项';
	$.fn.datebox.defaults.formatter = function(date){
		var y = date.getFullYear();
		var m = date.getMonth()+1;
		var d = date.getDate();
		return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
	};
	$.fn.datebox.defaults.parser = function(s){
		if (!s) return new Date();
		var ss = s.split('-');
		var y = parseInt(ss[0],10);
		var m = parseInt(ss[1],10);
		var d = parseInt(ss[2],10);
		if (!isNaN(y) && !isNaN(m) && !isNaN(d)){
			return new Date(y,m-1,d);
		} else {
			return new Date();
		}
	};
}
if ($.fn.datetimebox && $.fn.datebox){
	$.extend($.fn.datetimebox.defaults,{
		currentText: $.fn.datebox.defaults.currentText,
		closeText: $.fn.datebox.defaults.closeText,
		okText: $.fn.datebox.defaults.okText,
		missingMessage: $.fn.datebox.defaults.missingMessage
	});
}
if ($.fn.datetimespinner){
	$.fn.datetimespinner.defaults.selections = [[0,4],[5,7],[8,10],[11,13],[14,16],[17,19]]
}

//一下部门内容需要提供翻译资料
var Msg = {
		//系统提示
		sys_index:'首页',
		sys_remaind:'系统提示',
		sys_remaind1:'提示',
		sys_success:'操作成功！',
		sys_err:'操作失败！',
		sys_confirm:'确认',
		sys_deleate:'您确认想要删除记录吗？',
		sys_warn:'警告',
		sys_add_success:'新增成功!',
		sys_opreat:'操作提示',
		sys_errmeage:'错误提示',
		sys_select_one:'请选中一行！',
		sys_confirm1:'确认对话框',
		sys_confirm2:'确认提示',
		sys_logout:'您确定要退出登录吗?',
		sys_error: '系统异常,请重试',
		
		
		agent_check_date: '必须为日期格式如2017-01-01',
		agent_check_date_contract:'合约开始日期不能大于结束日期',
		agent_check_time: '必须为时间格式如00:00',
		agent_check_declared: '申报价值只能输入正整数',
		agent_check_volume: '体积系数只能输入正整数',
		agent_check_weight_limit: '重量限制只能输入正整数',
		agent_exist: '该代理服务简称已存在',
		agent_attribute_exist: '该代理服务已存在特殊属性',
		agent_product_required: '适用产品必选一项',
		agent_goods_required: '货物类型必选一项',
		agent_payment_required: '付款方式必选一项',
		agent_attached_required: '附加服务必选一项',
		agent_customer_required: '客户类型必选一项',
		agent_declared_compare: '申报价值的开始数量不能大于结束数量',
		
		//系统管理
		sys_delete_txt1:'删除成功！',
		sys_delete_txt2:'删除失败！',
		sys_delete_txt3:'请选择要删除的记录',
		sys_update_txt:'更新成功！',
		sys_update_txt1:'更新失败，请重试！',
		sys_update_txt2:'配置值为空',
		sys_save_txt:'保存成功！',
		sys_save_txt2:'存在重复配置编码，保存失败！',
		sys_delete_txt1:'删除成功！',
		sys_no_permissions_txt1:'没有权限！',
		sys_delete_txt3:'请选择要删除的记录',
		sys_update_txt:'更新成功！',
		sys_update_txt1:'更新失败，请重试！',
		sys_save_txt3:'保存失败，请重试！',
		frequency_10:'请至少选中一行记录！',
		frequency_11:'请选中一行记录！',
		//用户管理
		sys_userMgr_04:'系统中已存在相同的用户登录ID（工号）',
		sys_user_code:'员工工号',
		sys_user_name:'员工姓名',
		sys_user_role:'角色',
		sys_user_create_tm:'创建时间',
		sys_save_success: '保存成功',
		sys_delete_success: '删除成功',
		sys_only_select_one: '只能选择一条数据',
		set_associated_roles: '设置关联角色',
		btn_role_set_menu:'设置关联权限',
		sys_pls_select: '请选择',
		
		//重量段模板
		weightTemplate_eq_weight:'开始重量必须小于等于结束重量',
		weightTemplate_status_invalid: '失效',
		weightTemplate_status_draft: '草稿',
		weightTemplate_status_valid: '生效',
		weightTemplate_not_exist:'重量段模板不存在',
		weightTemplate_del_not_draft: '只有草稿状态才可以进行删除',
		weightTemplate_syserror: '系统异常',
		weightTemplate_not_null_id_status: 'ID与状态不能为空',
		weightTemplate_not_exist: '重量段模板不存在',
		weightTemplate_updateinvalid: '不能操作失效的数据',
		weightTemplate_status_error: '状态异常',
		weightTemplate_exist: '代理服务与货物类型不能重复',
		weightTemplate_update_draft: '只有草稿状态才能修改',
		weightTemplate_id_not_null:'重量模板ID不能为空',
		weightTemplate_start_end_not_null:'开始重量与结束重量不能为空',
		weightTemplate_tmplid_not_null: '重量模板ID不能为空',
		weightTemplate_detail_exist: '重量段标题不能重复，并且重量段不能重叠',
		weightTemplate_valid2invalid:'状态为草稿只能转为生效',
		weightTemplate_draft2valid:'状态为生效只能转为失效',
		weightTemplate_eq_greater_zero: '必须大于0',
		weightTemplate_eq_date_out_six_month: '时间间隔不能超过半年',
		sys_name_code:'系统名称',
		//角色管理
		sys_role_name:'角色名称',
		//菜单
		sys_menu_name:'菜单名称',
		sys_menu_type:'类型',
		sys_menu_type_menu:'菜单',
		sys_menu_parent_menu:'上级菜单',
		sys_menu_type_button:'按钮',
		
		//账单模板
		accountTemplate_setDefaultValue: "设置默认值",
		accountTemplate_cannot_setDefaultValue: "不能设置默认值",
		accountTemplate_not_null_defaultValue: "不能设置默认值",
		accountTemplate_not_null_defaultValue_mappingField: "映射字段与默认值不能同时为空",
		accountTemplate_not_null_file: "文件不能为空",
		accountTemplate_file_max_out: "文件大小不能超过2MB",
		accountTemplate_invalid_file_type: "目前只支持xlsx、xls、csv类型导入",
		accountTemplate_id_not_null: "ID不能为空",
		accountTemplate_not_exist: "账单模板不存在",
		accountTemplate_exist: "账单模板已经存在"
}




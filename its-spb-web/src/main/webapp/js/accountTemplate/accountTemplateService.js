$(function(){
        	//标准模板字段
        	var stdTmplFieldList = [];
        	function formatter(date){  
                var y = date.getFullYear();  
                var m = date.getMonth()+1;  
                var d = date.getDate();  
                var h = date.getHours();  
                var min = date.getMinutes();  
                var sec = date.getSeconds();  
                var str = y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d) +' '+(h<10?('0'+h):h)+':'+(min<10?('0'+min):min)+':'+(sec<10?('0'+sec):sec);  
                return str;  
            }  
            function parser(s){  
                if (!s) return new Date(); 
                var date = new Date(s);
                if (isNaN(date.getTime())) {
                	$(this).datebox("setValue", "");
                	return new Date();
                }
                return date;
            }  
            
            $("#uploadForm").form({
            	onSubmit: function() {
            		var $fileInput =  $("#upload_file");
            		if (navigator.userAgent.indexOf("MSIE") < 0){
            			var file =$fileInput[0].files[0];
                		if (!file) {
                			return false;
                		}
                		if (file.size > _FILE_MAX_SIZE) {
                			$.messager.alert("提示信息", "文件大小不能超过2MB");
                			return false;
                		}
            		}
            		if (!$fileInput.val()) {
            			$.messager.alert("提示信息", "文件不能为空");
            			return;
            		}
            		var id = "uploader-frame";
            		var iframe = $('<iframe id="'+id+'" name="'+id+'" style="display:none;">');
            		var url = $(this).attr('action');
            		$(this).attr('target', id).append(iframe).attr('action', url);
            	}
            });
            $("#search_createTmStart").datetimebox({
                formatter: formatter,
                parser: parser
            });
            $("#search_createTmEnd").datetimebox({
                formatter: formatter,
                parser: parser
            });
            
            
            $("#edit_tmplType").combobox({
    			valueField : 'stdTmplId',  
                textField : 'stdTmplName',
                panelHeight:'auto',
                editable: false,
                onSelect: function(record){
                	var id = record.stdTmplId;
                	$.ajax({
                    	url: "${ctx}/accountTemplate/selectStdTmplFieldByStdId",
                    	method: "GET",
                    	data:{
                    		stdTmplId: record.stdTmplId
                    	},
                    	success: function(data){
                    		createMappingTable(data);
                    	}
                    });
                }
    		});
            var buildParams = function(){
            	var params = {};
            	params.tmplName = $("#edit_tmplName").val();
            	params.acctTmplId = $("#edit_acctTmplId").val();
            	params.stdTmplId = $("#edit_tmplType").combobox("getValue");
            	params.filePath = $("#file_path").val();
            	var mappingList = [];
            	$("#mappingTable tbody .mapping_row").each(function(index, row){
            		var $row = $(row);
            		var mappingFieldName = $row.find("input[name='mapping_field']").val();
            		if (mappingFieldName === _SET_DEFAULT_VALUE) {
            			mappingFieldName = "";
            		}
            		var defaultValue = $row.find("input[name='defualtValue']").val();
            		mappingList.push({
            			stdTmplFieldId: $row.attr("id"),
            			mappingFieldName: mappingFieldName,
            			defaultValue: defaultValue
            		});
            	});
            	params.mappingList = mappingList;
            	return params;
            }
            
            var clearForm = function() {
            	$("#mappingTable").find("tbody").empty();
            	$("#edit_tmplName").val("");
            	$("#edit_acctTmplId").val("");
            	var $file = $("#upload_file");
            	$file.after($file.clone().val(""));      
            	$file.remove();  
            	mappingList= [];
            	$("#file_path").val("");
            	$("#edit_tmplType").combobox("setValue", "");
            }
            
            var isStrEmpty = function(val){
            	return val === undefined || val === null || val.trim().length === 0;
            }
            $.extend($.fn.validatebox.defaults.rules, {
                mapping_not_empty: {
            		validator: function(value,param){
            			var inptuVal = $(param[0]).val();
            			return !(isStrEmpty(value) && isStrEmpty(inptuVal));
            		},
            		message: '映射字段与默认值不能同时为空'
                }
            });
            $.extend($.fn.validatebox.defaults.rules, {
                defaultValue_required: {
            		validator: function(value,param){
            			if ($(param[0]).length === 0) {
            				return true;
            			}
            			var inptuVal = $(param[0]).val();
            			return !isStrEmpty(inptuVal);
            		},
            		message: '默认值不能为空'
                }
            });
            $.extend($.fn.validatebox.defaults.rules, {
                defaultValue_rule: {
            		validator: function(value,param){
            			return !(value === defaultOption.text && param[0] !== "1");
            		},
            		message: '不能设置默认项'
                }
            });
            $("#addTmplWin").window({
            	iconCls:'icon-save',
            	modal:true,
            	minimizable:false,
            	resizable:false,
            	closed:true,
            	onClose: clearForm,
            	onOpen: function(){
            		$("#upload_file").on("change", function(){
                    	var val = $(this).val();
                    	if (val !== undefined && val.length != 0) {
                    		$("#uploadForm").submit();
                    	}
                    });
            	}
            });
            
            var createMappingTable = function(data) {
            	var $dataSpace = $("#mappingTable").find("tbody");
            	$dataSpace.empty();
            	$.each(data, function(index, item){
            		var $row = $("<tr>").attr("id", item.stdFieldId).addClass("mapping_row");
            		var $seqCell = $("<td class='mapping_seq'>").text(index);
            		var $nameCell = $("<td class='mapping_name'>").text(item.fieldName);
            		var $fieldCell = $("<td class='mapping_field'>");
            		var $defaultCell = $("<td class='mapping_default'>");
            		var $selectInput = $("<input name=\"mapping_field\" class=\"easyui-combobox\">");
            		var selectConfig = {
                   		valueField : 'value',  
                        textField : 'text',
                        panelHeight:'auto',
                        data: mappingList,
                        validType: "defaultValue_rule['" + item.ifDefualt + "']",
                        onSelect: function(record){
                        	/*当选择设置默认值 选项时，启动默认值验证, 否则清除验证*/
                       		if (record.value === "-1" && item.ifRequired === 1 && item.ifDefualt === 1) {
                       			var $input= $defaultCell.find("input[name='defualtValue']");
                       			$input.validatebox("enableValidation");
                       			$input.validatebox("validate");
                       		} else if (record.value !== "-1" && item.ifRequired === 1 && item.ifDefualt === 1){
                       			$defaultCell.find("input[name='defualtValue']").validatebox("disableValidation");
                       		}
                        },
                        
                   	};
            		$fieldCell.append($selectInput);
            		var $stdFileIdInput = $("<input type='hidden' name='stdFieldId'>").val(item.stdFieldId);
            		$fieldCell.append($stdFileIdInput);
            		if (item.ifRequired === 1) {
            			$nameCell.addClass("required_right");
            			selectConfig.required = true;
            		}
            		$row.append($seqCell).append($nameCell).append($fieldCell).append($defaultCell);
            		$dataSpace.append($row);
            		$selectInput.combobox(selectConfig);
            		if (item.ifDefualt === 1) {
            			var $defaultInput = $("<input name='defualtValue' class='easyui-validatebox'/>");
            			//validType
            			$defaultInput.validatebox({
            				required: true,
            				novalidate: true
            			});
            			$defaultCell.append($defaultInput);
            		}
            	});
            	
            	var acctTmplId = $("#edit_acctTmplId").val();
            	if (!isStrEmpty(acctTmplId)) {
            		var templatePath = $("#file_path").val();
            		setMappingValues(templatePath, acctTmplId);
            	}
            	
            	
            }
            var setMappingValues = function(templatePath, acctTmplId) {
            	$.ajax({
            		url: "${ctx}/accountTemplate/selectMappingInfo",
            		type: "GET",
            		dataType: "json",
            		data:{
            			templatePath: templatePath,
            			acctTmplId: acctTmplId
            		},
            		success: function(data) {
            			if (data.errorMsg) {
            				$.mssage.error("系统异常");
            				return;
            			}
            			uploadCallback(templatePath, data.titleList);
            			$.each(data.mappingList, function(index, item) {
            				var $row = $("#" + item.stdTmplFieldId);
            				var selectValue;
            				if (isStrEmpty(item.mappingFieldName) && !isStrEmpty(item.defaultValue)) {
            					selectValue = _SET_DEFAULT_VALUE;
            				} else {
            					selectValue = item.mappingFieldName;
            				}
            				$row.find(".mapping_field .easyui-combobox").combobox("setValue", selectValue);
            				$row.find(".mapping_default input[name='defualtValue']").val(item.defaultValue);
            			});
            		}
            	});
            }
            $.ajax({
            	url: "${ctx}/accountTemplate/selectAllStandardTemplate",
            	type: "GET",
            	success: function(data){
            		$("#edit_tmplType").combobox("loadData", data);
            	}
            });
            var disableBtns = function(isDisable) {
            	var btns = ["editTmpl_btn", "delTmpl_btn"];
            	$.each(btns, function(index, btn){
            		$("#" + btn).linkbutton(isDisable ? "disable" : "enable");
            	});
            }
            $('#tmpl_table').datagrid({
                url: "${ctx}/accountTemplate/selectAccountTemplate",
                pageList: [10,20,50,100],
                pageSize: 20,
                rownumbers:true,
                pagination:true,
                remoteSort:true,
                checkOnSelect:false,
                selectOnCheck:false,
                fit: true,
                singleSelect:true,
                toolbar: '#toolbar',
                columns:[[
                        {field: 'tmplName', title: '帐单名称', width: 100, align:'center'}, //重量段名称
                        {field: 'createBy', title: '创建人', width: 100, align:'center'}, //创建人
                        {field: 'createTm', title: '创建时间', width: 180, align:'center', formatter: function(value){ //创建时间
                            return new Date(value - new Date().getTimezoneOffset() * 60 * 1000).toLocaleString();
                        }},
                        {field: 'updateBy', title: '修改人', width: 100, align:'center'}, //修改人
                        {field: 'updateTm', title: '修改时间', width: 180, align:'center', formatter: function(value){ //修改时间
                            return new Date(value - new Date().getTimezoneOffset() * 60 * 1000).toLocaleString();
                        }},
                        {field: 'stdTmplId', title: 'stdTmplId', width: 100, align:'center', hidden: true},
                        {field: 'acctTmplId', title: 'acctTmplId', width: 100, align:'center', hidden: true},
                        {field: 'templatePath', title: 'templatePath', width: 100, align:'center', hidden: true}
                ]],
                onLoadSuccess: function (data) {
                    if(data.total=='0'){ // 查询无记录时提醒
                        $("#not_exist").show();
                    }else{
                        $("#not_exist").hide();
                    }
                },
                onSelect: function(index,row) {
                	disableBtns(false);
                },
                onBeforeLoad: function(){
                	disableBtns(true);
                },
                onUnselect: function(){
                	disableBtns(true);
                }
            });
            
            $("#delTmpl_btn").click(function(){
            	if ($(this).linkbutton("options").disabled) {
            		return;
            	}
            	var rowData = $("#tmpl_table").datagrid("getSelected");
            	$.messager.confirm("确认信息", "是否确认删除？", function(r){
            		if (!r) {
            			return;
            		}
            		$.ajax({
                        url: "${ctx}/accountTemplate/deleteStandardTemplate",
                        type: "POST",
                        dataType: "json",
                        data: {
                        	acctTmplId: rowData.acctTmplId
                        },
                        success:function(data){
                            if (data.status === "Y") {
                                $.messager.alert(Msg.sys_remaind, "删除成功");
                                $("#addTmplWin").window("close");
                                $('#tmpl_table').datagrid("reload");
                            } else {
                                $.messager.alert(Msg.sys_remaind, data.msg);
                            }
                        }
                   });
            	});
            });
            
            $("#reset_btn").click(function(){
            	$("#search_form").form("clear");
            });
            
            $("#search_btn").click(function(){
            	var params = $.serializeObject($("#search_form"));
                $('#tmpl_table').datagrid("load",params);
            });
            $("#addTmpl_btn").click(function(){
                $("#addTmplWin").window("open");
            });
            $("#cancel_btn").click(function(){
            	$("#addTmplWin").window("close");
            });
            $("#editTmpl_btn").click(function(){
            	if ($(this).linkbutton("options").disabled) {
            		return;
            	}
            	var rowData = $("#tmpl_table").datagrid("getSelected");
            	$("#edit_tmplName").val(rowData.tmplName);
            	$("#edit_tmplName").validatebox("validate");
            	$("#edit_acctTmplId").val(rowData.acctTmplId);
            	$("#file_path").val(rowData.templatePath);
            	$("#edit_tmplType").combobox("select", rowData.stdTmplId);
            	$("#addTmplWin").window("open");
            });
            $("#saveTmpl_btn").click(function(){
            	if (!$("#edit_tmplName").validatebox("isValid") || isStrEmpty($("#edit_tmplType").combobox("getValue"))) {
            		return false;
            	}
            	
            	var isValid = $("#mappingForm").form("validate");
            	if (!isValid) {
            		return false;
            	}
            	var params = buildParams();
            	params.mappingList = JSON.stringify(params.mappingList);
            	$.ajax({
                    url: "${ctx}/accountTemplate/updateStandardTemplate",
                    type: "POST",
                    dataType: "json",
                    data: params,
                    success:function(data){
                        if (data.status === "Y") {
                            $.messager.alert(Msg.sys_remaind, Msg.sys_save_success);
                            $("#addTmplWin").window("close");
                            $('#tmpl_table').datagrid("reload");
                        } else {
                            $.messager.alert(Msg.sys_remaind, data.msg);
                        }
                    }
               });
            });
        });
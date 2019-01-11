/**
 * 
 */
function myformatter(date){
		var y = date.getFullYear();
		var m = date.getMonth()+1;
		var d = date.getDate();
		return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
}

function myparser(s){
	if (!s) return new Date();
	var ss = (s.split('-'));
	var y = parseInt(ss[0],10);
	var m = parseInt(ss[1],10);
	var d = parseInt(ss[2],10);
	if (!isNaN(y) && !isNaN(m) && !isNaN(d)){
		return new Date(y,m-1,d);
	} else {
		return new Date();
	}
}

function checkDate(RQ) {
    var date = RQ;
    var result = date.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/);
    if (result == null)
        return false;
    var d = new Date(result[1], result[3] - 1, result[4]);
    return (d.getFullYear() == result[1] && (d.getMonth() + 1) == result[3] && d.getDate() == result[4]);

}
function checkDateTime(strValue) {
		var objRegExp = /^\d{4}[-](\d{2})[-]\d{2}\s*\d{2}[:]\s*\d{2}[:]\s*\d{2}$/;
		if(!objRegExp.test(strValue)){
		return false;
		}
		else{
		return true;
		}

}

//弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
function msgShow(title, msgString, msgType) {
	$.messager.alert(title, msgString, msgType);
}

/**
 * 自适应窗口大小
 * @param el
 */
function _doResize(el){
	  el.datagrid('resize', {
	    	width: document.body.clientWidth * 1,
	    	height: document.body.clientHeight * 0.98
    	   });
}

/**
 * 分页处理
 * @param data
 * @returns
 */
function pagerFilter(data){
	if (typeof data.length == 'number' && typeof data.splice == 'function'){	// is array
		data = {
			total: data.length,
			rows: data
		};
	}
	var dg = $(this);
	var opts = dg.datagrid('options');
	var pager = dg.datagrid('getPager');
	pager.pagination({
		onSelectPage:function(pageNum, pageSize){
			opts.pageNumber = pageNum;
			opts.pageSize = pageSize;
			pager.pagination('refresh',{
				pageNumber:pageNum,
				pageSize:pageSize
			});
			dg.datagrid('loadData',data);
		}
	});
	if (!data.originalRows){
		data.originalRows = (data.rows);
	}
	var start = (opts.pageNumber-1)*parseInt(opts.pageSize);
	var end = start + parseInt(opts.pageSize);
	data.rows = (data.originalRows.slice(start, end));
	return data;
}


$.extend($.fn.datagrid.methods, {
    fixRownumber : function (jq) {
        return jq.each(function () {
            var panel = $(this).datagrid("getPanel");
            //获取最后一行的number容器,并拷贝一份
            var clone = $(".datagrid-cell-rownumber", panel).last().clone();
            //由于在某些浏览器里面,是不支持获取隐藏元素的宽度,所以取巧一下
            clone.css({
                "position" : "absolute",
                left : -1000
            }).appendTo("body");
            var width = clone.width("auto").width();
            //默认宽度是25,所以只有大于25的时候才进行fix
            if (width > 25) {
                //多加5个像素,保持一点边距
                $(".datagrid-header-rownumber,.datagrid-cell-rownumber", panel).width(width + 5);
                //修改了宽度之后,需要对容器进行重新计算,所以调用resize
                $(this).datagrid("resize");
                //一些清理工作
                clone.remove();
                clone = null;
            } else {
                //还原成默认状态
                $(".datagrid-header-rownumber,.datagrid-cell-rownumber", panel).removeAttr("style");
            }
        });
    }
});

$.extend($.fn.validatebox.defaults.rules, {
    idcard: {// 验证身份证
        validator: function (value) {
            return /^\d{15}(\d{2}[A-Za-z0-9])?$/i.test(value);
        },
        message: '身份证号码格式不正确'
    },
    maxLength: {    
        validator: function(value, param){    
        	var len = value.length;
            return len <= param[0];    
        },    
        message: '输入内容长度必须小于等于{0}个字符'   
    },
    minLength: {
        validator: function (value, param) {
        	var len = value.length;
            return len >= param[0];
        },
        message: '请输入至少{0}个字符'
    },
    length: { validator: function (value, param) {
        var len = value.length;
        return len >= param[0] && len <= param[1];
    },
        message: "输入内容长度必须介于{0}和{1}之间."
    },
    phone: {// 验证电话号码
        validator: function (value) {
            return /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value);
        },
        message: '格式不正确,请使用下面格式:020-88888888'
    }, 
	chinesePhone: {// 验证电话号码支持前缀+86|#
        validator: function (value) {
            return /^(\+86|#)?((0\d{2,3})-?)(\d{7,8})$/i.test(value);
        },
        message: '请输入正确的座机号'
    },
	mobileAndPhone: {// 座机号与手机号同时验证
        validator: function (value) {
            return /(^(0|86|17951)?(13[0-9]|15[012356789]|17[0678]|18[0-9]|14[57])[0-9]{8}$)|(^(\+86|#)?((0\d{2,3})-?)(\d{7,8})$)/i.test(value);
        },
        message: '请输入正确的座机号或手机号'
    },
    mobile: {// 验证手机号码
        validator: function (value) {
            return /^((1[3,5,8][0-9])|(14[5,7])|(17[0,6,7,8]))\d{8}$/i.test(value);
        },
        message: '手机号码格式不正确'
    },
    mobileCheck: {// 验证手机号码
        validator: function (value,mobileType) {
        	var phoneCode;
        	if(mobileType[0]==0){
        		phoneCode = $("#phoneCodeAdd").combobox("getValue");
        	}else if(mobileType[0]==1){
        		phoneCode = $("#phoneCodeUpdate").combobox("getValue");
        	}
        	if(phoneCode=="+86"){
    			return /^((1[3,5,8][0-9])|(14[5,7])|(17[0,6,7,8]))\d{8}$/i.test(value);
    		}else if(phoneCode=="+852"){
    			return /^(([2,3,5,8]\d{7})|([2,3,5,8]\d{7}?\-[0-9]{1,4}))$/i.test(value);
    		}
        },
        message: '手机号码格式不正确'
    },
    positive_int:{  
        validator:function(value){  
             return /^[1-9]\d*$/.test(value);  
        },  
        message:'只能输入正数'  
    }, 
    intOrFloat: {// 验证整数或小数
        validator: function (value) {
            return /^\d+(\.\d+)?$/i.test(value);
        },
        message: '请输入数字，并确保格式正确'
    },
    currency: {// 验证货币
        validator: function (value) {
            return /^\d+(\.\d+)?$/i.test(value);
        },
        message: '货币格式不正确'
    },
    qq: {// 验证QQ,从10000开始
        validator: function (value) {
            return /^[1-9]\d{4,9}$/i.test(value);
        },
        message: 'QQ号码格式不正确'
    },
    integer: {// 验证整数 可正负数
        validator: function (value) {
            //return /^[+]?[1-9]+\d*$/i.test(value);

            return /^([+]?[0-9])|([-]?[0-9])+\d*$/i.test(value);
        },
        message: '请输入整数'
    },
    age: {// 验证年龄
        validator: function (value) {
            return /^(?:[1-9][0-9]?|1[01][0-9]|120)$/i.test(value);
        },
        message: '年龄必须是0到120之间的整数'
    },

    chinese: {// 验证中文
        validator: function (value) {
            return /^[\Α-\￥]+$/i.test(value);
        },
        message: '请输入中文'
    },
    english: {// 验证英语
        validator: function (value) {
            return /^[A-Za-z]+$/i.test(value);
        },
        message: '请输入英文'
    },
    unnormal: {// 验证是否包含空格和非法字符
        validator: function (value) {
            return /.+/i.test(value);
        },
        message: '输入值不能为空和包含其他非法字符'
    },
    username: {// 验证用户名
        validator: function (value) {
            return /^[a-zA-Z][a-zA-Z0-9_]{5,15}$/i.test(value);
        },
        message: '用户名不合法（字母开头，允许6-16字节，允许字母数字下划线）'
    },
    radio: {
        validator: function (value, param) {
            var frm = param[0], groupname = param[1], ok = false;
            $('input[name="' + groupname + '"]', document[frm]).each(function () { //查找表单中所有此名称的radio
                if (this.checked) { ok = true; return false; }
            });

            return ok;
        },
        message: '需要选择一项！'
    },
    faxno: {// 验证传真
        validator: function (value) {
            //            return /^[+]{0,1}(\d){1,3}[ ]?([-]?((\d)|[ ]){1,12})+$/i.test(value);
            return /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value);
        },
        message: '传真号码不正确'
    },
    zip: {// 验证邮政编码
        validator: function (value, param) {
        	var str = value.replace(/(^\s*)|(\s*$)/g,"");
        	str = str.toUpperCase(str);
        	$(param[0]).val(str);
        	return /^[^\u4e00-\u9fa5]{0,}$/i.test(str); ///^[A-Za-z0-9]+$/i.test(str)
        },  
        message: '邮政编码请输入英文字符'
    },
    /*
    sendaddress: {// 验证寄件人地址
        validator: function (value) {
        	var str = value.replace(/(^\s*)|(\s*$)/g,"");
        	return /^[^\u4e00-\u9fa5]{0,}$/i.test(str);
        },
        message: '寄件人地址请输入英文字符'
    },
    */
    networkcode: {// 验证网点代码
        validator: function (value) {
        	var str = value.replace(/(^\s*)|(\s*$)/g,"");
        	return /^[^\u4e00-\u9fa5]{0,}$/i.test(str);
        },
        message: '网点代码请输入英文字符'
    },   
    consigneeEmpCodes: {// 验证收件员工号
        validator: function (value) {
        	var str = value.replace(/(^\s*)|(\s*$)/g,"");
        	return /^[^\u4e00-\u9fa5]{0,}$/i.test(str);
        },
        message: '收件员工号请输入英文字符'
    },   
    ip: {// 验证IP地址
        validator: function (value) {
            return /d+.d+.d+.d+/i.test(value);
        },
        message: 'IP地址格式不正确'
    },
    name: {// 验证姓名，可以是中文或英文
        validator: function (value) {
            return /^[\Α-\￥]+$/i.test(value) | /^\w+[\w\s]+\w+$/i.test(value);
        },
        message: '请输入姓名'
    },
    date: {// 验证姓名，可以是中文或英文
        validator: function (value) {
            //格式yyyy-MM-dd或yyyy-M-d
            return /^(?:(?!0000)[0-9]{4}([-]?)(?:(?:0?[1-9]|1[0-2])\1(?:0?[1-9]|1[0-9]|2[0-8])|(?:0?[13-9]|1[0-2])\1(?:29|30)|(?:0?[13578]|1[02])\1(?:31))|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)([-]?)0?2\2(?:29))$/i.test(value);
        },
        message: '清输入合适的日期格式'
    },
    datetime :{
    	validator: function (value) {
    		//可以匹配如下 12:30 PM
    		//2004-02-29
    		//2004/3/31 02:31:35
    		//2011-07-25 15:44:45
    		//2004/3/31 02:31:35 AM
    		return /^(?:(?:(?:(?:(?:(?:1[6-9]|[2-9]\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00)))(\/|-)(?:0?2\1(?:29)))|(?:(?:(?:1[6-9]|[2-9]\d)?\d{2})(\/|-)(?:(?:(?:0?[13578]|1[02])\2(?:31))|(?:(?:0?[1,3-9]|1[0-2])\2(29|30))|(?:(?:0?[1-9])|(?:1[0-2]))\2(?:0?[1-9]|1\d|2[0-8])))))\s(?:([0-1]\d|2[0-3]):[0-5]\d:[0-5]\d)$/m.test(value);
    	},
        message: '清输入合适的日期时间格式'
    },
    msn: {
        validator: function (value) {
            return /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(value);
        },
        message: '请输入有效的msn账号(例：abc@hotnail(msn/live).com)'
    },
    coOrderNo: {
    	validator: function (value) {
    		return /^[a-zA-Z0-9-_]+$/.test(value);
    	},
    	message: '【客户单号】格式错误，只能包含数字和字母'
    },
    same: {
        validator: function (value, param) {
            if ($("#" + param[0]).val() != "" && value != "") {
                return $("#" + param[0]).val() == value;
            } else {
                return true;
            }
        },
        message: '两次输入的密码不一致！'
    },
    contraNo: {
    	validator: function (value) {
    		return /^[A-Za-z0-9\-]+$/.test(value);
    	},
    	message: '【合同编号】格式错误，只能包含数字、字母和中横杠'
    },
    checkContractNo:{
    	validator : function(value,param){ 
    	      var flag;
              if(""!=$.trim(value)){
              	$.ajax({  
              		url : 'checkContractNo.do',  
              		type: "POST", 
              		data: {"contractNo":$.trim(value),"spFullName":$.trim($('#spFullName').val()),"sspFullName":$.trim($('#sspFullName').val())}, 
              		async: false,
              		dataType: "json",   
              		success : function(data) {
              			if (!data.result) {  
              				flag = true;      
              			}else{  
              				flag = false;  
              			}  
              		}  
              	});  
              }else{
              	flag = true;
              }
              if(flag){  
                  $("#contractNo").removeClass('validatebox-invalid');  
              }  
              return flag;  
        },  
    	message: '合同编号已经存在，请确认合同编号！'  
    },
    sppMonthlyCard:{
    	validator: function (value) {
    		return /^[0-9]+$/.test(value);
    	},
    	message: '【月结卡号】格式错误，只能包含数字'
    },
    checkMonthlyCard:{
        validator : function(value,param){  
            var flag;
            if(""!=$.trim(value)){
            	$.ajax({  
            		url : 'checkMonthlyCard.do',  
            		type: "POST", 
            		data: {"monthlyCard":$.trim(value),"cmId":$.trim($('#cmId2').val()),"spFullName":$.trim($('#spFullName').val()),"sspFullName":$.trim($('#sspFullName').val())}, 
            		async: false,
            		dataType: "json",   
            		success : function(data) {
            			if (!data.result) {  
            				flag = true;      
            			}else{  
            				flag = false;  
            			}  
            		}  
            	});  
            }else{
            	flag = true;
            }
            if(flag){  
                $("#spMonthlyCard1").removeClass('validatebox-invalid');  
                $("#spMonthlyCard2").removeClass('validatebox-invalid');  
            }  
            return flag;  
        },  
        message: '月结卡号已经存在，请确认月结卡号！' 
    },
    
    checkLoginName :{  
        validator : function(value,param){  
            var flag;
            if(""!=$.trim(value)){
            	$.ajax({  
            		url : 'checkLoginName.do',  
            		type: "POST", 
            		data: {"loginName":$.trim(value),"ssoUserId":$("#ssoUserId").val()}, 
            		async: false,
            		dataType: "json",   
            		success : function(data) {
            			if (!data.result) {  
            				flag = true;      
            			}else{  
            				flag = false;  
            			}  
            		}  
            	});  
            }else{
            	flag = true;
            }
            if(flag){  
                $("#loginname").removeClass('validatebox-invalid');  
            }  
            return flag;  
        },  
        message: '用户账号已经存在，请确认用户账号！'  
    },
    checkCoOrderNo :{  
    	validator : function(value,param){  
    		var flag;
    		if(""!=$.trim(value)){
    			$.ajax({  
    				url : 'checkCode.do',  
    				type: "POST", 
    				data: {"coOrderNo":$.trim(value)}, 
    				async: false,
    				dataType: "json",   
    				success : function(data) {
    					if (data!=""&&data!=null) {  
    						var cmCode = $.trim($("#CM_Code").val());
    						if(''==cmCode){
    							$("#CM_Code").val(data.CM_Code);
    							$("#cm_id").val(data.CM_ID_Shipper);
    							flag = true; 
    							$("#CM_Code").removeClass('validatebox-invalid');
    						}else if(data.CM_Code==cmCode){
    							flag = true; 
    						}else{
    							flag = false;
    						}
    					}else{  
    						flag = true;  
    					}  
    				}  
    			});  
    		}else{
    			flag = true;
    		}
    		if(flag){  
    			$("#CO_OrderNo").removeClass('validatebox-invalid');  
    		}  
    		return flag;  
    	},  
    	message: '客户单号不属于当前客户代码,请确认客户单号！'  
    },
    checkCoOrderNoSame :{  
    	validator : function(value,param){  
    		var flag;
    		if(""!=$.trim(value)){
    			$.ajax({  
    				url : 'getCheckCoOrder.do',  
    				type: "POST", 
    				data: {"coOrderNo":$.trim(value)}, 
    				async: false,
    				dataType: "text",   
    				success : function(data) {
    					if (data=="1") {  
    						flag = true;  
    					}else{  
    						flag = false;  
    					}  
    				}  
    			});  
    		}else{
    			flag = true;
    		}
    		if(flag){  
    			$("#CO_OrderNo").removeClass('validatebox-invalid');  
    		}  
    		return flag;  
    	},  
    	message: '客户单号与服务商单号重复,请确认客户单号！'  
    },
    checkCmCode :{  
    	validator : function(value,param){  
    		var flag = false;
    		if(""!=$.trim(value)){
    			$.ajax({  
    				url : 'checkedCmCode.do',  
    				type: "POST", 
    				data: {"cmCode":$.trim(value)}, 
    				async: false,
    				dataType: "text",   
    				success : function(data) {
    					if(data!=""&&data!=null){
    						$("#cm_id").val(data);
    						flag = true;
    	 				}else{
    	 					$("#cm_id").val("");
    	 					flag = false;
    	 				}
    				}  
    			});  
    		}else{
    			$("#cm_id").val("");
    			flag = true;
    		}
    		if(flag){  
    			$("#CM_Code").removeClass('validatebox-invalid');  
    		}  
    		return flag;  
    	},  
    	message: '客户代码不存在，请确认客户代码！'  
    },
    checkCity :{  
        validator : function(value,param){  
            var flag=false;
            if(""!=$.trim(value)){
            	var result=$('#idCy'+param[0]).combobox('getData');
            	for(var item in result){
    	    		if(result[item].CY_Name==($.trim(value))){
    	    			flag = true;
    	    			break;
    	    		}
    	    	}
            }else{
            	flag = true;
            }
            if(flag){  
                $("#idCy"+param[0]).removeClass('validatebox-invalid');  
            }  
            return flag;  
        },  
        message: '请输入或选择有效的城市！'  
    },
    checkUserName :{  
    	validator : function(value,param){  
    		var flag=false;
    		if(""!=$.trim(value)){
    			var result=$('#user_'+param[0]).combobox('getData');
    			for(var item in result){
    				if(result[item].codeName==($.trim(value))){
    					flag = true;
    					break;
    				}
    			}
    		}else{
    			flag = true;
    		}
    		if(flag){  
    			$("#user_"+param[0]).removeClass('validatebox-invalid');  
    		}  
    		return flag;  
    	},  
    	message: '请输入或选择正确的工号姓名！'  
    },
    checkCountry :{  
        validator : function(value, param){  
            var flag=false;
            if(""!=$.trim(value)){
            	var result=$('#'+param[0]).combobox('getData');
            	for(var item in result){
            		if(result[item].bcCode==($.trim(value))){
    	    			flag = true;
    	    			break;
    	    		}
    	    	}
            }else{
            	flag = true;
            }
            if(flag){  
                $('#'+param[0]).removeClass('validatebox-invalid');  
            }  
            return flag;  
        },  
        message: '请输入或选择有效的国家！'  
    },
    checkCountryName :{  
        validator : function(value, param){  
            var flag=false;
            if(""!=$.trim(value)){
            	var result=$('#'+param[0]).combobox('getData');
            	for(var item in result){
            		if(result[item].bldName==($.trim(value))){
    	    			flag = true;
    	    			break;
    	    		}
    	    	}
            }else{
            	flag = true;
            }
            if(flag){  
                $('#'+param[0]).removeClass('validatebox-invalid');  
            }  
            return flag;  
        },  
        message: '请输入或选择有效的国家！'  
    },
    checkPort :{  
    	validator : function(value, param){  
    		var flag=false;
    		if(""!=$.trim(value)){
    			var result=$('#'+param[0]).combobox('getData');
    			for(var item in result){
    				if(result[item].bldCode==($.trim(value))){
    					flag = true;
    					break;
    				}
    			}
    		}else{
    			flag = true;
    		}
    		if(flag){  
    			$('#'+param[0]).removeClass('validatebox-invalid');  
    		}  
    		return flag;  
    	},  
    	message: '口岸编辑错误'  
    },
    equaldDate: {  
        validator: function (value, param) {  
            var start = $(param[0]).datetimebox('getValue');  //获取开始时间    
            return value > start;                             //有效范围为当前时间大于开始时间    
        },  
        message: '结束日期应大于开始日期!'                     //匹配失败消息  
    } 
});

/**
$.extend($.fn.validatebox.defaults.rules, {
    CHS: {
        validator: function (value, param) {
            return /^[\u0391-\uFFE5]+$/.test(value);
        },
        message: '请输入汉字'
    },
    ZIP: {
        validator: function (value, param) {
            return /^[1-9]\d{5}$/.test(value);
        },
        message: '邮政编码不存在'
    },
    QQ: {
        validator: function (value, param) {
            return /^[1-9]\d{4,10}$/.test(value);
        },
        message: 'QQ号码不正确'
    },
    mobile: {
        validator: function (value, param) {
            return /^((\(\d{2,3}\))|(\d{3}\-))?13\d{9}$/.test(value);
        },
        message: '手机号码不正确'
    },
    loginName: {
        validator: function (value, param) {
            return /^[\u0391-\uFFE5\w]+$/.test(value);
        },
        message: '登录名称只允许汉字、英文字母、数字及下划线。'
    },
    safepass: {
        validator: function (value, param) {
            return safePassword(value);
        },
        message: '密码由字母和数字组成，至少6位'
    },
    equalTo: {
        validator: function (value, param) {
            return value == $(param[0]).val();
        },
        message: '两次输入的字符不一至'
    },
    number: {
        validator: function (value, param) {
            return /^\d+$/.test(value);
        },
        message: '请输入数字'
    },
    idcard: {
        validator: function (value, param) {
            return idCard(value);
        },
        message:'请输入正确的身份证号码'
    }
});
**/


/* 密码由字母和数字组成，至少6位 */
/**
var safePassword = function (value) {
    return !(/^(([A-Z]*|[a-z]*|\d*|[-_\~!@#\$%\^&\*\.\(\)\[\]\{\}<>\?\\\/\'\"]*)|.{0,5})$|\s/.test(value));
};

var idCard = function (value) {
    if (value.length == 18 && 18 != value.length) return false;
    var number = value.toLowerCase();
    var d, sum = 0, v = '10x98765432', w = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2], a = '11,12,13,14,15,21,22,23,31,32,33,34,35,36,37,41,42,43,44,45,46,50,51,52,53,54,61,62,63,64,65,71,81,82,91';
    var re = number.match(/^(\d{2})\d{4}(((\d{2})(\d{2})(\d{2})(\d{3}))|((\d{4})(\d{2})(\d{2})(\d{3}[x\d])))$/);
    if (re == null || a.indexOf(re[1]) < 0) return false;
    if (re[2].length == 9) {
        number = number.substr(0, 6) + '19' + number.substr(6);
        d = ['19' + re[4], re[5], re[6]].join('-');
    } else d = [re[9], re[10], re[11]].join('-');
    if (!isDateTime.call(d, 'yyyy-MM-dd')) return false;
    for (var i = 0; i < 17; i++) sum += number.charAt(i) * w[i];
    return (re[2].length == 9 || number.charAt(17) == v.charAt(sum % 11));
};

var isDateTime = function (format, reObj) {
    format = format || 'yyyy-MM-dd';
    var input = this, o = {}, d = new Date();
    var f1 = format.split(/[^a-z]+/gi), f2 = input.split(/\D+/g), f3 = format.split(/[a-z]+/gi), f4 = input.split(/\d+/g);
    var len = f1.length, len1 = f3.length;
    if (len != f2.length || len1 != f4.length) return false;
    for (var i = 0; i < len1; i++) if (f3[i] != f4[i]) return false;
    for (var i = 0; i < len; i++) o[f1[i]] = f2[i];
    o.yyyy = s(o.yyyy, o.yy, d.getFullYear(), 9999, 4);
    o.MM = s(o.MM, o.M, d.getMonth() + 1, 12);
    o.dd = s(o.dd, o.d, d.getDate(), 31);
    o.hh = s(o.hh, o.h, d.getHours(), 24);
    o.mm = s(o.mm, o.m, d.getMinutes());
    o.ss = s(o.ss, o.s, d.getSeconds());
    o.ms = s(o.ms, o.ms, d.getMilliseconds(), 999, 3);
    if (o.yyyy + o.MM + o.dd + o.hh + o.mm + o.ss + o.ms < 0) return false;
    if (o.yyyy < 100) o.yyyy += (o.yyyy > 30 ? 1900 : 2000);
    d = new Date(o.yyyy, o.MM - 1, o.dd, o.hh, o.mm, o.ss, o.ms);
    var reVal = d.getFullYear() == o.yyyy && d.getMonth() + 1 == o.MM && d.getDate() == o.dd && d.getHours() == o.hh && d.getMinutes() == o.mm && d.getSeconds() == o.ss && d.getMilliseconds() == o.ms;
    return reVal && reObj ? d : reVal;
    function s(s1, s2, s3, s4, s5) {
        s4 = s4 || 60, s5 = s5 || 2;
        var reVal = s3;
        if (s1 != undefined && s1 != '' || !isNaN(s1)) reVal = s1 * 1;
        if (s2 != undefined && s2 != '' && !isNaN(s2)) reVal = s2 * 1;
        return (reVal == s1 && s1.length != s5 || reVal > s4) ? -10000 : reVal;
    }
};
**/

function getCurrentDayString()
{
	    var mydate = new Date();
		var month = mydate.getMonth() + 1;
		month = month < 10 ? ("0" + month) : month;
		var dt = mydate.getDate();
		dt = dt < 10 ? ("0" + dt) : dt;
		var hh = mydate.getHours();
		hh = hh < 10 ? ("0" + hh) : hh;
		var mm = mydate.getMinutes();
		mm = mm < 10 ? ("0" + mm) : mm;
		var ss = mydate.getSeconds();
		ss = ss < 10 ? ("0" + ss) : ss;
//var dateStr = mydate.getFullYear() +"-"+mydate.getMonth()+"-"+mydate.getDate()+" "+mydate.getHours()+":"+mydate.getMinutes()+":"+mydate.getSeconds();
		var dateStr = mydate.getFullYear() +"-"+month+"-"+dt+" "+hh+":"+mm+":"+ss;
	  return dateStr;
}

/**
 * 加指定天日期
 * @param dateParameter
 * @param num
 * @returns {String}
 */
function addByTransDate(dateParameter, num) {
    var translateDate = "", dateString = "", monthString = "", dayString = "";
    translateDate = dateParameter.replace("-", "/").replace("-", "/");
    var newDate = new Date(translateDate);
    newDate = newDate.valueOf();
    newDate = newDate + num * 24 * 60 * 60 * 1000;
    newDate = new Date(newDate);
    //如果月份长度少于2，则前加 0 补位  
    if ((newDate.getMonth() + 1).toString().length == 1) {
       monthString = 0 + "" + (newDate.getMonth() + 1).toString();
    } else {
       monthString = (newDate.getMonth() + 1).toString();
    }
    //如果天数长度少于2，则前加 0 补位  
    if (newDate.getDate().toString().length == 1) {
       dayString = 0 + "" + newDate.getDate().toString();
    } else {
       dayString = newDate.getDate().toString();
    }
    dateString = newDate.getFullYear() + "-" + monthString + "-" + dayString;
    return dateString;
}

/**
 * 减指定天日期
 * @param dateParameter
 * @param num
 * @returns {String}
 */
function reduceByTransDate(dateParameter, num) {
    var translateDate = "", dateString = "", monthString = "", dayString = "";
    translateDate = dateParameter.replace("-", "/").replace("-", "/");
    var newDate = new Date(translateDate);
    newDate = newDate.valueOf();
    newDate = newDate - num * 24 * 60 * 60 * 1000;
    newDate = new Date(newDate);
    //如果月份长度少于2，则前加 0 补位  
    if ((newDate.getMonth() + 1).toString().length == 1) {
      monthString = 0 + "" + (newDate.getMonth() + 1).toString();
    } else {
      monthString = (newDate.getMonth() + 1).toString();
    }
    //如果天数长度少于2，则前加 0 补位  
    if (newDate.getDate().toString().length == 1) {
      dayString = 0 + "" + newDate.getDate().toString();
    } else {
      dayString = newDate.getDate().toString();
    }
    dateString = newDate.getFullYear() + "-" + monthString + "-" + dayString;
    return dateString;
}
 
//得到日期  主方法
function showTime(pdVal) {
    var trans_day = "";
    var cur_date = new Date();
    var cur_year = new Date().format("yyyy");
    var cur_month = cur_date.getMonth() + 1;
    var real_date = cur_date.getDate();
    cur_month = cur_month > 9 ? cur_month : ("0" + cur_month);
    real_date = real_date > 9 ? real_date : ("0" + real_date);
    eT = cur_year + "-" + cur_month + "-" + real_date;
    if (pdVal == 1) {
      trans_day = addByTransDate(eT, 1);
    }
    else if (pdVal == -1) {
      trans_day = reduceByTransDate(eT, 1);
    }
    else {
      trans_day = eT;
    }
   //处理
    return trans_day;
}


function convertCurrency(currencyDigits) {
	// Constants:
	 var MAXIMUM_NUMBER = 99999999999.99;
	 // Predefine the radix characters and currency symbols for output:
	 var CN_ZERO = "零";
	 var CN_ONE = "壹";
	 var CN_TWO = "贰";
	 var CN_THREE = "叁";
	 var CN_FOUR = "肆";
	 var CN_FIVE = "伍";
	 var CN_SIX = "陆";
	 var CN_SEVEN = "柒";
	 var CN_EIGHT = "捌";
	 var CN_NINE = "玖";
	 var CN_TEN = "拾";
	 var CN_HUNDRED = "佰";
	 var CN_THOUSAND = "仟";
	 var CN_TEN_THOUSAND = "万";
	 var CN_HUNDRED_MILLION = "亿";
	 var CN_SYMBOL = "";
	 var CN_DOLLAR = "元";
	 var CN_TEN_CENT = "角";
	 var CN_CENT = "分";
	 var CN_INTEGER = "整";
	 // Variables:
	 var integral; // Represent integral part of digit number.
	 var decimal; // Represent decimal part of digit number.
	 var outputCharacters; // The output result.
	 var parts;
	 var digits, radices, bigRadices, decimals;
	 var zeroCount;
	 var i, p, d;
	 var quotient, modulus;
	 // Validate input string:
	 currencyDigits = currencyDigits.toString();
	 // Normalize the format of input digits:
	 currencyDigits = currencyDigits.replace(/,/g, ""); // Remove comma delimiters.
	 currencyDigits = currencyDigits.replace(/^0+/, ""); // Trim zeros at the beginning.
	 // Assert the number is not greater than the maximum number.
	 // Process the coversion from currency digits to characters:
	 // Separate integral and decimal parts before processing coversion:
	 parts = currencyDigits.split(".");
	 if (parts.length > 1) {
	  integral = parts[0];
	  decimal = parts[1];
	  // Cut down redundant decimal digits that are after the second.
	  decimal = decimal.substr(0, 2);
	 }
	 else {
	  integral = parts[0];
	  decimal = "";
	 }
	 // Prepare the characters corresponding to the digits:
	 digits = new Array(CN_ZERO, CN_ONE, CN_TWO, CN_THREE, CN_FOUR, CN_FIVE, CN_SIX, CN_SEVEN, CN_EIGHT, CN_NINE);
	 radices = new Array("", CN_TEN, CN_HUNDRED, CN_THOUSAND);
	 bigRadices = new Array("", CN_TEN_THOUSAND, CN_HUNDRED_MILLION);
	 decimals = new Array(CN_TEN_CENT, CN_CENT);
	 // Start processing:
	 outputCharacters = "";
	 // Process integral part if it is larger than 0:
	 if (Number(integral) > 0) {
	  zeroCount = 0;
	  for (i = 0; i < integral.length; i++) {
	   p = integral.length - i - 1;
	   d = integral.substr(i, 1);
	   quotient = p / 4;
	   modulus = p % 4;
	   if (d == "0") {
	    zeroCount++;
	   }
	   else {
	    if (zeroCount > 0)
	    {
	     outputCharacters += digits[0];
	    }
	    zeroCount = 0;
	    outputCharacters += digits[Number(d)] + radices[modulus];
	   }
	   if (modulus == 0 && zeroCount < 4) {
	    outputCharacters += bigRadices[quotient];
	   }
	  }
	  outputCharacters += CN_DOLLAR;
	 }
	 // Process decimal part if there is:
	 if (decimal != "") {
	  for (i = 0; i < decimal.length; i++) {
	   d = decimal.substr(i, 1);
	   if (d != "0") {
	    outputCharacters += digits[Number(d)] + decimals[i];
	   }
	  }
	 }
	 // Confirm and return the final output string:
	 if (outputCharacters == "") {
	  outputCharacters = CN_ZERO + CN_DOLLAR;
	 }
	 if (decimal == "") {
	  outputCharacters += CN_INTEGER;
	 }
	 outputCharacters = CN_SYMBOL + outputCharacters;
	 return outputCharacters;
	}



function getMonthBetween(startDate,endDate){
	startDate=new Date(startDate.replace(/-/g,'/'));
	endDate=new Date(endDate.replace(/-/g,'/'));
	var num=0;
	var year=endDate.getFullYear()-startDate.getFullYear();
	num+=year*12;
	var month=endDate.getMonth()-startDate.getMonth();
	num+=month;
	var day=endDate.getDate()-startDate.getDate();
	if(day>0){
	//if(day>15){ num+=1; }
	num+=1;
	}else if(day<0){
	//if(day<-15){num-=1; }
	//num-=1;
	}
	return num;
	}

var easyuiPanelOnMove = function(left, top) {
	var parentObj = $(this).panel('panel').parent();
	if (left < 0) {
	$(this).window('move', {
	left : 1
	});
	}
	if (top < 0) {
	$(this).window('move', {
	top : 1
	});
	}
	var width = $(this).panel('options').width;
	var height = $(this).panel('options').height;
	var right = left + width;
	var buttom = top + height;
	var parentWidth = $(window).width();
	var parentHeight = 768;
	
	if(parentObj.css("overflow")=="hidden"){
	if(left > parentWidth-width){
	$(this).window('move', {
	"left":parentWidth-width
	});
	}
	if(top > parentHeight-height){
	$(this).window('move', {
	"top":parentHeight-height
	});
	}
	}
	};
	$.fn.panel.defaults.onMove = easyuiPanelOnMove;
	$.fn.window.defaults.onMove = easyuiPanelOnMove;
	$.fn.dialog.defaults.onMove = easyuiPanelOnMove;

	
	(function($){  
	    //备份jquery的ajax方法  
	    var _ajax=$.ajax;  
	      
	    //重写jquery的ajax方法  
	    $.ajax=function(opt){  
	        //备份opt中error和success方法  
	        var fn = {  
	            error:function(XMLHttpRequest, textStatus, errorThrown){},  
	            success:function(data, textStatus){
	            }  
	        }; 
	        if(opt.error){  
	            fn.error=opt.error;  
	        }  
	        if(opt.success){  
	            fn.success=opt.success;  
	        }  
	          
	        //扩展增强处理  
	        var _opt = $.extend(opt,{  
	            error:function(XMLHttpRequest, textStatus, errorThrown){  
	                //错误方法增强处理  
	                fn.error(XMLHttpRequest, textStatus, errorThrown);  
	            },  
	            success:function(data, textStatus){
	                fn.success(data, textStatus);  
	            },
	            complete: function(XMLHttpRequest, textStatus) {
	                var sessionstatus = XMLHttpRequest.getResponseHeader("sessionstatus"); //通过XMLHttpRequest取得响应头，sessionstatus，  
	                if (sessionstatus == "timeout") {
	                	window.location.replace("getMenu.do");
	                }
	            }
	        });  
	        _ajax(_opt);  
	    };  
	})(jQuery);  
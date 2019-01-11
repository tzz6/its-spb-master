/**
 * jQuery方法扩展
 * 
 * @author 尔演@Eryan eryanwcp@gmail.com
 * 
 * @version 2013-05-26
 */

/**
 * 
 * 将form表单元素的值序列化成对象
 * 
 * @returns object
 */

$.serializeObject = function(form) {
	var o = {};
	$.each(form.serializeArray(), function(index) {
		if (o[this['name']]) {
			o[this['name']] = o[this['name']] + "," + this['value'];
		} else {
			o[this['name']] = this['value'];
		}
	});
	return o;
};

$.fn.serializeJson=function($form){  
    var serializeObj={};  
    var array=$form.serializeArray();  
    var str=$form.serialize();  
    $(array).each(function(){  
        if(serializeObj[this.name]){  
            if($.isArray(serializeObj[this.name])){  
                serializeObj[this.name].push(this.value);  
            }else{  
                serializeObj[this.name]=[serializeObj[this.name],this.value];  
            }  
        }else{  
            serializeObj[this.name]=this.value;   
        }  
    });  
    return serializeObj;  
};  

$.fn.jsonFillForm = function($form,jsonValue) {
    var obj = $form;
    $.each(jsonValue, function(name, ival) {
        var $oinput = obj.find(":input [name=" + name + "]");
        if ($oinput.attr("type") == "radio" || $oinput.attr("type") == "checkbox") {
		            $oinput.each(function() {
		                if (Object.prototype.toString.apply(ival) == '[object Array]') {
		                	for (var i = 0; i < ival.length; i++) {
		                        if ($(this).val() == ival[i])
		                            $(this).attr("checked", "checked");
		                    }
		                } else {
		                    if ($(this).val() == ival)
		                        $(this).attr("checked", "checked");
		                }
		            });
        } else if ($oinput.attr("type") == "textarea") {            
            obj.find("[name=" + name + "]").html(ival);
        } else {
            obj.find("[name=" + name + "]").val(ival);
        }
    });
};

$.fn.timestampFormat = function(dateSeconds,mask){
	if(dateSeconds === null || $.trim(dateSeconds) === '' || dateSeconds === undefined){
		return '';
	}
	var date = new Date();
	date.setTime(dateSeconds);
	var d = date;
    var zeroize = function (value, length)
    {
        if (!length) length = 2;
        value = String(value);
        for (var i = 0, zeros = ''; i < (length - value.length); i++)
        {
            zeros += '0';
        }
        return zeros + value;
    };
 
    return mask.replace(/"[^"]*"|'[^']*'|\b(?:d{1,4}|m{1,4}|yy(?:yy)?|([hHMstT])\1?|[lLZ])\b/g, function ($0)
    {
        switch ($0)
        {
            case 'd': return d.getDate();
            case 'dd': return zeroize(d.getDate());
            case 'ddd': return ['Sun', 'Mon', 'Tue', 'Wed', 'Thr', 'Fri', 'Sat'][d.getDay()];
            case 'dddd': return ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'][d.getDay()];
            case 'M': return d.getMonth() + 1;
            case 'MM': return zeroize(d.getMonth() + 1);
            case 'MMM': return ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'][d.getMonth()];
            case 'MMMM': return ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'][d.getMonth()];
            case 'yy': return String(d.getFullYear()).substr(2);
            case 'yyyy': return d.getFullYear();
            case 'h': return d.getHours() % 12 || 12;
            case 'hh': return zeroize(d.getHours() % 12 || 12);
            case 'H': return d.getHours();
            case 'HH': return zeroize(d.getHours());
            case 'm': return d.getMinutes();
            case 'mm': return zeroize(d.getMinutes());
            case 's': return d.getSeconds();
            case 'ss': return zeroize(d.getSeconds());
            case 'l': return zeroize(d.getMilliseconds(), 3);
            case 'L': var m = d.getMilliseconds();
                if (m > 99) m = Math.round(m / 10);
                return zeroize(m);
            case 'tt': return d.getHours() < 12 ? 'am' : 'pm';
            case 'TT': return d.getHours() < 12 ? 'AM' : 'PM';
            case 'Z': return d.toUTCString().match(/[A-Z]+$/);
            // Return quoted strings with the surrounding quotes removed
            default: return $0.substr(1, $0.length - 2);
        }
    });
};

$.fn.fileUpload = function($file, module){
	var formData = new FormData();
	formData.append("file", $file);
	formData.append("name", $file.name);
	formData.append("module", module);
	
    var curWwwPath = window.document.location.href;
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    var localhostPath = curWwwPath.substring(0, pos);
    //var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    
   // var url = (localhostPath + projectName)+'/file/upload';
    //var url = '../file/upload';
    var url = localhostPath+'/file/upload';
    var result = null;
    
    $.ajax({
        url: url,
        type: 'POST',
        xhr: function() {  // custom xhr
            myXhr = $.ajaxSettings.xhr();
            if(myXhr.upload){ // check if upload property exists
                myXhr.upload.addEventListener('progress',
                function(e){
                	if(e.lengthComputable){
            	        $('progress').attr({value:e.loaded,max:e.total});
            	    }
                }, false); // for handling the progress of the upload
            }
            return myXhr;
        },
        beforeSend: function(){},
        success: function(data){
        	result = data;
        },
        error: function(){},
        data: formData,
        cache: false,
        contentType: false,
        processData: false,
        async: false
    });
    
    return result;
};

$.fn.download = function (url, data){
	if( url && data ){ 
        data = typeof data == 'string' ? data : jQuery.param(data);
        var inputs = '';
        jQuery.each(data.split('&'), function(){ 
            var pair = this.split('=');
            inputs+='<input type="hidden" name="'+ pair[0] +'" value="'+ pair[1] +'" />'; 
        });
        jQuery('<form action="'+ url +'" method="'+ ('post') +'">'+inputs+'</form>')
        .appendTo('body').submit().remove();
    }
};

/**
 * return 
 * 0 : the fromDatetime is equals toDatetime;
 * 1 : the fromDatetime is later than toDatetime;
 * -1 : the fromDatetime is earier than toDatetime.
 * */
$.fn.datetimeCompare = function(fromDatetimeStr, toDatetimeStr){
	var fromDatetime = parseInt((new Date(fromDatetimeStr)).getTime());
	var toDatetime = parseInt((new Date(toDatetimeStr)).getTime());
	var time = parseInt(fromDatetime - toDatetime);
	if(time === 0){
		return 0;
	}else if(time > 0){
		return 1;
	}else if(time < 0){
		return -1;
	}
};

function isValidDate(d) {
    if ( Object.prototype.toString.call(d) !== "[object Date]" )
        return false;
    return !isNaN(d.getTime());
}

/**
 * 扩展日期格式化 例：new Date().format("yyyy-MM-dd HH:mm:ss")
 *
 * "M+" :月份
 * "d+" : 天
 * "h+" : 小时
 * "m+" : 分钟
 * "s+" : 秒钟
 * "q+" : 季度
 * "S" : 毫秒数
 * "X": 星期 如星期一
 * "Z": 返回周 如周二
 * "F":英文星期全称，返回如 Saturday
 * "L": 三位英文星期，返回如 Sat
 * @param format 格式化字符串
 * @returns {*}
 */
Date.prototype.format = function(format) {
    if(!isValidDate(this)){
        return '';
    }
    var week = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
    var week_cn = [ '日', '一', '二', '三', '四', '五', '六'];
    var o = {
		"M+" : this.getMonth() + 1, //月份
		"d+" : this.getDate(), //天
		"H+" : this.getHours(), //小时
		"m+" : this.getMinutes(), //分钟
		"s+" : this.getSeconds(), //秒钟
		"q+" : Math.floor((this.getMonth() + 3) / 3), //季度
		"S" : this.getMilliseconds(),//毫秒数
        "X": "星期" + week_cn[this.getDay() ], //星期
        "Z": "周" + week_cn[this.getDay() ],  //返回如 周二
        "F": week[this.getDay()],  //英文星期全称，返回如 Saturday
        "L": week[this.getDay()].slice(0, 3)//三位英文星期，返回如 Sat
	};
	if (/(y+)/.test(format))
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(format))
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
	return format;
};

/**
 * 日期格式化.
 * @param value 日期
 * @param format 格式化字符串 例如："yyyy-MM-dd"、"yyyy-MM-dd HH:mm:ss"
 * @returns 格式化后的字符串
 */
 $.formatDate = function(value,format) {
	if (value == null || value == '' || value == 'null' ) {
		return '';
	}
	var dt;
	if (value instanceof Date) {
		dt = value;
	} else {
		dt = new Date(value);
		if (isNaN(dt)) {
			//将那个长字符串的日期值转换成正常的JS日期格式
			value = value.replace(/\/Date\((-?\d+)\)\//, '$1'); 
			dt = new Date();
			dt.setTime(value);
		}
	}
	return dt.format(format);
};
/**
 * 
 * 增加formatString功能
 * 
 * 使用方法：$.formatString('字符串{0}字符串{1}字符串','第一个变量','第二个变量');
 * 
 * @returns 格式化后的字符串
 */
$.formatString = function(str) {
	for ( var i = 0; i < arguments.length - 1; i++) {
		str = str.replace("{" + i + "}", arguments[i + 1]);
	}
	return str;
};

/**
 * 根据长度截取先使用字符串，超长部分追加...
 * @param str 对象字符串
 * @param len 目标字节长度
 * @return 处理结果字符串
 */
$.cutString = function(str, len) {
	//length属性读出来的汉字长度为1
	if(str.length*2 <= len) {
		return str;
	}
	var strlen = 0;
	var s = "";
	for(var i = 0;i < str.length; i++) {
		s = s + str.charAt(i);
		if (str.charCodeAt(i) > 128) {
			strlen = strlen + 2;
			if(strlen >= len){
				return s.substring(0,s.length-1) + "...";
			}
		} else {
			strlen = strlen + 1;
			if(strlen >= len){
				return s.substring(0,s.length-2) + "...";
			}
		}
	}
	return s;
};
/**
 * Object to String
 * 不通用,因为拼成的JSON串格式不对.
 */
$.objToStr = function(o) {
	var r = [];
	if (typeof o == "string" || o == null) {
		return "@" + o + "@";
	}
	if (typeof o == "object") {
		if (!o.sort) {
			r[0] = "{";
			for ( var i in o) {
				r[r.length] = "@" + i + "@";
				r[r.length] = ":";
				r[r.length] = obj2str(o[i]);
				r[r.length] = ",";
			}
			r[r.length - 1] = "}";
		} else {
			r[0] = "[";
			for ( var i = 0; i < o.length; i++) {
				r[r.length] = obj2str(o[i]);
				r[r.length] = ",";
			}
			r[r.length - 1] = "]";
		}
		return r.join("");
	}
	return o.toString();
};

/**
 * json字符串转换为Object对象.
 * @param json json字符串
 * @returns Object
 */
$.jsonToObj = function(json){ 
    return eval("("+json+")"); 
};
/**
 * json对象转换为String字符串对象.
 * @param o Json Object
 * @returns   Object对象
 */
$.jsonToString = function(o) {
	var r = [];
	if (typeof o == "string")
		return "\"" + o.replace(/([\'\"\\])/g, "\\$1").replace(/(\n)/g, "\\n").replace(/(\r)/g, "\\r").replace(/(\t)/g, "\\t") + "\"";
	if (typeof o == "object") {
		if (!o.sort) {
			for ( var i in o)
				r.push(i + ":" + obj2str(o[i]));
			if (!!document.all && !/^\n?function\s*toString\(\)\s*\{\n?\s*\[native code\]\n?\s*\}\n?\s*$/.test(o.toString)) {
				r.push("toString:" + o.toString.toString());
			}
			r = "{" + r.join() + "}";
		} else {
			for ( var i = 0; i < o.length; i++)
				r.push(obj2str(o[i]));
			r = "[" + r.join() + "]";
		}
		return r;
	}
	return o.toString();
};


/**
 * 获得URL参数
 * 
 * @returns 对应名称的值
 */
$.getUrlParam = function(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
};

/**
 * 接收一个以逗号分割的字符串，返回List，list里每一项都是一个字符串
 * 
 * @returns list
 */
$.getList = function(value) {
	if (value != undefined && value != '') {
		var values = [];
		var t = value.split(',');
		for ( var i = 0; i < t.length; i++) {
			values.push('' + t[i]);/* 避免他将ID当成数字 */
		}
		return values;
	} else {
		return [];
	}
};

/**
 * 获得项目根路径
 * 
 * 使用方法：$.bp();
 * 
 * @returns 项目根路径
 */
$.bp = function() {
	var curWwwPath = window.document.location.href;
	var pathName = window.document.location.pathname;
	var pos = curWwwPath.indexOf(pathName);
	var localhostPaht = curWwwPath.substring(0, pos);
	var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
	return (localhostPaht + projectName);
};

/**
 * 
 * 使用方法:$.pn();
 * 
 * @returns 项目名称(/base)
 */
$.pn = function() {
	return window.document.location.pathname.substring(0, window.document.location.pathname.indexOf('\/', 1));
};


/**
 * 
 * 生成UUID
 * 
 * @returns UUID字符串
 */
$.random4 = function() {
	return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
};
$.UUID = function() {
	return (eu.random4() + eu.random4() + "-" + eu.random4() + "-" + eu.random4() + "-" + eu.random4() + "-" + eu.random4() + eu.random4() + eu.random4());
};

/**
 * 扩展js 去字符串空格
 */
String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, '');
};
String.prototype.ltrim = function() {
	return this.replace(/(^\s*)/g, '');
};
String.prototype.rtrim = function() {
	return this.replace(/(\s*$)/g, '');
};


var DECMAP = { "0": "零", "1": "一", "2": "二", "3": "三", "4": "四", "5": "五", "6": "六", "7": "七", "8": "八", "9": "九"};
var NLEN = ["千", "百", "十", "亿", "千", "百", "十", "万", "千", "百", "十", ""];
/**
 * 数字转换为中文格式
 * @param dec
 * @returns {string}
 */
$.formatDecimals = function (dec) {
    var _td = dec + "";
    var out = "";
    if (_td.indexOf(".") >= 0) {
        var sp = _td.split(".");
        for (var i = 0; i < sp[0].length; i++) {
            out += DECMAP[(sp[0].charAt(i))];
            out += NLEN[NLEN.length - sp[0].length + i];
        }
        out += "点";
        for (var j = 0; j < sp[1].length; j++) {
            out += DECMAP[(sp[1].charAt(j))];
        }
    } else {
        for (var g = 0; g < _td.length; g++) {
            out += DECMAP[(_td.charAt(g))];
            out += NLEN[NLEN.length - _td.length + g];
        }
    }
    return out;
};

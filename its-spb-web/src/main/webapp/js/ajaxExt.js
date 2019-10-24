/**
 * jQuery Ajax封装通用方法
 * @returns
 */
$(function() {
	/**
	 * Ajax封装通用方法
	 * url 发送请求的地址
	 * data 发送到服务器的数据，数组存储，如：{"date": new Date().getTime(), "state": 1}
	 * async 默认值：true。默认设置下，所有请求均为异步请求。如果需要发送同步请求，请将此选项设置为 false。
	 *       注意，同步请求将锁住浏览器，用户其它操作必须等待请求完成才可以执行。
	 * type 请求方式("POST" 或 "GET")， 默认为 "GET"
	 * dataType 预期服务器返回的数据类型，常用的如：xml、html、json、text
	 * successFunction 成功回调函数
	 * errorFunction 失败回调函数
	 */
	jQuery.ajaxext = function(url, type, async, dataType, data, successFunction, errorFunction) {
		async = (async == null || async == "" || typeof (async) == "undefined") ? "true" : async;
		type = (type == null || type == "" || typeof (type) == "undefined") ? "post" : type;
		dataType = (dataType == null || dataType == "" || typeof (dataType) == "undefined") ? "json" : dataType;
		data = (data == null || data == "" || typeof (data) == "undefined") ? {"date" : new Date().getTime()} : data;
		debugger
		$.ajax({
			url : url,
			type : type,
			async : async,
			dataType : dataType,
			data : data,
			headers : {//请求头
				'Accept' : 'application/json; charset=utf-8',
				'its-username' : window.sessionStorage.getItem("username"),
				'its-language' : window.sessionStorage.getItem("lang"),
				'its-refreshToken' : window.sessionStorage
						.getItem("refreshToken"),
				'its-token' : window.sessionStorage.getItem("token")
			},
			success : function(successData) {
				var code = successData.code;
				if (code != null && code != "") {
					if (code == "401" || code == "1004") {
						alert("请重新登录")
					}
				} else {
					successFunction(successData);
				}
			},
			error : function() {
				errorFunction();
			}
		});
	};

});
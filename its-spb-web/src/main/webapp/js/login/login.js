$(function() {
	var ctx = $("#ctx").val();
	// 换一个验证码
	$("#securityCodeImg").bind("click", function() {
		$(this).attr("src", ctx + "/verifyCodeServlet?t=" + new Date().getTime());
	});

	
	$("#login_form").ajaxForm({
		beforeSubmit : function(form_data, form, option) {
			$("#errorInfo").html("");
			var username = $("#username").val();
			if(username == '' || username == '用户名' || username == 'Username'){
				 $("#errorInfo").html($.i18n.prop("login.username.info"));
				return false;
			}
			var password = $("#password").val();
			if(password == '' || password == 'Password' || password == '密码'){
				$("#errorInfo").html($.i18n.prop("login.password.info"));
				return false;
			}
			var verifyCode = $("#verifyCode").val();
			if(verifyCode == '' || verifyCode == 'VerifyCode' || verifyCode == '验证码'){
				$("#errorInfo").html($.i18n.prop("login.verifycode.info"));
				return false;
			}
			return true;
		},
		success : function(data) {
			var json = eval('(' + data + ')');
			if(json.status == 'success'){
				var url = json.message;
				window.location.href = json.message;
			}else{
				$("#btn_login").attr('disabled',false);
				if(json.status == 'verifyCodeError'){
					$("#errorInfo").html("验证码错误");
				}else if(json.status == 'userError'){
					$("#errorInfo").html("用户名或密码不正确");
				}else{
					window.location.reload();
				}
				$("#securityCodeImg").attr("src", ctx + "/verifyCodeServlet?t=" + new Date().getTime());
			}
		},
		error : function() {
			$("#errorInfo").html($.i18n.prop("login.error.info"));
		},
		timeout : 20000
	});
	
	var autologin = $("#autologin").attr("checked");
	if(autologin == 'checked'){
		loginSubmit();
	}
	
	var language = GetCookie("lang");
	//	alert(language);
});


function loginSubmit(){
	$("#errorInfo").html("");
	var username = $("#username").val();
	if(username == '' || username == '用户名' || username == 'Username'){
		 $("#errorInfo").html($.i18n.prop("login.username.info"));
		return;
	}
	var password = $("#password").val();
	if(password == '' || password == 'Password' || password == '密码'){
		$("#errorInfo").html($.i18n.prop("login.password.info"));
		return;
	}
	$("#btn_login").attr('disabled',true);
	$("#login_form").submit();
}




function ajaxSubmit(){
	var ctxStr = $("#ctx").val();
	$("#errorInfo").html("");
	var username = $("#username").val();
	if(username == '' || username == '用户名' || username == 'Username'){
		 $("#errorInfo").html($.i18n.prop("login.username.info"));
		 return;
	}
	var password = $("#password").val();
	if(password == '' || password == 'Password' || password == '密码'){
		$("#errorInfo").html($.i18n.prop("login.password.info"));
		return;
	}
	var verifyCode = $("#verifyCode").val();
	if(verifyCode == '' || verifyCode == 'VerifyCode' || verifyCode == '验证码'){
		$("#errorInfo").html($.i18n.prop("login.verifycode.info"));
		return;
	}
	$("#btn_login").attr('disabled',true);
	var lang = $("#lang").val();
	var savePassword = $("#savePassword").val();
	//公钥用后台java代码生成
	var publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDgSCCL8R3E2cNAO38TkBsk6c/Q2tJXNwkquGGuY9fiWVyOj7m/TzGuq7prAu6PoAtikXA8TgGMB/Z/MxYz6BfomlHPKAjfjpHXf4A5g0RsHHESxNHRE9QP4ir/MJ5PwVVJgA1ibw8dkHzX7ID3f+3V/XdqBHiuyELwi7gao7Ja6wIDAQAB";
	var encrypt = new JSEncrypt();
	encrypt.setPublicKey(publicKey);
	// RAS加密
	password = encrypt.encrypt(password);
	
	$.ajax({url : ctxStr+'/login?random='+ new Date().getTime(),
		type : "POST",
		data : {
			'username' : username,
			'password' : password,
			'verifyCode' : verifyCode,
			'lang' : lang,
			'savePassword' : savePassword
		},
		async : false,
		success : function(data) {
			var json = data;
			if(json.status == 'success'){
				var refreshToken = json.refreshToken;
				var token = json.token;
				window.sessionStorage.setItem("lang", lang);
				window.sessionStorage.setItem("username", username);
				window.sessionStorage.setItem("token", token);
				window.sessionStorage.setItem("refreshToken", refreshToken);
//				window.sessionStorage.clear();
				var url = json.message;
				window.location.href = json.message;
			}else{
				$("#btn_login").attr('disabled',false);
				if(json.status == 'verifyCodeError'){
					$("#errorInfo").html("验证码错误");
				}else if(json.status == 'userError'){
					$("#errorInfo").html("用户名或密码不正确");
				}else{
					window.location.reload();
				}
				$("#securityCodeImg").attr("src", ctxStr + "/verifyCodeServlet?t=" + new Date().getTime());
			}
		}
	});
}
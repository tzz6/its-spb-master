$(function() {

	//js国际化
	var ctx = $("#ctx").val();
	var lang = $("#lang").val();
	jQuery.i18n.properties({
		name : 'js',
		path : ctx + '/js/i18n/',
		mode : 'map',
		language : lang,
		callback : function() {
		}
	});
});


//切换语言
function changeLanguage() {
	var language = $("#language").val();
	if(language !=""){
		addCookie("lang", language);
		window.location.href = window.location.href;
	}
}

//设置Cookie
function addCookie(objName, objValue) {
	var days = 30;
	var str = objName + "=" + objValue;
	var date = new Date();
	date.setTime(date.getTime() + days * 24 * 60 * 60 * 1000);
	str += "; expires=" + date.toGMTString();
	str += "; path=/";
	document.cookie = str;
}

//取Cookie的值
function GetCookie(name) {
    var arg = name + "=";
    var alen = arg.length;
    var clen = document.cookie.length;
    var i = 0;
    while (i < clen) {
        var j = i + alen;
        //alert(j);
        if (document.cookie.substring(i, j) == arg) return getCookieVal(j);
        i = document.cookie.indexOf(" ", i) + 1;
        if (i == 0) break;
    }
    return null;
}

function getCookieVal(offset) {
    var endstr = document.cookie.indexOf(";", offset);
    if (endstr == -1) endstr = document.cookie.length;
    
    return unescape(document.cookie.substring(offset, endstr));
}
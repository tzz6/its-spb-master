$(function () {
    casLogin();
})

function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return r[2];
    return '';
}

function funcUrlDel(name) {
    var loca = window.location;
    var baseUrl = loca.origin + loca.pathname + "?";
    var query = loca.search.substr(1);
    if (query.indexOf(name) > -1) {
        var obj = {}
        var arr = query.split("&");
        for (var i = 0; i < arr.length; i++) {
            arr[i] = arr[i].split("=");
            obj[arr[i][0]] = arr[i][1];
        }
        ;
        delete obj[name];
        var url = baseUrl + JSON.stringify(obj).replace(/[\"\{\}]/g, "").replace(/\:/g, "=").replace(/\,/g, "&");
        return url
    }
    ;
}

function casLogin() {
    var ctxStr = $("#ctx").val();
    var token = getQueryString("token");
    if (token != null && token != "") {
        $.ajax({
            url: ctxStr + '/cas/knock?random=' + new Date().getTime(),
            type: "GET",
            data: {
                'token': token
            },
            async: false,
            success: function (data) {
                var json = data;
                if (json.status == 'success') {
                    var token = json.token;
                    window.localStorage.setItem("token", token);
                    var url = funcUrlDel("token");
                    if (url.length == url.indexOf("?") + 1) {
                        url = url.substr(0, url.length - 1);
                    }
                    window.location.href = url;
                } else {
                    alert("error");
                }
            }
        });
    }
}

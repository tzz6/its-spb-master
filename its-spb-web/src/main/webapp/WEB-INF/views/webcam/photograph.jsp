<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/commons/common.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>拍照</title>
<%-- <script type="text/javascript" src="${ctx}/js/common/jquery-2.1.4.js"></script> --%>
<script type="text/javascript" src="${ctx}/js/Webcam/jquery-1.4.1.min.js"></script>
<script type="text/javascript" src="${ctx}/js/Webcam/jquery.webcam.js"></script>
<%-- <script type="text/javascript" src="${ctx}/js/photograph/photograph.js"></script> --%>

<script>

$(function() {
	var pos = 0, ctx = null, saveCB, image = [];
	var w = 320,h = 240;
	var canvas = document.createElement("canvas");
	canvas.setAttribute('width', 320);
	canvas.setAttribute('height', 240);
	if (canvas.toDataURL) {
		ctx = canvas.getContext("2d");
		image = ctx.getImageData(0, 0, 320, 240);
		saveCB = function(data) {
			var col = data.split(";");
			var img = image;
			for(var i = 0; i < 320; i++) {
				var tmp = parseInt(col[i]);
				img.data[pos + 0] = (tmp >> 16) & 0xff;
				img.data[pos + 1] = (tmp >> 8) & 0xff;
				img.data[pos + 2] = tmp & 0xff;
				img.data[pos + 3] = 0xff;
				pos+= 4;
			}
			if (pos >= 4 * 320 * 240) {
				ctx.putImageData(img, 0, 0);
// 				alert(canvas.toDataURL("image/png"));
				$("#img").attr("src",canvas.toDataURL("image/png"));
				$.post("${ctx}/webcam/uploadImage", {type: "data", image: canvas.toDataURL("image/png")},
			 	function (data) {
// 					alert(data);
	 	 			var json = eval('(' + data + ')');
// 	 	 			alert("拍照："+json.status+"保存目录："+json.message);
	 	 			$("#messageId").html("拍照："+json.status+"保存目录："+json.message);
	 	 		});
				pos = 0;
			}
		};
	} else {
		saveCB = function(data) {
			image.push(data);
			pos+= 4 * 320;
			if (pos >= 4 * 320 * 240) {
				$.post("${ctx}/webcam/uploadImage", {type: "pixel", w: w, h: h , pix:image.join('|')});
				pos = 0;
			}
		};
	}

	//初始化摄像头
	$("#webcam").webcam({
		width: 320,
		height: 240,
		mode: "callback",
		swffile: "${ctx}/js/Webcam/jscam_canvas_only.swf",
		onSave: saveCB,
		onCapture: function () {
			webcam.save();
		},
		debug: function (type, string) {
			console.log(type + ": " + string);
		}
	});

});

//拍照
function photograph(){
	webcam.capture();
}
</script>
</head>
<body>
<div id="webcam" style="height:100px;width:100px;background:red;"></div>
<br><br><br><br><br><br><br>
<br><br><br><br><br><br><br>
<input type="button" value="拍照" onclick="photograph();">
<br><br><br><br>
<div>
<img id="img" alt="" src="" style="height:240px;width:320px;">
</div>
<div id="messageId">
</div>
</body>
</html>

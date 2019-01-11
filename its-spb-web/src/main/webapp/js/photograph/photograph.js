$(function() {  
//    LODOP=getLodop();  
	var web_demo = $("#ctx").val();
    var pos = 0, ctx = null, image = [];  
    var ctxh = null;  
    $("#webcam").webcam({  
        width: 320,//320  
        height: 240,//240  
        mode: "callback",  
        swffile: web_demo + "/js/Webcam/jscam_canvas_only.swf",
        onSave: function(data) {  
            var col = data.split(";");  
            var img = image;  
            // for(var i = 0; i < 320; i++) {  
            for(var i = 0; i < 320; i++) {  
                var tmp = parseInt(col[i]);  
                img.data[pos + 0] = (tmp >> 16) & 0xff;  
                img.data[pos + 1] = (tmp >> 8) & 0xff;  
                img.data[pos + 2] = tmp & 0xff;  
                img.data[pos + 3] = 0xff;  
                pos+= 4;  
            }  
            //if (pos >= 4 * 320 * 240) {  
            if (pos >= 4 * 320 * 240) {  
                  
                //ctxh.clearRect(0, 0, 320, 240);  
                ctxh.putImageData(img, 0, 0);  
                image = ctxh.getImageData(0, 0, 320, 240);  
  
                ctx.clearRect(0, 0, 160, 200);  
                ctx.putImageData(image,-80,-40,80,40,160,200);  
                
                pos = 0;  
            }  
        },  
        onCapture: function () {  
            webcam.save();  
            jQuery("#flash").css("display", "block");  
            jQuery("#flash").fadeOut("fast", function () {  
                jQuery("#flash").css("opacity", 1);  
            });  
            webcam.save();  
        }  
    });  
  
    window.addEventListener("load", function() {  
  
     jQuery("body").append("<div id=\"flash\"></div>");  
     var canvas = document.getElementById("canvas");  
  
     if (canvas.getContext) {  
         ctx = document.getElementById("canvas").getContext("2d");  
         ctxh = document.getElementById("canvashidden").getContext("2d");  
          
         ctx.clearRect(0, 0, 160, 200);  
         ctxh.clearRect(0, 0,320, 240);  
         var img = new Image();  
           
         img.src = "<%=basePath %>${examPeoplePo.pic}";  
           
         img.onload = function() {  
            ctx.drawImage(img, 0, 0);  
            ctxh.drawImage(img, 152, 50);  
         }  
           
         image = ctxh.getImageData(0, 0, 320, 240);  
     }  
    }, false);  
  
});  
      
    // 上传图片，jQuery版  
    function sendImage(){  
      // 获取 canvas DOM 对象  
      var canvas = document.getElementById("canvas");  
      // 获取Base64编码后的图像数据，格式是字符串  
      // "data:image/png;base64,"开头,需要在客户端或者服务器端将其去掉，后面的部分可以直接写入文件。  
      var dataurl = canvas.toDataURL("image/png");  
      // 为安全 对URI进行编码  
      // data%3Aimage%2Fpng%3Bbase64%2C 开头  
      var imagedata =  encodeURIComponent(dataurl);  
       
      var data = {  
              exampeopleid: $("#exampeopleid").val(),  
              jkzseq: $("#jkzseq").val(),  
              fileinput: imagedata  
       };  
      $.ajax({  
            type : "POST",  
            url : "uploadExamPic.action",  
            data : data,  
            beforeSend : function(XMLHttpRequest) {  
                //loading();  
            },  
            success : function(data) {  
                var json = eval(arguments[2].responseText);  
                if (json[0].result == "ok") {  
                    $("#pic").val(json[0].filepath);  
//                    LODOP.ADD_PRINT_IMAGE(124 ,530 , 130, 185,"<img border='0' src='<%=basePath %>json[0].filepath'>");   
                    document.getElementById("printdiv").style.display = "block";  
                }else{  
                    $.messager.alert("提示",json[0].message,"error");  
                }  
            }  
        });  
    }  
      
/**
 * 
 */

axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;UTF-8';

axios.interceptors.request.use(function(config) {

	if (config.method === 'post') {
		config.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;UTF-8';
		config.data = Qs.stringify(config.data);
	}

	// get参数
	config.paramsSerializer = function(params) {
		return Qs.stringify(params, {
			arrayFormat : 'repeat'
		});
	}
	return config;
}, function(error) {

});


function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); // 匹配目标参数
    var result = window.location.search.substr(1).match(reg); // 对querystring匹配目标参数
    if (result != null) {
        return decodeURIComponent(result[2]);
    } else {
        return null;
    }
}


function createBase64 (inputFile, callback) {
    /*input_file：文件按钮对象*/
    /*callback: 转换成功后执行的方法*/
    if (typeof (FileReader) === 'undefined') {
        console.log("不支持 FileReader.");
    } else {
        try {
            /*图片转Base64 核心代码*/
            var file = inputFile.files[0];

            //判断上传控件是否选择了文件
            if (!file)
                return;

            //这里我们判断下类型如果不是图片就返回 去掉就可以上传任意文件  
            //if (!/image\/\w+/.test(file.type)) {
            //    console.log("请确保文件为图像类型Base64出错.");
            //    $.alert({ msg: "请确保文件为图像类型" });
            //    return;
            //}


            //创建文件流对象
            var reader = new FileReader();
            reader.onload = function () {
                var image = new Image();
                image.src = this.result;
                image.onload = function () {
                    var min = expectWidth = this.width;
                    var max = expectHeight = this.height;


                    var bl = max < min;//高小于宽为true
                    if (bl) {
                        max = expectWidth;
                        min = expectHeight;
                    }
                    var scale = max / min;//计算宽高比
                    if ((expectWidth > 1440 || expectHeight > 1440) && scale <= 2) {
                        if (bl) {
                            expectWidth = 1440;//最大值为1440
                            expectHeight = expectHeight * (min / max);
                        }
                        else {
                            expectHeight = 1440;
                            expectWidth = expectWidth * (min / max);
                        }
                    }
                    if (expectWidth > 1440 && expectHeight > 1440 && scale > 2) {
                        if (bl) {
                            expectHeight = 1440;
                            expectWidth = expectWidth * (min / max);
                        } else {
                            expectWidth = 1440;
                            expectHeight = expectHeight * (min / max);
                        }
                    }

                    var Orientation = null;

                    //获取照片方向角属性，用户旋转控制  
                    EXIF.getData(image, function () {
                        // alert(EXIF.pretty(this));  
                        EXIF.getAllTags(this);
                        //alert(EXIF.getTag(this, 'Orientation'));   
                        Orientation = EXIF.getTag(this, 'Orientation');
                        //return;  
                    });

                    var canvas = document.createElement("canvas");
                    //var ctx = canvas.getContext("2d");
                    canvas.width = expectWidth;
                    canvas.height = expectHeight;
                    //ctx.drawImage(this, 0, 0, expectWidth, expectHeight);

                    var base64 = null;
                    var mpImg = new MegaPixImage(image);
                    mpImg.render(canvas,
                        {
                            maxWidth: expectWidth,
                            maxHeight: expectHeight,
                            quality: 0.5,
                            orientation: Orientation
                        });

                    base64 = canvas.toDataURL("image/jpeg", 0.7);
                    callback(base64);
                };
            }
            reader.readAsDataURL(file);
        } catch (e) {
            console.log("图片转Base64出错." + e.toString());
        }
    }
}
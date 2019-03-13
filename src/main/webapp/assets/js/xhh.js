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
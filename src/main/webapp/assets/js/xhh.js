/**
 * 
 */

axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;UTF-8';

axios.interceptors.request.use(function (config) {
    
    if (config.method === 'post') {
        config.headers.post['Content-Type'] = 'application/x-www-form-urlencoded;UTF-8';
        config.data = Qs.stringify(config.data);
    }

    //get参数
    config.paramsSerializer = function (params) {
        return Qs.stringify(params, {
            arrayFormat: 'repeat'
        });
    }
    return config;
}, function (error) {
    
});
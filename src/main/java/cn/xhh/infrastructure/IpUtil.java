package cn.xhh.infrastructure;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取请求客户端真实ip
 */
public final class IpUtil {
    /**
     * 获取当前请求的客户端ip
     * @param request
     * @return
     */
    public final static String getIp(HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getRemoteAddr();
        }
        if(ip.indexOf(",")!=-1){
            String[] ips = ip.split(",");
            ip = ips[0].trim();
        }

        return ip;
    }
}

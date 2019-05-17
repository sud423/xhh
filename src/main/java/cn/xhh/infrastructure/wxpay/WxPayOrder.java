package cn.xhh.infrastructure.wxpay;

import cn.xhh.domainservice.identity.SessionManager;
import cn.xhh.infrastructure.IpUtil;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public final class WxPayOrder {

    @Value("${app_id}")
    private String appId;

    @Value("${mch_id}")
    private String mchId;

    @Value("${paterner_key}")
    private String paternerKey;


    public Map create(HttpServletRequest request,String tradeNo,String fee){
        try {
            //拼接统一下单地址参数
            Map<String, String> paraMap = new HashMap<String, String>();
            paraMap.put("appid", appId);
            paraMap.put("mch_id", mchId);
            paraMap.put("nonce_str", WXPayUtil.generateNonceStr());
            paraMap.put("openid", SessionManager.getUser().getOpenId());
            paraMap.put("out_trade_no", tradeNo);//订单号
            paraMap.put("spbill_create_ip", IpUtil.getIp(request));
            paraMap.put("total_fee", fee);
            paraMap.put("trade_type", "JSAPI");
            paraMap.put("notify_url", "");// 此路径是微信服务器调用支付结果通知路径随意写
            String sign = WXPayUtil.generateSignature(paraMap, paternerKey);
            paraMap.put("sign", sign);
            String xml = WXPayUtil.mapToXml(paraMap);//将所有参数(map)转xml格式

            // 统一下单 https://api.mch.weixin.qq.com/pay/unifiedorder
            String unifiedorder_url = "https://api.mch.weixin.qq.com/pay/unifiedorder";

            String xmlStr = HttpRequest.sendPost(unifiedorder_url, xml);//发送post请求"统一下单接口"返回预支付id:prepay_id

            //以下内容是返回前端页面的json数据
            String prepay_id = "";//预支付id
            if (xmlStr.indexOf("SUCCESS") != -1) {
                Map<String, String> map = WXPayUtil.xmlToMap(xmlStr);
                prepay_id = (String) map.get("prepay_id");
            }
            Map<String, String> payMap = new HashMap<String, String>();
            payMap.put("appId", appId);
            payMap.put("timeStamp", WXPayUtil.getCurrentTimestamp() + "");
            payMap.put("nonceStr", WXPayUtil.generateNonceStr());
            payMap.put("signType", "MD5");
            payMap.put("package", "prepay_id=" + prepay_id);
            String paySign = WXPayUtil.generateSignature(payMap, paternerKey);
            payMap.put("paySign", paySign);
            return payMap;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

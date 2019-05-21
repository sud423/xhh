package cn.xhh.infrastructure.wxpay;

import cn.xhh.domainservice.identity.SessionManager;
import cn.xhh.infrastructure.IpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

@Component
public final class WxPayOrder {

    private final static Logger log = Logger.getLogger(WxPayOrder.class);
    @Value("${app_id}")
    private String appId;

    @Value("${mch_id}")
    private String mchId;

    @Value("${paterner_key}")
    private String paternerKey;


    public Map create(HttpServletRequest request,String tradeNo,String period,String fee){
        try {
            //拼接统一下单地址参数
            Map<String, String> paraMap = new HashMap<String, String>();
            paraMap.put("appid", appId);
            paraMap.put("mch_id", mchId);
            paraMap.put("nonce_str", WXPayUtil.generateNonceStr());
            paraMap.put("body", String.format("鑫恒辉-%s期账单",period));
            paraMap.put("openid", SessionManager.getUser().getOpenId());

            paraMap.put("out_trade_no", tradeNo);//订单号
            paraMap.put("total_fee", fee);
            paraMap.put("spbill_create_ip", IpUtil.getIp(request));

            paraMap.put("notify_url", "http://c.supaotui.com/wxpay/notify");// 此路径是微信服务器调用支付结果通知路径随意写
            paraMap.put("trade_type", "JSAPI");
            String sign = WXPayUtil.generateSignature(paraMap, paternerKey);
            paraMap.put("sign", sign);
            String xml = WXPayUtil.mapToXml(paraMap);//将所有参数(map)转xml格式

            log.debug(xml);

            // 统一下单 https://api.mch.weixin.qq.com/pay/unifiedorder
            String unifiedorder_url = "https://api.mch.weixin.qq.com/pay/unifiedorder";


            String xmlStr = HttpRequest.sendPost(unifiedorder_url, xml);//发送post请求"统一下单接口"返回预支付id:prepay_id

            log.debug(xmlStr);

            //以下内容是返回前端页面的json数据
            String prepay_id = "";//预支付id
            if (xmlStr.toLowerCase().indexOf("SUCCESS") != -1) {
                Map<String, String> map = WXPayUtil.xmlToMap(xmlStr);
                prepay_id = map.get("prepay_id");
            }

            log.debug(prepay_id);

            System.out.println(prepay_id);
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

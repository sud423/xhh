package cn.xhh.infrastructure.wxpay;

import cn.xhh.domainservice.identity.SessionManager;
import cn.xhh.infrastructure.OptResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

@Component
public final class WxPayOrder {

    private final static Logger log = Logger.getLogger(WxPayOrder.class);

    private WXPayConfig payConfig;

    @Value("${app_id}")
    private String appId;

    @Value("${mch_id}")
    private String mchId;

    @Value("${paterner_key}")
    private String paternerKey;

    public OptResult create(String out_trade_no,String total_fee,String period,String spbill_create_ip){
        try {
            //统一下单参数
            Map<String, String> paraMap = new HashMap<>();
            paraMap.put("body", new String(String.format("鑫恒辉-%s期账单", period).getBytes("ISO8859-1")));
            paraMap.put("openid", SessionManager.getUser().getOpenId());
            paraMap.put("out_trade_no", out_trade_no);//订单号
            paraMap.put("total_fee", total_fee);
            paraMap.put("spbill_create_ip", spbill_create_ip);

            paraMap.put("notify_url", "http://c.supaotui.com/wxpay/notify");// 此路径是微信服务器调用支付结果通知路径随意写
            paraMap.put("trade_type", "JSAPI");
            paraMap.put("sign_type", WXPayConstants.MD5);

            WXPay wxPay=new WXPay(payConfig);

            Map<String,String> responseMap= wxPay.unifiedOrder(paraMap);

            //判断通信返回状态码 是否成功
            if (!"SUCCESS".equals(responseMap.get("return_code"))) {
                return OptResult.Failed(responseMap.get("return_msg"));
            }

            //判断交易标识返回状态码 是否成功
            if (!"SUCCESS".equals(responseMap.get("result_code"))) {
                return OptResult.Failed(responseMap.get("err_code_des"));
            }

            Map<String, String> payMap = new HashMap<>();
            payMap.put("appId", payConfig.getAppID());
            payMap.put("timeStamp", WXPayUtil.getCurrentTimestamp() + "");
            payMap.put("nonceStr", WXPayUtil.generateNonceStr());
            payMap.put("signType", WXPayConstants.MD5);
            payMap.put("package", "prepay_id=" + responseMap.get("prepay_id"));
            String paySign = WXPayUtil.generateSignature(payMap, paternerKey);
            payMap.put("paySign", paySign);
            return OptResult.Successed(payMap);
        }
        catch (Exception ex){
            log.error(ex);
            return  OptResult.Failed(ex.getMessage());
        }

    }


}

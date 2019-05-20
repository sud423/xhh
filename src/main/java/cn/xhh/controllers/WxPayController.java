package cn.xhh.controllers;

import cn.xhh.application.BillService;
import cn.xhh.infrastructure.wxpay.WXPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Map;

@Controller
@RequestMapping(value="/wxpay")
public class WxPayController {

    @Autowired
    private BillService billService;

    @RequestMapping(value={"/callback","/notify"})
    public String callBack(HttpServletRequest request, HttpServletResponse response){
        //System.out.println("微信支付成功,微信发送的callback信息,请注意修改订单信息");
        InputStream is = null;
        try {
            is = request.getInputStream();//获取请求的流信息(这里是微信发的xml格式所有只能使用流来读)
            String xml = WXPayUtil.inputStreamToString(is, "UTF-8");
            Map<String, String> notifyMap = WXPayUtil.xmlToMap(xml);//将微信发的xml转map

            if(notifyMap.get("return_code").equals("SUCCESS")){
                if(notifyMap.get("result_code").equals("SUCCESS")){
                    String ordersSn = notifyMap.get("out_trade_no");//商户订单号
                    String amountpaid = notifyMap.get("total_fee");//实际支付的订单金额:单位 分
//                    BigDecimal amountPay = (new BigDecimal(amountpaid).divide(new BigDecimal("100"))).setScale(2);//将分转换成元-实际支付金额:元
                    //String openid = notifyMap.get("openid");  //如果有需要可以获取
                    //String trade_type = notifyMap.get("trade_type");
                    billService.updateCallback(ordersSn,amountpaid);
                    /*以下是自己的业务处理------仅做参考
                     * 更新order对应字段/已支付金额/状态码
                     */
//                    Orders order = ordersService.selectOrdersBySn(ordersSn);
//                    if(order != null) {
//                        order.setLastmodifieddate(new Date());
//                        order.setVersion(order.getVersion().add(BigDecimal.ONE));
//                        order.setAmountpaid(amountPay);//已支付金额
//                        order.setStatus(2L);//修改订单状态为待发货
//                        int num = ordersService.updateOrders(order);//更新order
//
//                        String amount = amountPay.setScale(0, BigDecimal.ROUND_FLOOR).toString();//实际支付金额向下取整-123.23--123
//                        /*
//                         * 更新用户经验值
//                         */
//                        Member member = accountService.findObjectById(order.getMemberId());
//                        accountService.updateMemberByGrowth(amount, member);
//
//                        /*
//                         * 添加用户积分数及添加积分记录表记录
//                         */
//                        pointService.updateMemberPointAndLog(amount, member, "购买商品,订单号为:"+ordersSn);

//                    }

                }
            }

            //告诉微信服务器收到信息了，不要在调用回调action了========这里很重要回复微信服务器信息用流发送一个xml即可
            response.getWriter().write("<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>");
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping("/notify")
    public String wxpaySuccess(HttpServletRequest request, HttpServletResponse response){

        String result=null;


        return result;
    }
}

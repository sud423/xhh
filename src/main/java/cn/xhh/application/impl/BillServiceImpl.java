package cn.xhh.application.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.xhh.application.BillService;
import cn.xhh.domain.business.*;
import cn.xhh.infrastructure.OptResult;
import cn.xhh.infrastructure.Utils;
import cn.xhh.infrastructure.wxpay.WXPayConstants;
import cn.xhh.infrastructure.wxpay.WXPayUtil;
import cn.xhh.infrastructure.wxpay.WXPayOrder;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.xhh.domainservice.identity.SessionManager;
import cn.xhh.dto.BillDto;
import cn.xhh.infrastructure.ListResult;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BillServiceImpl implements BillService {
	/**
	 * 密钥
	 */
	@Value("${paterner_key}")
	private String paternerKey;

	@Autowired
	private BillRepository billRepository;

	@Autowired
	private FlowRepository flowRepository;

	@Autowired
	private WXPayOrder wxPayOrder;
	
	@Override
	public ListResult<BillDto> queryBillByStatus(int page, int status) {
		
		PageHelper.startPage(page, 10, true);
		List<Bill> bills=billRepository.getBillByStatus(SessionManager.getTenantId(), SessionManager.getUserId(), status);
		PageInfo<Bill> billInfos=new PageInfo<Bill>(bills);
		
		ModelMapper modelMapper = new ModelMapper();
		Converter<ArrayList<Bill>, ArrayList<BillDto>> converter = new AbstractConverter<ArrayList<Bill>, ArrayList<BillDto>>() {

			@Override
			protected ArrayList<BillDto> convert(ArrayList<Bill> source) {

				return modelMapper.map(source, new TypeToken<ArrayList<BillDto>>() {
				}.getType());
			}
		};
		PropertyMap<PageInfo<Bill>, PageInfo<BillDto>> propertyMap = new PropertyMap<PageInfo<Bill>, PageInfo<BillDto>>() {
			@Override
			protected void configure() {
				using(converter).map(source.getList(), destination.getList());
			}
		};
		
		modelMapper.addMappings(propertyMap);
		
		PageInfo<BillDto> pageInfo = modelMapper.map(billInfos, new TypeToken<PageInfo<BillDto>>() {
		}.getType());
		return new ListResult<>(pageInfo);
	}

	@Override
	public BillDto queryBillItem(int billId) {
		
		List<BillItem> items=countDiscount(billId);
		if(items==null || items.size()<=0)
			return  null;

		BillDto dto=new BillDto();
		dto.setId(billId);
		float totalPrice=(float)items.stream().mapToDouble(BillItem::getPrice).sum();
		dto.setPrice(totalPrice);

		dto.setItems(items);
		float totalRate=(float)items.stream().mapToDouble(BillItem::getDis).sum();
		dto.setRebate(totalRate);
		return dto;
	}

	@Override
	@Transactional
	public OptResult createPay(int billId, String channel, String ip) {

		List<BillItem> items=countDiscount(billId);
		if(items==null || items.size()<=0)
			return null;

		Bill bill=billRepository.getBillById(billId);

		Flow flow=new Flow();
		flow.setId(Utils.generateCode());
		flow.setAddTime(new Date());
		flow.setSourceId(bill.getId());
		flow.setSourceName("bill");
		flow.setChannel(channel);
		flow.setCreateIp(ip);
		flow.setOptTime(new Date());
		flow.setStatus((byte) 10);
		flow.setUserId(SessionManager.getUserId());
		flow.setTenantId(SessionManager.getTenantId());

		if(!bill.isAdjust()){
			//计算总价格
			float totalPrice=bill.getPrice();
			//计算总折扣后的费用
			float totalRate=(float)items.stream().mapToDouble(BillItem::getDis).sum();
			//去掉折扣实际支付费用
			BigDecimal b  =   new BigDecimal(totalPrice-totalRate);
			float fee=b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue()*100;
			flow.setAmount((int)fee);
		}
		else{
			flow.setAmount((int)(bill.getAdjustPrice()*100));
		}

		flowRepository.add(flow);
		bill.setBillNumber(flow.getId());
		bill.setPayTime(new Date());
		bill.setPayChannel((byte)10);
		billRepository.update(bill);
		return wxPayOrder.create(flow.getId(),Integer.toString(flow.getAmount()),bill.getPeriod(),ip);

	}

	@Override
	@Transactional
	public OptResult payComplete(int billId) {
		Bill bill = billRepository.getBillById(billId);

		OptResult result = wxPayOrder.orderQuery(bill.getBillNumber());

		if (result.getCode() != 0)
			return result;

		Flow flow = flowRepository.get(bill.getBillNumber());

		Map<String, String> resData = (Map<String, String>) result.getResult();

		String tradeState = resData.get("trade_state");

		boolean isSucc = false;
		String msg=resData.get("trade_state_desc");

		switch (tradeState) {
			case "SUCCESSS":
				isSucc = true;
				flow.setStatus((byte) 20);
				bill.setStatus((byte) 20);

				billRepository.update(bill);
				break;
			case "NOTPAY":
				flow.setStatus((byte) 30);
				break;
			case "REFUND":
				flow.setStatus((byte) 50);
				break;
			case "CLOSED":
				flow.setStatus((byte) 60);
				break;
			case "REVOKED":
				flow.setStatus((byte) 70);
				break;
			case "USERPAYING":
				flow.setStatus((byte) 80);
				break;
			default:
				flow.setStatus((byte) 40);
				msg = "支付失败(其他原因，如银行返回失败)";
				break;
		}

		flow.setResultMsg(msg);
		flow.setOptTime(new Date());
		flowRepository.update(flow);
		if (isSucc)
			return OptResult.Successed();
		else
			return OptResult.Failed(msg);
	}

	@Override
	public OptResult updateCallback(Map<String,String> notifyMap) {

		try {
			if (!WXPayUtil.isSignatureValid(notifyMap, paternerKey, WXPayConstants.SignType.HMACSHA256)) {
				Bill bill=billRepository.getBillByNumber(notifyMap.get("out_trade_no"));

				BigDecimal b  =   new BigDecimal(notifyMap.get("total_fee"));
				float amount=b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue()/100;
				bill.setStatus((byte)30);
				bill.setAmount(amount);
				bill.setArrivalTime(new Date());
				billRepository.update(bill);


				Flow flow=flowRepository.get(bill.getBillNumber());
				if(flow.getStatus()!=20){
					flow.setStatus((byte)20);
					flow.setResultMsg("支付成功");
					flow.setOptTime(new Date());

					flowRepository.update(flow);
				}

				return OptResult.Successed();

			}

		}
		catch (Exception ex){
			ex.printStackTrace();
		}
		return OptResult.Failed("签名验证失败");
	}

	private List<BillItem> countDiscount(int billId){

		List<BillItem> items=billRepository.getItemByBillId(billId);

		if(items==null || items.size()==0)
		return items;

		for(int i=0;i<items.size();i++){
			BigDecimal b  =   new BigDecimal(items.get(i).getDis()/100);
			float   discount   =  b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

			float rebate=items.get(i).getPrice()*(1-discount);

			items.get(i).setDis(rebate);
		}

		return items;
	}


}

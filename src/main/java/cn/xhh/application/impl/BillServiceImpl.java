package cn.xhh.application.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.xhh.application.BillService;
import cn.xhh.domain.business.*;
import cn.xhh.infrastructure.OptResult;
import cn.xhh.infrastructure.Utils;
import cn.xhh.infrastructure.wxpay.WxPayOrder;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.xhh.domainservice.identity.SessionManager;
import cn.xhh.dto.BillDto;
import cn.xhh.infrastructure.ListResult;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BillServiceImpl implements BillService {

	@Autowired
	private BillRepository billRepository;

	@Autowired
	private FlowRepository flowRepository;

	@Autowired
	private WxPayOrder wxPayOrder;
	
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
		flow.setBillId(bill.getId());
		flow.setChannel(channel);
		flow.setCreateIp(ip);
		flow.setOptTime(new Date());
		flow.setStatus((byte) 10);
		flow.setUserId(SessionManager.getUserId());
		flow.setTenantId(SessionManager.getTenantId());
		flow.setClose(false);
		if(!bill.isAdjust()){
			//计算总价格
			float totalPrice=bill.getPrice();
			//计算总折扣后的费用
			float totalRate=(float)items.stream().mapToDouble(BillItem::getDis).sum();
			//去掉折扣实际支付费用
			BigDecimal b  =   new BigDecimal(totalPrice-totalRate);
			float fee=b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
			flow.setAmount((int)fee*100);
		}
		else{
			flow.setAmount((int)bill.getAdjustPrice()*100);
		}
		bill.setBillNumber(flow.getId());
		flowRepository.add(flow);
		billRepository.update(bill);
		return wxPayOrder.create(flow.getId(),Integer.toString(flow.getAmount()),bill.getPeriod(),ip);

	}

	@Override
	public int updateCallback(String outTradeNo, String fee) {

		Bill bill=billRepository.getBillByNumber(outTradeNo);
		bill.setArrivalTime(new Date());

		BigDecimal b  =   new BigDecimal(Float.parseFloat(fee)/100);
		float amount=b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

		bill.setAmount(amount);
		bill.setStatus((byte)20);

		int result=billRepository.update(bill);

		return result;
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

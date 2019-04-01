package cn.xhh.application.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.xhh.domain.business.Bill;
import cn.xhh.domain.business.BillItem;
import cn.xhh.domain.business.BillRepository;
import cn.xhh.domainservice.identity.SessionManager;
import cn.xhh.dto.BillDto;
import cn.xhh.infrastructure.ListResult;

@Service
public class BillService implements cn.xhh.application.BillService {

	@Autowired
	private BillRepository billRepository;
	
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
		return new ListResult<BillDto>(pageInfo);
	}

	@Override
	public BillDto queryBillItem(int billId) {
		
		List<BillItem> items=billRepository.getItemByBillId(billId);
		BillDto dto=new BillDto();
		dto.setId(billId);
		float totalPrice=(float)items.stream().mapToDouble(BillItem::getPrice).sum();
		dto.setPrice(totalPrice);
		dto.setItems(items);
		return dto;
	}

	
}

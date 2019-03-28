package cn.xhh.application.impl;

import java.util.ArrayList;
import java.util.Date;
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

import cn.xhh.application.OrderService;
import cn.xhh.domain.business.Order;
import cn.xhh.domain.business.OrderRepository;
import cn.xhh.domainservice.identity.SessionManager;
import cn.xhh.dto.OrderClientDto;
import cn.xhh.dto.OrderDriverDto;
import cn.xhh.infrastructure.ListResult;
import cn.xhh.infrastructure.OptResult;
import cn.xhh.infrastructure.Validate;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Override
	public ListResult<OrderDriverDto> findOrderByDriver(int status, int pageNum) {

		int s = 10;

		switch (status) {
		case 0:
			s = 10;
			break;
		case 1:
			s = 20;
			break;
		}

		PageHelper.startPage(pageNum, 5, true);
		List<Order> orders = orderRepository.findOrderByDriver(SessionManager.getUserId(), SessionManager.getTenantId(),
				s);

		PageInfo<Order> pageoInfo = new PageInfo<Order>(orders);
		ModelMapper modelMapper = new ModelMapper();
		Converter<ArrayList<Order>, ArrayList<OrderDriverDto>> converter = new AbstractConverter<ArrayList<Order>, ArrayList<OrderDriverDto>>() {

			@Override
			protected ArrayList<OrderDriverDto> convert(ArrayList<Order> source) {

				PropertyMap<Order, OrderDriverDto> orderMap = new PropertyMap<Order, OrderDriverDto>() {
					@Override
					protected void configure() {
						map().setName(source.getClient().getName());
						map().setCell(source.getClient().getCell());
					}
				};
				modelMapper.addMappings(orderMap);
				return modelMapper.map(source, new TypeToken<ArrayList<OrderDriverDto>>() {
				}.getType());
			}
		};
		PropertyMap<PageInfo<Order>, PageInfo<OrderDriverDto>> propertyMap = new PropertyMap<PageInfo<Order>, PageInfo<OrderDriverDto>>() {
			@Override
			protected void configure() {
				using(converter).map(source.getList(), destination.getList());
			}
		};
//		};
		modelMapper.addMappings(propertyMap);

		PageInfo<OrderDriverDto> pageInfo = modelMapper.map(pageoInfo, new TypeToken<PageInfo<OrderDriverDto>>() {
		}.getType());
		return new ListResult<OrderDriverDto>(pageInfo);
	}

	@Override
	public ListResult<OrderClientDto> findOrderByClient(int status, int pageNum) {
		PageHelper.startPage(pageNum, 5, true);
		ArrayList<Integer> s = new ArrayList<Integer>();

		switch (status) {
		case 0:
			s.add(1);
			s.add(10);
			s.add(21);
			break;
		case 1:
			s.add(20);
			s.add(30);
			break;
		}

		List<Order> orders = orderRepository.findOrderByClient(SessionManager.getUserId(), SessionManager.getTenantId(),
				s);
		PageInfo<Order> pageOrderInfo = new PageInfo<Order>(orders);

		ModelMapper modelMapper = new ModelMapper();
		Converter<ArrayList<Order>, ArrayList<OrderClientDto>> converter = new AbstractConverter<ArrayList<Order>, ArrayList<OrderClientDto>>() {

			@Override
			protected ArrayList<OrderClientDto> convert(ArrayList<Order> source) {

				PropertyMap<Order, OrderClientDto> orderMap = new PropertyMap<Order, OrderClientDto>() {
					@Override
					protected void configure() {
						map().setName(source.getDriver().getName());
						map().setCell(source.getDriver().getCell());
					}
				};
				modelMapper.addMappings(orderMap);
				return modelMapper.map(source, new TypeToken<ArrayList<OrderClientDto>>() {
				}.getType());
			}
		};
		PropertyMap<PageInfo<Order>, PageInfo<OrderClientDto>> propertyMap = new PropertyMap<PageInfo<Order>, PageInfo<OrderClientDto>>() {
			@Override
			protected void configure() {
				using(converter).map(source.getList(), destination.getList());
			}
		};
//		PropertyMap<Order, OrderClientDto> orderMap = new PropertyMap<Order, OrderClientDto>() {
//			@Override
//			protected void configure() {
//				map().setName(source.getDriver().getName());
//				map().setCell(source.getDriver().getCell());
//			}
//		};
		modelMapper.addMappings(propertyMap);

		PageInfo<OrderClientDto> dtos = modelMapper.map(pageOrderInfo, new TypeToken<PageInfo<OrderClientDto>>() {
		}.getType());
		// PageInfo<OrderClientDto> pageInfo = new PageInfo<OrderClientDto>(dtos);
		return new ListResult<OrderClientDto>(dtos);
	}

	@Override
	public OptResult save(Order order) {
		if (Validate.isValid(order)) {

			int res = 0;
			if (order.getId() == 0) {

				order.setAddTime(new Date());
				order.setTenantId(SessionManager.getTenantId());// 所属租户编号
				order.setUserId(SessionManager.getUserId());// 下单人编号
				res = orderRepository.add(order);
			}

			if (res > 0)
				return OptResult.Successed();
			return OptResult.Failed("信息保存失败，请稍候重试");
		}

		return Validate.verify(order);
	}

}

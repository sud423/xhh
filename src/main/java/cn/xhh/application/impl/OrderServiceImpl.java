package cn.xhh.application.impl;

import java.util.ArrayList;
import java.util.List;

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

		List<Order> orders = orderRepository.findOrderByDriver(SessionManager.getUserId(), SessionManager.getTenantId(),
				s);
		PageHelper.startPage(pageNum, 5, true);
		PropertyMap<Order, OrderDriverDto> orderMap = new PropertyMap<Order, OrderDriverDto>() {
			@Override
			protected void configure() {
				map().setName(source.getClient().getName());
				map().setCell(source.getClient().getCell());
			}
		};
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(orderMap);

		List<OrderDriverDto> dtos = modelMapper.map(orders, new TypeToken<List<OrderDriverDto>>() {
		}.getType());
		PageInfo<OrderDriverDto> pageInfo = new PageInfo<OrderDriverDto>(dtos);
		return new ListResult<OrderDriverDto>(pageInfo);
	}

	@Override
	public ListResult<OrderClientDto> findOrderByClient(int status, int pageNum) {
		PageHelper.startPage(pageNum, 5, true);
		ArrayList<Integer> s=new ArrayList<Integer>();

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

		List<Order> orders = orderRepository.findOrderByClient(SessionManager.getUserId(), SessionManager.getTenantId(),s);
		PropertyMap<Order, OrderClientDto> orderMap = new PropertyMap<Order, OrderClientDto>() {
			@Override
			protected void configure() {
				map().setName(source.getDriver().getName());
				map().setCell(source.getDriver().getCell());
			}
		};
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(orderMap);

		List<OrderClientDto> dtos = modelMapper.map(orders, new TypeToken<List<OrderClientDto>>() {
		}.getType());
		PageInfo<OrderClientDto> pageInfo = new PageInfo<OrderClientDto>(dtos);
		return new ListResult<OrderClientDto>(pageInfo);
	}

	
}

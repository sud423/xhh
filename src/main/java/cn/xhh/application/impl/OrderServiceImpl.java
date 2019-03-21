package cn.xhh.application.impl;

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
import cn.xhh.dto.OrderDriverDto;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Override
	public List<OrderDriverDto> findOrderByDriver(int status,int page) {
		PageHelper.startPage(page, 10, true);
		List<Order> orders = orderRepository.findOrderByDriver(SessionManager.getUserId(), SessionManager.getTenantId(),
				status);

		PropertyMap<Order, OrderDriverDto> orderMap = new PropertyMap<Order,OrderDriverDto>() {
			@Override
			protected void configure() {
				map().setName(source.getClient().getName());
				map().setCell(source.getClient().getCell());
			}
		};
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(orderMap);

		List<OrderDriverDto> dtos = modelMapper.map(orders, new TypeToken<List<OrderDriverDto>>() {}.getType());
		PageInfo<OrderDriverDto> pageInfo = new PageInfo<OrderDriverDto>(dtos);
		return pageInfo.getList();
	}

}

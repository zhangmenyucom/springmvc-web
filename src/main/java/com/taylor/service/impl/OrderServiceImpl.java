package com.taylor.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taylor.dao.OrderDao;
import com.taylor.dto.OrderSearchDto;
import com.taylor.entity.Order;
import com.taylor.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderDao orderDao;

	@Override
	public List<Order> queryOrderList(Order order) {
		return orderDao.queryOrderList(order);
	}

	@Override
	public Page<Order> queryOrderListWithPage(OrderSearchDto orderDto) {
		Order order = new Order();
		order.setId(orderDto.getId());
		order.setMerchantId(orderDto.getMerchantId());
		order.setName(orderDto.getName());
		Page<Order> page = PageHelper.startPage(orderDto.getCurrenPage(), orderDto.getPageSize());
		orderDao.select(order);
		return page;
	}

}

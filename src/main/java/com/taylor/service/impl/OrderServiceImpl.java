package com.taylor.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.taylor.dao.OrderDao;
import com.taylor.dao.TestDao;
import com.taylor.entity.Order;
import com.taylor.entity.Test;
import com.taylor.service.OrderService;
import com.taylor.service.TestService;

@Service
public class OrderServiceImpl implements OrderService{
	@Autowired
	private OrderDao orderDao;
	
	@Override
	public List<Order> queryOrderList(Order order){
		return orderDao.queryOrderList(order);
	}

}

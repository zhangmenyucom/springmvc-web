package com.taylor.service;

import java.util.List;

import com.taylor.entity.Order;

public interface OrderService {

	List<Order> queryOrderList(Order order);

}

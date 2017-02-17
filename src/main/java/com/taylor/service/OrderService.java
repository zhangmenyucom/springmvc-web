package com.taylor.service;

import java.util.List;

import com.github.pagehelper.Page;
import com.taylor.dto.OrderSearchDto;
import com.taylor.entity.Order;

public interface OrderService {

	List<Order> queryOrderList(Order order);

	Page<Order> queryOrderListWithPage(OrderSearchDto order);

}

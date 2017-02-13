package com.taylor.dao;

import java.util.List;

import com.taylor.entity.Order;

import tk.mybatis.mapper.common.Mapper;

public interface OrderDao extends Mapper<Order> {
    
    public List<Order> queryOrderList(Order order);

}

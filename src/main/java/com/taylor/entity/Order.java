package com.taylor.entity;

import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "t_order")
public class Order {
	private Integer id;
	private Long merchantId;
	private String name;
}

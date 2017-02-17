package com.taylor.dto;

import lombok.Data;

@Data
public class OrderSearchDto {
	
	private Integer id;
	private Long merchantId;
	private String name;
	private int pageSize;
	private int currenPage;
}

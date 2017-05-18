package com.taylor.elasticsearch;

import lombok.Data;

import java.util.List;

@Data
public class QueryResult<T> {
	private long totalCount;
	private int pageSize;
	private int pageFrom;
	private List<T> items;

}

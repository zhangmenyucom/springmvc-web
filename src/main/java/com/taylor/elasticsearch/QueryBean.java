/**
 * 
 */
package com.taylor.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.elasticsearch.search.sort.SortOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author haoli
 *  Date: 2015-7-14 imuc-thirdpart
 *  Too much parameters , encap them in a bean
 *  
 *  update:zhangtao
 *  Date: 2016-9-29
 */
@Data
@AllArgsConstructor
public class QueryBean {
	private HashMap<String, Object> parameters;// 字段值等于
	private Map<String, Object> mustParams;// 针对中文字段，避免因分词匹配上别的数据
	private List<ESRangeFilter> rangeFilters;
	private List<EsTermsFilter> termFilters;
	private String type;// es索引文档
	private int pageFrom;
	private int pageSize;
	private String sortFiled;
	private SortOrder sortOrder;
	private String groupByFiled;
	private String sumFiled;
	private List<SortOrderExt> sortOrderExts;

	public QueryBean(HashMap<String, Object> parameters, int pageFrom, int pageSize) {
		this.parameters = parameters;
		this.pageFrom = pageFrom;
		this.pageSize = pageSize;
	}

	public QueryBean(HashMap<String, Object> parameters, int pageFrom, int pageSize, String sortFiled,
			SortOrder sortOrder) {
		super();
		this.parameters = parameters;
		this.pageFrom = pageFrom;
		this.pageSize = pageSize;
		this.sortFiled = sortFiled;
		this.sortOrder = sortOrder;
	}
}

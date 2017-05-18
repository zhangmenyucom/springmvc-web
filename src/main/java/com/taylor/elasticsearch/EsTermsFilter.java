package com.taylor.elasticsearch;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 查询所有指定字段值的集合， 例：查询status等于1,2,4...的结果集
 * 
 * @author 张涛
 * 
 */
@Data
public class EsTermsFilter implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7828873390557944550L;
	private String filedName;// 指定字段的名称
	private List<?> terms;// 范围
}

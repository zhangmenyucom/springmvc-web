package com.taylor.elasticsearch;

import lombok.Data;

import java.io.Serializable;

/**
 * 查询某指定字段x~y的范围结果集
 * 
 * @author 张涛
 * 
 */
@Data
public class ESRangeFilter implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4351596773633549856L;
	private String filedName;// 字段名称
	private Object gt; // 大于
	private Object lt;// 小于
	private Object from; // 大于等于，与gt互斥
	private Object to;// 小于等于，与lt互斥

	public void setGt(Object gt) {
		this.gt = gt;
		this.from = null;
	}

	public void setLt(Object lt) {
		this.lt = lt;
		this.to = null;
	}

	public void setFrom(Object from) {
		this.from = from;
		this.gt = null;
	}

	public void setTo(Object to) {
		this.to = to;
		this.lt = null;
	}
}

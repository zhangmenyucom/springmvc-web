package com.taylor.elasticsearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 等同于sql中group by
 * @author taylor
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AggregationResult implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4692497205060695236L;
	
	private String key;//等同于sql结果中group by 字段值
	private Long count;//等同于sql结果中group by 的数量
	private Double sum;// 聚合结果
	
}

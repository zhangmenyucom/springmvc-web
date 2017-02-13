package com.taylor.shard.strategy;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class BaseShardDomain implements BaseShard {

	/**
	 * 商户id
	 */
	private Long merchantId;

	/**
	 * 具体表的index 如：index =0时，表示xxxtable_0
	 */
	private Integer tableIndex;

}


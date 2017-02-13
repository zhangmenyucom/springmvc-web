package com.taylor.shard.strategy;

/**
 * 分表接口
 * @author miao
 *
 */
public interface BaseShard {
	
	public Long getMerchantId();
	
	public Integer getTableIndex();
	
}

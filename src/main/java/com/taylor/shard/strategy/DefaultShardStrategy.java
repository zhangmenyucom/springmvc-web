package com.taylor.shard.strategy;

import java.util.Map;

import com.google.code.shardbatis.strategy.ShardStrategy;
import com.taylor.utils.JacksonUtil;
import com.taylor.utils.StringUtils;

public class DefaultShardStrategy implements ShardStrategy {

	/**
	 * 缺省分表数量
	 */
	private static final int DEFAULT_TABLE_COUNT = 12;

	@Override
	public String getTargetTableName(String baseTableName, Object paramObject, String methodName) {
		// 分表策略实现
		BaseShard baseShard = getShard(paramObject);

		int tableIndex = getTableIndexByShard(baseShard);

		if (tableIndex == -1) {
			return baseTableName;
		}

		return baseTableName + "_" + tableIndex;
	}

	/**
	 * 根据商户号 (merchantId), 获取表的分表号
	 * 
	 * @param baseShard
	 * @return
	 */
	public int getTableIndexByShard(BaseShard baseShard) {
		if (baseShard == null) {
			return -1;
		}
		if (baseShard.getTableIndex() != null) {
			return baseShard.getTableIndex().intValue();
		}

		if (baseShard.getMerchantId() == null) {
			return -1;
		}

		int tableCount = getShardTableCount();

		return (int) (baseShard.getMerchantId().longValue() % tableCount);
	}

	protected int getShardTableCount() {
		return DEFAULT_TABLE_COUNT;
	}

	/**
	 * 根据 mapper 方法名和方法参数方法名和方法参数，获取 merchantId
	 * 
	 * @param paramObject
	 *            方法参数
	 * @param methodName
	 *            方法名
	 * @return 商户号（merchantId）
	 */
	protected BaseShard getShard(Object paramObject) {

		final Object shardObject = getFirstParamFromMethodParams(paramObject);
		if (shardObject != null && shardObject instanceof Long) {
			return new BaseShard() {
				@Override
				public Integer getTableIndex() {
					return null;
				}
				@Override
				public Long getMerchantId() {
					return Long.valueOf(String.valueOf(shardObject));
				}
			};
		}
		if (shardObject != null && shardObject instanceof BaseShard) {
			return (BaseShard) shardObject;
		}

		throw new RuntimeException("Can not get shard keys");
	}

	/**
	 * 获取方法参数集合的第一个参数
	 * 
	 * @return
	 */
	private Object getFirstParamFromMethodParams(Object params) {
		if (params == null) {
			return params;
		}
		if (params instanceof Map) {
			Map<?, ?> map = (Map<?, ?>) params;
			Object obj = map.get("param1");
			if (obj == null) {
				return getExtBaseShard(map);
			} else {
				return obj;
			}
		}else if(params instanceof Object){
			return JacksonUtil.getInstance().bean2Bean(params, BaseShardDomain.class);
		}
		return params;
	}

	@SuppressWarnings("rawtypes")
	protected BaseShard getExtBaseShard(Map paramMap) {
		Object merchantId = paramMap.get("merchantId");
		Object tableIndex = paramMap.get("tableIndex");
		if (merchantId == null && tableIndex == null) {
			return null;
		}
		if ((!StringUtils.isLong(String.valueOf(merchantId))) && (!StringUtils.isPositiveInteger(String.valueOf(tableIndex)))) {
			return null;
		}

		BaseShardDomain baseShard = new BaseShardDomain();
		baseShard.setMerchantId(StringUtils.parseLong(String.valueOf(merchantId)));
		baseShard.setTableIndex(StringUtils.parseInt(String.valueOf(tableIndex)));

		return baseShard;
	}

}

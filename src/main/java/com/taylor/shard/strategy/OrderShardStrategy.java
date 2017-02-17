package com.taylor.shard.strategy;

public class OrderShardStrategy extends DefaultShardStrategy {

	private static final int ORDER_TABLE_COUNT = 12;

	@Override
	protected int getShardTableCount() {
		return ORDER_TABLE_COUNT;
	}
}

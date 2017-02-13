package com.taylor.shard.strategy;

public class OrderShardStrategy extends DefaultShardStrategy {

	@Override
	protected int getShardTableCount() {
		return 2;
	}
}

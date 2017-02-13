package com.taylor.entity;

import javax.persistence.Table;

import com.taylor.shard.strategy.BaseShardDomain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Table
@EqualsAndHashCode(callSuper=false)
public class Order extends BaseShardDomain {
    private int id;
    private String name;
}

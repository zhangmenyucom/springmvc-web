package com.taylor.entity;

import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name = "t_user")
public class User {
	private Integer Id;
	private String name;

}

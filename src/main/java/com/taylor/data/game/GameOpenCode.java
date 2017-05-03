package com.taylor.data.game;

import java.util.Date;

import lombok.Data;

@Data
public class GameOpenCode {
	private Integer id;// "3228459"
	private String lottery;// "t1s30"
	private String issue;// "20170502-1962"
	private String code;// "4,6,0,3,8",
	private String code1;// "09,10,19,26,29,30,43,44,47,49,50,54,57,61,62,63,65,66,67,70",
	private String code2;// null,
	private Date openTime;// ": 1493713260000,
	private Integer clearStatus;// "1,
	private Date clearTime;// ": 1493713263000
}

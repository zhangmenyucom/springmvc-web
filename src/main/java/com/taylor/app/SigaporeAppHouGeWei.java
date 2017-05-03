package com.taylor.app;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.httpclient.HttpException;

import com.taylor.common.CommonResponse;
import com.taylor.data.game.GameOpenCode;
import com.taylor.post.singapore.LatestResultPost;
import com.taylor.post.singapore.Singapore30OrderPost;

public class SigaporeAppHouGeWei {
	public static void main(String[] args) {
		startThread();
	}

	public synchronized static void startThread() {
		String lastesCodeNum = "";
		String cureentCodeNum = "";
		int factor = Constants.factor;
		int initMutiply = factor;
		int lastNum = -1;
		int cureentNum = 0;
		Singapore30OrderPost singapore30OrderPost = new Singapore30OrderPost();
		LatestResultPost lrp = new LatestResultPost();
		try {
			while (true) {
				CommonResponse resp = lrp.queryLatestResult();
				if (resp.getError() == 0) {
					GameOpenCode gameOpenCode = resp.getData().getGameOpenCode();
					cureentCodeNum = gameOpenCode.getIssue();
					/** 如果等于上期睡眠5秒 **/
					if (cureentCodeNum.equals(lastesCodeNum)) {
						Thread.sleep(5000);
						continue;
					} else {
						lastesCodeNum = cureentCodeNum;
						cureentNum = Integer.parseInt(gameOpenCode.getCode().substring(gameOpenCode.getCode().lastIndexOf(',') + 1, gameOpenCode.getCode().length()));
						System.out.println("方案：大小单双后二星压个位---最新一期号码：" + gameOpenCode.getIssue() + "开奖码：" + gameOpenCode.getCode() + ",尾数为：------" + cureentNum + "--------" + (cureentNum % 2 == 0 ? "双" : "单"));
						if (cureentNum % 2 == lastNum % 2 && lastNum != -1) {
							initMutiply = factor;
							System.out.println("恭喜你中奖了。。。");
						} else {
							if (lastNum != -1) {
								initMutiply = 2 * initMutiply + factor;
							}
						}
						lastNum = cureentNum;
						singapore30OrderPost.postOrder("dxdsh",initMutiply, lastNum % 2 == 0 ? "单双,双" : "单双,单");
						System.out.println("加注成功，加注倍数"+initMutiply+"下注方案："+(lastNum % 2 == 0 ? "单双,双" : "单双,单")+",操作时间:"+new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date()));
					}
				} else {
					System.out.println("执行结果出错。。。");
					break;
				}
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

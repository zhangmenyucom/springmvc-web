package com.taylor.app.inverse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.httpclient.HttpException;

import com.taylor.common.CommonResponse;
import com.taylor.data.game.GameOpenCode;
import com.taylor.post.singapore.LatestResultPost;
import com.taylor.post.singapore.Singapore30OrderPost;

public class SigaporeAppHouShiWei extends Thread {

	public static void main(String[] args) {
		new SigaporeAppHouShiWei().start();
	}

	public void run() {
		System.out.println("***************************后二星压【十】位启动成功*********************************");
		startThread();
	}

	public synchronized static void startThread() {
		String lastesCodeNum = "";
		String cureentCodeNum = "";
		int factor = Constants.factor;
		int initMutiply = factor;
		int lastNum = -1;
		int cureentNum = 0;
		int oddTimes = 0;
		int evenTimes = 0;
		String strategyName = "";
		int strategyCode = 0;// 1:连续双后压单，2：连续单后压双 3：按上次开奖压
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
						Thread.sleep(Constants.sleepTime);
						continue;
					} else {
						lastesCodeNum = cureentCodeNum;
						cureentNum = Integer.parseInt(gameOpenCode.getCode().substring(gameOpenCode.getCode().lastIndexOf(',') - 1, gameOpenCode.getCode().length() - 2));
						System.out.println("方案：大小单双后二星压十位---最新一期号码：" + gameOpenCode.getIssue() + "开奖码：" + gameOpenCode.getCode() + ",尾数为：------" + cureentNum + "--------" + (cureentNum % 2 == 0 ? "双" : "单"));
						if (strategyCode == 3 && (cureentNum % 2 == lastNum % 2 && lastNum != -1)) {
							initMutiply = factor;
							System.out.println("❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤恭喜你中奖了❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤");
							System.out.println("");
						} else if (strategyCode == 1 && (cureentNum % 2 == 1 && lastNum != -1)) {
							initMutiply = factor;
							System.out.println("❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤恭喜你中奖了❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤");
							System.out.println("");
							strategyCode = 0;
						} else if (strategyCode == 2 && (cureentNum % 2 == 0 && lastNum != -1)) {
							initMutiply = factor;
							System.out.println("❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤恭喜你中奖了❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤");
							System.out.println("");
							strategyCode = 0;
						} else {
							if (lastNum != -1 && (strategyCode == 1 || strategyCode == 2)) {
								initMutiply = 2 * initMutiply + factor + (2 * initMutiply + factor) / Constants.increaseFactor;
							}
						}
						lastNum = cureentNum;
						/** 收手重来 **/
						if (initMutiply > Constants.maxMutiply) {
							initMutiply = Constants.maxMutiply / Constants.maxTerentFactor;
						}
						System.out.println("当前金额：【----------------" + resp.getData().getLotteryBalance() + "元----------------】");

						/** 奇偶次数变化 **/
						if (lastNum % 2 == 0) {
							evenTimes++;
							oddTimes = 0;
						} else {
							oddTimes++;
							evenTimes = 0;
						}
						System.out.println("当前连续双数：【" + evenTimes + "】次，连续单数：【" + oddTimes + "】次");
						if (evenTimes >= Constants.inverseNum) {
							/** 持续多次双后开始投单 **/
							singapore30OrderPost.postOrder("dxdsh", initMutiply, "单,单双");
							strategyName = "连续" + evenTimes + "次双后压单";
							strategyCode = 1;
						} else if (oddTimes >= Constants.inverseNum) {
							/** 持续多次单后开始投双 **/
							singapore30OrderPost.postOrder("dxdsh", initMutiply, "双,单双");
							strategyName = "连续" + oddTimes + "次单后压双";
							strategyCode = 2;
						} else {
							/** 剩下的按上次出奖投注 **/
							// singapore30OrderPost.postOrder("dxdsh",
							// initMutiply, lastNum % 2 == 0
							// ? "单双,双" : "单双,单");
							// strategyName = "按上次开奖下注：" + (lastNum % 2 == 0 ?
							// "双,单双" : "单,单双");
							// strategyCode = 3;
						}
						if (strategyCode == 1 || strategyCode == 2) {
							System.out.println("--------------------------------------------------------加注成功，加注倍数【★★★" + initMutiply + "★★★★★★】，下注方案：【" + strategyName + "】,操作时间:【" + new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date()) + "】");
						}
					}
				} else {
					System.err.println("执行结果出错。。。");
					break;
				}
			}
		} catch (HttpException e) {
			e.printStackTrace();
			startThread();
		} catch (IOException e) {
			e.printStackTrace();
			startThread();
		} catch (InterruptedException e) {
			e.printStackTrace();
			startThread();
		}
	}
}

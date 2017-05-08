package com.taylor.app.inverse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.httpclient.HttpException;

import com.taylor.common.CommonResponse;
import com.taylor.data.game.GameOpenCode;
import com.taylor.post.singapore.LatestResultPost;
import com.taylor.post.singapore.Singapore30OrderPost;

public class SigaporeAppQianGeWei extends Thread {
	public static void main(String[] args) {
		startThread();
	}

	public void run() {
		System.out.println("***************************后二星压【个】位启动成功*********************************");
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
		int currentTime = 0;
		String strategyName = "";
		int strategyCode = 0;// 1:连续双后压单，2：连续单后压双 3：按上次开奖压
		boolean isReverse = false;// 是否反转
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
						cureentNum = Integer.parseInt(gameOpenCode.getCode().substring(gameOpenCode.getCode().lastIndexOf(',')-5, gameOpenCode.getCode().length()-6));
						System.out.println(
								"方案：大小单双前二星压个位---最新一期号码：" + gameOpenCode.getIssue() + "开奖码：" + gameOpenCode.getCode() + ",尾数为：------" + cureentNum + "--------" + (cureentNum % 2 == 0 ? "双" : "单"));

						/** 未反转情况 **/
						if (!isReverse) {
							if (strategyCode == 3 && (cureentNum % 2 == lastNum % 2 && lastNum != -1)) {
								initMutiply = factor;
								currentTime = 0;
								System.out.println("❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤恭喜你中奖了❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤");
								System.out.println("");
								strategyCode = 0;
							} else if (strategyCode == 1 && (cureentNum % 2 == 1 && lastNum != -1)) {
								initMutiply = factor;
								currentTime = 0;
								System.out.println("❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤恭喜你中奖了❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤");
								System.out.println("");
								strategyCode = 0;
							} else if (strategyCode == 2 && (cureentNum % 2 == 0 && lastNum != -1)) {
								initMutiply = factor;
								currentTime = 0;
								System.out.println("❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤恭喜你中奖了❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤");
								System.out.println("");
								strategyCode = 0;
							} else {
								if (lastNum != -1) {
									initMutiply = 2 * initMutiply + factor + (2 * initMutiply + factor) / Constants.increaseFactor;
								}
							}
						} else {
							if (strategyCode == 3 && lastNum != -1) {
								if (cureentNum % 2 != lastNum % 2) {
									System.out.println("❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤恭喜你中奖了❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤");
								} else {
									System.out.println("XXXXXXXXXX反转失败,从头开始XXXXXXXXXXXX");
								}
							} else if (strategyCode == 1 && lastNum != -1) {
								if (cureentNum % 2 == 0) {
									System.out.println("❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤恭喜你中奖了❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤");
								} else {
									System.out.println("XXXXXXXXXX反转失败,从头开始XXXXXXXXXXXX");
								}
							} else if (strategyCode == 2 && lastNum != -1) {
								if (cureentNum % 2 == 1) {
									System.out.println("❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤恭喜你中奖了❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤❤");
								} else {
									System.out.println("XXXXXXXXXX反转失败,从头开始XXXXXXXXXXXX");
								}
							}
							System.out.println("");
							initMutiply = factor;
							currentTime = 0;
							strategyCode = 0;
							isReverse = false;
						}

						/** 到达连续指数，开始反转 **/
						if (currentTime >= Constants.reversetimes) {
							isReverse = true;
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
							if (!isReverse) {
								singapore30OrderPost.postOrder("dxdsq", initMutiply, "单双,单");
								strategyName = "连续" + evenTimes + "次双后压单";
							} else {
								singapore30OrderPost.postOrder("dxdsq", initMutiply, "单双,双");
								strategyName = "连续" + evenTimes + "次双后压单----反转";
							}
							currentTime++;
							strategyCode = 1;
						} else if (oddTimes >= Constants.inverseNum) {
							/** 持续多次单后开始投双 **/
							if (!isReverse) {
								singapore30OrderPost.postOrder("dxdsq", initMutiply, "单双,双");
								strategyName = "连续" + oddTimes + "次单后压双";
							} else {
								singapore30OrderPost.postOrder("dxdsq", initMutiply, "单双,单");
								strategyName = "连续" + oddTimes + "次单后压双----反转";
							}
							currentTime++;
							strategyCode = 2;
						} else {
							/** 剩下的按上次出奖投注 **/
							if (!isReverse) {
								singapore30OrderPost.postOrder("dxdsq", initMutiply, lastNum % 2 == 0 ? "单双,双" : "单双,单");
								strategyName = "按上次开奖下注：" + (lastNum % 2 == 0 ? "单双,双" : "单双,单");
							} else {
								singapore30OrderPost.postOrder("dxdsq", initMutiply, lastNum % 2 == 0 ? "单双,单" : "单双,双");
								strategyName = "按上次开奖下注：" + (lastNum % 2 == 0 ? "单双,单" : "单双,双----反转");
							}
							currentTime++;
							strategyCode = 3;
						}
						System.out.println("当前连续次数+++++++++++++++++++++++++++++【      " + currentTime + "  】++++++++++++++++++++++++++++++++");
						System.out.println("--------------加注成功，加注倍数【★★★----" + initMutiply + "----★★★】，下注方案：【" + strategyName + "】,操作时间:【" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "】");
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

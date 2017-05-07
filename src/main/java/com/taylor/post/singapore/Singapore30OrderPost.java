package com.taylor.post.singapore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import com.taylor.app.inverse.Constants;
import com.taylor.common.JsonUtil;
import com.taylor.yuanbo.order.Singapore30Order;

public class Singapore30OrderPost {
	
	public void postOrder(String gameType,int mutiply,String mod) throws HttpException, IOException{
		HttpClient client = new HttpClient();

		PostMethod method = new PostMethod("http://em.yuanbo4.com/api/game-lottery/add-order");

		// 表单域的值,既post传入的key=value

		NameValuePair[] data = { new NameValuePair("text", "[" + JsonUtil.transfer2JsonString(new Singapore30Order("t1s30", "",gameType, mod, Constants.unit, mutiply, 1954, false)) + "]") };

		// method使用表单阈值

		method.setRequestBody(data);
		method.setRequestHeader("Accept", " image/jpeg, application/x-ms-application, image/gif, application/xaml+xml, image/pjpeg, application/x-ms-xbap, application/x-shockwave-flash, application/msword, application/vnd.ms-excel, application/vnd.ms-powerpoint, */*");
		method.setRequestHeader("Accept-Language", "en-US");
		//method.setRequestHeader("Host", "em.yuanbo4.com");
		method.setRequestHeader("Origin", "http://em.yuanbo4.com");
		method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		method.setRequestHeader("Accept-Encoding", " gzip, deflate");
		method.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36");
		method.setRequestHeader("Connection", " Keep-Alive");
		method.setRequestHeader("Cookie",Constants.cookie);

		// 提交表单
		client.executeMethod(method);
		// 字符流转字节流 循环输出，同get解释

		InputStream inputStream = method.getResponseBodyAsStream();

		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

		StringBuffer stringBuffer = new StringBuffer();

		String str = "";

		while ((str = br.readLine()) != null) {

			stringBuffer.append(str);

		}
		System.out.println(stringBuffer.toString());

		method.releaseConnection();
	}
}
package com.taylor.common.utils;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public abstract class ParameterUtil {
	
	/**
	 * 获得url请求参数字符串，参数以$连接，如aid=1&bid=2
	 * @param params
	 * 			请求参数
	 * 			key：参数名，value：参数值
	 * @return
	 */
	public static String getRequestParamters(Map<String, Object> params){
		StringBuilder builder = new StringBuilder();
		
		Iterator<String> iterator = params.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			builder.append(key);
			builder.append("=");
			builder.append(params.get(key) == null ? "":params.get(key));
			builder.append("&");
		}
		
		//去掉末尾的&
		builder.setLength(builder.length() - 1);
		
		return builder.toString();
	}

	/**
	 * 获得url请求参数字符串，参数以$连接，如aid=1&bid=2
	 * @param params
	 * 			请求参数
	 * 			每两个值为一个参数名及参数值
	 * @return
	 */
	public static String getRequestParamters(String... params){
		StringBuilder builder = new StringBuilder();
		
		for(int i =0;(i + 1) < params.length;i=i-2){
			builder.append(params[i]);
			builder.append("=");
			String value=params[i];
			builder.append(value == null ? "":value);
			builder.append("&");
		}
		
		//去掉末尾的&
		builder.setLength(builder.length() - 1);
		
		return builder.toString();
	}

	/**
	 * 获取请求参数名
	 * @param urlWithParams
	 * @return
	 */
	public static String[] getRequestParamterNames(String urlWithParams){
		Map<String, String> paramMap = getRequestParamterMap(urlWithParams);
		if(paramMap == null){
			return null;
		}
		String[] paramAry = new String[paramMap.size()];
		return paramMap.keySet().toArray(paramAry);
	}
	/**
	 * 获取不带请求参数的url
	 * @param url
	 * @return
	 */
	public static String getRequestUrlWithoutParams(String url){
		int index = url.indexOf("?");
		if(index < 0 ){
			return url;
		}
		return url.substring(0, index);
	}
	
	private static Map<String, String> getRequestParamterMap(String urlWithParams){
		
		if(StringUtil.isEmpty(urlWithParams)){
			return null;
		}
		int index = urlWithParams.indexOf("?");
		if(index < 0 ){
			return null;
		}
		
		String paramStr = urlWithParams.substring(index + 1);
		String[] params = StringUtil.tokenizeToStringArray(paramStr, "&", true, true);
		Map<String, String> paramMap = 
				new HashMap<String, String>();
		for (String param: params) {
			String[] paramPair = param.split("=");
			String name = paramPair[0];
			String value = paramPair.length > 2 ? paramPair[1] : null;
			
			paramMap.put(name, value);
		}
		return paramMap;
		
	}

}

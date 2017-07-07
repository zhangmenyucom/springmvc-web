package com.taylor.common.utils;

import com.alibaba.fastjson.JSON;
import org.apache.logging.log4j.Logger;

/**
 * log4j2日志工具类
 * @author xiongmiao
 *
 */
public class Log4j2Util {

	/**
	 * debug
	 * message convert to json
	 * @param log
	 * @param message
	 */
	public static void debug2Json(Logger log, Object message){
		if(log.isDebugEnabled()){
			log.debug(JSON.toJSON(message));
		}
	}

	/**
	 * debug
	 * message convert to json
	 * @param log
	 * @param message
	 * @param params
	 */
	public static void debug2Json(Logger log, String message, Object params){
		if(log.isDebugEnabled()){
			log.debug(message, JSON.toJSON(params));
		}
	}
	
	/**
	 * debug
	 * message convert to json
	 * @param log
	 * @param message
	 */
	public static void debug(Logger log, Object message){
		if(log.isDebugEnabled()){
			log.debug(message);
		}
	}
	
	/**
	 * debug
	 * @param log
	 * @param message
	 * @param params
	 */
	public static void debug(Logger log, String message, Object... params){
		if(log.isDebugEnabled()){
			log.debug(message, params);
		}
	}
	
	/**
	 * info
	 * @param log
	 * @param message
	 * @param params
	 */
	public static void info(Logger log, String message, Object... params){
		if(log.isInfoEnabled()){
			log.info(message, params);
		}
	}

	/**
	 * warn
	 * @param log
	 * @param message
	 * @param params
	 */
	public static void warn(Logger log, String message, Object... params){
		if(log.isWarnEnabled()){
			log.warn(message, params);
		}
	}

	/**
	 * error
	 * @param log
	 * @param message
	 * @param params
	 */
	public static void error(Logger log, Throwable t, String message, Object... params){
		if(log.isErrorEnabled()){
			log.error(message, params);
			log.error(message, t);
		}
	}
	
}

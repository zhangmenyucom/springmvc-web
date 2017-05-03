package com.taylor.common;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import tk.mybatis.mapper.util.StringUtil;

public class JsonUtil {
	private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

	/**
	 * 把JSON字符串转换为指定的对象
	 * 
	 * @param jsonString
	 * @param valueType
	 * @return
	 */
	public static <T> T transferToObj(String jsonString, Class<T> valueType) {
		if (StringUtil.isEmpty(jsonString)) {
			throw new IllegalArgumentException("[Assertion failed] - the object argument must be blank");
		}

		T value = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
			mapper.configure(SerializationFeature.INDENT_OUTPUT, false);
			mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
			value = mapper.readValue(jsonString, valueType);
		} catch (JsonParseException e) {
			logger.error(e.getMessage(), "jsonString=[" + jsonString + "]");
		} catch (JsonMappingException e) {
			logger.error(e.getMessage(), "jsonString=[" + jsonString + "]");
		} catch (IOException e) {
			logger.error(e.getMessage(), "jsonString=[" + jsonString + "]");
		}

		return value;
	}

	/**
	 * 把指定的对象生成JSON字符串
	 * 
	 * @param value
	 * @return
	 */
	public static <T> String transfer2JsonString(T value) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
		StringWriter sw = new StringWriter();
		JsonGenerator gen;
		try {
			gen = new JsonFactory().createGenerator(sw);
			mapper.writeValue(gen, value);
			gen.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), "value=[" + value + "]");

		}

		return sw.toString();
	}

	/**
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String writeMap2JsonString(Map map) {
		if (CollectionUtils.isEmpty(map)) {
			return "";
		}
		String jsonString = "";
		try {
			ObjectMapper mapper = new ObjectMapper();
			jsonString = mapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage(), "map=[" + map + "]");
		}

		return jsonString;
	}

	/**
	 * 
	 * @param jsonString
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> readJson2Map(String jsonString) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(jsonString, Map.class);
		} catch (JsonParseException e) {
			logger.error(e.getMessage(), "jsonString=[" + jsonString + "]");
		} catch (JsonMappingException e) {
			logger.error(e.getMessage(), "jsonString=[" + jsonString + "]");
		} catch (IOException e) {
			logger.error(e.getMessage(), "jsonString=[" + jsonString + "]");
		}

		return null;
	}

	/**
	 * 对于复杂集合类的转换 例如：
	 * <p>
	 * <li>1.List<JavaBean>:
	 * 调用方法是：readJson2Map(jsonString,List.class,JavaBean.class);</li>
	 * <li>2.Map<JavaBean1,JavaBean2>:调用方法是:readJson2Map(jsonString,Map.class,
	 * JavaBean1.class,JavaBean2.class);</li>
	 * </p>
	 * 
	 * @param jsonString
	 * @return
	 */
	public static <T> T readJson2Collection(String jsonString, Class<?> parametrized, Class<?>... parameterClasses) {
		if (StringUtil.isEmpty(jsonString)) {
			return null;
		}

		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL);
			JavaType javaType = mapper.getTypeFactory().constructParametricType(parametrized, parameterClasses);
			return mapper.<T> readValue(jsonString, javaType);
		} catch (JsonParseException e) {
			logger.error(e.getMessage(), "jsonString=[" + jsonString + "]");
		} catch (JsonMappingException e) {
			logger.error(e.getMessage(), "jsonString=[" + jsonString + "]");
		} catch (IOException e) {
			logger.error(e.getMessage(), "jsonString=[" + jsonString + "]");
		}

		return null;
	}
}

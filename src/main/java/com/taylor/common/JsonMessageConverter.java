package com.taylor.common;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

public class JsonMessageConverter extends Jackson2JsonMessageConverter {

	@SuppressWarnings("unchecked")
	public static <T> T getObjectFromMessage(Message message) {
			return (T) JsonMessageConverter.getInstance().fromMessage(message);
	}

	public static JsonMessageConverter getInstance() {
		return JsonMessageConverterHolder.jsonMessageConverter;
	}

	private static class JsonMessageConverterHolder {
		private static final JsonMessageConverter jsonMessageConverter = new JsonMessageConverter();
	}
}
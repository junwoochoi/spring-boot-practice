package com.example.junsta.util;

import java.io.IOException;

import org.apache.ibatis.javassist.bytecode.SignatureAttribute.ClassType;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

	private static final ObjectMapper mapper = new ObjectMapper();
	
	public static String convertToString(Object object) throws JsonProcessingException {
		return mapper.writeValueAsString(object);
	}
	
	public static Object parseToObj(String str, Class<?> type) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(str, type);
	}
}

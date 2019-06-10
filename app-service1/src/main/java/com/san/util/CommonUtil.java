package com.san.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommonUtil {

	static Logger logger = LoggerFactory.getLogger(CommonUtil.class);

	public static ApplicationContext ctx;
	private static ObjectMapper objectMapper = new ObjectMapper();

	public static Object fetchBean(String beanId) {
		return ctx.getBean(beanId);
	}

	public static String fetchStringFromInputStream(InputStream inputStream) throws IOException {
		BufferedReader inStreamReader = new BufferedReader(new InputStreamReader(inputStream));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = inStreamReader.readLine()) != null) {
			response.append(inputLine);
		}
		inputStream.close();
		return response.toString();
	}

	public static String convertToJsonString(Object object) throws JsonGenerationException, JsonMappingException, IOException {
		String out = null;
		ByteArrayOutputStream byteOutputStream = null;
		try {
			byteOutputStream = new ByteArrayOutputStream();
			objectMapper.writeValue(byteOutputStream, object);
			out = byteOutputStream.toString();
		} finally {
			byteOutputStream.close();
		}
		return out;
	}

	public static void writeJson(Writer writer, Object object) throws JsonGenerationException, JsonMappingException, IOException {
		try {
			objectMapper.writeValue(writer, object);
		} finally {
			writer.close();
		}
	}

	public static String readStringFromReader(Reader reader) throws JsonParseException, JsonMappingException, IOException {
		StringBuilder stringBuilder = null;
		try {
			BufferedReader br = new BufferedReader(reader);
			String str = null;
			stringBuilder = new StringBuilder();
			while ((str = br.readLine()) != null) {
				System.out.println(str);
				stringBuilder.append(str);
			}
		} finally {
			reader.close();
		}
		return stringBuilder.toString();
	}

	@SuppressWarnings("rawtypes")
	public static Map readJsonMapFromReader(Reader reader) throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			BufferedReader br = new BufferedReader(reader);
			String str = null;
			StringBuilder stringBuilder = new StringBuilder();
			while ((str = br.readLine()) != null) {
				System.out.println(str);
				stringBuilder.append(str);
			}
			map = objectMapper.readValue(stringBuilder.toString(), new TypeReference<HashMap>() {
			});
		} finally {
			reader.close();
		}
		return map;
	}

	public static JsonNode readJsonNodeFromReader(Reader reader) throws JsonProcessingException, IOException {
		JsonNode jsonNode = null;
		try {
			jsonNode = objectMapper.readTree(reader);
		} finally {
			reader.close();
		}
		return jsonNode;
	}

	public static JsonNode fetchJSONResource(String resource) {
		JsonNode jsonNode = null;
		InputStream is = null;
		try {
			is = CommonUtil.class.getClassLoader().getResourceAsStream(resource);
			jsonNode = objectMapper.readTree(is);
			is.close();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {

		}
		return jsonNode;
	}

	public static String fetchResourceLocation() {
		String path = "";
		boolean resourceLocationFound = false;
		try {
			URL url = CommonUtil.class.getClassLoader().getResource("log4j.properties");
			if (url != null) {
				path = url.getPath();
				File file = new File(path);
				if (file.isFile()) {
					path = file.getParent();
					resourceLocationFound = true;
				}
			}
			if (!resourceLocationFound) {
				System.out.println("log4j.properties not found inside WEB-INF/classes");
				System.out.println("Other resources also may not found due to relative location configuration");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return path;
	}

	@SuppressWarnings("unchecked")
	public static Object bindJSONToObject(String jsonString, @SuppressWarnings("rawtypes") Class clazz) throws JsonParseException, JsonMappingException, IOException {
		return objectMapper.readValue(jsonString, clazz);
	}

	public static Type getGenericParameterTypeOfClass(Class<?> clazz, int parameterIndex) {
		return ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[parameterIndex];
	}

	public static Type getGenericParameterTypeOfInterface(Class<?> clazz, int parameterIndex) {
		return ((ParameterizedType) clazz.getGenericInterfaces()[0]).getActualTypeArguments()[parameterIndex];
	}
}

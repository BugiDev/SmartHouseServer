package rs.smart.house.util;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.ObjectMapper;

import rs.smart.house.exceptions.SenergyException;

/**
 * JSON Utilities
 */
public abstract class JsonUtil {

	/**
	 * Method: convertJsonToMap Description: Called to create a Map of Strings from a JSON String
	 * 
	 * @param json
	 *            JSON String
	 * @return Map <String, String> - Key-Value pair map representation of JSON
	 */
	public static Map<String, String> convertJsonToMap(String json) {
		Map<String, String> jsonProperties = new HashMap<String, String>();
		JsonFactory f = new JsonFactory();
		JsonParser jp;
		try {
			jp = f.createJsonParser(json);
			jp.nextToken(); // will return JsonToken.START_OBJECT (verify?)
			while (jp.nextToken() != JsonToken.END_OBJECT) {
				String fieldname = jp.getCurrentName();
				jp.nextToken(); // move to value, or START_OBJECT/START_ARRAY
				String value = jp.getText();
				jsonProperties.put(fieldname, value);
			}
			jp.close(); // ensure resources get cleaned up timely and properly
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonProperties;
	} // End convertJsonToMap

	/**
	 * Method serializes Map to JSON String
	 * 
	 * @param mapObj
	 *            - Map
	 * @return JSON String
	 * @throws SenergyException
	 */
	public static String convertMapToJSON(Map<String, Object> mapObj) throws SenergyException {
		ObjectMapper mapper = new ObjectMapper();
		Writer strWriter = new StringWriter();
		try {
			mapper.writeValue(strWriter, mapObj);
		} catch (IOException e) {
			throw new SenergyException(e.getMessage());
		}
		String jsonString = strWriter.toString();

		return jsonString;
	}

	/**
	 * Method serializes List of Maps to JSON String
	 * 
	 * @param fieldName
	 *            - name of the list field
	 * @param listMapObj
	 *            - list of Maps
	 * @return JSON String
	 * @throws SenergyException
	 */
	public static String convertListToJSON(String fieldName, List<Map<String, Object>> listMapObj) throws SenergyException {
		String returnValue = null;
		Writer strWriter = new StringWriter();
		JsonFactory jsonFactory = new JsonFactory();
		JsonGenerator jsonGenerator = null;

		try {
			jsonGenerator = jsonFactory.createJsonGenerator(strWriter);
			jsonGenerator.writeStartObject();
			jsonGenerator.writeArrayFieldStart(fieldName);
			for (Map<String, Object> mSO : listMapObj) {
				jsonGenerator.writeString(JsonUtil.convertMapToJSON(mSO));
			}
			jsonGenerator.writeEndArray();
			jsonGenerator.writeEndObject();
			jsonGenerator.flush();
			returnValue = strWriter.toString();
		} catch (IOException e) {
			throw new SenergyException(e.getMessage());
		} finally {
			if (jsonGenerator != null) {
				try {
					jsonGenerator.close();
				} catch (IOException e) {
					throw new SenergyException(e.getMessage());
				}
			}

			try {
				strWriter.close();
			} catch (IOException e) {
				throw new SenergyException(e.getMessage());
			}
		}

		return returnValue;
	}
	
	public static String convertObjectToJSON(Object obj) throws SenergyException {
		ObjectMapper mapper = new ObjectMapper();
		Writer strWriter = new StringWriter();
		try {
			mapper.writeValue(strWriter, obj);
		} catch (IOException e) {
			throw new SenergyException(e.getMessage());
		}
		String jsonString = strWriter.toString();

		return jsonString;
	}

}

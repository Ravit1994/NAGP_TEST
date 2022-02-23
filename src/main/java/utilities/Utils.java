package utilities;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Utils {

	public static String generateStringFromResource(String relativePath) throws IOException {
		relativePath = System.getProperty("user.dir") +  relativePath;
		return new String(Files.readAllBytes(Paths.get(relativePath)));
	}

	public static String getValueFromResponse(Response response, String path){
		JsonPath jsonPathEvaluator = response.jsonPath();
		String result  = jsonPathEvaluator.get(path);
		return result;
	}

//	public static String geRandomName(){
//
//		return result;
//
//	}
//public static Map<String, String> create_map_from_String() {
//	String value = "{first_name = naresh,last_name = kumar,gender = male}";
//	value = value.substring(1, value.length() - 1);
//	String[] keyValuePairs = value.split(",");
//	Map<String, String> map = new HashMap<>();
//
//		for (String pair : keyValuePairs)
//		{
//			String[] entry = pair.split("=");
//			map.put(entry[0].trim(), entry[1].trim());
//		}
//	return map;
//	}
}
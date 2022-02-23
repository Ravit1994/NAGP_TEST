package utilities;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class RestClientWrapper {

	public  String resource;
	public  String baseUrl;
	public  String key;
	public  String token;
	private  RequestSpecification request;
	private  Response restResponse;
	private static PrintStream log;

	static {
		try {
			log = new PrintStream(new FileOutputStream("log.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public RestClientWrapper(String baseUrl, RequestSpecification request, String key, String Authorization) throws FileNotFoundException {
//		PrintStream log;
		if (this.request == null) {

			this.request = request
				.baseUri(baseUrl)
//				.queryParam("key", key)
				.header("Authorization", Authorization)
					.header("Content-Type", "application/json")
				.filter(RequestLoggingFilter.logRequestTo(log))
				.filter(ResponseLoggingFilter.logResponseTo(log));

		}
	}
	public void log(){
//		request.filter(RequestLoggingFilter.logRequestTo(log))
//					.filter(ResponseLoggingFilter.logResponseTo(log));
	}
	// Http calls
	public Response get(String resource) throws Exception {

		restResponse = request
				.when().log().all().urlEncodingEnabled(false).get(resource)
				.then().log().all().extract().response();

		return restResponse;
	}

	public Response post(String resource, String bodyString) throws Exception {
//		File file = new File(System.getProperty("user.dir")+"/src/test/java/test_data/photo-1642059223867-6f077f07075c.jfif");
		restResponse = request.when().log().all().urlEncodingEnabled(false).body(bodyString)
//				.multiPart("file",file,"multipart/form-data")
				.post(resource)
				.then().log().all().extract().response();
		return restResponse;
	}

	public Response postwithfile(String resource, String bodyString) throws Exception {
		File file = new File(System.getProperty("user.dir")+"/src/test/java/test_data/photo-1642059223867-6f077f07075c.jfif");
		restResponse = request.when().log().all().urlEncodingEnabled(false).body(bodyString)
				.multiPart("file",file,"multipart/form-data")
				.post(resource)
				.then().log().all().extract().response();
		return restResponse;
	}

	public Response put(String resource, String bodyString) throws Exception {
		restResponse = request.when().log().all().urlEncodingEnabled(false).body(bodyString).put(resource)
				.then().log().all().extract().response();
		return restResponse;
	}

	public Response delete(String resource) throws Exception {
		restResponse = request.when().log().all().urlEncodingEnabled(false).delete(resource)
				.then().log().all().extract().response();
//		assertEquals(restResponse.getStatusCode(),200);
		return restResponse;
	}

	//Add additional parameters to request
	public void addAdditionalParameters(String key,String value){
//		Map<String,String> hm = new HashMap<String,String>();
//		hm.put(key,value);
		request.header("key","value");
	}

}

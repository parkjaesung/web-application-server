package http;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
 
public class ResponseFactory {
	
	public static Response get200Html(String content, String encoding){
		return get200Html(content,encoding,null);
	}
	
	public static Response get200Html(String content, String encoding, Map<String,String> cookie){
		return createSimpleResponse("200", content, encoding, cookie, null);
	}
	
	public static Response get200Json(Object data, String encoding){
		return get200Json(data, encoding, null);
	}
	
	public static Response get200Json(Object data, String encoding, Map<String,String> cookie){
		Gson gson = new GsonBuilder().create();
		String content = gson.toJson(data);
		
		return createSimpleResponse("200", content, encoding, cookie, null, "application/json");
	}
	
	public static Response get302(String location, String encoding){
		return get302(location, encoding, null);
	}
	
	public static Response get302(String location, String encoding, Map<String,String> cookie){
		return createSimpleResponse("302", null, encoding, cookie, location);
	}
	
	public static Response get400Html(String encoding){
		return createSimpleResponse("400","400",encoding);
	}
	
	public static Response get404Html(String encoding){
		return createSimpleResponse("404","404",encoding);
	}
	
	public static Response get500Html(String encoding){
		return createSimpleResponse("500","500",encoding);
	}
	
	
	
	private static Response createSimpleResponse(String statusCode, String body, String encoding){
		return createSimpleResponse(statusCode, body, encoding, null);
	}
	
	private static Response createSimpleResponse(String statusCode, String body, String encoding, Map<String,String> cookie){
		return createSimpleResponse(statusCode, body, encoding, cookie, null);
	}
	
	private static Response createSimpleResponse(String statusCode, String body, String encoding, Map<String,String> cookie, String location){
		return createSimpleResponse(statusCode, body, encoding, cookie, location, "text/html");
	}
	
	private static Response createSimpleResponse(String statusCode, String body, String encoding, Map<String,String> cookie, String location, String contentType){
		byte[] bodyBytes = (body != null) ? body.getBytes() : "".getBytes();
		Header header = Header.Builder
				.statusCode(statusCode)
				.contentType(contentType)
				.length(bodyBytes.length)
				.encoding(encoding)
				.location(location)
				.cookie(cookie)
				.build();
		
		return new Response(header, bodyBytes);
	}
}
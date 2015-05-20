package http;

import java.util.Map;

//TODO POJO를 받아서 JSON형식으로 return할 수 있도록 개선할 것. 
public class ResponseFactory {
	
	public static Response get200Html(String content, String encoding){
		return get200Html(content,encoding,null);
	}
	
	public static Response get200Html(String content, String encoding, Map<String,String> cookie){
		return createSimpleResponse("200", content, encoding, cookie, null);
	}
	
	public static Response get302(String location, String encoding){
		return get302(location, encoding, null);
	}
	
	public static Response get302(String location, String encoding, Map<String,String> cookie){
		return createSimpleResponse("302", null, encoding, cookie, location);
	}
	
	public static Response get400Html(String encoding){
		return createSimpleResponse("400","400",encoding, null, null);
	}
	
	public static Response get404Html(String encoding){
		return createSimpleResponse("404","404",encoding, null, null);
	}
	
	public static Response get500Html(String encoding){
		return createSimpleResponse("500","500",encoding, null, null);
	}
	
	private static Response createSimpleResponse(String statusCode, String body, String encoding, Map<String,String> cookie, String location){
		byte[] bodyBytes = body.getBytes();
		Header header = Header.Builder
				.statusCode(statusCode)
				.contentType("text/html")
				.length(bodyBytes.length)
				.encoding(encoding)
				.cookie(cookie)
				.build();
		
		return new Response(header, bodyBytes);
	}
}
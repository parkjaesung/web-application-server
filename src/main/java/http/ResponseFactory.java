package http;

import java.util.Map;

public class ResponseFactory {
	
	public static Response get200Html(String content, String encoding){
		return get200Html(content,encoding,null);
	}
	public static Response get200Html(String content, String encoding, Map<String,String> cookie){
		byte[] body = content.getBytes();
		Header header = Header.Builder
				.statusCode("200")
				.contentType("text/html")
				.length(body.length)
				.encoding(encoding)
				.cookie(cookie)
				.build();
		
		return new Response(header, body);
	}
	
	public static Response get302(String location, String encoding){
		return get302(location, encoding, null);
	}
	
	public static Response get302(String location, String encoding, Map<String,String> cookie){
		Header header = Header.Builder
				.statusCode("302")
				.location(location)
				.encoding(encoding)
				.cookie(cookie)
				.build();
		
		return new Response(header, null);
	}
	
	public static Response get400Html(String encoding){
		byte[] body = "400".getBytes();
		Header header = Header.Builder
				.statusCode("403")
				.contentType("text/html")
				.length(body.length)
				.encoding(encoding)
				.build();
		
		return new Response(header, body);
	}
	
	public static Response get404Html(String encoding){
		byte[] body = "404".getBytes();
		Header header = Header.Builder
				.statusCode("404")
				.contentType("text/html")
				.length(body.length)
				.encoding(encoding)
				.build();
		
		return new Response(header, body);
	}
	
	public static Response get500Html(String encoding){
		byte[] body = "500".getBytes();
		Header header = Header.Builder
				.statusCode("500")
				.contentType("text/html")
				.length(body.length)
				.encoding(encoding)
				.build();
		
		return new Response(header, body);
	}
	
	
	
	
}

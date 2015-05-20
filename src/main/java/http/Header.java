package http;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Header {
	
	private static final Map<String, String> status = new HashMap<String, String>();
	static{
		status.put("200", "Ok");
		status.put("302", "Found");
		status.put("400", "Bad Request");
		status.put("403", "Forbidden");
		status.put("404", "Not Found");
		status.put("500", "Internal Server Error");
	}

	private final String statusCode;
	private final String location;
	private final String contentType;
	private final Map<String,String> cookie;
	private final Integer length;
	private final String encoding;
	
	private Header(Builder builder){
		this.statusCode = builder.statusCode;
		this.location = builder.location;
		this.contentType = builder.contentType;
		this.length = builder.length;
		this.encoding = builder.encoding;
		this.cookie = builder.cookie;
	}
	
	public static class Builder {
		private String statusCode;
		private String location;
		private String contentType;
		private Map<String,String> cookie;
		private Integer length;
		private String encoding;
		
		public static Builder n() {
			return new Builder();
		}

		public Builder statusCode(String statusCode) {
			this.statusCode = statusCode;
			return this;
		}
		
		public Builder location(String location) {
			this.location = location;
			return this;
		}
		
		public Builder contentType(String contentType) {
			this.contentType = contentType;
			return this;
		}
		
		public Builder cookie(Map<String,String> cookie) {
			this.cookie = cookie;
			return this;
		}
		
		public Builder length(int length) {
			this.length = length;
			return this;
		}
		
		public Builder encoding(String encoding) {
			this.encoding = encoding;
			return this;
		}

		public Header build(){
			return new Header(this);
		}
	}
	
	

	public byte[] getBytes(){
	
		String headerString = "";
		if(statusCode != null)
			headerString += "HTTP/1.1 " + statusCode + " "+status.get(statusCode)+" \r\n";
		if(location != null)
			headerString += "Location: " + location + "\r\n";
		if(contentType != null)
			headerString += "Content-Type: " + contentType + ";";
		if(encoding != null)
			headerString += "charset="+ encoding +"\r\n";
		if(length != null)
			headerString += "Content-Length: " + length + "\r\n";
		if(cookie!= null){
			headerString += "Set-Cookie: ";
			for(Map.Entry<String, String> entry : cookie.entrySet()){
				headerString += entry.getKey() +"="+ entry.getValue()+";";
			}
			headerString += "\r\n";
		}
		
		//header 마지막
		headerString += "\r\n";

		try {
			return headerString.getBytes(encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
}

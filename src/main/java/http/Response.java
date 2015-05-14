package http;

public class Response {
	private Header header;
	private byte[] body;

	public Response(Header header, byte[] body) {
		this.header = header;
		this.body = body;
	}

	public byte[] getHeader() {
		return header.getBytes();
	}

	public byte[] getBody() {
		if(body == null)
			return "".getBytes();
		
		return body;
	}	
}

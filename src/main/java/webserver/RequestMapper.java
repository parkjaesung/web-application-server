package webserver;

import http.Header;
import http.Request;
import http.Response;
import http.ResponseFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import controller.Controller;

public class RequestMapper {
	String root;
	String encoding;
	Map<String,Controller> requestMapping;
	
	public RequestMapper(String root, String encoding){
		this(root, null, encoding);
	}
	
	public RequestMapper(String root, Map<String,Controller> requsetMapping, String encoding){
		this.root = root;
		this.encoding = encoding;
		this.requestMapping = requsetMapping;
	}

	public Response getResponse(Request rq){
		Controller controller;
		
		//invalid request면 400
		if(!rq.isValid()) return ResponseFactory.get400Html("UTF-8");
		
		//uri가 requestmapping 에 있을 경우 controller연결
		if(this.requestMapping != null && (controller = requestMapping.get(rq.getFileName())) != null){
			return controller.service(rq);
			
		//uri가 requestmapping 에 없을 경우 파일 연결
		}else{
			try {
				byte[] body = Files.readAllBytes(Paths.get(root + rq.getFilePath()));
				Header header = Header.Builder.n()
						.statusCode("200")
						.contentType(rq.getAccept())
						.encoding(encoding)
						.length(body.length)
						.build();
				
				return new Response(header,body);
			} catch (IOException e) {
				//파일 없으면 404
				return ResponseFactory.get404Html("UTF-8");
			}
		}
	}
}

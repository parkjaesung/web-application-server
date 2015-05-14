package webserver;

import http.Response;
import http.Header;
import http.Request;
import http.ResponseFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.Controller;

public class RequestMapper {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	
	String root;
	String encoding;
	Map<String,String> requestMapping;
	
	
	public RequestMapper(String root, String encoding){
		this(root, null, encoding);
	}
	
	public RequestMapper(String root, Map<String,String> requsetMapping, String encoding){
		this.root = root;
		this.encoding = encoding;
		this.requestMapping = requsetMapping;
	}
	

	@SuppressWarnings("unchecked")
	public Response getResponse(Request rq){
		String controllerName;
		
		if(!rq.isValid()) return ResponseFactory.get400Html("UTF-8");
		
		if(this.requestMapping != null && (controllerName = requestMapping.get(rq.getFileName())) != null){
			try {
				Class<Controller> controller = (Class<Controller>) Class.forName("controller."+controllerName);
				return controller.newInstance().service(rq);
				
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IOException e) {
				log.debug("controller Mapping error, there is no controller {}.",controllerName);
				return ResponseFactory.get500Html("UTF-8");
			}			
		}else{
			try {
				byte[] body = Files.readAllBytes(Paths.get(root + rq.getFilePath()));
				Header header = Header.Builder.statusCode("200").contentType(rq.getAccept()).encoding(encoding).length(body.length).build();
				return new Response(header,body);
			} catch (IOException e) {
				return ResponseFactory.get404Html("UTF-8");
			}
		}
	}
}

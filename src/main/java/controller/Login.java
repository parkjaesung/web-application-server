package controller;

import http.Request;
import http.Response;
import http.ResponseFactory;

import java.util.HashMap;
import java.util.Map;

import model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webserver.RequestHandler;
import db.UserDb;

public class Login implements Controller {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	
	@Override
	public Response service(Request rq){
		Map<String,String> params = rq.getParams(); 
		
		log.debug("controller loginController, param : {}.",params);
		
		if(params != null){
			String userId = params.get("userId");
			String password = params.get("password");	
			User user = UserDb.data.get(userId);
			
			if(user == null){
				return ResponseFactory.get302("/login.html?invalid=userId","UTF-8");
			}
			
			if(!user.getPassword().equals(password)){
				return ResponseFactory.get302("/login.html?invalid=password","UTF-8");
			}
			
			log.debug("{} logged in.", user.toString());
			
			Map<String,String> cookie = new HashMap<String,String>();
			cookie.put("logined", "true");

			return ResponseFactory.get302("/index.html","UTF-8",cookie);
		}else{
			return ResponseFactory.get400Html("UTF-8");
		}
	}
}

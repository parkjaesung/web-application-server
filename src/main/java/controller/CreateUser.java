package controller;

import http.Request;
import http.Response;
import http.ResponseFactory;

import java.util.Map;

import model.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.Controller.Urlmap;
import webserver.RequestHandler;
import db.UserDb;

@Urlmap(url="create")
public class CreateUser implements Controller {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

	@Override
	public Response service(Request rq){
		Map<String,String> params = rq.getParams(); 
		
		log.debug("controller userController, param : {}.",params);
		if(params != null){
			String userId = params.get("userId");
			String password = params.get("password");
			String name = params.get("name");
			String email = params.get("email");
			
			
			User user = new User(userId,password,name,email);
			UserDb.data.put(userId, user);
			log.debug("added {}.",user.toString());

			return ResponseFactory.get302("/index.html","UTF-8");
		}else{
			return ResponseFactory.get400Html("UTF-8");
		}		
	}
}

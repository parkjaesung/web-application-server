package controller;

import http.Request;
import http.Response;
import http.ResponseFactory;

public class PrintCookie implements Controller{

	@Override
	public Response service(Request rq){
		return ResponseFactory.get200Html(rq.getCookie().toString(), "UTF-8");
	}
}

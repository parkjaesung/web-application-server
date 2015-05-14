package controller;

import http.Request;
import http.Response;
import http.ResponseFactory;

import java.io.IOException;

public class PrintCookie implements Controller{

	@Override
	public Response service(Request rq) throws IOException {
		return ResponseFactory.get200Html(rq.getCookie().toString(), "UTF-8");
	}
}

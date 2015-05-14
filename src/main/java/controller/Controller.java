package controller;

import http.Response;
import http.Request;

import java.io.IOException;


public interface Controller {	
	public abstract Response service(Request rq) throws IOException;
}

package controller;

import http.Request;
import http.Response;


public interface Controller {	
	Response service(Request rq);
}

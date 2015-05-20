package webserver;

import http.Request;
import http.Response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	
	private Socket connection;

	public RequestHandler(Socket connectionSocket) {
		this.connection = connectionSocket;
	}

	public void run() {
		log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
		
		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			DataOutputStream dos = new DataOutputStream(out);
			RequestMapper rm = new RequestMapper("webapp", RequestMap.getMap(), "UTF-8");
			Request rq = new Request(in);
			response(dos, rm.getResponse(rq));
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	//response객체를 받아서 header와 body를 차례로 전송
	private void response(DataOutputStream dos, Response rp) {
		try {
			byte[] header = rp.getHeader();
			byte[] body = rp.getBody();
			dos.write(header, 0, header.length);
			dos.write(body, 0, body.length);
			dos.writeBytes("\r\n");
			dos.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}

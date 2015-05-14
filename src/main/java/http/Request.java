package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpRequestUtils;
import util.IOUtils;
import webserver.RequestHandler;

//대체로 getter만 만들었는데, 단위테스트를 위해서 setter도 만들어 두는것이 타당한지??
//field가 너무 많은것 처럼 느껴지는데 개선할 방법이 있는지?
public class Request{
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	
	private BufferedReader inputReader;
	private int contentLength;
	private String urlHeader;
	private String url;
	private String method;
	private String path;
	private String fileName;
	private String query;
	private String cookieString;
	private Map<String, String> cookie;
	private String accept;
	private String body;
	private Map<String, String> params;
	private Boolean valid;
	
	public Request(InputStream in){
		try {
			inputReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			urlHeader = inputReader.readLine();
			log.debug("url {} requested",urlHeader);
			
			//아래 private 함수들은 클래스 내부에서만 쓸 거라서 클래스의 필드들을 활용하도록 하고
			//파라미터 없는 void() 함수로 했는데 괜찮은지??
			//여기에서 try/catch로 처리하기 편하게 하려고 전부 throw 하도록 했는데, return boolean으로 처리하는게 나은지?
			parseUrl(); 
			parseHeader();
			getBody();
			
		} catch (IOException e) {
			//헤더가 형식에 안맞을 경우 invalid 처리. rqMapper에서 bad request로 넘김.
			log.debug("wrong header!");
			valid = false;
		}
		valid = true;
	}
	
	public void parseUrl() throws IOException{
		if(urlHeader == null) throw new IOException();
		
		String[] splited = urlHeader.split(" ");
		if(splited.length < 2)  throw new IOException();
		
		method = splited[0];
		url = splited[1];

		Pattern pattern = Pattern.compile("(.*\\/)([^\\/?]*)\\??(.*$)");
		Matcher matcher = pattern.matcher(this.url);
		
		if(matcher.matches()){
			path = matcher.group(1);
			fileName = matcher.group(2);
			query = matcher.group(3);
			
			if(method.equals("GET") && query.length() > 0){
				params = HttpRequestUtils.parseQueryString(this.query);
			}
		}else{
			 throw new IOException();
		}
	}
	
	public void parseHeader() throws IOException{
		String line = null;
		
		while(!"".equals(line)){
			line = inputReader.readLine();
			
			switch(line.split(": ")[0]){
			case "Content-Length":
				contentLength = Integer.parseInt(line.split(": ")[1]);
				break;
			
			case "Accept":
				accept = line.split(": ")[1];
				break;
			
			case "Cookie":
				cookieString = line.split(": ")[1];
				parseCookie();
				break;
			}
		}
	}
	
	public void parseCookie(){
		if(cookieString == null) return;
		
		cookie = new HashMap<String,String>();
		Pattern pattern = Pattern.compile("([^=]+)=([^;]+);?");
		Matcher matcher = pattern.matcher(cookieString);
	
		while(matcher.find()){
			if(matcher.group(1) != null && matcher.group(2) != null){
				cookie.put(matcher.group(1),matcher.group(2));
			}
		}
	}
	
	private void getBody() throws IOException {
		body = IOUtils.readData(inputReader, contentLength);
		
		if(method.equals("POST")){
			params =  HttpRequestUtils.parseQueryString(body);	
		}
	}

	public String getUrl(){
		return url;
	}
	
	public String getMethod() {
		return method;
	}

	public String getPath() {
		return path;
	}

	public String getFileName() {
		return fileName;
	}

	public String getQuery() {
		return query;
	}
	
	public String getFilePath() {
		return path+fileName;
	}
	
	public String getAccept() {
		return accept;
	}

	public Map<String,String> getParams(){
		return params;
	}
	
	public Map<String,String> getCookie() {
		return cookie;
	}
	
	public Boolean isValid(){
		return valid;
	}
}
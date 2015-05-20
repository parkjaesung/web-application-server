package webserver;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import controller.Controller;

public class RequestMap {
	private static Map<String, Controller> map = new HashMap<String, Controller>();
	
	public static Map<String,Controller> getMap(){
		return map;
	}
	
	public static void setMap(Map<String,Controller> newMap){
		for(Entry<String, Controller> pair : newMap.entrySet()){
			map.put(pair.getKey(), pair.getValue());	
		}
	}
}

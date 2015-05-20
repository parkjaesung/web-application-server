package webserver;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import controller.Controller;
import controller.Controller.Urlmap;

public class RequestMap {
	private static final Logger log = LoggerFactory.getLogger(RequestMap.class);
	private static Map<String, Controller> map = new HashMap<String, Controller>();
	
	static{
		//automatically map controllers by annotation
		Class<Urlmap> annotation = Urlmap.class;
		Reflections reflections = new Reflections("controller");
		Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(annotation);
		
		for(Class<?> controller : annotated){
			Urlmap urlMap = (Urlmap)controller.getAnnotation(annotation);
			try {
				map.put(urlMap.url(), (Controller) controller.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				log.debug("annotation url mapping error!");
				e.printStackTrace();
			}
		}
		
	}
	
	public static Map<String,Controller> getMap(){
		return map;
	}
	
	public static void setMap(Map<String,Controller> newMap){
		for(Entry<String, Controller> pair : newMap.entrySet()){
			map.put(pair.getKey(), pair.getValue());	
		}
	}
	
	//xml file controller mapping
	@SuppressWarnings("unchecked")
	public static void setMap(File xmlFile){
		try {
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);		
			XPath xpath = XPathFactory.newInstance().newXPath();
			
			//<controller>
			NodeList controllers = (NodeList)xpath.evaluate("//controller", document, XPathConstants.NODESET);
			for(int i= 0; i< controllers.getLength(); i++){
				NodeList properties = controllers.item(i).getChildNodes();
				String controllerName = null;
				Controller controller = null;
				
				for(int j= 0; j< properties.getLength(); j++){
					Node property = properties.item(j);
					switch(property.getNodeName()){
					
					//<controller-name>
					case "controller-name":
						controllerName = property.getTextContent();
						break;
						
					//<controller-class>
					case "controller-class":
						try{
							
							//java reflection. 클래스 인스턴스를 생성.
							String path = "controller."+property.getTextContent();
							Class<Controller> controllerClass = (Class<Controller>) Class.forName(path);
							controller = controllerClass.newInstance();
						}catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
							log.debug("controller Class \"{}\" not Found!", property.getTextContent());
							e.printStackTrace();
						}
						break;
					}
				}
				
				//맵에 집어넣는다.
				if(controllerName != null && controller != null){
					map.put(controllerName, controller);
					log.debug("Controller \"{}\" registered at url \"{}\".",controller.getClass().getTypeName() , controllerName);
				}
			}
			
		}catch (SAXException | IOException | ParserConfigurationException | XPathExpressionException | DOMException e) {
			log.debug("error reading xml file!");
			e.printStackTrace();
		}
	}
}
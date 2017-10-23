package cmpe202.project.webserver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

public class WebDBUtility {
	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	private File webDBFile;
	private Document doc;
	private String userWithApps;
	
	HashMap<String, WebDBEntry> webAppList;
	HashMap<String,LinkedList<WebDBEntry>> userWebAppList;
	
	
	public WebDBUtility() throws ParserConfigurationException, SAXException, IOException, TransformerException{
		factory = DocumentBuilderFactory.newInstance();
		builder = factory.newDocumentBuilder();
		if(isWebDBExist()){
			webDBFile = new File("webDB.xml");
			doc = builder.parse(webDBFile);
		}else{
			initWebDB();
		}
		initWebAppList();
		getUserWebAppList();
	}
	
	public String getWebAppStatus(String url){

		if(url.equals("/"))
			url = "ROOT";
		else
			url = url.substring(1,url.length());
		
		if(url.contains("?"))
			url = url.substring(0, url.indexOf("?"));
		if(url.contains("/"))
			url = url.substring(0, url.indexOf("/"));
		else if(url.contains("."))
			url = "ROOT";
		
		if(webAppList.containsKey(url)){
			WebDBEntry w = webAppList.get(url); 
			return w.getStatus();
		}
		
		return null;
	}
	
	public boolean isWebDBExist(){
		File contextDir = new File(Constants.APP_HOME);
		for(File file : contextDir.listFiles()){
			if(file.isFile() && (file.getName().equalsIgnoreCase("webDB.xml"))){
				return true;
			}
		}
		return false;
	}

	public void initWebDB() throws TransformerException {
		doc = builder.newDocument();
		Element root = doc.createElement("WebDB");
		doc.appendChild(root);
		SaveToXML();
	}
	
	public boolean isWebAppExit(String webAppName){
		if(isWebDBExist())
			return webAppList.containsKey(webAppName);

		return false;
	}
	
	public boolean isDirExit(String dir){
		File contextDir = new File(Constants.STATIC_FILE_HOME);
		for(File file : contextDir.listFiles()){
			if(file.isDirectory() && (file.getName().equalsIgnoreCase(dir))){
				return true;
			}
		}
		return false;
	}
	
	private void initWebAppList(){
		webAppList = new HashMap<String,WebDBEntry>();
		doc.getDocumentElement().normalize();
		Element root = doc.getDocumentElement();
		NodeList nodeList = root.getChildNodes();
		
		for(int i=0;i<nodeList.getLength();i++){
			Node nNode = nodeList.item(i);
			if(nNode.getNodeType() == Node.ELEMENT_NODE){
				Element element = (Element) nNode;
				String name = element.getElementsByTagName("name").item(0).getTextContent();
				String username = element.getElementsByTagName("username").item(0).getTextContent();
				String status = element.getElementsByTagName("status").item(0).getTextContent();
				String welcomeFile = element.getElementsByTagName("welcomeFile").item(0).getTextContent();
				webAppList.put(name,new WebDBEntry(name, status, username, welcomeFile));
			}
		}
	}
	
	public HashMap<String,WebDBEntry> getAllWebApp(){
		return webAppList;
	}
	
	public HashMap<String,LinkedList<WebDBEntry>> getUserWebAppList() throws ParserConfigurationException, SAXException, IOException, TransformerException{
		userWebAppList = new HashMap<String,LinkedList<WebDBEntry>>();
		
		ArrayList<String> userList = new UserDBUtility().getUserList();
		StringBuilder strlist = new StringBuilder();

		for(String u : userList){
			LinkedList<WebDBEntry> list = new LinkedList<WebDBEntry>();
			StringBuilder tmp = new StringBuilder();
			for(Entry<String,WebDBEntry> entry: webAppList.entrySet()){
				if(entry.getValue().getUsername().equals(u)){
					list.add(entry.getValue());
					tmp.append(entry.getValue().getName() + "," + entry.getValue().getStatus() + ";");
				}
			}
			strlist.append(u + ":" + tmp.toString() + "/");
			userWebAppList.put(u, list);
		}
		
		userWithApps = strlist.toString();
		return userWebAppList;
	}
	
	public String userWithAppList(){
		return userWithApps;
	}
	
	public LinkedList<WebDBEntry> getUserWebAppList(String user){
		return userWebAppList.get(user);
	}
	
	public boolean deleteWebDBEntry(String webAppName) throws TransformerException, IOException{
		if(isDirExit(webAppName) && webAppList.containsKey(webAppName)){
			deleteFromXML(webAppName);
			File temp = new File(Constants.STATIC_FILE_HOME + "\\" + webAppName);
			FileUtils.deleteDirectory(temp);
			HttpServer server = new ServerContext().getServer();
			HttpContext httpContext = StartServer.getContext(webAppName);	
			server.removeContext(httpContext);
			webAppList.remove(webAppName);
			return true;
		}else if(isDirExit(webAppName)==false && webAppName.contains(webAppName)){
			deleteFromXML(webAppName);
			webAppList.remove(webAppName);
			return true;
		}
		
		return false;
	}
	
	public void deleteFromXML(String webAppName) throws TransformerException{
		Element root = doc.getDocumentElement();
		NodeList nodeList = root.getChildNodes();
		
		for(int i=0;i<nodeList.getLength();i++){
			Node nNode = nodeList.item(i);
			if(nNode.getNodeType() == Node.ELEMENT_NODE){
				Element element = (Element) nNode;
				String name = element.getElementsByTagName("name").item(0).getTextContent();
				if(name.equalsIgnoreCase(webAppName)){
					element.getParentNode().removeChild(element);
					doc.normalize();
					SaveToXML();
				}
			}
		}
	}
	
	public void addWebDBEntry(String webAppName, String username, String status, String welcomeFile) throws TransformerException{
		if(isWebDBExist())
		{
			Element root = doc.getDocumentElement();
			Element webApp = doc.createElement("webapp");
			
			Element w = doc.createElement("name");
			w.setTextContent(webAppName);
			
			Element u = doc.createElement("username");
			u.setTextContent(username);
			
			Element s = doc.createElement("status");
			s.setTextContent(status);
			
			Element wf = doc.createElement("welcomeFile");
			wf.setTextContent(status);

			webApp.appendChild(w);
			webApp.appendChild(u);
			webApp.appendChild(s);
			webApp.appendChild(wf);
			
			root.appendChild(webApp);
	
			SaveToXML();
			webAppList.put(webAppName, new WebDBEntry(webAppName, username, status, welcomeFile));
		}
	}
	
	public WebDBEntry getWebAppEntry(String name){
		if(isWebAppExit(name))
			return webAppList.get(name);
		return null;
	}
	
	public boolean updateWebAppRunState(String webAppName, String status) throws TransformerException{
		if(isWebAppExit(webAppName)){
			WebDBEntry entry = webAppList.get(webAppName);
			entry.setStatus(status);
			updateWebDBEntry(webAppName, status);
			return true;
		}
		return false;
	}
	
	public void updateWebDBEntry(String webAppName, String status) throws TransformerException{
		Element root = doc.getDocumentElement();
		NodeList nodeList = root.getChildNodes();
		
		for(int i=0;i<nodeList.getLength();i++){
			Node nNode = nodeList.item(i);
			if(nNode.getNodeType() == Node.ELEMENT_NODE){
				Element element = (Element) nNode;
				String name = element.getElementsByTagName("name").item(0).getTextContent();
				if(name.equalsIgnoreCase(webAppName)){
					element.getElementsByTagName("status").item(0).setTextContent(status);
					doc.normalize();
					SaveToXML();
				}
			}
		}
	}
	
	public void SaveToXML() throws TransformerException{
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File("webDB.xml"));
        transformer.transform(source, result);
	}	
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, JAXBException, TransformerException{
		WebDBUtility u = new WebDBUtility();
//		u.addWebDBEntry("kognito", "mmgehlot", "Start","index.html");

//		u.getAllWebApp();
		LinkedList<WebDBEntry> list = u.getUserWebAppList().get("mmgehlot");
		for(WebDBEntry w : list)
			System.out.println(w.getName());
//		System.out.println(u.getWebAppStatus("/kognito"));
	}

}

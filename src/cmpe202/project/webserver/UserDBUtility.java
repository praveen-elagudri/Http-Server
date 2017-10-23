package cmpe202.project.webserver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class UserDBUtility {
	private DocumentBuilderFactory factory;
	private DocumentBuilder builder;
	private File userDBFile;
	private Document doc;
	private ArrayList<String> list;
	HashMap<String,UserDBEntry> userList;
	
	public UserDBUtility() throws ParserConfigurationException, SAXException, IOException, TransformerException{
		
		factory = DocumentBuilderFactory.newInstance();
		builder = factory.newDocumentBuilder();

		if(isUserDBExist()){
			userDBFile = new File("userDB.xml");
			doc = builder.parse(userDBFile);
		}else{
			initUserDB();
		}
		
		initUserList();
	}
	
	public boolean isUserDBExist(){
		File contextDir = new File(Constants.APP_HOME);
		for(File file : contextDir.listFiles()){
			if(file.isFile() && (file.getName().equalsIgnoreCase("userDB.xml"))){
				return true;
			}
		}
		return false;
	}
	
	private void initUserList(){
		userList = new HashMap<String,UserDBEntry>(); 
		doc.getDocumentElement().normalize();
		Element root = doc.getDocumentElement();
		list = new ArrayList<String>();
		NodeList nodeList = root.getChildNodes();
		
		for(int i=0;i<nodeList.getLength();i++){
			Node nNode = nodeList.item(i);
			if(nNode.getNodeType() == Node.ELEMENT_NODE){
				Element element = (Element) nNode;
				String name = element.getElementsByTagName("username").item(0).getTextContent();
				String password = element.getElementsByTagName("password").item(0).getTextContent();
				String role = element.getElementsByTagName("role").item(0).getTextContent();
				userList.put(name,new UserDBEntry(name, password, role));
				list.add(name);
			}
		}
	}
	
	public HashMap<String,UserDBEntry> getAllUser(){
		return userList;
	}
	
	public ArrayList<String> getUserList(){
		return list;
	}

	public void initUserDB() throws TransformerException {
		doc = builder.newDocument();
		Element root = doc.createElement("UserDB");
		doc.appendChild(root);
		SaveToXML();
	}
	
	public boolean isUserExit(String username){
		HashMap<String,UserDBEntry> hmap = getAllUser();
		if(isUserDBExist()){
			return hmap.containsKey(username);
		}
		return false;
	}
	
	
	public void updateUserDBEntry(String username, String password) throws TransformerException{
		Element root = doc.getDocumentElement();
		NodeList nodeList = root.getChildNodes();
		
		for(int i=0;i<nodeList.getLength();i++){
			Node nNode = nodeList.item(i);
			if(nNode.getNodeType() == Node.ELEMENT_NODE){
				Element element = (Element) nNode;
				String name = element.getElementsByTagName("username").item(0).getTextContent();
				if(name.equalsIgnoreCase(username)){
					element.getElementsByTagName("password").item(0).setTextContent(password);
					doc.normalize();
					SaveToXML();
				}
			}
		}
		
	}
	
	public void deleteUserDBEntry(String username) throws TransformerException{
		Element root = doc.getDocumentElement();
		NodeList nodeList = root.getChildNodes();
		
		for(int i=0;i<nodeList.getLength();i++){
			Node nNode = nodeList.item(i);
			if(nNode.getNodeType() == Node.ELEMENT_NODE){
				Element element = (Element) nNode;
				String name = element.getElementsByTagName("username").item(0).getTextContent();
				if(name.equalsIgnoreCase(username)){
					element.getParentNode().removeChild(element);
					doc.normalize();
					SaveToXML();
				}
			}
		}
	}
	
	public void addUserDBEntry(String username, String password, String role) throws TransformerException{
		if(isUserDBExist())
		{
			Element root = doc.getDocumentElement();
			Element user = doc.createElement("user");
			
			Element u = doc.createElement("username");
			u.setTextContent(username);
			
			Element p = doc.createElement("password");
			p.setTextContent(password);
			
			Element r = doc.createElement("role");
			r.setTextContent(role);
			
			user.appendChild(u);
			user.appendChild(p);
			user.appendChild(r);
			
			root.appendChild(user);
	
			SaveToXML();
		}
	}
	
	public void SaveToXML() throws TransformerException{
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File("userDB.xml"));
        transformer.transform(source, result);
	}	
	
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, JAXBException, TransformerException{
		UserDBUtility u = new UserDBUtility();
//		u.deleteUserDBEntry("Manmohan");
		u.addUserDBEntry("Manmohan", "abc", "manager");
//		u.updateUserDBEntry("Manmohan", "xyz");
		
//		for(UserDBEntry user : u.getAllUser()){
//			System.out.println(user.getName() + " : " + user.getPassword() +" : " + user.getRole());
//		}
	}
}

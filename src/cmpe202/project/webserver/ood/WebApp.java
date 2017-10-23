package cmpe202.project.webserver.ood;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import cmpe202.project.webserver.WebDBEntry;
import cmpe202.project.webserver.WebDBUtility;

public class WebApp {
	WebDBUtility w; 
	
	public WebApp() throws ParserConfigurationException, SAXException, IOException, TransformerException{
		w = new WebDBUtility();
	}
	
	public boolean start(String appName){
		WebDBEntry entry = w.getWebAppEntry(appName);
		try {
			return w.updateWebAppRunState(entry.getName(), "Start");
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean stop(String appName){
		WebDBEntry entry = w.getWebAppEntry(appName);
		try {
			return w.updateWebAppRunState(entry.getName(), "Stop");
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean Undeploy(String appName) throws TransformerException, IOException{
		return w.deleteWebDBEntry(appName);
	}
}

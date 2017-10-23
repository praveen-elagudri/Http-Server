package cmpe202.project.webserver.ood;

import java.io.IOException;

import javax.xml.transform.TransformerException;

public class UndeployWebApp implements Command {
	private WebApp webApp;
	
	public UndeployWebApp(WebApp webApp){
		this.webApp = webApp;
	}
	
	public boolean execute(String appName) throws Exception{
		return webApp.Undeploy(appName);
	}
}

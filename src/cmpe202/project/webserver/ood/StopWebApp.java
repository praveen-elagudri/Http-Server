package cmpe202.project.webserver.ood;

public class StopWebApp implements Command {
	private WebApp webApp;
	
	public StopWebApp(WebApp webApp){
		this.webApp = webApp;
	}
	
	public boolean execute(String appName){
		return webApp.stop(appName);
	}

}

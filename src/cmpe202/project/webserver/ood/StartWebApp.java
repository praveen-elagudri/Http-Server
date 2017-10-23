package cmpe202.project.webserver.ood;

public class StartWebApp implements Command {
	private WebApp webApp;
	
	public StartWebApp(WebApp webApp){
		this.webApp = webApp;
	}
	
	public boolean execute(String appName){
		return webApp.start(appName);
	}
}

package cmpe202.project.webserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

public class StartServer implements State {
	LoginHandler loginHandler;
	static HashMap<String,HttpContext> contextMap;
	
	@Override
	public void doAction(ServerContext serverContext) throws IOException {
		
    	HttpServer server = serverContext.getServer();
    	contextMap = new HashMap<String,HttpContext>();
    	
    	if(serverContext.getServerState().equals("Stop")){
	    	AppContext appContext = new AppContext();
	    	ArrayList<String> appList = appContext.getAppList();
	    	
	    	for(String app : appList)
    			contextMap.put(app,server.createContext("/" + app, new RequestHandler(app)));
//	    	HttpContext rootCtx =  server.createContext("/", new RequestHandler("ROOT"));
	    	
	    	HttpContext adminCtx = server.createContext("/", new RootHandler());
	    	HttpContext uploadCtx = server.createContext("/Upload", new FileUploader());
	    	HttpContext admintCtx = server.createContext("/admin", new AdminHandler());
//	    	HttpContext developerCtx = server.createContext("/dev", new DeveloperHandler());
	    	HttpContext shutdownCtx =  server.createContext("/Shutdown", new ShutdownHandler());
	    	
////	    	adminCtx.setAuthenticator(getRootAuthenticator());
//	    	uploadCtx.setAuthenticator(getRootAuthenticator());
//	    	shutdownCtx.setAuthenticator(getRootAuthenticator());
////	    	developerCtx.setAuthenticator(getDeveloperAuthenticator());
//	    	uploadCtx.setAuthenticator(getDeveloperAuthenticator());
	    	
	    	server.setExecutor(null);
	    	server.start();
	    	serverContext.setServerState("Start");
	    	System.out.println("Server has started");
    	}
	}
	
	public static HttpContext getContext(String appName){
		return contextMap.get(appName.toLowerCase());
	}
	
	private Authenticator getRootAuthenticator() {
		loginHandler = new LoginHandler();
		Authenticator authenticator = new BasicAuthenticator("ROOT") {
			public boolean checkCredentials(String username, String password) {
				return loginHandler.authenticate(username, password);
			}
		  };
		  return authenticator;
		}	

	private Authenticator getDeveloperAuthenticator() {
		loginHandler = new LoginHandler();
		Authenticator authenticator = new BasicAuthenticator("ROOT") {
			public boolean checkCredentials(String username, String password) {
				boolean result = loginHandler.authenticate(username, password);
				return result;
			}
		  };
		  return authenticator;
		}	
	
}

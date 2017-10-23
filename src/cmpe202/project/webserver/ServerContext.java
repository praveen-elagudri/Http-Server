package cmpe202.project.webserver;

import com.sun.net.httpserver.HttpServer;

public class ServerContext {
	private String serverState;
	private HttpServer httpServer;
	
	public ServerContext(){
		httpServer = WebServer.getInstance();
		serverState = "Stop";
	}
	
	public HttpServer getServer(){
		return httpServer;
	}
	
	public void setServerState(String serverState){
		this.serverState = "Start";
	}
	
	public String getServerState(){
		return serverState;
	}
}

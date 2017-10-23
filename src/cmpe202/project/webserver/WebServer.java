package cmpe202.project.webserver;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class WebServer {
	private static HttpServer server;
	
	private WebServer(){}
	
	public static synchronized HttpServer getInstance(){
		if(server==null){
			synchronized (WebServer.class) {
				if(server==null){
					try {
						server = HttpServer.create(new InetSocketAddress(8080),0);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return server;
	}
}

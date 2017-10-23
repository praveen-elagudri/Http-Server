package cmpe202.project.webserver;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ShutdownHandler implements HttpHandler {
	@Override
	public void handle(HttpExchange arg0) throws IOException {
		ServerContext serverContext = new ServerContext();
		StopServer stopServer = new StopServer();
		stopServer.doAction(serverContext);
	}
}

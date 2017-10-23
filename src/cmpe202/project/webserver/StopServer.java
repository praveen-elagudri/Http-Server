package cmpe202.project.webserver;

import java.io.IOException;

import com.sun.net.httpserver.HttpServer;

public class StopServer implements State {

	@Override
	public void doAction(ServerContext serverContext) throws IOException {
		HttpServer server = serverContext.getServer();
		if(serverContext.getServerState().toString().equals("Start")){
			server.stop(0);
			serverContext.setServerState("Stop");
			System.out.println("Server has started");
		}
	}

	@Override
	public String toString(){
		return "Stop";
	}
}

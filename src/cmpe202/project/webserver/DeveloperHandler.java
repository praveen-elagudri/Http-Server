package cmpe202.project.webserver;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class DeveloperHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange t) throws IOException {
		OutputStream out = t.getResponseBody();
		String url = "user.html";
		String path = Constants.APP_HOME + "\\root\\" + url;
		StaticResourceHandler.send(t,path,out);
		out.close();

	}

}

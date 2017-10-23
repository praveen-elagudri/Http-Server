package cmpe202.project.webserver;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RootHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange t) throws IOException {
		String url = t.getRequestURI().toString();
		String path=null;
		String tmpUrl;
		OutputStream out = t.getResponseBody();
		
		if(url.contains("/")&&url.length()<2)
			path = Constants.APP_HOME + "\\root\\manager.html";
		
		if(url.contains("/")&&url.length()>1){
			tmpUrl = url.replaceAll("/","\\\\");
			path = Constants.APP_HOME + "\\root" + tmpUrl;
		}
		System.out.println(path);
		StaticResourceHandler.send(t, path, out);
		out.close();
	}
}

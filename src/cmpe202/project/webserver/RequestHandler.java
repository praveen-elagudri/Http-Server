package cmpe202.project.webserver;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RequestHandler implements HttpHandler {
	private String context;
	
	public RequestHandler() {
	}
	
	RequestHandler(String context){
		this.context = context; 
	}
	
	
	public void handle(HttpExchange t) throws IOException{
		AppContext appContext = new AppContext();
		String uri = t.getRequestURI().toString();
		OutputStream outputStream = t.getResponseBody();
		
		try{
			WebDBUtility w = new WebDBUtility();
			String status = w.getWebAppStatus(uri);
			if(status!=null){
				if(status.equals("Stop")){
		            t.sendResponseHeaders(501, 0);
		            outputStream.write("<h1>501 : Service Unavailable </h1><h3>Website is currently down</h3>".getBytes());
		            outputStream.close();
		            return;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		String webApp = null;
		
		String tempURI = uri.substring(1, uri.length());
		
		if(tempURI.contains("/"))
			webApp = tempURI.substring(0, tempURI.indexOf("/"));
		else
			webApp = tempURI.substring(1, tempURI.length());
		
		boolean isExist = appContext.IsWebAppExist(webApp);
		
		if(isExist){
			if(uri.contains("?")){
				int rindex = uri.lastIndexOf('?');
				uri = uri.substring(0, rindex);
			}
			
			if(uri.contains("/"))
			{
				uri = uri.replaceAll("/","\\\\");
//				int loc = uri.indexOf("/");
//				String t1= uri.substring(1,loc);
//				String t2 = uri.substring(loc, uri.length());
//				uri = t1 + "\\" + t2;
			}
			
			String path = Constants.STATIC_FILE_HOME + uri ;
			StaticResourceHandler.send(t, path, outputStream);
			
		}else{
            t.sendResponseHeaders(404, 0);
            outputStream.write("<h1>404 : Not Found </h1><h3>Page you are looking for not found!</h3>".getBytes());
            outputStream.close();
		}
	}
}

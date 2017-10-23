package cmpe202.project.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.sun.net.httpserver.Authenticator.Result;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import cmpe202.project.webserver.ood.StartWebApp;
import cmpe202.project.webserver.ood.StopWebApp;
import cmpe202.project.webserver.ood.UndeployWebApp;
import cmpe202.project.webserver.ood.WebApp;
import cmpe202.project.webserver.ood.WebAppAgent;

public class AdminHandler implements HttpHandler {
	
	@Override
	public void handle(HttpExchange t) throws IOException {
		String url = t.getRequestURI().toString();
		Headers head = t.getRequestHeaders();
		List<String> params = head.get("cmd");
		String cmd = params.get(0);
		boolean result = false;
//		String[] cmdList = cmd.split(",");
		
		OutputStream out = t.getResponseBody();
		AppContext appContext = new AppContext();
		
		try{
			WebDBUtility w = new WebDBUtility();
			WebApp webApp = new WebApp();
			WebAppAgent wAgent = new WebAppAgent();
			
			if(cmd.startsWith("Stop")){
				String appName = cmd.substring(cmd.indexOf(",")+1,cmd.length());
				StopWebApp stopWeb = new StopWebApp(webApp);
				result = wAgent.doAction(stopWeb, appName);
				System.out.println("Stop : " + result);
				t.sendResponseHeaders(200, 0);
				if(result)
					out.write("true".getBytes());
				else
					out.write("false".getBytes());
			}

			if(cmd.startsWith("Start")){
				String appName = cmd.substring(cmd.indexOf(",")+1,cmd.length());
				StartWebApp startWeb = new StartWebApp(webApp);
				result = wAgent.doAction(startWeb, appName);
				System.out.println("Start : " + result);
				t.sendResponseHeaders(200, 0);
				if(result)
					out.write("true".getBytes());
				else
					out.write("false".getBytes());
			}
			
			if(cmd.startsWith("Undeploy")){
				String appName = cmd.substring(cmd.indexOf(",")+1,cmd.length());
				UndeployWebApp undeployWebApp = new UndeployWebApp(webApp);
				result = wAgent.doAction(undeployWebApp, appName);
				System.out.println("Deleted : " + result);

				t.sendResponseHeaders(200, 0);				
				if(result)
					out.write("true".getBytes());
				else

					out.write("false".getBytes());
			}
			
			if(cmd.equals("getapps")){
				String apps = w.userWithAppList();
				System.out.println(apps);
				t.sendResponseHeaders(200, 0);
				out.write(apps.getBytes());
			}
			
			if(cmd.startsWith("CreateUser")){
				String[] param = cmd.substring(cmd.indexOf(":")+1,cmd.length()).split(",");
				String username = param[0];
				String password = param[1];
				UserDBUtility u = new UserDBUtility();
				u.addUserDBEntry(username, password, "developer");
			}
			
			out.close();			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}

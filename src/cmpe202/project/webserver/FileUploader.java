package cmpe202.project.webserver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class FileUploader implements HttpHandler{
	
	public boolean isDirExit(String dir){
		File contextDir = new File(Constants.STATIC_FILE_HOME);
		for(File file : contextDir.listFiles()){
			if(file.isDirectory() && (file.getName().equalsIgnoreCase(dir))){
				return true;
			}
		}
		return false;
	}

	@Override
	public void handle(HttpExchange t) throws IOException {
		Headers head = t.getRequestHeaders();
		OutputStream out = t.getResponseBody();		
		List<String> filenames = head.get("Filename");
		
		if(filenames==null){
			t.sendResponseHeaders(403, 0);
			out.write("<h1>403 : Forbidden request </h1>".getBytes());
			out.close();
			return;
		}
		
		System.out.println(filenames.toString());
		String data = filenames.get(0);
		
		String[] fileAttrib = data.split(",");
		String filename = fileAttrib[0];
		String filetype = fileAttrib[1];
		String fs = fileAttrib[2];
		String welcomeFile = fileAttrib[3];
//		String username = t.getPrincipal().getUsername();
		
		String dir = filename.substring(0, filename.indexOf("."));
		if(!isDirExit(dir)){
			int byteRead=-1;
			byte[] buffer = new byte[4096];
		
			InputStream inputStream = t.getRequestBody();
			String filepath = Constants.STATIC_FILE_HOME + "\\" +  filename;
			FileOutputStream outputStream = new FileOutputStream(filepath);
			
			while((byteRead=inputStream.read(buffer))!=-1){
				outputStream.write(buffer,0,byteRead);
			}
			
			outputStream.close();
			
			UnzipUtility uz =  new UnzipUtility();
			uz.unzipFunction(Constants.STATIC_FILE_HOME, filepath);
			try {
				WebDBUtility u = new WebDBUtility();
				u.addWebDBEntry(dir, "Admin", "Stop", "index.html");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			t.sendResponseHeaders(200, 0);
			out.write("success".getBytes());
		}else{
			t.sendResponseHeaders(409, 0);
			out.write("Web application with this name already exist!".getBytes());
		}
		out.close();
	}
}
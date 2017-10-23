package cmpe202.project.webserver;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.sun.net.httpserver.HttpExchange;

public class StaticResourceHandler {

	public static void send(HttpExchange t, String resourcePath, OutputStream outputStream) throws IOException{
		File staticFile = new File(resourcePath);
        String contentType = null;
    	
    	try{
	        String filepath = staticFile.getPath();
	    	Path path = Paths.get(filepath);
	    	contentType = Files.probeContentType(path);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
		
        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(staticFile);
            byte[] buffer = new byte[(int)staticFile.length()];
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(buffer, 0, buffer.length);
            t.sendResponseHeaders(200, 0);
            outputStream.write(buffer,0,buffer.length);
            outputStream.close();
            
        }catch (IOException e)
        {
            t.sendResponseHeaders(404, 0);
            outputStream.write("<h1>404 : Not Found </h1><h3>Page you are looking for not found!</h3>".getBytes());
            outputStream.close();
            e.printStackTrace();
        }
        finally
        {
            if (fis != null)
            {
                try
                { fis.close(); }
                catch (IOException e)
                { e.printStackTrace();}
            }
        }			
	}
}

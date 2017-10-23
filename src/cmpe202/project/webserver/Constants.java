package cmpe202.project.webserver;

import java.io.File;
import java.util.List;

public interface Constants
{
    String APP_HOME = System.getProperty("user.dir");
    
    String STATIC_FILE_HOME = APP_HOME + File.separator + "webapps";
    
    String LIB_ROOT = APP_HOME + File.separator + "lib";
    
    List<String> webAppList = null;
    
    String NOT_FOUND = new StringBuilder("HTTP/1.1 404 NOT FOUND\r\n").append("Server: HttpServer\r\n")
            .append("Content-Length: 9\r\n")
            .append("Connection: closed\r\n")
            .append("\r\n")
            .append("NOT FOUND")
            .toString();

}

package cmpe202.project.webserver;

import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws IOException
    {
    	ServerContext serverContext = new ServerContext();
    	StartServer start = new StartServer();
    	start.doAction(serverContext);
    }
}
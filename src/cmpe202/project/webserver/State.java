package cmpe202.project.webserver;

import java.io.IOException;

public interface State {
	public void doAction(ServerContext serverContext) throws IOException;
}

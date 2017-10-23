package cmpe202.project.webserver.ood;

public class WebAppAgent {
	public boolean doAction(Command cmd, String appName) throws Exception{
		return cmd.execute(appName);
	}
}

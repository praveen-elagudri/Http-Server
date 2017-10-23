package cmpe202.project.webserver.ood;

public interface Command {
	public boolean execute(String appName) throws Exception;
}

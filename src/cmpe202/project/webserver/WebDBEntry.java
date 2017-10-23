 package cmpe202.project.webserver;

public class WebDBEntry {
	private String name;
	private String status;
	private String username;
	private String welcomeFile;
	
	public WebDBEntry(){
		
	}
	
	public WebDBEntry(String name, String status, String username, String welcomeFile){
		this.name = name;
		this.status = status;
		this.username = username;
		this.welcomeFile = welcomeFile;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getWelcomeFile() {
		return welcomeFile;
	}

	public void setWelcomeFile(String welcomeFile) {
		this.welcomeFile = welcomeFile;
	}
}

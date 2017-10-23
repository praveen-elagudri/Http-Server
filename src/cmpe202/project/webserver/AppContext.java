package cmpe202.project.webserver;

import java.io.File;
import java.util.ArrayList;

public class AppContext {
	private ArrayList<String> list;
	
	AppContext(){
		getAppList();
	}
	
	public ArrayList<String> getAppList(){
//		final File f = new File(AppContext.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		File contextDir = new File(Constants.STATIC_FILE_HOME);
		list = new ArrayList<String>();
		for(File file : contextDir.listFiles()){
			if(file.isDirectory()){
				list.add(file.getName());
			}
		}
		return list;
	}
	
	public boolean IsWebAppExist(String webApp){
		return list.contains(webApp);
	}
	
	public static void main(String[] args){
		AppContext app = new AppContext();
		for(String f : app.getAppList())
			System.out.println(f);
	}
	
}
package cmpe202.project.webserver;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

interface Iterator {
	public boolean hasNext();
	public Object next();
}

interface Container{
	public Iterator getIterator();
}

public class WebAppList implements Container {
//	public String webAppList[];
	private ArrayList<String> webAppList;
	private String username;
	
	public WebAppList(){
		getAppList();
	}
	
	@Override
	public Iterator getIterator() {
		return new WebAppListIterator();
	}
	
	public ArrayList<String> getAppList(){
//		final File f = new File(AppContext.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		File contextDir = new File(Constants.STATIC_FILE_HOME);
		webAppList = new ArrayList<String>();
		for(File file : contextDir.listFiles()){
			if(file.isDirectory()){
				webAppList.add(file.getName());

			}
		}
		return webAppList;
	}
	
	public boolean IsWebAppExist(String webApp){
		return webAppList.contains(webApp);
	}

	
	private class WebAppListIterator implements Iterator{
		int index=0;
		
		@Override
		public boolean hasNext() {
			return index < webAppList.size();
		}

		@Override
		public Object next() {
			if(this.hasNext())
				return webAppList.get(index++);
			return null;
		}
	}
	
	public static void main(String[] args){
		WebAppList web = new WebAppList();
		for(Iterator iter = web.getIterator();iter.hasNext();){
			String name = (String) iter.next();
			System.out.println(name);
		}
			
	}
}

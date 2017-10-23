package cmpe202.project.webserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoginHandler{
	
	private HashMap<String,ArrayList<String>> currentUsers = new HashMap<String,ArrayList<String>>();

	public boolean authenticate(String username, String password){

		UserDBUtility users = null;
		try {
			users = new UserDBUtility();
		} catch (Exception e) {
			e.printStackTrace();
		}
		HashMap<String,UserDBEntry> userList = users.getAllUser();

		if(userList.containsKey(username)){
			UserDBEntry u = userList.get(username);
			if(u.getPassword().equals(password)){
				return true;
			}
		}
		
		return false;
	}
	
	public ArrayList<String> getCurrentLoggedInUsers(String user){
		return currentUsers.get(user);
	}
	
}

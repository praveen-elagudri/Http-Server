package cmpe202.project.webserver;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserDB {
	private List<UserDBEntry> userDBEntry;

	@XmlElement
	public List<UserDBEntry> getUserDBEntry() {
		return userDBEntry;
	}
	public void setUserDBEntry(List<UserDBEntry> userDBEntry) {
		this.userDBEntry = userDBEntry;
	}
}

package Core;

public class HostInfo {
	public String host;
	public String username;
	public String password;
	public boolean valid() {
		if(host==null||username==null||password==null||host.isEmpty()||username.isEmpty()||password.isEmpty())
			return false;
		else {
			return true;
		}
	}
}

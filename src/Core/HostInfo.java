package Core;
import java.net.*;

public class HostInfo {
	public String host;//填写连接url，例如"jdbc:mysql://localhost:3306/pine?serverTimezone=UTC"
	public String username;//用户名
	public String password;//密码
	public boolean valid() {
		if(host==null||username==null||password==null||host.isEmpty()||username.isEmpty()||password.isEmpty())
			return false;
		else {
			return true;
		}
	}
	public boolean equals(HostInfo info){
		if(this.host.equals(info.host)&&this.username.equals(info.username)&&this.password.equals(info.password))
			return true;
		else
			return false;
	}
}

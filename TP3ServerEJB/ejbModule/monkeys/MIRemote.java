package monkeys;

import javax.ejb.Remote;

@Remote
public interface MIRemote {
	
	public void subscribe(String id);
	public void disconnect(String pirateId);
	public void move(int x,int y);
}

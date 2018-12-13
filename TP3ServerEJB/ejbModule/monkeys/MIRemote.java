package monkeys;

import javax.ejb.Remote;

@Remote
public interface MIRemote {
	
	public void subscribe(String id);
	public void disconnect(String pirateId);
	
	/**
	 * Déplacement du pirate
	 * @param x coordonnée x ciblée
	 * @param y coordonnée y ciblée
	 * @param id id du pirate a déplacer
	 */
	public void move(int x,int y, int id);
}

package monkeys;

import java.util.ArrayList;

import javax.ejb.Local;

@Local
public interface CommunicationLocal {
	public void sendMap(int[][] map, String id);
	public void sendPirate(Element pirate);
	public void sendMonkey(Element monkey);
	public void sendRhum (Element rhum);
	public void sendElements(ArrayList<Element> elements);
}

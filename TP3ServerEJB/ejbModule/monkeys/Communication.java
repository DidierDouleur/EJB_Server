package monkeys;

import java.util.ArrayList;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.StreamMessage;
import javax.jms.Topic;

/**
 * Session Bean implementation class Communication
 */
@Stateless
@LocalBean
public class Communication implements CommunicationLocal {
	// youpi tralala
	@Inject
	private JMSContext context;

	@Resource(mappedName = "java:jboss/exported/topic/monkeys")
	private Topic topic;

	/**
	 * Default constructor.
	 */
	public Communication() {
	}

	@Override
	public void sendMap(int[][] map, String id) {
		sendIntArrayMessage(map, id, "map");
	}

//	public void sendYourPirate(Element pirate, int energy) {
//		StreamMessage message = context.createStreamMessage();
//		try {
//			message.setJMSType("YourPirate");
//			message.setIntProperty("id", pirate.getId());
//			message.setIntProperty("x", pirate.getPosX());
//			message.setIntProperty("y", pirate.getPosY());
//			// TODO : Valeur en dur, modifier !!!!!!!!
//			// message.setIntProperty("energy", pirate.getEnergy());
//			message.setIntProperty("energy", energy);
//		} catch (JMSException e) {
//			e.printStackTrace();
//		}
//		context.createProducer().send(topic, message);
//	}

	public void sendYourID(int id) {
		StreamMessage message = context.createStreamMessage();
		try {
			message.setJMSType("YourID");
			message.setIntProperty("id", id);
		} catch (JMSException e) {
			e.printStackTrace();
		}
		context.createProducer().send(topic, message);
	}

	public void sendElements(ArrayList<Element> list) {
		StreamMessage message = context.createStreamMessage();
		System.out.println("send elements size => " + list.size());
		try {
			message.setJMSType("AllElements");
			message.setIntProperty("size", list.size());
			for (int i = 0; i < list.size(); i++) {
				message.setIntProperty("id" + i, list.get(i).getId());
				message.setIntProperty("x" + i, list.get(i).getPosX());
				message.setIntProperty("y" + i, list.get(i).getPosY());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		context.createProducer().send(topic, message);
	}

	// public void sendYourPirate(Pirate pirate) {
	// StreamMessage message = context.createStreamMessage();
	// try {
	// message.setJMSType("YourPirate");
	// message.setIntProperty("id", pirate.getId());
	// message.setIntProperty("x", pirate.getPosX());
	// message.setIntProperty("y", pirate.getPosY());
	// message.setIntProperty("energy", pirate.getEnergy());
	// } catch (JMSException e) {
	// e.printStackTrace();
	// }
	// context.createProducer().send(topic, message);
	// }

	public void sendPirate(Pirate pirate) {
		StreamMessage message = context.createStreamMessage();
		try {
			message.setJMSType("Pirate");
			message.setIntProperty("id", pirate.getId());
			message.setIntProperty("x", pirate.getPosX());
			message.setIntProperty("y", pirate.getPosY());
			message.setIntProperty("energy", pirate.getEnergy());
		} catch (JMSException e) {
			e.printStackTrace();
		}
		context.createProducer().send(topic, message);
	}

	public void sendDeathPirate(Pirate pirate) {
		StreamMessage message = context.createStreamMessage();
		try {
			message.setJMSType("DeatPirate");
			message.setIntProperty("id", pirate.getId());
		} catch (JMSException e) {
			e.printStackTrace();
		}
		context.createProducer().send(topic, message);
	}

//	public void sendSinge(Singe singe) {
//		StreamMessage message = context.createStreamMessage();
//		try {
//			message.setJMSType("Singe");
//			message.setIntProperty("id", singe.getId());
//			message.setIntProperty("x", singe.getPosX());
//			message.setIntProperty("y", singe.getPosY());
//		} catch (JMSException e) {
//			e.printStackTrace();
//		}
//		context.createProducer().send(topic, message);
//	}

//	public void sendRhum(Rhum rhum) {
//		StreamMessage message = context.createStreamMessage();
//		try {
//			message.setJMSType("Rhum");
//			message.setIntProperty("id", rhum.getId());
//			message.setIntProperty("x", rhum.getPosX());
//			message.setIntProperty("y", rhum.getPosY());
//		} catch (JMSException e) {
//			e.printStackTrace();
//		}
//		context.createProducer().send(topic, message);
//	}

//	public void sendTresor(Tresor tresor) {
//		StreamMessage message = context.createStreamMessage();
//		try {
//			message.setJMSType("Tresor");
//			message.setIntProperty("id", tresor.getId());
//			message.setIntProperty("x", tresor.getPosX());
//			message.setIntProperty("y", tresor.getPosY());
//		} catch (JMSException e) {
//			e.printStackTrace();
//		}
//		context.createProducer().send(topic, message);
//	}

	public void deletePirate(Pirate pirate) {
		StreamMessage message = context.createStreamMessage();
		try {
			message.setJMSType("SuppressionPirate");
			message.setIntProperty("id", pirate.getId());
		} catch (JMSException e) {
			e.printStackTrace();
		}
		context.createProducer().send(topic, message);
	}

	// TODO : AJOUTER suppersionPirates

	private void sendIntArrayMessage(int[][] array, String id, String type) {
		StreamMessage message = context.createStreamMessage();
		try {
			message.setStringProperty("id", id);
			message.setJMSType(type);
			message.writeInt(array.length);
			for (int i = 0; i < array.length; i++) {
				for (int j = 0; j < array[i].length; j++) {
					message.writeInt(array[i][j]);
				}
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
		context.createProducer().send(topic, message);
	}
}

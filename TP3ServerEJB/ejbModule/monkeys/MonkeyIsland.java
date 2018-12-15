package monkeys;

import java.io.IOException;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

/**
 * Session Bean implementation class MonkeyIsland
 */
@Stateful
public class MonkeyIsland implements MIRemote {

	/**
	 * Id des pirate compris entre 1 et 99
	 */
	private int idPirate = 1;

	/**
	 * id des singes compris entre 100 et 200
	 */
	private int idSinge = 100;

	/**
	 * TODO : Liste a supprimer
	 */
	// TODO : récupérer les pirates de la DB si il en existe
	private ArrayList<Pirate> listPirates = new ArrayList<Pirate>();

	/**
	 * Liste de tous les elements de la carte
	 */
	private ArrayList<Element> listElements;

	/**
	 * 
	 */
	private Island mainland;

	// TODO : CONFIRMER L IDEE
	/**
	 * 
	 */
	private boolean runningGame;

	@EJB
	private Configuration configuration;

	@EJB
	private Communication communication;

	///////////////// NEW

	@PersistenceContext(unitName = "MonkeysDS")
	private EntityManager entityManager;

	// EntityManager pour Island
	public void addIsland(Island island) throws Exception {
		entityManager.persist(island);
	}

	public void deleteIsland(Island island) throws Exception {
		entityManager.remove(island);
	}

	public Island getIsland(int id) throws Exception {
		return entityManager.find(Island.class, id);
	}

	// EntityManager pour Element
	public void addElement(Element element) throws Exception {
		entityManager.persist(element);
	}

	public void deleteElement(Element element) throws Exception {
		entityManager.remove(element);
	}

	public Element getElement(int id) throws Exception {
		return entityManager.find(Element.class, id);
	}

	/////////////// END NEW
	/**
	 * Default constructor.
	 */
	public MonkeyIsland() {
	}

	@Override
	public void subscribe(String id) {

		// Si la partie n'est pas en cours, on en créer une
		if (!runningGame) {
			this.newGame(id);
			this.communication.sendMap(this.mainland.getMap(), id);
		}
		this.communication.sendYourPirate(createPirate(5, 5), this.configuration.readFileEnergy());
		createPirate(2,2);
		createMonkey(3,3);
		createMonkey(7,9);
		this.communication.sendElements(listElements);
		System.out.println("endTest");

		// Pirate pirate = new
		// Pirate(idPirate++,5,5,this.configuration.readFileEnergy());
		// listPirates.add(pirate);
		// this.communication.sendYourPirate(pirate);

		// //TODO : remove Test Pirate
		// Pirate pirate = new Pirate(1,5,5,this.configuration.readFileEnergy());
		// this.communication.sendPirate(pirate);
		// //TODO : remove Test Singe
		// Singe singe = new Singe (2,5,6);
		// this.communication.sendSinge(singe);
		// //TODO : remove Test Rhum
		// Rhum rhum = new Rhum(3,6,5,12);
		// this.communication.sendRhum(rhum);
		// //TODO TESTER MORT PIRATE
		//
		// //TODO : REMOVE tresor
		// Tresor tresor = new Tresor(36,9,8);
		// this.communication.sendTresor(tresor);
	}

	@Override
	public void disconnect(String id) {

		int auxId = Integer.parseInt(id);

		// Suppression du pirate de la liste des elements du jeu
		for (int i = 0; i < listElements.size(); i++) {
			if (listElements.get(i).getId() == auxId) {
				listElements.remove(i);
			}
		}
		// TODO : Envoyer le message aux autres utilisateur que le pirate n'est plus
		// dans la partie
	}

	private void newGame(String name) {
		int id = Integer.parseInt(name);
		this.listElements = new ArrayList<Element>();
		try {
			this.mainland = this.getIsland(id);
			if (this.mainland != null) {
				if (this.mainland.getMap() == null) {
					this.mainland.setMap(this.configuration.readFileMap());
				}
			} else {
				this.mainland = new Island();
				this.mainland.setMap(this.configuration.readFileMap());
				this.addIsland(this.mainland);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		runningGame = true;
	}

	// Avec objet pirate
	// @Override
	// public void move(int x, int y, int id) {
	// System.out.println("Move demandé " + x + y + id);
	// for (int i = 0; i < listPirates.size(); i++) {
	// if (listPirates.get(i).getId() == id) {
	// listPirates.get(i).setPosX(x);
	// listPirates.get(i).setPosY(y);
	// System.out.println("Pirate " + id + "deplacer en x:" + x + " y:" + y);
	// // TODO une fois le déplacement fait, utiliser la comm pour envoyer l'info a
	// // tous les joueur
	// }
	// }
	// }

	// Avec objet element
	@Override
	public void move(int x, int y, int id) {
		System.out.println("Move demandé " + x + y + id);
		for (Element element : listElements) {
			if (element.getId() == id) {
				element.setPosX(x);
				element.setPosY(y);
				System.out.println("Pirate " + id + "deplacer en x:" + x + " y:" + y);
				
				//TODO : Partie mettant a jour la base de donnée
				try {
					this.addElement(element);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// TODO une fois le déplacement fait, utiliser la comm pour envoyer l'info a
				// tous les joueur
			}
		}
	}

	/**
	 * TODO
	 * 
	 * @param x
	 * @param y
	 */
	public void createMonkey(int x, int y) {
		Element monkey = new Element();
		monkey.setPosX(x);
		monkey.setPosY(y);
		monkey.setId(idSinge++);
		this.listElements.add(monkey);
	}

	/**
	 * TODO
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public Element createPirate(int x, int y) {
		Element pirate = new Element();
		pirate.setPosX(x);
		pirate.setPosY(y);
		pirate.setId(idPirate++);
		this.listElements.add(pirate);
		return pirate;
	}

	public void endGame() {
		// On vide la DB des element de la liste puis on vide la liste
		for (Element element : listElements) {
			try {
				this.deleteElement(element);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		listElements.clear();
		runningGame = false;
	}
	

}

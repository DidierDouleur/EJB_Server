package monkeys;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateful;
import javax.ejb.Timer;
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
	 * Liste de tous les elements de la carte
	 */
	private ArrayList<Element> listElements;

	/**
	 * 
	 */
	private Island mainland;

	/**
	 * booléen servant d'indicateur pour savoir si une partie est lancée
	 */
	private boolean runningGame;

	@EJB
	private Configuration configuration;

	@EJB
	private Communication communication;

	@EJB
	private MoveTimer moveTimer;

	@PersistenceContext(unitName = "MonkeysDS")
	private EntityManager entityManager;

	/////////////// END NEW
	/**
	 * Default constructor.
	 */
	public MonkeyIsland() {
		this.runningGame = false;
	}

	@Override
	public void subscribe(String id) {

		this.newGame(id);
		this.communication.sendMap(this.mainland.getMap(), id);
		this.communication.sendYourID(this.createPirate(5, 5));
		this.communication.sendElements(listElements);

	}

	@Override
	public void disconnect(String id) {
		// TODO ENVOYER A TOUS LES JOUEUR LA DISPARITION DU JOUEUR
		int auxId = Integer.parseInt(id);
		for (int i = 0; i < listElements.size(); i++) {
			if (listElements.get(i).getId() == auxId) {
				listElements.remove(i);
			}
		}
		try {
			Element elem = this.getElement(auxId);
			this.deleteElement(elem);

		} catch (Exception e) {
			e.printStackTrace();
		}
		// } finally {
		// boolean test = true;
		// for (Element element : listElements) {
		// if (element.getType().matches("PIRATE")) {
		// System.out.println("MONKEYISLAND.java disconnect essaie endGame type ==
		// pirate ????");
		// test = false;
		// }
		// }
		// if (test) {
		// this.endGame();
		// }
		// }
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
		this.createMonkey(this.configuration.readMonkeysNbr());
		this.createRhum(7, 4);
		runningGame = true;
	}

	/**
	 * Methode appelée lors de la communication client -> serveur
	 */
	@Override
	public void move(int x, int y, int id) {
		System.out.println("Move demandé " + x + y + id);
		for (Element element : listElements) {
			if (element.getId() == id && element.getState()) {
				element.notifyObserver(element.getPosX() + x, element.getPosY() + y);
			}
		}
		this.communication.sendElements(listElements);
	}

	/**
	 * TODO Methode créant un singe, l'ajoutant à la liste des elements de la partie
	 * et l'ajoute en DB
	 * 
	 * @param x
	 *            position x du singe
	 * @param y
	 *            position y du singe
	 */
	public void createMonkey(int x, int y) {
		Element monkey = new Element();
		monkey.setPosX(x);
		monkey.setPosY(y);
		monkey.setEnergy(-1);
		monkey.setType("MONKEY");
		monkey.setState(true);
		monkey.addObserver(new ObserverMonkey(monkey, this));
		this.listElements.add(monkey);
		try {
			this.addElement(monkey);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * methode créant des singes a des positions aléatoires
	 * 
	 * @param nbr
	 *            : nombres de singes a créer
	 */
	public void createMonkey(int nbr) {
		Random rand = new Random();
		int x = 0;
		int y = 0;
		for (int i = 0; i < nbr; i++) {
			x = rand.nextInt(9) + 1;
			y = rand.nextInt(9) + 1;
			this.createMonkey(x, y);
		}
	}

	/**
	 * Methode créant un rhum, l'ajoutant à la liste des elements de la partie et
	 * l'ajoute en DB
	 * 
	 * @param x
	 * @param y
	 */
	public void createRhum(int x, int y) {
		Element rhum = new Element();
		rhum.setPosX(x);
		rhum.setPosY(y);
		rhum.setEnergy(this.configuration.readRhumEnergy());
		rhum.setType("RHUM");
		rhum.setState(true);
		this.listElements.add(rhum);
		try {
			this.addElement(rhum);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Methode créant un pirate, l'ajoutant à la liste des elements de la partie et
	 * l'ajoute en DB
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public int createPirate(int x, int y) {
		Element pirate = new Element();
		pirate.setPosX(x);
		pirate.setPosY(y);
		pirate.setEnergy(this.configuration.readFileEnergy());
		pirate.setType("PIRATE");
		pirate.setState(true);
		System.out.println("mouchard1:");
		pirate.addObserver(new ObserverPirate(pirate, this));
		System.out.println("mouchard2:");

		this.listElements.add(pirate);
		try {
			this.addElement(pirate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pirate.getId();
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

	/**
	 * Fonction permettant de faire se déplacer l'ensemble des singes de la partie
	 */
	private void updateMonkeys() {
		Random rand = new Random();
		List<List<Integer>> lPosition = new ArrayList<>();
		int[] direction = { 0, 1, 0, -1, 1, 0, -1, 0 };
		for (Element elem : this.listElements) {
			if (elem.getType().equals("MONKEY")) {
				for (int i = 0; i < direction.length; i += 2) {
					if (canMove(elem.getPosX() + i, elem.getPosY() + i + 1)) {
						ArrayList<Integer> lUnePosition = new ArrayList<>();
						lUnePosition.add(i);
						lUnePosition.add(i + 1);
						lPosition.add(lUnePosition);
					}
				}
				if (!lPosition.isEmpty()) {
					int random = rand.nextInt(lPosition.size());
					int dx = lPosition.get(random).get(0);
					int dy = lPosition.get(random).get(1);

					elem.notifyObserver(elem.getPosX() + dx, elem.getPosY() + dy);
				}

			}
		}
		this.communication.sendElements(listElements);
	}

	/**
	 * Fonction permettant a un singe de savoir si il peut aller sur une case.
	 * 
	 * @param x
	 *            Coordonnées x de la case ciblée
	 * @param y
	 *            Coordonnées y de la case ciblée
	 * @return true si le singe peut bouger, false sinon
	 */
	private boolean canMove(int x, int y) {
		Boolean canMove = false;
		Boolean cellEmpty = true;
		if (this.mainland.getMap()[x][y] == 1) {
			for (Element testElem : this.getElements()) {
				if (testElem.getPosX() == x && testElem.getPosY() == y) {
					if (testElem.getType().equals("MONKEY")) {
						cellEmpty = false;
						canMove = false;
					} else {
						cellEmpty = false;
						canMove = true;
					}
				}
			}
			if (cellEmpty) {
				canMove = true;
			}

		}
		return canMove;
	}

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

	public void updateElement(Element element) throws Exception {
		entityManager.merge(element);
	}

	public void deleteElement(Element element) throws Exception {
		entityManager.remove(element);
	}

	public Element getElement(int id) throws Exception {
		return entityManager.find(Element.class, id);
	}

	// SETTERS ET GETTERS

	public void setElements(ArrayList<Element> elements) {
		this.listElements = elements;
	}

	public ArrayList<Element> getElements() {
		return this.listElements;
	}

	public Island getMainIsland() {
		return this.mainland;
	}

	public Communication getCommunication() {
		return this.communication;
	}

}

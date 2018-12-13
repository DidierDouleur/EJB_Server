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
	
	private int idPirate=0;
	
	//TODO : récupérer les pirates de la DB si il en existe
	private ArrayList<Pirate> listPirates = new ArrayList<Pirate>();

	private Island mainland;

	@EJB
	private Configuration configuration;
	
	@EJB
	private Communication communication;

	///////////////// NEW

	@PersistenceContext(unitName = "MonkeysDS")
	private EntityManager entityManager;

	public void addIsland(Island island) throws Exception {
		entityManager.persist(island);
	}

	public void deleteIsland(Island island) throws Exception {
		entityManager.remove(island);
	}

	public Island getIsland(int id) throws Exception {
		return entityManager.find(Island.class, id);
	}

	/////////////// END NEW
	/**
	 * Default constructor.
	 */
	public MonkeyIsland() {
	}

	@Override
	public void subscribe(String id){
		
		//System.out.println("serveur"+id);
		this.newGame(id);
		this.communication.sendMap(this.mainland.getMap(),id);
		
		Pirate pirate = new Pirate(idPirate++,5,5,this.configuration.readFileEnergy());
		listPirates.add(pirate);
		this.communication.sendYourPirate(pirate);
		
		
//		//TODO : remove Test Pirate
//		Pirate pirate = new Pirate(1,5,5,this.configuration.readFileEnergy());
//		this.communication.sendPirate(pirate);
//		//TODO : remove Test Singe
//		Singe singe = new Singe (2,5,6);
//		this.communication.sendSinge(singe);
//		//TODO : remove Test Rhum
//		Rhum rhum = new Rhum(3,6,5,12);
//		this.communication.sendRhum(rhum);
//		//TODO TESTER MORT PIRATE
//
//		//TODO : REMOVE tresor
//		Tresor tresor = new Tresor(36,9,8);
//		this.communication.sendTresor(tresor);
	}

	@Override
	public void disconnect(String id) {
	
	}

	private void newGame(String name) {
		int id = Integer.parseInt(name);
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
	}

	@Override
	public void move(int x, int y, int id) {
		System.out.println("Move demandé " + x + y + id);
		for(int i=0; i<listPirates.size();i++) {
			if(listPirates.get(i).getId() == id) {
				listPirates.get(i).setPosX(x);
				listPirates.get(i).setPosY(y);
				System.out.println("Pirate " + id + "deplacer en x:" + x + " y:" + y);
				//TODO une fois le déplacement fait, utiliser la comm pour envoyer l'info a tous les joueur
			}
		}
		
	}

}

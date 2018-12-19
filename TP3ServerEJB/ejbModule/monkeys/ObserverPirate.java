package monkeys;

import java.util.ArrayList;

import javax.ejb.EJB;

public class ObserverPirate implements Observer {

	/**
	 * Pirate observée
	 */
	private Element pirate;

	private MonkeyIsland monkeyIsland;

	public ObserverPirate(Element element, MonkeyIsland monkeyIsland) {
		this.pirate = element;
		this.monkeyIsland = monkeyIsland;
	}

	@Override
	public void update(int x, int y) {

		Island island = monkeyIsland.getMainIsland();
		boolean moved = false;

		System.out.println("ObserverPirate => (update) x : " + x + " et y :" + y);

		// Si la case ciblé est de la terre
		if (island.getMap()[x][y] == 1) {
			
			for (Element testElem : monkeyIsland.getElements()) {

				if (testElem.getPosX() == x && testElem.getPosY() == y) {
					System.out.println("type " + testElem.getType());
					switch (testElem.getType()) {
					case "MONKEY":
						moved = true;
						System.out.println("CASE MONKEY x " + testElem.getPosX());
						this.pirate.move(x, y);
						this.pirate.setEnergy(this.pirate.getEnergy() - 1);
						this.pirate.setState(false);
						try {
							monkeyIsland.updateElement(pirate);
						} catch (Exception e) {
							e.printStackTrace();
						}
						System.out.println("MORT PIRATE");
						break;

					case "PIRATE":
						moved = true;
						System.out.println("CASE PIRATE " + testElem.getId());
						break;
					case "RHUM":
						moved = true;
						System.out.println("CASE RHUM");
						this.pirate.move(x, y);
						this.pirate.setEnergy(this.pirate.getEnergy() - 1 + testElem.getEnergy());
						testElem.setState(false);
						try {
							monkeyIsland.updateElement(pirate);
						} catch (Exception e) {
							e.printStackTrace();
						}
						// TODO LANCER TIMER RESPAWN RHUM
						break;
					default:
						moved = true;
						System.out.println("CASE DEFAULT");
						this.pirate.move(x, y);
						this.pirate.setEnergy(this.pirate.getEnergy() - 1);
						try {
							monkeyIsland.updateElement(pirate);
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					}
				}
			}
			if (!moved) {
				System.out.println("HORS CASE");
				this.pirate.move(x, y);
				this.pirate.setEnergy(this.pirate.getEnergy() - 1);
				try {
					monkeyIsland.updateElement(pirate);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("Energy " + this.pirate.getEnergy());
		if (this.pirate.getEnergy() <= 0) {
			this.pirate.setState(false);
			try {
				monkeyIsland.updateElement(this.pirate);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}

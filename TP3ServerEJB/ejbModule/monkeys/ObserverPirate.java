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

		// Si la case ciblé est de la terre
		if (island.getMap()[x][y] == 1) {
			
			for (Element testElem : monkeyIsland.getElements()) {

				if (testElem.getPosX() == x && testElem.getPosY() == y) {
					switch (testElem.getType()) {
					case "MONKEY":
						moved = true;
						this.pirate.move(x, y);
						this.pirate.setEnergy(this.pirate.getEnergy() - 1);
						this.pirate.setState(false);
						try {
							monkeyIsland.updateElement(pirate);
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;

					case "PIRATE":
						moved = true;
						break;
					case "RHUM":
						moved = true;
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
				this.pirate.move(x, y);
				this.pirate.setEnergy(this.pirate.getEnergy() - 1);
				try {
					monkeyIsland.updateElement(pirate);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
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

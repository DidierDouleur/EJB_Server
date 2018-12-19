package monkeys;

import java.util.ArrayList;

import javax.ejb.EJB;

public class ObserverMonkey implements Observer {

	private Element monkey;

	private MonkeyIsland monkeyIsland;

	public ObserverMonkey(Element element, MonkeyIsland monkeyIsland) {
		this.monkey = element;
		this.monkeyIsland = monkeyIsland;
	}

	@Override
	public void update(int x, int y) {

		boolean moved = false;

		for (Element testElem : monkeyIsland.getElements()) {

			if (testElem.getPosX() == x && testElem.getPosY() == y) {
				System.out.println("type " + testElem.getType());
				switch (testElem.getType()) {
				
				case "PIRATE":
					moved = true;
					this.monkey.move(x, y);
					testElem.setState(false);
					try {
						monkeyIsland.updateElement(monkey);
						monkeyIsland.updateElement(testElem);

					} catch (Exception e) {
						e.printStackTrace();
					}
					break;

				default:
					moved = true;
					System.out.println("CASE DEFAULT");
					this.monkey.move(x, y);
					try {
						monkeyIsland.updateElement(monkey);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				}
			}
		}
		if (!moved) {
			this.monkey.move(x, y);
			try {
				monkeyIsland.updateElement(monkey);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}

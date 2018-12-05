package monkeys;

public class Rhum extends Element {

	private int energy;

	public Rhum(int id, int x, int y, int energy) {
		super();
		this.setId(id);
		this.setPosX(x);
		this.setPosY(y);
		this.energy = energy;
	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}
}

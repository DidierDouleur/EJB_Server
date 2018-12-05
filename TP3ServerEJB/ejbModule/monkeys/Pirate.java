package monkeys;

public class Pirate extends Element {
	
	private int energy;
	
	public Pirate(int id,int x,int y,int energy) {
		super();
		this.setId(id);
		this.setPosX(x);
		this.setPosY(y);
		this.setEnergy(energy);
	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

}

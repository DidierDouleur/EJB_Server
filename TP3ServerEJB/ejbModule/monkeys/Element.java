package monkeys;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Element implements Serializable {
	private int posX;
	private int posY;
	private int id;
	private int energy;
	private String type;
	private boolean state;

	private ArrayList<Observer> observers;

	public void addObserver(Observer o) {
		this.observers.add(o);
	}

	public void removeObserver(Observer o) {
		this.removeObserver(o);
	}

	public void notifyObserver(int x, int y) {
		for (Observer o : observers) {
			o.update(x, y);
		}
	}

	public Element() {
		observers = new ArrayList<Observer>();
	}

	/**
	 * @return the posX
	 */
	public int getPosX() {
		return posX;
	}

	/**
	 * @param posX
	 *            the posX to set
	 */
	public void setPosX(int posX) {
		this.posX = posX;
	}

	/**
	 * @return the posY
	 */
	public int getPosY() {
		return posY;
	}

	/**
	 * @param posY
	 *            the posY to set
	 */
	public void setPosY(int posY) {
		this.posY = posY;
	}

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		this.energy = energy;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void move(int x, int y) {
		this.setPosX(x);
		this.setPosY(y);
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public boolean getState() {
		return this.state;
	}

}

package com.carter.visualvirusspread;

import java.awt.Color;

public class Human {
	private int health;
	private int xCoor, yCoor;
	private int presumedPeriod;
	private int deathTimer;
	public int getPresumedPeriod() {
		return presumedPeriod;
	}
	public void setPresumedPeriod(int presumedPeriod) {
		this.presumedPeriod = presumedPeriod;
	}
	public int getDeathTimer() {
		return deathTimer;
	}
	public void setDeathTimer(int deathTimer) {
		this.deathTimer = deathTimer;
	}
	// dead, infected, presumed, healthy
	private Color [] colors = {Color.decode("#21243d"), Color.decode("#b7472a"),Color.decode("#ffe75e"), Color.decode("#61d4b3")};
	public Human(int health) {
		setHealth(health);
	}

	public Human(int health, int period) {
		setHealth(health);
		setDeathTimer(period);
	}
	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
	}
	public int getxCoor() {
		return xCoor;
	}
	public void setxCoor(int xCoor) {
		this.xCoor = xCoor;
	}
	public int getyCoor() {
		return yCoor;
	}
	public void setyCoor(int yCoor) {
		this.yCoor = yCoor;
	}
	public Color getColor() {
		return colors[getHealth()];
	}
	public Color getColor(int i) {
		return colors[i];
	}
	
}

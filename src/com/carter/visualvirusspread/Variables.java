package com.carter.visualvirusspread;

import java.awt.Color;
import java.util.ArrayList;

public class Variables {
	private int WIDTH, HEIGHT;
	private int days;
	private Color titleBGC = Color.decode("#899857"); 
	private Color mainBGC = Color.decode("#65587f");
	private int humanPopulation;
	private int mobility, presumedPeriod, deathTimer;
	private ArrayList<Human> healthy, infected, presumed, dead;
	
	public Variables() {
		days = 0;
		healthy = new ArrayList<Human>();
		infected = new ArrayList<Human>();
		presumed = new ArrayList<Human>();
		dead = new ArrayList<Human>();
	}

	
	
	public int getMobility() {
		return mobility;
	}



	public void setMobility(int mobility) {
		this.mobility = mobility;
	}



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



	public ArrayList<Human> getHumans(int i){
		if(i == 0) return dead;
		if(i == 1) return infected;
		if(i == 2) return presumed;
		else return healthy;
	}
	public void increaseTime() {
		days ++;
	}
	public int getHealthy() {
		return healthy.size();
	}
	public int getDay() {
		return days;
	}
	public int getHumanPopulation() {
		return humanPopulation;
	}

	public void setHumanPopulation(int humanPopulation) {
		this.humanPopulation = humanPopulation;
		for(int i =0; i <getHumanPopulation() - getInfectedPopulation(); i ++) {
			healthy.add(new Human(3));
		}
	}

	public int getInfectedPopulation() {
		return infected.size();
	}

	public void setInfectedPopulation(int infectedPopulation, int period) {
		for(int i = 0; i < infectedPopulation; i ++) {
			infected.add(new Human(1, period));

		}
	}

	public int getPresumedPopulation() {
		return presumed.size();
	}


	public int getDeadPopulation() {
		return dead.size();
	}


	public Color getMainColor() {
		return mainBGC;
	}
	public Color getTitleBGC() {
		return titleBGC;
	}
	public int getWIDTH() {
		return WIDTH;
	}


	public void setWIDTH(int wIDTH) {
		WIDTH = wIDTH;
	}


	public int getHEIGHT() {
		return HEIGHT;
	}


	public void setHEIGHT(int hEIGHT) {
		HEIGHT = hEIGHT;
	}
	
	public void changeHealth(ArrayList<Human> humans, int hp) {
		if(hp ==2) {
			for(Human h : humans) {
				healthy.remove(h);
				h.setPresumedPeriod(getPresumedPeriod());
				h.setHealth(hp);
				presumed.add(h);
				
			}	
		}else if(hp ==1) {
			for(Human h : humans) {
				presumed.remove(h);
				h.setDeathTimer(getDeathTimer());
				h.setHealth(hp);
				infected.add(h);
			}
		}
		else if(hp == 0) {
			for(Human h : humans) {
				infected.remove(h);
				h.setHealth(hp);
				dead.add(h);
			}
		}
	}
	
	
}

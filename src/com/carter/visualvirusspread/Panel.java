package com.carter.visualvirusspread;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Panel extends JPanel implements ActionListener, Runnable{

	private boolean finished = false;
	private Thread thread;
	private boolean running = false;
	private Variables var;
	private Virus virus;
	JButton start, speedUp;
	private Human human1 = new Human(0);
	private int dayTime = 1000;
	Random r;
	public Panel() {
		r = new Random();
		this.setLayout(null);
		var = new Variables();
		var.setWIDTH(800);
		var.setHEIGHT(600);
		start = new JButton("Start Simulation");
		start.setBounds(var.getWIDTH()/2-75,var.getHEIGHT()/2-25,150,50);
		start.addActionListener(this);
		this.add(start);
		this.setPreferredSize(new Dimension(var.getWIDTH(),var.getHEIGHT()));
	}
	
	private void tick() {
			var.increaseTime();
			//get new human location
			ArrayList<Human> humans = var.getHumans(3);
			for(int i = 0; i <var.getHealthy(); i++) {
				humanMove(humans.get(i));
			}
			humans = var.getHumans(2);
			for(int i =0 ; i <humans.size(); i ++) {
				humanMove(humans.get(i));
			}
			humans = var.getHumans(1);
			for(int i = 0; i < humans.size(); i ++) humanMove(humans.get(i));

			//get infection and change status
			humans = var.getHumans(3);
			ArrayList<Human> change = var.getHumans(1); //get infected population
			HashSet<Human> set = new HashSet<Human>();
			ArrayList<Human> edits = new ArrayList<Human>();
			for(Human infected : change) {
				for(Human healthy: humans) {
					if(Math.abs(healthy.getxCoor()-infected.getxCoor()) <virus.getRadius() && Math.abs(healthy.getyCoor() - infected.getyCoor())<virus.getRadius()) {
						if(!set.contains(healthy)) {
							edits.add(healthy);
							set.add(healthy);
						}
					}
				}
			}
			var.changeHealth(edits, 2);
			edits = new ArrayList<Human>();
			for(Human presumed : var.getHumans(2)) {
				if(presumed.getPresumedPeriod() == 0) {
					edits.add(presumed);
				}
				else if(presumed.getPresumedPeriod() >0) {
					presumed.setPresumedPeriod(presumed.getPresumedPeriod()-1);
				}
			}
			var.changeHealth(edits, 1);
			edits = new ArrayList<Human>();
			for(Human infected : var.getHumans(1)) {
				if(infected.getDeathTimer() == 0) edits.add(infected);
				else if(infected.getDeathTimer() >0) {
					infected.setDeathTimer(infected.getDeathTimer()-1);
				}
			}
			var.changeHealth(edits, 0);
			repaint();
			if(var.getDeadPopulation() == var.getHumanPopulation()) stopSim();



	}
	
	//human specimen moves in random direction
	private void humanMove(Human human) {
		if(human.getxCoor() == 0 && human.getyCoor() == 0) {
			human.setxCoor(r.nextInt(var.getWIDTH()-250)+20);
			human.setyCoor(r.nextInt(500)+50);
		}else {
			int upDown = r.nextInt(2);
			int leftRight = r.nextInt(2);
			int newX = (leftRight == 1)? human.getxCoor()+var.getMobility() : human.getxCoor() -var.getMobility();
			int newY = (upDown == 1)? human.getyCoor() +var.getMobility() : human.getyCoor() -var.getMobility();
			if(newX >20 && newX < var.getWIDTH()-220 && newY > 50 && newY < 550) {
				human.setxCoor(newX);
				human.setyCoor(newY);
			}
			
		}
	}
	private void startSim() {
		running = true;
		start.setVisible(false);
		thread = new Thread(this);
		speedUp = new JButton("Speed Up");
		speedUp.setBounds(625, 400, 150, 50);
		speedUp.addActionListener(this);
		this.add(speedUp);
		thread.start();
		
	}
	private void stopSim() {
		running = false;
		finished = true;
		speedUp.setVisible(false);
		JOptionPane.showMessageDialog(null, "Everyone is dead");
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	private void initializeSim() {
		virus = new Virus();
		JTextField virusName = new JTextField("Virus Foo");
		JTextField virusRadius = new JTextField("5");
		JTextField humanPopulation = new JTextField("500");
		JTextField infectedPopulation = new JTextField("1");
		JTextField mobility = new JTextField("2");
		JTextField presumedPeriod = new JTextField("10");
		JTextField deathTimer = new JTextField("30");
		JLabel limits = new JLabel("Conditions: Dead cannot infect, presumed cannot infect");
		Object [] prompt = {
				limits,
				"Virus Name: ", virusName,
				"Spread Radius: ", virusRadius,
				"Human Population: ", humanPopulation,
				"Number Infected: ", infectedPopulation, 
				"Mobility: ", mobility,
				"Virus Hiding Days: ", presumedPeriod,
				"Days for infected to die: ", deathTimer
		};
		
		int result = JOptionPane.showConfirmDialog(null, prompt,"Simulation Setup", JOptionPane.OK_OPTION);
		if(result == JOptionPane.OK_OPTION) {
			try {
				virus.setName(virusName.getText());
				virus.setRadius(Integer.parseInt(virusRadius.getText()));

				var.setMobility(Integer.parseInt(mobility.getText()));
				var.setPresumedPeriod(Integer.parseInt(presumedPeriod.getText()));
				var.setDeathTimer(Integer.parseInt(deathTimer.getText()));
				var.setInfectedPopulation(Integer.parseInt(infectedPopulation.getText()), var.getDeathTimer());
				var.setHumanPopulation(Integer.parseInt(humanPopulation.getText()));
				startSim();
			}catch(Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Invalid Input(s)");
			}
		}
	}
	public void paintComponent(Graphics g) {
		if(!running && !finished) {
			g.setColor(var.getTitleBGC());
			g.fillRect(0, 0, var.getWIDTH(), var.getHEIGHT());
		}else{
			g.setColor(var.getMainColor());
			g.fillRect(0, 0, var.getWIDTH(), var.getHEIGHT());
			drawStats(g);
			drawHumans(g, var.getHumans(3));
			drawHumans(g, var.getHumans(2));
			drawHumans(g, var.getHumans(1));
			drawHumans(g, var.getHumans(0));
		}

	}
	
	public void drawHumans(Graphics g, ArrayList<Human> humans) {
		for(int i = 0; i < humans.size(); i ++) {
			g.setColor(humans.get(i).getColor());
			g.fillRect(humans.get(i).getxCoor(), humans.get(i).getyCoor(), 5, 5);
		}
	}
	
	public void drawStats(Graphics g) {
		
		g.setColor(Color.white);
		g.drawString("Day: "+var.getDay(), var.getWIDTH()-100, 50);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setStroke(new BasicStroke(5));
		g2d.drawString(virus.getName(), var.getWIDTH()-200, 50);
		g2d.drawRect(var.getWIDTH()-200, 100, 190, 400);
		g.setColor(human1.getColor(3));
		g.fillRect(var.getWIDTH()-170, 110, 5, 5);
		g.drawString("Healthy: "+var.getHealthy(), var.getWIDTH()-160, 116);
		g.setColor(human1.getColor(2));
		g.fillRect(var.getWIDTH()-170, 130, 5, 5);
		g.drawString("Presumed: "+var.getPresumedPopulation(), var.getWIDTH()-160, 136);
		g.setColor(human1.getColor(1));
		g.fillRect(var.getWIDTH()-170, 150, 5, 5);
		g.drawString("Infected: "+var.getInfectedPopulation(),var.getWIDTH()-160, 156);
		g.setColor(human1.getColor(0));
		g.fillRect(var.getWIDTH()-170, 170, 5, 5);
		g.drawString("Dead: "+var.getDeadPopulation(), var.getWIDTH()-160, 176);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().contentEquals("Start Simulation")) initializeSim();
		if(e.getActionCommand().contentEquals("Speed Up")) {
			dayTime -= (dayTime - 50 > 300)? 50 : 0;
		}
		
	}
	@SuppressWarnings("static-access")
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(running) {
			try {
				tick();
				thread.sleep(dayTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			
		}
	}
	
}

package com.carter.visualvirusspread;

import javax.swing.JFrame;

public class Main {

	public static void main(String [] args) {
		new Main();
	}
	
	public Main() {
		JFrame frame = new JFrame();
		frame.setTitle("Visual Virus Spread");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Panel panel = new Panel();
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}
}

package main;

import javax.swing.JFrame;

import screen.ScreenController;

public class Main {
	
	public static JFrame frame;
	
	public static void main(String[] args) {
		
		frame = new JFrame("Gennetic Runner || pre alpha");
		
		ScreenController screen = new ScreenController();
		
		frame.add(screen);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		
		frame.setVisible(true);
		
		screen.start();
		
	}
	
}

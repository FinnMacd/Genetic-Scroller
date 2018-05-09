package screen;

import java.awt.Color;
import java.awt.Graphics2D;

import game.Map;
import math.Matrix;
import network.Network;

public class WatchScreen extends Screen{
	
	private static Network network;
	
	public static int sum = 0;
	
	private Map map;
	
	private boolean autoRun = true;
	private int killtime = 120;
	
	public WatchScreen(ScreenController screen) {
		super(screen);
		map = new Map();
	}
	
	public void init() {
		if(!contInit && init)return;
		init = true;
		contInit = true;
		map.reset();
		map.start();
		network.setScore(0);
		killtime = 120;
	}
	
	public static void setNetwork(Network net) {
		network = net;
	}
	
	public void update(){
		
		if(map.getPlayer().isAlive()) {
			
			double[] inputData = new double[18];
			
			for(int i = 0; i < 16; i++)inputData[i] = map.getPlayer().getSightLength(i);
			
			inputData[16] = map.getPlayer().getVX();
			inputData[17] = map.getPlayer().getVY();
			
			Matrix next = network.simpleTest(Matrix.rowMatrix(inputData));
			
			if(next.getAttribute(0, 0) > 0.5)map.getPlayer().jump();
			if(next.getAttribute(1, 0) > 0.5)map.getPlayer().moveLeft();
			if(next.getAttribute(2, 0) > 0.5)map.getPlayer().moveRight();
			
		}else {
			killtime--;
			
			if(autoRun && killtime < 0) {
				sum+=network.getScore();
				screenController.changeScreen(ScreenController.CONTROL);
			}
		}
		
		if(ScreenController.input.left) {
			map.reset();
			map.start();
		}
		if(ScreenController.input.right) {
			screenController.setUPS(48000);
			ScreenController.input.right = false;
		}
		if(ScreenController.input.up) {
			screenController.setUPS((int)ScreenController.UPS + 600);
			ScreenController.input.up = false;
		}
		if(ScreenController.input.down) {
			screenController.setUPS(((int)ScreenController.UPS - 600 < 0)?(int)ScreenController.UPS:(int)ScreenController.UPS - 600);
			ScreenController.input.down = false;
		} 
		if(ScreenController.input.space) {
			autoRun = !autoRun;
			ScreenController.input.space = false;
		}
		if(ScreenController.input.ctrl && !map.getPlayer().isAlive()) {
			screenController.changeScreen(ScreenController.CONTROL);
		}
		
		map.update();
		
		network.setScore(map.getPlayer().getX() - map.getTime()*0 - map.getPlayer().getScoreOffset());
		
	}
	
	public void draw(Graphics2D g) {
		map.draw(g);
		
		g.setColor(Color.red);
		g.drawString("Score: " + network.getScore(), 20, 40);
		g.drawString("Trial: " + (network.getID() + 1), 20, 80);
		g.drawString("Time: " + map.getTime(), 560, 40);
		
	}
	
}

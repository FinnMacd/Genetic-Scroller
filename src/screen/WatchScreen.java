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
		//map.randomize();
		map.start();
		network.setScore(0);
		killtime = 120;
	}
	
	public static void setNetwork(Network net) {
		network = net;
	}
	
	public void update(){
		
		if(map.getPlayer().isAlive()) {
			Matrix next = network.simpleTest(Matrix.rowMatrix(new double[] {
					1-map.getPlayer().getSightLength(0),
					1-map.getPlayer().getSightLength(1),
					1-map.getPlayer().getSightLength(2),
					}));
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
		
		network.setScore(map.getPlayer().getX() - map.getTime()*0);
		
	}
	
	public void draw(Graphics2D g) {
		map.draw(g);
		
		g.setColor(Color.red);
		g.drawString("Score: " + network.getScore(), 20, 40);
		g.drawString("Trial: " + (network.getID() + 1), 20, 80);
		g.drawString("Time: " + map.getTime(), 560, 40);
		
	}
	
}

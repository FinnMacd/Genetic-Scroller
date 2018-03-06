package screen;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.Comparator;

import network.Network;

public class ControlScreen extends Screen{
	
	private Network[] networks;
	private int generation = 0;
	private int bestScore = 0, averageScore = 0;
	
	private boolean testing = false, doneTest = false, hasEvolved = true;
	private int currentTest = 0;
	
	public ControlScreen(ScreenController screen) {
		super(screen);
	}
	
	public void init() {
		if(!contInit && init)return;
		init = true;
		
		networks = new Network[100];
		
		for(int i = 0; i < networks.length; i++) {
			networks[i] = new Network(4,50,3);
			networks[i].setID(i);
		}
		System.out.println(networks[currentTest].getScore());
	}
	
	@SuppressWarnings("unchecked")
	public void update() {
		
		if(testing) {
			
			WatchScreen.setNetwork(networks[currentTest]);
			screenController.changeScreen(ScreenController.WATCH);
			currentTest++;
			if(currentTest == networks.length) {
				testing = false;
				doneTest = true;
			}else if(currentTest == networks.length/4-1) {
				averageScore = WatchScreen.sum/(currentTest+1);
				WatchScreen.sum = 0;
			}
			
		}else {
			
			if(doneTest) {
				Arrays.sort(networks, new Comparator<Network>() {
					public int compare(Network o1, Network o2) {
						
						if((o1).getScore() < (o2).getScore())return 1;
						else if((o1).getScore() == (o2).getScore())return 0;
						return -1;
						
					}});
				for(int i = 0; i < networks.length; i++)networks[i].setID(i);
				screenController.setUPS(60);
				bestScore = networks[0].getScore();
				
				doneTest = false;
				
				WatchScreen.sum = 0;
				
				screenController.setDraw(true);
				
				evolve();
				generation++;
					
			}
			
			if(ScreenController.input.space) {
				testing = true;
				currentTest = 0;
				ScreenController.input.space = false;
			}
			
			if(ScreenController.input.right) {
				testing = true;
				currentTest = 0;
				ScreenController.input.right = false;
				screenController.setDraw(false);
			}
			
		}
		
	}
	
	public void evolve() {
		for(int i = 0; i < networks.length/4; i++) {
			networks[i+networks.length/4] = networks[i].mutate();
			networks[i+networks.length/4].setID(i+networks.length/4);
			networks[i+networks.length/2] = networks[i].mutate();
			networks[i+networks.length/2].setID(i+networks.length/2);
			networks[i+3*networks.length/4] = networks[i].mutate();
			networks[i+3*networks.length/4].setID(i+3*networks.length/4);
		}
	}
	
	public void draw(Graphics2D g) {
		g.setColor(Color.RED);
		g.drawString("Generations Passed: " + generation, 20, 40);
		g.drawString("Best Score: " + bestScore, 20, 80);
		g.drawString("Average Score: " + averageScore, 20, 120);
	}
	
}

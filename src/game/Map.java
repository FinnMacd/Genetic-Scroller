package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Scanner;

import screen.ScreenController;

public class Map {
	
	private Player player;
	
	public int width, height, size = 80;
	private int[][] map;
	
	private int numUps, updateCap = 800, startX = 60, startY = 240, screenX = 0;
	
	public Map() {
		
		player = new Player(startX,startY, this);
		player.start();
		
		loadMap();
		
	}
	
	private void loadMap() {
		Scanner scanner = new Scanner(getClass().getResourceAsStream("/Map"));
		width = Integer.parseInt(scanner.nextLine().split("=")[1]);
		height = Integer.parseInt(scanner.nextLine().split("=")[1]);
		map = new int[width][height];
		for (int y = 0; y < height; y++) {
			String[] bl = scanner.nextLine().split("");
			for (int x = 0; x < width; x++) {
				map[x][y] = Integer.parseInt(bl[x]);
			}
		}
	}
	
	public void start() {
		numUps = 0;
		player.start();
	}
	
	public void reset() {
		player.reset(startX, startY);
		screenX = 0;
		updateCap = 800;
	}
	
	public void update() {
		
		screenX = player.getX() - ScreenController.width/2;
		
		if(screenX < 0)screenX = 0;
		if(screenX > width*size - ScreenController.width)screenX = width*size - ScreenController.width;
		
		player.update();
		
	}
	
	public void draw(Graphics2D g) {
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 640, 480);
		
		
		g.setColor(Color.BLUE);
		
		int start = (screenX/size > 0)?screenX/size:0;
		int end = (start + 9 < width)?start + 9:width;
		
		for(int y = 0; y < height; y++) {
			for(int x = start; x < end; x++) {
				if(map[x][y] == 1)g.fillRect(x*size-screenX, y*size, size, size);
			}
		}
		
		player.draw(g, screenX);
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public int getTime() {
		return (updateCap+60-numUps)/60;
	}
	
	public boolean isWall(int x, int y) {
		if(x < 0 || x >= size*width || y < 0 || y >= size*height)return false;
		return map[x/size][y/size] == 1;
	}
	
	public boolean isWall(int[] pos) {
		return isWall(pos[0], pos[1]);
	}
	
	public boolean isWall(int[] pos, int x, int y) {
		return isWall(pos[0]+x, pos[1]+y);
	}
	
}

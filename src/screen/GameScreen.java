package screen;

import java.awt.Graphics2D;

import game.Map;

public class GameScreen extends Screen{
	
	private Map map;
	
	public GameScreen(ScreenController screen) {
		super(screen);
		
		map = new Map();
	}
	
	public void init() {
		super.init();
		map.updateCap = 10000000;
	}
	
	public void update() {
		
		if(screenController.input.left)map.getPlayer().moveLeft();
		if(screenController.input.right)map.getPlayer().moveRight();
		if(screenController.input.up)map.getPlayer().jump();
		
		map.update();
		
	}
	
	public void draw(Graphics2D g){
		map.draw(g);
	}
	
}

package screen;

import java.awt.Graphics2D;

public abstract class Screen {
	
	protected ScreenController screenController;
	
	protected boolean contInit = false, init = false;
	
	public Screen(ScreenController screen) {
		screenController = screen;
	}
	
	protected void init() {
		if(!contInit && init)return;
		init = true;
	}
	
	protected void update() {
		
	}
	
	protected void draw(Graphics2D g) {
		
	}
	
}

package screen;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import main.Inputs;
import main.Main;

public class ScreenController extends Canvas implements Runnable{
	
	public static int GAME = 0, WATCH = 1, CONTROL = 2;
	public static int width = 640, height = 480;
	public static double scale = 1.0;
	
	public static Inputs input;
	
	private Thread screenThread;
	private boolean isRunning = false, draw = true;
	
	public static double UPS = 60.0, ns = 1000000000.0/UPS;
	
	private BufferedImage image;
	private Graphics2D g2d;
	
	private Screen[] screens;
	private int currentScreen;
	
	public ScreenController() {
		
		setPreferredSize(new Dimension((int)(width*scale), (int)(height*scale)));
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		g2d = (Graphics2D)image.getGraphics();
		
		input = new Inputs(this);
		
		addMouseListener(input);
		addMouseMotionListener(input);
		addKeyListener(input);
		addFocusListener(input);
		
		screens = new Screen[] {
			
			new GameScreen(this),
			new WatchScreen(this),
			new ControlScreen(this)
			
		};
//		changeScreen(GAME);
		changeScreen(CONTROL);
		
		
	}
	
	public void start() {
		if(isRunning)return;
		
		isRunning = true;
		screenThread = new Thread(this);
		screenThread.start();
		
	}
	
	public void stop() {
		if(!isRunning)return;
		try {
			screenThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		
		double lastTime = System.nanoTime();
		double carry = 0;
		
		int updates = 0, frames = 0;
		long printTime = System.currentTimeMillis();
		
		while(isRunning) {
			
			carry += System.nanoTime() - lastTime;
			lastTime = System.nanoTime();
			
			if(draw) {
				while(carry >= ns) {
					update();
					updates++;
					carry -= ns;
				}

				draw();
				drawToScreen();
				frames ++;
			}else {
				carry = 0;
				update();
				updates++;
			}
			
			if(System.currentTimeMillis() - printTime >= 1000) {
				System.out.println("UPS: " + updates + " || Frames: " + frames);
				updates = frames = 0;
				printTime = System.currentTimeMillis();
			}
			
		}
		
	}
	
	private void update() {
		
		screens[currentScreen].update();
		
	}
	
	private void draw() {
		
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, width, height);
		screens[currentScreen].draw(g2d);
		
	}
	
	private void drawToScreen() {
		
		BufferStrategy bs = getBufferStrategy();
		
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, (int)(width*scale), (int)(height*scale), null);
		g.dispose();
		
		bs.show();
		
	}
	
	public void changeScreen(int i) {
		currentScreen = i;
		screens[currentScreen].init();
	}
	
	public void setScale(double scale) {
		this.scale = scale;
		setPreferredSize(new Dimension((int)(width*scale), (int)(height*scale)));
		Main.frame.pack();
		Main.frame.setLocationRelativeTo(null);
		
	}
	
	public void setUPS(int ups) {
		UPS = ups;
		ns = 1000000000.0/UPS;
	}
	
	public void setDraw(boolean draw) {
		this.draw = draw;
	}
	
}

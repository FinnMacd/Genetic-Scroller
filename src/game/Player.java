package game;

import java.awt.Color;
import java.awt.Graphics2D;

import screen.ScreenController;

public class Player {
	
	private Map map;
	
	private int maxSpeed = 5, width = 40, height = 70;
	private double x,y,vx,vy, sightAngle = 0.1;
	
	private int[][] sightPoints;
	private boolean sightCheck = false, isAlive = false, isFalling = false, isMoving = false;
	
	public int numUpgrades = 0;
	
	public double carry = 0;
	
	public Player(int x, int y, Map map) {
		this.x = x;
		this.y = y;
		this.vx = 0;
		this.vy = 0;
		this.map = map;
		sightPoints = new int[16][3];
	}
	
	public void update() {
		if(!isAlive)return;
		
		if(!map.isWall(getCorner(2),1,0) && !map.isWall(getCorner(3),-1,0))isFalling = true;
		
		if(isFalling) {
			vy += 0.3;
			if(map.isWall(getCorner(2), 1, (int)vy) || map.isWall(getCorner(3), -1, (int)vy)) {
				isFalling = false;
				y = map.size*(int)((getCorner(2)[1] + vy)/map.size) - height/2;
				vy = 0;
			}
		}
		
		if(vy < 0) {
			if(map.isWall(getCorner(0), -1, (int)vy) || map.isWall(getCorner(1), 1, (int)vy)) {
				y = map.size*(int)((getCorner(2)[1])/map.size) + height/2 + 1;
				vy = 0;
			}
		}
		
		if(!isMoving)vx *= 0.95;
		
		if(vx > 0) {
			if(map.isWall((int)(getCorner(0)[0] + vx), (int)(getCorner(0)[1] - 1)) || map.isWall((int)(getCorner(3)[0] + vx), (int)(getCorner(3)[1] - 1))) {
				x = map.size*(int)((getCorner(0)[0] + vx)/map.size) - width/2;
				vx = 0;
			}
		}
		
		if(vx < 0) {
			if(map.isWall((int)(getCorner(1)[0] + vx), (int)(getCorner(1)[1] - 1)) || map.isWall((int)(getCorner(2)[0] + vx), (int)(getCorner(2)[1] - 1))) {
				x = map.size*(int)((getCorner(0)[0])/map.size) + width/2;
				vx = 0;
			}
		}
		
		x += vx;
		y += vy;
		
		checkSight();
		
		isMoving = false;
		
	}
	
	public void draw(Graphics2D g, int screen) {
		
		x -= screen;
		
		g.setColor(Color.GREEN);
		g.fillRect((int)(x - width/2), (int)(y-height/2), width, height);
		g.setColor(Color.red);
		
		for(int i = 0; i < 16; i++) {
			
			double angle = i*Math.PI/8.0;
			
			g.drawLine((int)x, (int)y, (int)(x+Math.cos(angle)*map.size*5), (int)(y+Math.sin(angle)*map.size*5));
			
		}
		
		for(int [] pos:sightPoints) {
			g.fillOval((int)x + pos[0]-5, (int)y + pos[1] - 5, 10, 10);
		}
		
		x += screen;
		
	}
	
	public void checkSight() {

		for(int i = 0; i < 16; i++) {
			
			double angle = i*Math.PI/8.0;
			
			for(int l = 0; l < 41; l++) {
				double length = map.size*5*l/40;
				int tx = (int)(x+Math.cos(angle)*length);
				int ty = (int)(y+Math.sin(angle)*length);
				if(map.isWall(tx, ty) || l == 40) {
					sightPoints[i][0] = tx - (int)x;
					sightPoints[i][1] = ty - (int)y;
					sightPoints[i][2] = l*100/40;
					break;
				}
			}
			
		}
		
	}
	
	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void start() {
		isAlive = true;
	}
	
	public void reset(int x, int y) {
		isAlive = false;
		setPos(x,y);
		vx = 0;
		vy = 0;
		numUpgrades = 0;
		carry = 0;
	}
	
	public void moveLeft() {
		isMoving = true;
		if(vx > -maxSpeed)vx -= 0.1;
	}
	
	public void moveRight() {
		isMoving = true;
		if(vx < maxSpeed)vx += 0.1;
	}
	
	public void jump() {
		if(!isFalling)vy = -10;
	}
	
	public int getX() {
		return (int)x;
	}
	
	public int getY() {
		return (int)y;
	}
	
	public void kill() {
		isAlive = false;
	}
	
	public boolean isAlive() {
		return isAlive;
	}
	
	public int[] getSightPoints(int i, double length) {
		
		int[] ret = new int[3];
		
		return ret;
		
	}
	
	public int[] getCorner(int i) {
		
		int[] ret = new int[2];
		
		ret[0] = (int)x;
		ret[1] = (int)y;
		
		if(i == 0 || i == 3)ret[0] += width/2;
		else ret[0] -= width/2;
		
		if(i == 0 || i == 1)ret[1] -= height/2;
		else ret[1] += height/2;
		
		return ret;
		
	}
	
	public double getSightLength(int i) {
		return ((double)sightPoints[i][2]/100.0);
	}
	
}

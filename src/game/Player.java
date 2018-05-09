package game;

import java.awt.Color;
import java.awt.Graphics2D;

import screen.ScreenController;

public class Player {
	
	private Map map;
	
	private int maxSpeed = 5, width = 40, height = 70, jumpTime = 0, scoreOffset = 0;
	private double x,y,vx,vy;
	
	private int[][] sightPoints;
	private boolean isAlive = false, isFalling = true, isMoving = false;
	
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
				y = map.size*(int)(y/map.size + 1) - height/2;
				vy = 0;
			}
		}
		if(vy < 0) {
			if(map.isWall(getCorner(0), -1, (int)vy) || map.isWall(getCorner(1), 1, (int)vy)) {
				y = map.size*(int)(y/map.size) + height/2;
				vy = 0;
			}
		}
		
		if(!isMoving)vx *= 0.95;
		if(vx < 0.1 && vx > -0.1)vx = 0;
		
		if(vx > 0) {
			if(map.isWall(getCorner(0), (int)vx, 1) || map.isWall(getCorner(3), (int)vx, -1)) {
				x = map.size*(int)((getCorner(0)[0] + vx)/map.size) - width/2 - 1;
				vx = 0;
			}
		}else if(vx < 0) {
			if(map.isWall(getCorner(1), (int)vx, 1) || map.isWall(getCorner(2), (int)vx, -1)) {
				x = map.size*(int)((getCorner(1)[0] + 1)/map.size) + width/2 + 1;
				vx = 0;
			}
		}
		
		if(y > ScreenController.height + height || (y < 0 && !isFalling))isAlive = false;
		
		x += vx;
		y += vy;
		jumpTime--;
		
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
					sightPoints[i][2] = (int)l;
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
		isFalling = true;
		setPos(x,y);
		jumpTime = 0;
		scoreOffset = 0;
		vx = 0;
		vy = 0;
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
		if(!isFalling) {
			jumpTime = 120;
			scoreOffset += 20;
			vy = -10;
		}
	}
	
	public int getX() {
		return (int)x;
	}
	
	public int getY() {
		return (int)y;
	}
	
	public double getVX() {
		return vx / maxSpeed;
	}
	
	public double getVY() {
		return vy / maxSpeed;
	}
	
	public void kill() {
		isAlive = false;
	}
	
	public boolean isAlive() {
		return isAlive;
	}
	
	public int getScoreOffset() {
		return scoreOffset;
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
		return ((double)sightPoints[i][2]/(map.size*5));
	}
	
}

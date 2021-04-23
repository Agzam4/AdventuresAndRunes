package Entity;

import TileMap.TileMap;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Laser extends MapObject {
	
	private boolean hit;
	private boolean remove;
	private BufferedImage[] hitSprites;

	int fixsedX;
	int fixsedY;
	Player player;
	public double dir;
	
	public Laser(TileMap tm, int x, int y, int dir, Player player) {
		
		super(tm);
		
		this.player = player;
		
		fixsedX = x;
		fixsedY = y;
		this.dir = dir;
		
		facingRight = right;
		
		moveSpeed = 3.8;
		if(right) dx = moveSpeed;
		else dx = -moveSpeed;
		
		width = 5;
		height = 5;
		cwidth = 5;
		cheight = 5;
		
		// load sprites
		try {
			animation = new Animation();
			animation.setFrames(new BufferedImage[] {new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)});
			animation.setDelay(70);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setHit() {
		if(hit) return;
		hit = true;
		animation.setFrames(hitSprites);
		animation.setDelay(70);
		dx = 0;
	}
	
	public boolean shouldRemove() { return remove; }
	
	public void update() {
		x = fixsedX;
		y = fixsedY;
		for (int i = 0; i < 250; i++) {
			dx = Math.sin(dir)*5;
			dy = Math.cos(dir)*5;
			checkTileMapCollision();
			setPosition(xtemp, ytemp);
			if(player.intersects(this)) {
//				hit = true;
				player.hit(50);
			}
			if(hit)
				break;
		}
		
		animation.update();
		if(hit && animation.hasPlayedOnce()) {
			remove = true;
		}
		
	}
	
	public void draw(Graphics2D g) {
		if(!vis)
			return;
		setMapPosition();
		g.setColor(new Color(100,255,180,60));
		for (int i = 0; i < 9; i++) {
			g.setStroke(new BasicStroke(i));
			drawLine(g);
		}
		for (int i = 1; i < 7; i++) {
			fillOval(g, i);
		}
		g.setStroke(new BasicStroke(1));
		super.draw(g);
		
	}
	
	boolean vis = false;

	public void setVisible(boolean b) {
		vis = b;
	}
	
	private void fillOval(Graphics2D g, int size) {
		g.fillOval(
				(int)(x+ xmap - width / 2) - size,
				(int)(y + ymap - height / 2) - size,
				size*2, size*2);

	}
	private void drawLine(Graphics2D g) {
		g.drawLine(
				(int) (fixsedX + xmap - width / 2),
				(int) (fixsedY + ymap - height / 2),
				(int)(x+ xmap - width / 2),
				(int)(y + ymap - height / 2));
	}
	
}



















package Entity;

import TileMap.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Life extends MapObject {
	
	private boolean hit;
	private boolean remove;
	private BufferedImage[] sprites;
	private BufferedImage[] hitSprites;
	
	public Life(TileMap tm) {
		
		super(tm);
		
		facingRight = right;
		
		moveSpeed = 3.8;
		dx = Math.random()*20 - 10;
		dy = Math.random()*10;
		
		width = 30;
		height = 30;
		cwidth = 13;
		cheight = 13;
		
		try {
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Player/Life.png"
				)
			);
			
			sprites = new BufferedImage[4];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(
					i * width,
					0,
					width,
					height
				);
			}
			
			hitSprites = new BufferedImage[3];
			for(int i = 0; i < hitSprites.length; i++) {
				hitSprites[i] = spritesheet.getSubimage(
					i * width,
					height,
					width,
					height
				);
			}
			
			animation = new Animation();
			animation.setFrames(sprites);
			animation.setDelay(70);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	int timee = 0;
	
	public void setHit() {
		if(hit) return;
		hit = true;
		animation.setFrames(hitSprites);
		animation.setDelay(70);
		dx = 0;
	}
	
	public boolean shouldRemove() { return remove; }
	
	public void update() {
		
		dy++;
		if(dy > 10)
			dy = 10;
		
		dx = dx*0.8;
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		animation.update();
		if(timee < 5000) {
			timee++;
		}else {
			if(hit && animation.hasPlayedOnce()) {
				remove = true;
			}
		}
		
	}
	
	public void draw(Graphics2D g) {
		setMapPosition();
		super.draw(g);
	}
	
}



















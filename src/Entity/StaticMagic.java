package Entity;

import TileMap.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import GameState.Level1State;

public class StaticMagic extends MapObject {
	
	private boolean hit;
	private boolean remove;
	private boolean isStatic;
	private BufferedImage[] sprites;
	private BufferedImage[] spawnSprites;
	
	
	public StaticMagic(TileMap tm, boolean right, boolean isStatic) {
		
		super(tm);
		this.isStatic = isStatic;
		facingRight = right;
		
		moveSpeed = 3.8;
		if(right) dx = moveSpeed;
		else dx = -moveSpeed;
		
		width = 30;
		height = 30;
		cwidth = 15;
		cheight = 15;
		
		// load sprites
		try {
			
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Player/static_magic.png"
				)
			);
			
			spawnSprites = new BufferedImage[3];
			for(int i = 0; i < spawnSprites.length; i++) {
				spawnSprites[i] = spritesheet.getSubimage(
					i * width,
					0,
					width,
					height
				);
			}
			
			sprites = new BufferedImage[4];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(
					i * width,
					height,
					width,
					height
				);
			}
			
			animation = new Animation();
			animation.setFrames(spawnSprites);
			animation.setDelay(70);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void setHit() {
		if(hit) return;
		hit = true;
		animation.setFrames(spawnSprites);
		animation.setDelay(70);
		dx = 0;
	}
	
	public boolean shouldRemove() { return remove; }
	
	int timing = 0;
	
	public void update() {
		
		checkTileMapCollision();
		if(!isStatic)
		setPosition(xtemp, ytemp);
		
		if(timing > 1000 && !hit) {
			setHit();
		}
		timing++;
		animation.update();
		if(hit && animation.hasPlayedOnce()) {
			remove = true;
		}
		if(animation.hasPlayedOnce()) {
			animation.setFrames(sprites);
			animation.setDelay(70);
		}
	}
	
	public void draw(Graphics2D g) {
		
		setMapPosition();
		
		super.draw(g);
		
	}
	
}



















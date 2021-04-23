package Entity;

import TileMap.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class StoneGoblin extends MapObject {
	
	private boolean hit;
	private boolean remove;
	boolean right;
	private BufferedImage[] sprites;
	private BufferedImage[] main;
	
	public StoneGoblin(TileMap tm, boolean right) {
		
		super(tm);
		facingRight = right;
		moveSpeed = 3.8;
		if(right) dx = moveSpeed;
		else dx = -moveSpeed;
		this.right = right;
		
		width = 30;
		height = 30;
		cwidth = 30;
		cheight = 30;
		
		try {
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Player/StoneGoblin.png"
				)
			);
			sprites = new BufferedImage[1];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(
					i * width,
					0,
					width,
					height
				);
			}
			main = new BufferedImage[] {sprites[0]};
			animation = new Animation();
			animation.setFrames(main);
			animation.setDelay(250);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setHit() {
		if(hit) return;
		animation.setDelay(70);
		dx = 0;
	}
	
	public boolean shouldRemove() { return remove; }
	
	
	public boolean isRight() {
		return right;
	}
	
	public void update() {
		
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		animation.update();
	}
	
	public void draw(Graphics2D g) {
		setMapPosition();
		super.draw(g);
	}
}


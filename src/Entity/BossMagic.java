package Entity;

import TileMap.TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import Entity.Enemies.GoblinBoss;

public class BossMagic extends MapObject {
	
	private boolean hit;
	private boolean remove;
	private BufferedImage[] sprites;
	private BufferedImage[] spawnSprites;
	
	public static int HOMING_IN = 0;
	
	GoblinBoss goblinBoss;
	Player player;
	int type;
	double dir = 0;

	private final double[] speedArr = {1};
	private final double[] maxSpeedArr = {3};
	
	public BossMagic(TileMap tm, GoblinBoss boss, Player player, int type) {
		
		super(tm);
		
		x = boss.getx();
		y = boss.gety();
		dir = Math.atan2(boss.getx(),boss.gety());
		
		goblinBoss = boss;
		this.player = player;
		this.type = type;
		
		facingRight = right;
		
		moveSpeed = speedArr[type];
		maxSpeed = maxSpeedArr[type];
		
		width = 30;
		height = 30;
		cwidth = 15;
		cheight = 15;
		dir = Math.atan2(player.getx()-x,player.gety()-y);
		
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
	double speed = 0;
	
	public void update() {
		switch (type) {
		case 0:
			double wx = player.getx()-x;
			double wy = player.gety()-y;
			double ndir = Math.atan2(player.getx()-x,player.gety()-y);
			dir = (dir-ndir) * 0.95 + ndir;
			speed += (50-Math.sqrt(wx*wx + wy*wy))/-250;
			if(speed > 5)
				speed = 5;
			if(speed < -5)
				speed = -5;
			dx = Math.sin(dir)*speed;
			dy = Math.cos(dir)*speed;
			break;
		default:
			break;
		}
		
		if(timing > 1000 && !hit) {
			hit = true;
			animation.setFrames(spawnSprites);
			animation.setDelay(70);
		}
		
		checkTileMapCollision();
		
		setPosition(xtemp, ytemp);
		
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

	public double getDx() {
		return dx;
	}
	public double getDy() {
		return dy;
	}
}



















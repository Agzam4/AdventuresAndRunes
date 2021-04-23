package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Entity.Animation;
import TileMap.TileMap;

public class SwampCreature extends TYPE_Tank {

	private ArrayList<BufferedImage[]> sprites;
	
	private final int[] numFrames = {
			2, 6, 1, 2, 4, 2, 5
	};
	
	public SwampCreature(TileMap tm){

		super(tm);

		hasSwampBoost = true;
		
		moveSpeed = 0.7;
		maxSpeed = 0.7;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;

		jumpStart = -4.8;
		stopJumpSpeed = 0.3;

		width = 30;
		height = 30;
		cwidth = 15;
		cheight = 20;

		health = maxHealth = 150;
		damage = 300;
		
		maxHP = maxHealth;

		// load sprites
		try {

			try {
				
				BufferedImage spritesheet = ImageIO.read(
					getClass().getResourceAsStream(
						"/Sprites/Enemies/swamp_creature.png"
					)
				);
				
				sprites = new ArrayList<BufferedImage[]>();
				for(int i = 0; i < 7; i++) {
					
					BufferedImage[] bi =
						new BufferedImage[numFrames[i]];
					
					for(int j = 0; j < numFrames[i]; j++) {
						
						if(i != SCRATCHING) {
							bi[j] = spritesheet.getSubimage(
									j * width,
									i * height,
									width,
									height
							);
						}
						else {
							bi[j] = spritesheet.getSubimage(
									j * width * 2,
									i * height,
									width * 2,
									height
							);
						}
						
					}
					sprites.add(bi);
					
				}
				
			}
			catch(Exception e) {
				e.printStackTrace();
			}

		}
		catch(Exception e) {
			e.printStackTrace();
		}

		animation = new Animation();
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(300);

		right = true;
		facingRight = true;

	}

	public void update() {

		super.update();
		
		// update position
//		getNextPosition();
//		checkTileMapCollision();
//		setPosition(xtemp, ytemp);
//
//		// check flinching
//		if(flinching) {
//			long elapsed =
//					(System.nanoTime() - flinchTimer) / 1000000;
//			if(elapsed > 400) {
//				flinching = false;
//			}
//		}
		
		// check attack has stopped
				if(currentAction == SCRATCHING) {
					if(animation.hasPlayedOnce()) scratching = false;
				}
				if(currentAction == FIREBALL) {
					if(animation.hasPlayedOnce()) firing = false;
				}
				
				// set animation
				if(scratching) {
					if(currentAction != SCRATCHING) {
						currentAction = SCRATCHING;
						animation.setFrames(sprites.get(SCRATCHING));
						animation.setDelay(50);
						width = 60;
					}
				}
				else if(firing) {
					if(currentAction != FIREBALL) {
						currentAction = FIREBALL;
						animation.setFrames(sprites.get(FIREBALL));
						animation.setDelay(100);
						width = 30;
					}
				}
				else if(dy > 0) {
					if(currentAction != FALLING) {
						currentAction = FALLING;
						animation.setFrames(sprites.get(FALLING));
						animation.setDelay(100);
						width = 30;
					}
				}
				else if(dy < 0) {
					if(currentAction != JUMPING) {
						currentAction = JUMPING;
						animation.setFrames(sprites.get(JUMPING));
						animation.setDelay(-1);
						width = 30;
					}
				}
				else if(left || right) {
					if(currentAction != WALKING) {
						currentAction = WALKING;
						animation.setFrames(sprites.get(WALKING));
						animation.setDelay(80);
						width = 30;
					}
				}
				else {
					if(currentAction != IDLE) {
						currentAction = IDLE;
						animation.setFrames(sprites.get(IDLE));
						animation.setDelay(400);
						width = 30;
					}
				}
				
				animation.update();
				
				// set direction
				if(currentAction != SCRATCHING && currentAction != FIREBALL) {
					if(right) facingRight = true;
					if(left) facingRight = false;
				}
				

		// update animation
		animation.update();

	}

	public void draw(Graphics2D g) {

		//if(notOnScreen()) return;

		setMapPosition();

		hp = health;
		super.draw(g);

	}
}

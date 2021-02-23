package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.Enemy;
import Entity.FireBall;
import Entity.Player;
import Main.GamePanel;
import TileMap.Tile;
import TileMap.TileMap;

public class Goblin extends Enemy{

	private ArrayList<BufferedImage[]> sprites;
	

	private boolean scratching;
	private boolean firing;
	protected static int DAMAGE = 100;

	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int GLIDING = 4;
	private static final int FIREBALL = 5;
	private static final int SCRATCHING = 6;

	private final int[] numFrames = {
			2, 6, 1, 2, 4, 2, 5
	};
	
	public Goblin(TileMap tm) {

		super(tm);

		moveSpeed = 0.6;
		maxSpeed = 0.3;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;

		jumpStart = -4.8;
		stopJumpSpeed = 0.3;

		width = 30;
		height = 30;
		cwidth = 15;
		cheight = 20;

		health = maxHealth = 50;
		damage = 250;
		
		maxHP = maxHealth;

		// load sprites
		try {

			try {
				
				BufferedImage spritesheet = ImageIO.read(
					getClass().getResourceAsStream(
						"/Sprites/Enemies/goblin.png"
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

	private void getNextPosition() {

		// TODO: AI
		if (Player.staticX < x) {
			left = true;
			right = false;
		}
		if (Player.staticX > x) {
			left = false;
			right = true;
		}
		if (Math.abs(Player.staticX - x) < tileSize/2 && (int)(Player.staticY/tileSize) == (int)(y/tileSize)) {
			left = false;
			right = false;
			scratching = true;
			dx = 0;
			damage = DAMAGE;
			cwidth = 30;
		}else {
			damage = 0;
			cwidth = 15;
		}

		
		if(Math.abs(Player.staticX - x) < tileSize * 10)
			jumping = Player.staticY < y - 5 && (right || left) && dx == 0;
		else {
			jumping = false;
			right = false;
			left = false;
			dx = 0;
		}
		
		// movement
		if(left) {
			dx -= moveSpeed;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		}
		else if(right) {
			dx += moveSpeed;
			if(dx > maxSpeed) {
				dx = maxSpeed;
			}
		}

		// falling
		if(falling) {
			dy += fallSpeed;
		}
		
		if(jumping && !falling) {
			dy = jumpStart;
			falling = true;
		}

		if(y+cheight > tileMap.getHeight()) {
			dead = true;
		}
	}

	public void update() {

		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);

		// check flinching
		if(flinching) {
			long elapsed =
					(System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 400) {
				flinching = false;
			}
		}
		
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

package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.Arrow;
import Entity.Enemy;
import Entity.Player;
import Entity.Tongue;
import TileMap.TileMap;

public class Frog extends Enemy {

	private ArrayList<BufferedImage[]> sprites;
	

	private boolean scratching;
	private boolean firing;
	private int fireBallDamage = 200;
	protected static int DAMAGE = 100;
	
	private double fire;
	private int fireCost;

	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int FIREBALL = 5;
	private static final int SCRATCHING = 6;

	private final int[] numFrames = {
			2, 6, 1, 2, 4, 3, 5
	};
	private Player player;


	private ArrayList<Tongue> arrows;
	
	public Frog(TileMap tm, Player player) {
		super(tm);
		
		this.player = player;

		moveSpeed = 0.4;
		maxSpeed = 1.2;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;

		jumpStart = -4.8;
		stopJumpSpeed = 0.3;

		fire  = 10;
		fireCost = 6;
		arrows = new ArrayList<Tongue>();
		
		width = 30;
		height = 30;
		cwidth = 15;
		cheight = 20;

		health = maxHealth = 75;
		damage = 250;
		
		maxHP = maxHealth;

		// load sprites
		try {

			try {
				
				BufferedImage spritesheet = ImageIO.read(
					getClass().getResourceAsStream(
						"/Sprites/Enemies/frog.png"
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
	
	private boolean startfirea = false;

	private void getNextPosition() {

		// TODO: AI
		damage = 0;
		cwidth = 15;
		scratching = false;
		left = false;
		right = false;
		
		boolean alf = false;
		
		if(Math.abs(Player.staticX - x) < tileSize*3 && !isWall) {
			if (Player.staticX < x) {
				right = true;
				left = false;
			}
			if (Player.staticX > x) {
				left = true;
				right = false;
			}
		}else if(!(Math.abs(Player.staticX - x) < tileSize*4) || isWall) {
			if (Math.abs(Player.staticX - x) > tileSize*6 && !isWall) {// && Math.abs(Player.staticX - x) > tileSize*5) {
				if(!isWall && !scratching) {// && Math.abs(Player.staticX - x) > tileSize*5
					if (Player.staticX < x) {
						left = true;
						right = false;
					}
					if (Player.staticX > x) {
						right = true;
						left = false;
					}
				}
			}else {
//				
				if((fire > fireCost && dx*5 == 0 && (int)(Player.staticY/tileSize) == (int)(y/tileSize)) || isWall) {
					if (Player.staticX < x && right) {
						facingRight = true;
					}	
					if (Player.staticX > x && left) {
						facingRight = false;
					}
					left = false;
					right = false;
					scratching = true;
					dx = 0;
				}else {
					if(!(Math.abs(Player.staticX - x) > tileSize*6 -  player.getCWidth())) {
						left = false;
						right = false;
						dx = 0;
					}
				}
			}
		}
		
		if(alf) {
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
		if(isWall) {
//			left = false;
//			right = false;
		}
		if(left && !right) {
			dx -= moveSpeed;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		}
		if(right && !left) {
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

		fire += 0.1;
		if(scratching) {
			if(fire > fireCost) {
				fire -= fireCost;
				startfirea = true;
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
				if(firing && fire > fireCost) {
					if(currentAction != SCRATCHING) {
						currentAction = SCRATCHING;
						animation.setFrames(sprites.get(SCRATCHING));
						animation.setDelay(50);
						width = 30;
					}
				}
				if(startfirea) {
					if(currentAction != FIREBALL) {
						currentAction = FIREBALL;
						animation.setFrames(sprites.get(FIREBALL));
						animation.setDelay(250);
						width = 30;
					}
					if(animation.getFrame() == numFrames[FIREBALL]-1) {
						Tongue fb = new Tongue(tileMap, facingRight);
						fb.setPosition(x, y);
						arrows.add(fb);
						startfirea = false;
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
				else if((Math.abs(Math.round(dx)) > 0)) {//left || right
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
				
				for(int i = 0; i < arrows.size(); i++) {
					arrows.get(i).update();
					if(arrows.get(i).shouldRemove()) {
						arrows.remove(i);
						i--;
					}
				}
				animation.update();
				
				// set direction
				if(currentAction == FIREBALL) {
					if (Player.staticX < x)
						facingRight = false;
					if (Player.staticX > x)
						facingRight = true;
				}else {
					if(right) facingRight = true;
					if(left) facingRight = false;

				}
				

		// update animation
		animation.update();

		checkAttack(player);
	}

	public void draw(Graphics2D g) {

		//if(notOnScreen()) return;
		try {
			for(int i = 0; i < arrows.size(); i++) {
				arrows.get(i).draw(g);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setMapPosition();

		hp = health;
		super.draw(g);

	}
	
	
	public void checkAttack(Player player) {
		
		// loop through enemies
			
		// fireballs
		for(int j = 0; j < arrows.size(); j++) {
			if(arrows.get(j).intersects(player)) {
				player.hit(fireBallDamage);
				arrows.get(j).setHit();
				break;
			}
		}

//		// check enemy collision
//		if(intersects(player)) {
//			hit(player.getDamage());
//		}
		
	}
}

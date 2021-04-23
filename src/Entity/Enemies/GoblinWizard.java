package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.Enemy;
import Entity.Player;
import Entity.StaticMagic;
import GameState.Level1State;
import TileMap.Tile;
import TileMap.TileMap;

public class GoblinWizard extends Enemy{

	private ArrayList<BufferedImage[]> sprites;
	

	private boolean scratching;
	private boolean firing;
	private int fireBallDamage = 100;
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


	private ArrayList<StaticMagic> magics;
	
	public GoblinWizard(TileMap tm, Player player) {
		super(tm);
		System.out.println("W");
		this.player = player;

		moveSpeed = 0.4;
		maxSpeed = 1.2;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;

		jumpStart = -4.8;
		stopJumpSpeed = 0.3;

		fire = 10;
		fireCost = 6;
		magics = new ArrayList<StaticMagic>();
		
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
						"/Sprites/Enemies/goblin_wizard.png"
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
		
		if(!isWall && !scratching && Math.abs(Player.staticX - x) < tileSize*3) {
			if (Player.staticX < x) {
				right = true;
				left = false;
			}
			if (Player.staticX > x) {
				left = true;
				right = false;
			}
		}else {
			if (!isWall && !scratching && Math.abs(Player.staticX - x) < tileSize*15 && Math.abs(Player.staticX - x) > tileSize*5) {
				if (Player.staticX < x) {
					left = true;
					right = false;
				}
				if (Player.staticX > x) {
					right = true;
					left = false;
				}
			}else {
				if((fire > fireCost && (int)(Player.staticY/tileSize) == (int)(y/tileSize)) || isWall) {
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
				}
			}
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
			left = false;
			right = false;
		}
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

		if(setNewx > -1) {
			x = setNewx;
			setNewx = -1;
		}
		if(setNewy > -1) {
			x = setNewy;
			setNewy = -1;
		}
		
	}
	
	int darknes = 0;

	public void update() {

		Level1State.screenDarknes = darknes * 10;
		
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
						StaticMagic fb = new StaticMagic(tileMap, facingRight, false);
						fb.setPosition(x, y);
						magics.add(fb);
						startfirea = false;
						darknes++;
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
				else if((left || right) && !isWall) {
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
				
				for(int i = 0; i < magics.size(); i++) {
					magics.get(i).update();
					if(magics.get(i).shouldRemove()) {
						magics.remove(i);
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
			for(int i = 0; i < magics.size(); i++) {
				magics.get(i).draw(g);
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
		for(int j = 0; j < magics.size(); j++) {
			if(magics.get(j).intersects(player)) {
				player.hit(fireBallDamage);
				magics.get(j).setHit();
				break;
			}
		}

//		// check enemy collision
//		if(intersects(player)) {
//			hit(player.getDamage());
//		}
		
	}
	
	private int getRandom1() {
		return (int) (Math.random()*11 - 5); // ###0###
	}

	int setNewx = -1;
	int setNewy = -1;
	
	@Override
	public void hit(int damage) {

			for (int i = 0; i < 10; i++) {
				int nx = (int) (((xtemp/tileSize) + getRandom1()));
				int ny = (int) (((ytemp/tileSize) + getRandom1()));
				try {
					if(nx != x/tileSize && ny != y/tileSize
							&& tileMap.getType(ny, nx) == Tile.NORMAL && 
							tileMap.getType(ny+1, nx) == Tile.BLOCKED) {
						StaticMagic arrow = new StaticMagic(tileMap, true, true); // TODO
						arrow.setPosition(nx* tileSize, ny * tileSize);
						magics.add(arrow);
						x = nx * tileSize;
						y = ny * tileSize;
						darknes++;
					} else {
//						x = nx * tileSize;
//						y = ny * tileSize;
//						break;\
					}
				} catch (Exception e) {
//					e.printStackTrace();
				}
			}
			super.hit(damage);
	}
}

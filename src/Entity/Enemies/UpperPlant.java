package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.Enemy;
import Entity.Player;
import Entity.UpperPlantTrap;
import TileMap.TileMap;

public class UpperPlant extends Enemy {
	
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int FIREBALL = 5;
	private static final int SCRATCHING = 6;
	private static int DAMAGE = 0;

	private ArrayList<BufferedImage[]> sprites;

	private final int[] numFrames = {
			2, 6, 1, 2, 4, 2, 5
	};
	private boolean scratching;
	private boolean firing;
	private UpperPlantTrap plantTrap;
	Player player;
	
	public UpperPlant(TileMap tm, Player player) {
		
		super(tm);
		
		this.player = player;
		DAMAGE = player.getMaxHealth()/2;

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

		health = maxHealth = 1;
		damage = 250;
		
		maxHP = maxHealth;

		// load sprites
		try {

			try {
				
				BufferedImage spritesheet = ImageIO.read(
					getClass().getResourceAsStream(
						"/Sprites/Enemies/up_plant.png"
					)
				);
//				System.out.println(Math.round(Math.random()));
				
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
						} else {
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
		
		plantTrap = new UpperPlantTrap(tileMap);

	}


	public void update() {
		right = false;
		left = false;
		
		if(plantTrap.isActivate()) {
			dy = tileSize/3;
		}
		
		if (Math.ceil(Player.staticX/tileSize) == Math.ceil(x/tileSize) &&
				Math.ceil(Player.staticY/tileSize) == Math.ceil(y/tileSize)) {
			player.setPosition(x, y);
			scratching = true;
			dx = 0;
			damage = DAMAGE;
			cwidth = 30;
			hp = maxHP = health = maxHealth = Integer.MAX_VALUE;
		}else {
			hp = maxHP = health = maxHealth = 1;
			damage = 0;
			cwidth = 15;
		}
		checkTileMapCollision();
		setPosition2(x, ytemp);
		
				if(currentAction == SCRATCHING) {
					if(animation.hasPlayedOnce()) scratching = false;
				}
				if(currentAction == FIREBALL) {
					if(animation.hasPlayedOnce()) firing = false;
				}
				
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
				
				if(currentAction != SCRATCHING && currentAction != FIREBALL) {
					if(right) facingRight = true;
					if(left) facingRight = false;
				}
				

		animation.update();
		plantTrap.update();

	}

	public void draw(Graphics2D g) {
		setMapPosition();
		plantTrap.draw(g);
		for (int y = startY; y < this.y - tileSize; y+=tileSize) {
			g.drawImage(sprites.get(JUMPING)[0], 
					(int)(x + xmap - tileSize / 2),
					(int)(y + ymap + 8 - height / 2) - 4, null);
		}
		super.draw(g);
	}
	
	@Override
	public void setPosition(double x, double y) {
		this.x = (x/tileSize)*tileSize + tileSize/2;
		this.y = (y/tileSize)*tileSize + tileSize/2;
		startY = (int) this.y;
		plantTrap.setPosition(this.x,this.y);
	}

	private int startY = 0;
	
	
	public void setPosition2(double x, double y) {
		this.x = x;
		this.y = y;
	}
}

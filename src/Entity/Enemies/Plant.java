package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.Enemy;
import Entity.Player;
import TileMap.TileMap;

public class Plant extends Enemy {
	
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int FIREBALL = 5;
	private static final int SCRATCHING = 6;
	private static final int DAMAGE = 250;

	private ArrayList<BufferedImage[]> sprites;

	private final int[] numFrames = {
			2, 6, 1, 2, 4, 2, 5
	};
	private boolean scratching;
	private boolean firing;
	
	public Plant(TileMap tm) {

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
						"/Sprites/Enemies/plant" + 0 + ".png"
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

	}


	public void update() {
		right = false;
		left = false;
		
		if (Math.round(Player.staticX*2/tileSize) == Math.round(x*2/tileSize) &&
				Math.round(Player.staticY/tileSize) == Math.round(y/tileSize)) {
			scratching = true;
			dx = 0;
			damage = DAMAGE;
			cwidth = 30;
		}else {
			damage = 0;
			cwidth = 15;
		}
		
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

	}

	public void draw(Graphics2D g) {
		setMapPosition();
		hp = health;
		super.draw(g);
	}
	
	@Override
	public void setPosition(double x, double y) {
		this.x = (x/tileSize)*tileSize + tileSize/2;
		this.y = (y/tileSize)*tileSize + tileSize/2;;
	}
}

package Entity.Enemies;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.BossMagic;
import Entity.Enemy;
import Entity.Laser;
import Entity.Life;
import Entity.Player;
import Entity.StaticMagic;
import Entity.StoneGoblin;
import GameState.Level1State;
import Main.GamePanel;
import TileMap.TileMap;

public class GoblinBoss extends Enemy{

	private ArrayList<BufferedImage[]> sprites;
	
	private Player player;

	private boolean scratching;
	private boolean firing;
	
	private int hp2;
	protected static int DAMAGE = 100;

	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	private static final int FIREBALL = 5;
	private static final int SCRATCHING = 6;
	

	private BufferedImage head;

	private final int[] numFrames = {
			2, 6, 1, 2, 4, 2, 5
	};

	private ArrayList<StoneGoblin> stoneGoblins = new ArrayList<StoneGoblin>();
	private ArrayList<BossMagic> bossMagics = new ArrayList<BossMagic>(); 
	private ArrayList<Life> lifes = new ArrayList<Life>(); 
	
	public GoblinBoss(TileMap tm, Player player) {
		super(tm);
		this.player = player;
		int[] stonePosX = {18,44,15,47};
		int[] stonePosY = {18,19,29,29};
		boolean[] isRight = {true,false,true,false};
		
		for (int i = 0; i < isRight.length; i++) {
			StoneGoblin stoneGoblin = new StoneGoblin(tm, isRight[i]);
			stoneGoblin.setPosition(stonePosX[i]*30 + 15, stonePosY[i]*30 + 19);
			stoneGoblin.setVector(0, 0);
			stoneGoblins.add(stoneGoblin);
		}
		moveSpeed = 0.05;
		maxSpeed = 5.3;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		hp2 = 10; 

		jumpStart = -4.8;
		stopJumpSpeed = 0.3;
		
		isImmortal = true;

		width = 30;
		height = 30;
		cwidth = 15;
		cheight = 20;

		health = maxHealth = 5;
		damage = 250;
		
		maxHP = maxHealth;

		// load sprites
		try {

			try {
				
				BufferedImage spritesheet = ImageIO.read(
					getClass().getResourceAsStream(
						"/Sprites/Enemies/GoblinBoss.png"
					)
				);
				
				head = spritesheet.getSubimage(150, 150, 11, 12);
				
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
	
	double blue = 0;
	
	private void ai_1() {
		moveSpeed = 0;
		firing = true;
		damage = 0;
	}
	
	int aggression = 100;
	
	ArrayList<StaticMagic> staticMagics = new ArrayList<StaticMagic>();
	
	
	
	private void ai_0() {
		if(aggression > 0) {
			aggression--;
			maxSpeed = 5.3;
			damage = (int) Math.sqrt(Math.pow(2, 11-hp2)/10) * 10 + 50;
			moveSpeed = 0.05 * ((12-hp2)/2);
			blue = moveSpeed;
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
				cwidth = 30;
			}else {
				damage = 0;
				cwidth = 15;
			}
		}else {
			damage = 10;
			moveSpeed = 0.2;
			maxSpeed = 1.2;
			blue = moveSpeed;
			if (!isWall && !scratching && Math.abs(Player.staticX - x) < tileSize*15 && Math.abs(Player.staticX - x) > tileSize*5) {
				if(left && Player.staticX - x < 0)
					firing = true;
				if(right && Player.staticX - x > 0)
					firing = true;
				if (Player.staticX < x) {
					left = true;
					right = false;
				}
				if (Player.staticX > x) {
					right = true;
					left = false;
				}
			}else {
				if (Player.staticX < x) {
					right = true;
					left = false;
				}
				if (Player.staticX > x) {
					left = true;
					right = false;
				}
			}
		}
		
		if(Math.round(dx/2) == 0) {
			jumping = true;
		}
		
//		maxSpeed = moveSpeed*500/(12-hp);
			
			if(Math.abs(Player.staticX - x) < tileSize * 10)
				jumping = Player.staticY < y - 5 && (right || left) && dx == 0;
			else {
				jumping = false;
				right = false;
				left = false;
				dx = 0;
			}
	}

	private void getNextPosition() {

		// TODO: AI
		switch (stoneGoblins.size()) {
		
		
		case 4:
			ai_0();
			break;case 3:
				ai_1();
				break;case 2:
					ai_1();
					break;case 1:
						ai_1();
						break;case 0:
							ai_1();
							break;default:
								break;
								
								
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

	Laser laser;
	double laserTime = 0;
	
	
	int attackTime = 0;
	
	public void update() {
		checkAttack(player);
		attackTime++;
		Level1State.screenDarknes = 255;
		//*
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

		if(stoneGoblins.size() < 4){
			if(!facingRight) {
				if(player.staticX-15 > x) {
					player.addVector(-10, 0);
				}
			}else {
				if(player.staticX+15 < x) {
					player.addVector(10, 0);
				}
			}
		}
		
//		fire += 0.1;
//		if(scratching) {
//			if(fire > fireCost) {
//				fire -= fireCost;
//			}
//		}
		
		// check attack has stopped
				if(currentAction == SCRATCHING) {
					if(animation.hasPlayedOnce()) scratching = false;
				}
				if(currentAction == FIREBALL) {
					if(animation.hasPlayedOnce()) {
						firing = false;
						if(stoneGoblins.size() < 4) {
							if(attackTime > 0 && attackTime < 250) {
								bossMagics.add(new BossMagic(tileMap, this, player, BossMagic.HOMING_IN));
								laserTime = 0;
							}
							if(attackTime > 750)
								attackTime = 0;
							
								}else {
							StaticMagic e = new StaticMagic(tileMap, facingRight, false);
							e.setPosition(x, y);
							staticMagics.add(e);
						}
					}
				}
				
				// set animation
				if(scratching) {
					if(currentAction != SCRATCHING) {
						currentAction = SCRATCHING;
						animation.setFrames(sprites.get(SCRATCHING));
						animation.setDelay(50);
						width = 60;
					}
				} else if(firing) {
					if(currentAction != FIREBALL) {
						currentAction = FIREBALL;
						animation.setFrames(sprites.get(FIREBALL));
						animation.setDelay(100);
						width = 30;
					}
				} else if(dy > 0) {
					if(currentAction != FALLING) {
						currentAction = FALLING;
						animation.setFrames(sprites.get(FALLING));
						animation.setDelay(100);
						width = 30;
					}
				} else if(dy < 0) {
					if(currentAction != JUMPING) {
						currentAction = JUMPING;
						animation.setFrames(sprites.get(JUMPING));
						animation.setDelay(-1);
						width = 30;
					}
				} else if(left || right) {
					if(currentAction != WALKING) {
						currentAction = WALKING;
						animation.setFrames(sprites.get(WALKING));
						animation.setDelay(80);
						width = 30;
					}
				} else {
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
				
				for(int i = 0; i < staticMagics.size(); i++) {
					staticMagics.get(i).update();
					if(staticMagics.get(i).shouldRemove()) {
						staticMagics.remove(i);
						i--;
					}
				}
				laserTime+=0.05;
				try {
					laser.dir = Math.toRadians(Math.cos(laserTime)*10+(90*(facingRight ? 1:-1)));
					laser.setVisible(attackTime > 250 && attackTime < 750);
					laser.update();
				} catch (NullPointerException e) {//TODO
				}
				for (int i = 0; i < bossMagics.size(); i++) {
					try {
						bossMagics.get(i).update();
						if(bossMagics.get(i).shouldRemove())
							bossMagics.remove(i);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				for (int i = 0; i < lifes.size(); i++) {
					try {
						lifes.get(i).update();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
		// update animation
		animation.update();
		//*/
	}
	public void checkAttack(Player player) {
		

		for(int j = 0; j < staticMagics.size(); j++) {
			if(player.intersects(staticMagics.get(j))) {
				player.hit((12-hp)*15 + 10);
				staticMagics.get(j).setHit();
				break;
			}
		}

		for(int j = 0; j < bossMagics.size(); j++) {
			if(bossMagics.get(j).intersects(player)) {
				player.hit(10);
				player.addVector(bossMagics.get(j).getDx()/5, bossMagics.get(j).getDy()/5);
				break;
			}
		}
		for (int k = 0; k < lifes.size(); k++) {
			if(lifes.get(k).intersects(player)) {
				player.heal(player.getMaxHealth()/4);
				lifes.remove(k);
//				lifes.get(k).setHit();TODO
				break;
			}
		}

//		// check enemy collision
//		if(intersects(player)) {
//			hit(player.getDamage());
//		}
		
	}

	ArrayList<Integer> freeXPos = new ArrayList<Integer>();
	ArrayList<Integer> freeYPos = new ArrayList<Integer>();
	
	@Override
	public void hit(int damage) {
		super.hit(damage);
		if(flinching) return;
		hp2--;
		flinching = true;
		flinchTimer = System.nanoTime();
		aggression = 1000;
	}

	double white;
	double red;
	int alfa = 0;
	
	public void draw(Graphics2D g) { // TODO
		try {

			setMapPosition();
			for (int i = 0; i < stoneGoblins.size(); i++) {
				try {
					stoneGoblins.get(i).draw(g);
				} catch (Exception e) {
					e.printStackTrace();
					//				System.exit(0);
				}
			}
			for (int i = 0; i < bossMagics.size(); i++) {
				try {
					bossMagics.get(i).draw(g);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for (int i = 0; i < lifes.size(); i++) {
				try {
					lifes.get(i).draw(g);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(hp2 < 0) {
				if(stoneGoblins.size() == 0) {
					dead = true;
				}else {
					hp2 = 10;
					Life life = new Life(tileMap);
					life.setPosition(x, y);
					lifes.add(life);
					int randomID = (int) (Math.random()*stoneGoblins.size());
					StoneGoblin sg = stoneGoblins.get(randomID);
					facingRight = stoneGoblins.get(randomID).isRight();
					right = false;
					left = false;
					x = sg.getx();
					y = sg.gety();
					laser = new Laser(tileMap, (int)x, (int)y, 0, player);
					dx = 0;
					stoneGoblins.remove(randomID);
				}
			}

			hp = health;
			white = (white-hp2)/1.11 + hp2;
			red = hp2;
			if(red > white)
				red = white;


			super.draw(g);

			//TODO
			if(stoneGoblins.size() < 4){
				g.setColor(new Color(35,255,180,50));
				if(facingRight) {
					g.fillRect(-1, -1, 
							(int)(x + xmap)-15, 
							GamePanel.HEIGHT+1);
					g.setColor(new Color(35,255,180));
					g.drawRect(-1, -1, 
							(int)(x + xmap)-15, 
							GamePanel.HEIGHT+1);

				} else {
					g.fillRect((int)(x + xmap) + 15, -1, 
							GamePanel.WIDTH, 
							GamePanel.HEIGHT+1);
					g.setColor(new Color(35,255,180));
					g.drawRect((int)(x + xmap)+ 15, -1, 
							GamePanel.WIDTH, 
							GamePanel.HEIGHT+1);
				}
			}


			try {
				for(int i = 0; i < staticMagics.size(); i++) {
					staticMagics.get(i).draw(g);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				laser.draw(g);
			} catch (NullPointerException e) {
			}

			if(player.gety()/30 > 10) {
				alfa+=10;
				if(alfa > 255) alfa = 255;
				int hpBarH = GamePanel.WIDTH - 120;
				int hpBarX = 80;
				g.setColor(Color.GRAY);
				g.fillRect(
						hpBarX,
						15,
						hpBarH,
						15
						);
				g.setColor(new Color(255,245,245,alfa));
				g.fillRect(
						hpBarX,
						15,
						(int)(hpBarH*white/10),
						15
						);
				g.setColor(new Color(255,50,50,alfa));
				g.fillRect(
						hpBarX,
						15,
						(int) (hpBarH*red/10) + 1,
						15
						);
				g.setColor(new Color(60,60,60,alfa));
				g.drawRect(
						hpBarX,
						15,
						hpBarH,
						15
						);
				g.drawImage(head, hpBarX-11, 10, 22, 24, null);
			}else {
				red = 0;
				white = 0;
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}

package Entity.Enemies;

import Entity.Enemy;
import Entity.Player;
import TileMap.TileMap;

public class TYPE_Tank extends Enemy {

	public static final int IDLE = 0;
	public static final int WALKING = 1;
	public static final int JUMPING = 2;
	public static final int FALLING = 3;
	public static final int FIREBALL = 5;
	public static final int SCRATCHING = 6;
	
	protected int DAMAGE = 100;
	
	public boolean scratching;
	public boolean firing;
	public boolean hasSwampBoost;

	public TYPE_Tank(TileMap tm) {
		super(tm);
	}

	@Override
	public void update() {// update position
		getNextPosition();
		try {
			checkTileMapCollision();
		} catch (ArrayIndexOutOfBoundsException e) {
			health = 0;
		}
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
//		if(currentAction == SCRATCHING) {
//			if(animation.hasPlayedOnce()) scratching = false;
//		}
//		if(currentAction == FIREBALL) {
//			if(animation.hasPlayedOnce()) firing = false;
//		}
//		
//		// set direction
//		if(currentAction != SCRATCHING && currentAction != FIREBALL) {
//			if(right) facingRight = true;
//			if(left) facingRight = false;
//		}


		// update animation
		animation.update();
		super.update();
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
		double kkk = tochSwamp && hasSwampBoost ? 1.75:1;
		
		if(left) {
			dx -= moveSpeed*kkk;
			if(dx < -maxSpeed*kkk) {
				dx = -maxSpeed*kkk;
			}
		}
		else if(right) {
			dx += moveSpeed*kkk;
			if(dx > maxSpeed) {
				dx = maxSpeed*kkk;
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
}

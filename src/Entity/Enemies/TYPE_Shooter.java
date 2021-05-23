package Entity.Enemies;

import Entity.Enemy;
import Entity.Player;
import TileMap.TileMap;

public class TYPE_Shooter extends Enemy {

	public static final int IDLE = 0;
	public static final int WALKING = 1;
	public static final int JUMPING = 2;
	public static final int FALLING = 3;
	public static final int FIREBALL = 5;
	public static final int SCRATCHING = 6;
	
	protected int DAMAGE = 100;
	
	public double fire;
	public int fireCost;
	
	public boolean scratching;
	public boolean firing;
	public boolean hasSwampBoost;

	public TYPE_Shooter(TileMap tm) {
		super(tm);
	}

	@Override
	public void update() {
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);

		if(flinching) {
			long elapsed =
					(System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 400) {
				flinching = false;
			}
		}

		animation.update();
		super.update();
	}

	private void getNextPosition() {

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
	}
}

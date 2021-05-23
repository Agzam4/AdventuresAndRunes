package Entity;

import Main.GamePanel;
import TileMap.TileMap;
import TileMap.Tile;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.color.ColorSpace;

public abstract class MapObject {
	
	// tile stuff
	protected TileMap tileMap;
	protected int tileSize = 30;
	protected double xmap;
	protected double ymap;
	protected int hp;
	protected int maxHP;
	
	// position and vector
	protected double x;
	protected double y;
	protected double dx;
	protected double dy;
	
	// dimensions
	protected int width;
	protected int height;
	
	// collision box
	protected int cwidth;
	protected int cheight;
	
	// collision
	protected int currRow;
	protected int currCol;
	protected double xdest;
	protected double ydest;
	protected double xtemp;
	protected double ytemp;
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;
	
	// animation
	protected Animation animation;
	protected int currentAction;
	protected int previousAction;
	protected boolean facingRight;
	
	// movement
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	protected boolean jumping;
	protected boolean falling;
	
	// movement attributes
	protected double moveSpeed;
	protected double maxSpeed;
	protected double stopSpeed;
	protected double fallSpeed;
	protected double maxFallSpeed;
	protected double jumpStart;
	protected double stopJumpSpeed;
	
	// constructor
	public MapObject(TileMap tm) {
		if(tm == null)
			return;
		tileMap = tm;
		tileSize = tm.getTileSize(); 
	}
	
	public boolean intersects(MapObject o) {
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		return r1.intersects(r2);
	}
	
	public Rectangle getRectangle() {
		return new Rectangle(
				(int)x - cwidth,
				(int)y - cheight,
				cwidth,
				cheight
		);
	}
	
	public void calculateCorners(double x, double y) {
		
		int leftTile = (int)(x - cwidth / 2) / tileSize;
		int rightTile = (int)(x + cwidth / 2 - 1) / tileSize;
		int topTile = (int)(y - cheight / 2) / tileSize;
		int bottomTile = (int)(y + cheight / 2 - 1) / tileSize;
		
		int tl = tileMap.getType(topTile, leftTile);
		int tr = tileMap.getType(topTile, rightTile);
		int bl = tileMap.getType(bottomTile, leftTile);
		int br = tileMap.getType(bottomTile, rightTile);
		
		topLeft = tl == Tile.BLOCKED;
		topRight = tr == Tile.BLOCKED;
		bottomLeft = bl == Tile.BLOCKED;
		bottomRight = br == Tile.BLOCKED;
		tochFinish = tl == Tile.FINISH || tr == Tile.FINISH ||
				bl ==  Tile.FINISH || br == Tile.FINISH;
		
		tochSwamp = tl == Tile.SWAMP || tr == Tile.SWAMP ||
				bl ==  Tile.SWAMP || br == Tile.SWAMP;
	}

	public boolean tochFinish = false;

	public boolean tochSwamp = false;
	public boolean isWall = false;
	
	public void checkTileMapCollision() {
		
		currCol = (int)x / tileSize;
		currRow = (int)y / tileSize;
		
		xdest = x + dx;
		ydest = y + dy;
		
		xtemp = x;
		ytemp = y;
		
		isWall = false;
		
		calculateCorners(x, ydest);
		if(dy < 0) {
			if(topLeft || topRight) {
				dy = 0;
				ytemp = currRow * tileSize + cheight / 2;
				isWall = true;
			}
			else {
				ytemp += dy;
			}
		}
		if(dy > 0) {
			if(bottomLeft || bottomRight) {
				dy = 0;
				falling = false;
				ytemp = (currRow + 1) * tileSize - cheight / 2;
				isWall = true;
			}
			else {
				ytemp += dy;
			}
		}
		
		calculateCorners(xdest, y);
		if(dx < 0) {
			if(topLeft || bottomLeft) {
				dx = 0;
				xtemp = currCol * tileSize + cwidth / 2;
				isWall = true;
			}
			else {
				xtemp += dx;
			}
		}
		if(dx > 0) {
			if(topRight || bottomRight) {
				dx = 0;
				xtemp = (currCol + 1) * tileSize - cwidth / 2;
				isWall = true;
			}
			else {
				xtemp += dx;
			}
		}
		
		if(!falling) {
			calculateCorners(x, ydest + 1);
			if(!bottomLeft && !bottomRight) {
				falling = true;
			}
		}
		
	}
	
	public int getx() { return (int)x; }
	public int gety() { return (int)y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getCWidth() { return cwidth; }
	public int getCHeight() { return cheight; }
	
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public void setMapPosition() {
		xmap = tileMap.getx();
		ymap = tileMap.gety();
	}
	
	public void setLeft(boolean b) { left = b; }
	public void setRight(boolean b) { right = b; }
	public void setUp(boolean b) { up = b; }
	public void setDown(boolean b) { down = b; }
	public void setJumping(boolean b) { jumping = b; }
	
	public boolean notOnScreen() {
		return x + xmap + width < 0 ||
			x + xmap - width > GamePanel.WIDTH ||
			y + ymap + height < 0 ||
			y + ymap - height > GamePanel.HEIGHT;
	}
	
	public void draw(java.awt.Graphics2D g) {
		if(facingRight) {
			g.drawImage(
				animation.getImage(),
				(int)(x + xmap - width / 2),
				(int)(y + ymap - height / 2) - 4,
				null
			);
		} else {
			g.drawImage(
				animation.getImage(),
				(int)(x + xmap - width / 2 + width),
				(int)(y + ymap - height / 2) - 4,
				-width,
				height,
				null
			);
		}
		Color mc = new Color(Color.HSBtoRGB( hp/((maxHP>0?maxHP:1)+0.0f)/3f, 1, 1));
		if(hp < maxHP) {
			g.setColor(new Color(76,76,76,150));//Color.GRAY);
			g.fillRect(
					(int)(x + xmap - (tileSize - 5) / 2 ),
					(int)(y + ymap - height / 2) - 8,
					tileSize - 5,
					5
			);
			g.setColor(new Color(25,25,25));//Color.GRAY);
			g.drawRect(
					(int)(x + xmap - (tileSize - 5) / 2 ),
					(int)(y + ymap - height / 2) - 8,
					tileSize - 5,
					5
			);
			g.setColor(new Color(mc.getRed(), mc.getGreen(), mc.getBlue(), 150));//Color.GRAY);
			g.fillRect(
//					(int)(x + xmap - (tileSize - 5) / 2 ),
//					(int)(y + ymap - height / 2) - 8,
//					tileSize - 5,
//					5
					(int)(x + xmap - (tileSize - 5) / 2 ) + 1,
					(int)(y + ymap - height / 2) - 7,
					hp*(tileSize - 5) / maxHP,
					3
			);
//			g.setColor(mc);//new Color(255,50,50));
//			g.fillRect(
//					(int)(x + xmap - (tileSize - 5) / 2 ),
//					(int)(y + ymap - height / 2) - 8,
//					hp*(tileSize - 5) / maxHP,
//					5
//			);
			g.setColor(mc);//Color.DARK_GRAY);
			g.drawRect(
//					(int)(x + xmap - (tileSize - 5) / 2 ),
//					(int)(y + ymap - height / 2) - 8,
//					tileSize - 5,
//					5
					(int)(x + xmap - (tileSize - 5) / 2 ) + 1,
					(int)(y + ymap - height / 2) - 7,
					hp*(tileSize - 5) / maxHP,
					3
			);	
		}
	}
}

















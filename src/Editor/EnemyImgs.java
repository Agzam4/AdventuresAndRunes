package Editor;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class EnemyImgs {

	BufferedImage img;
	String name;
	int x;
	int y;
	
	public String getName() {
		return name;
	}
	String getPos() {
		return x + ":" + y;
	}
	
	public EnemyImgs(BufferedImage img, int x, int y, String name) {
		this.name = name;
		this.img = img;
		this.x = x;
		this.y = y;
	}
	
	boolean tryDelite(int x, int y) {
		return 	(x < this.x+30 && x > this.x) && (y < this.y+30 && y > this.y);
	}
	
	void draw(Graphics2D g, int xx, int yy) {
		g.drawImage(img, x+xx, y+yy, null);
		g.drawRect(x+xx, y+yy, 30, 30);
	}
}

package TileMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Main.GameJPanel;

public class Background {

	private BufferedImage image;
	private double x;
	private double y;
	private double dx;
	private double dy;
	private double mouseScale;
	
	public Background(String s, double ms) throws IOException {
		image = ImageIO.read(getClass().getResourceAsStream(s));
		mouseScale = ms;
	}
	
	public void setPosition(int x, int y) {
		this.x = (x * mouseScale) % GameJPanel.WIDTH;
		this.y = (y * mouseScale) % GameJPanel.HEIGHT;
	}
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public void update() {
		x += dx;
		y += dy;
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(image, (int)x, (int)y, null);
		if(x < 0) 
			g.drawImage(image, (int)x + GameJPanel.WIDTH, (int)y, null);
		if(x > 0)
			g.drawImage(image, (int)x - GameJPanel.WIDTH, (int)y, null);
	}
}

package TileMap;

import Main.GamePanel;

import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;

public class Background {
	
	private BufferedImage image;
	
	private double x;
	private double y;
	private double dx;
	private double dy;
	
	private double moveScale;
	
	public Background(String s, double ms) {
		
		try {
			image = ImageIO.read(
				getClass().getResourceAsStream(s)
			);
			moveScale = ms;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void setPosition(double x, double y) {
		this.x = (x * moveScale) % GamePanel.WIDTH;
		this.y = (y * moveScale) % GamePanel.HEIGHT;
	}
	
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public void update() {
		x += dx;
		y += dy;
	}
	
	public void draw(Graphics2D g, boolean fillY) {
		g.setColor(new Color(28,51,29));
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		for (int xx = (int) x; xx < GamePanel.WIDTH; xx+= image.getWidth()) {
			if(fillY)
				g.drawImage(image, (int)xx, (int)y, image.getWidth(), GamePanel.HEIGHT, null);
			else
				g.drawImage(image, (int)xx, -image.getHeight() + GamePanel.HEIGHT + (int)y, null);
				
		}
//		if(x < 0) {
//			g.drawImage(
//				image,
//				(int)x + GamePanel.WIDTH * GamePanel.SCALE,
//				(int)y,
//				null
//			);
//		}
//		if(x > 0) {
//			g.drawImage(
//				image,
//				(int)x - GamePanel.WIDTH * GamePanel.SCALE,
//				(int)y,
//				null
//			);
//		}
	}
	
}








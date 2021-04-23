package Entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class HUD {
	
	private Player player;
	private Font font;
	
	public HUD(Player p) {
		player = p;
		try {
			font = new Font("Arial", Font.PLAIN, 14);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g) {
		g.setFont(font);
		drawBar(g, 15);
		g.setColor(new Color(255,50,50));
		g.fillRect(15, 15, player.getHealth()*50/player.getMaxHealth(), 6);
		drawBarBounds(g, 15);
		
		drawBar(g, 25);
		g.setColor(new Color(50,50,255));
		g.fillRect(15, 25, player.getFire()*50/player.getMaxFire(), 6);
		drawBarBounds(g, 25);
	}

	private void drawBar(Graphics2D g, int y) {
		g.setColor(Color.GRAY);
		g.fillRect(15, y, 50, 6);
	}

	private void drawBarBounds(Graphics2D g, int y) {
		g.setColor(Color.DARK_GRAY);
		g.drawRect(15, y, 50, 6);
	}
	
}














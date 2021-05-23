package Entity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import GameState.GameStateManager;
import Main.GamePanel;

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
	
	private final BasicStroke scBasicStroke = new BasicStroke(GamePanel.SCALE);
	private float newHP = 0;
	private float colorr = 1f;
	private float light = 1f;
	private float myBlue = 0.5f;
	private float newM = 0;
	
	public void draw(Graphics2D gg) {
		
		int value = player.getHealth()*100/player.getMaxHealth();
		int unvalue = 100-value;
		double outvalue = value-newHP;
		newHP = (float) ((newHP-value)/1.1 + value);
		
		Graphics2D g = (Graphics2D) GamePanel.fullImage.getGraphics();
		g.setFont(font);
		
		float timeCol = player.getHealth()
				/((player.getMaxHealth()>0?player.getMaxHealth():1)+0.0f)/3f;
		colorr = (float) ((colorr - timeCol)/1.1f + timeCol);
		
		boolean b = Math.round(newHP) == value;
		if(b) {
			light = (light-1f)/2f+1f;
		} else {
			light = 0;
		}

		Color lightColorFull = new Color(Color.HSBtoRGB(colorr, light, 1));
		Color darkColorFull = new Color(Color.HSBtoRGB(colorr, light,
				b ? .5f:1f));
		
		gg.setColor(Color.DARK_GRAY);
		gg.drawRect(5, 5, 100, 9);

		gg.setColor(lightColorFull);
		gg.drawRect(6, 6, value-2, 7);

		gg.setColor(darkColorFull);
		gg.drawRect(7, 7, value-4, 5);
		
		gg.setColor(new Color(
				lightColorFull.getRed(),lightColorFull.getGreen(),lightColorFull.getBlue(), b ? 150 : 255));
		gg.fillRect(8, 8, value-5, 4);
		
		gg.setColor(new Color(50,50,50,150));
		gg.fillRect(5+value, 5, unvalue, 9);

		int s = GamePanel.SCALE;
		g.setStroke(scBasicStroke);
		g.setColor(Color.WHITE);

		if(Math.round(Math.abs(outvalue)) != 0) {
			g.setColor(Color.WHITE);
			g.fillRect((5+value)*s, 6*s, (int) (-outvalue*s), 8*s);
			GameStateManager.fullIImgIsClear = false;
		}
		
		// #2

		int value2 = player.getFire()*50/player.getMaxFire();
		double outvalue2 = value2-newM;
		newM = (float) ((newM-value2)/1.1 + value2);


		if(outvalue2 < 0) {
			myBlue = myBlue/1.25f;
		}else {
			myBlue = (myBlue-.2194f)/1.25f+.2194f; // 2194
		}
		
		Color blue = new Color(Color.HSBtoRGB(.5f + myBlue, 1, 1));
		
		gg.setColor(new Color(84,0,255,50));
		gg.fillRect(5, 20, 50, 5);
		
		g.setColor(blue);//84,0,255));
		g.fillRect(5*s, 20*s, (int) (newM*s), 5*s);
		g.setColor(new Color(25,25,25));
		g.drawRect(5*s, 20*s, 50*s, 5*s);
		
//		try {
//			Paint paint = g.getPaint();
//			LinearGradientPaint gradientPaint = new  LinearGradientPaint(
//					new Point(5*s, 20*s),
//					new Point((int) (((grad%newM)*2)*s) + s, 20*s),
//					new float[] {0.75f,1f},
//					new Color[] {new Color(84,0,255), new Color(0,255,255)});
//			g.setPaint(gradientPaint);
//			g.fillRect(5*s, 20*s, (int) ((grad%newM)*s), 5*s);
//			g.setPaint(paint);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		// TODO: gradient
		
		
//		drawBar(g, 15);
//		g.setColor(new Color(255,50,50));
//		g.fillRect(15, 15, player.getHealth()*50/player.getMaxHealth(), 6);
//		drawBarBounds(g, 15);
//		
//		drawBar(g, 25);
//		g.setColor(new Color(50,50,255));
//		g.fillRect(15, 25, player.getFire()*50/player.getMaxFire(), 6);
//		drawBarBounds(g, 25);
		
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














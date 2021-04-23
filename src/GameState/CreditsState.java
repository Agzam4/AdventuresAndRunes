package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import Main.GamePanel;
import TileMap.Background;

public class CreditsState extends GameState {

	private Background bg;
	
	private int dark = 0;
	private double my = 0;
	private double vmy = 0;

	private static int TITLE = 25;
	private static int NORMAL = 12;
	
	private static int[] fontSizes = {TITLE,NORMAL,NORMAL,NORMAL,TITLE,NORMAL,TITLE,NORMAL};
	private static String lines[][] = {
			
			// ENG
			{
				"Game",
				"Idea - Agzam4",
				"Main developer - Agzam4",
				"Basic Platformer Engine by https://github.com/foreignguymike",
				"Music",
				"All Music by Agzam4, made in BeepBox.co",
				"Graphics",
				"All Graphics by Agzam4, made in paint.net",
				
			}
			
			
			
	};
	
	public CreditsState(GameStateManager gsm) {
		this.gsm = gsm;
		dark = 255;
		try {
			
			bg = new Background("/Backgrounds/menubg.png", 1);
			bg.setVector(-0.2, 0);
			
//			titleFont = new Font("Consolas",Font.PLAIN,28);
			
//			font = new Font("Arial", Font.PLAIN, 12);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		my = 75;
		vmy = 0;
	}
	
	@Override
	public void init() {
		
	}

	@Override
	public void update() {
		bg.update();
	}

	@Override
	public void draw(Graphics2D g) {
		bg.draw(g, true);
		int y = my > 10 ? 10 : (int) my;
		for (int i = 0; i < lines[0].length; i++) {
			g.setFont(new Font("Arial", Font.PLAIN, fontSizes[i]));
			y += fontSizes[i] + 7;
			drawStr(g, lines[0][i], y, Color.WHITE, Color.BLACK);
		}
		if(y < GamePanel.HEIGHT/2) {
			if(dark < 250)
				dark+=5;
			else
				gsm.setState(GameStateManager.MENUSTATE);
		}else {
			if(dark > 5)
				dark-=5;
		}
		g.setColor(new Color(0,0,0,dark));
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		if((int)vmy == 0)
			my-=0.5;
		else {
			if(my > 10)
				my = 10;
			my+=vmy;
			vmy = vmy*0.9;
		}
			
	}

	private void drawStr(Graphics2D g, String str, int y, Color c1, Color c2) {
		
		g.setColor(c2);
		int x = GamePanel.WIDTH/2 - g.getFontMetrics().stringWidth(str)/2 -5;
		g.drawString(str, x+1, y-1);
		g.drawString(str, x+1, y);
		g.drawString(str, x+1, y+1);

		g.drawString(str, x, y-1);
		g.drawString(str, x, y+1);

		g.drawString(str, x-1, y-1);
		g.drawString(str, x-1, y);
		g.drawString(str, x-1, y+1);

		g.setColor(c1);
		g.drawString(str, x, y);
	}
	
	@Override
	public void keyPressed(KeyEvent k) {
		switch (k.getKeyCode()) {
		case KeyEvent.VK_UP:
			vmy+=5;
			if(my > 10)
				my = 10;
			break;
		case KeyEvent.VK_DOWN:
			vmy-=5;
			if(my > 10)
				my = 10;
			break;
		default:
			break;
		}
	}

	@Override
	public void keyReleased(int k) {
		
	}

}

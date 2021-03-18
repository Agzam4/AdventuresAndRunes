package GameState;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Data.UserData;
import Main.GamePanel;
import TileMap.Background;

public class ScoreState extends GameState {
	
	private BufferedImage bg = getImage("/Backgrounds/grassbg1.png");
	BufferedImage star = getImage("/HUD/star.png");
	BufferedImage unstar = getImage("/HUD/unstar.png");
	
	String data;
	long time;
	int enemy;
	int hp;
	
	long target_time;
	int target_enemy;
	int target_hp;

	double $time;
	double $enemy;
	double $hp;
	
	int stars = 0;
	
	private int currentChoice = 0;
	private String[] options = {
		"Restart",
		"Next level"
	};
	
	public ScoreState(GameStateManager gsm, long time, int enemy, int hp, String data) {
		this.gsm = gsm;
		this.enemy = enemy;
		this.data = data;
		this.time = time;
		this.hp = hp;
		$time = $enemy = $hp = 0;
		stars = 0;
		
		try {
			target_time = Integer.parseInt(getString(data, "time"));
		} catch (Exception e) {
			target_time = 60;
		}
		System.out.println(time + "/" + target_time + "  " + !(time > target_time));

		addStar("damage_stars", enemy == 0);
		addStar("time_stars", !(time > target_time));
		addStar("hp_stars", hp > 333);
	}
	
	int lvl = Integer.parseInt(UserData.getData("level"));
	
	private void addStar(String id, boolean star) {
		if(star) stars++;
		try {
			String data = UserData.getData(id);
			while (data.length() < lvl) {
				data += "0";
			}
			data = data.substring(0,lvl+1);
			char[] cs = data.toCharArray();
			cs[lvl] = star ? '1':'0';
			UserData.writeData(id,new String(cs));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private BufferedImage getImage(String s) {
		try {
			return ImageIO.read(
					getClass().getResourceAsStream(s)
				);
		} catch (IOException e) {
		}
		return new BufferedImage(1, 1, 1);
	}
	@Override
	public void init() {
	}

	public String getString(String data, String name) throws StringIndexOutOfBoundsException {
		return data.substring(data.indexOf("<" + name + ">") + name.length()+2, data.indexOf("</" + name + ">"));
	}
	public String[] getStringArr(String data, String name) throws StringIndexOutOfBoundsException{
		return getString(data, name).split(",");
	}
	
	@Override
	public void update() {
	}

	@Override
	public void draw(Graphics2D g) {
		try {
			g.drawImage(bg, 0, 0, GamePanel.WIDTH, GamePanel.HEIGHT, null);
			drawStar(g, GamePanel.WIDTH/2 - 50, GamePanel.HEIGHT/3, 1.5, stars > -1);
			drawStar(g, GamePanel.WIDTH/2, GamePanel.HEIGHT/3, 2, stars > 1);
			drawStar(g, GamePanel.WIDTH/2 + 50, GamePanel.HEIGHT/3, 1.5, stars > 2);

			int h = GamePanel.HEIGHT/3;
			int w = GamePanel.WIDTH/2;
			Color yellow = new Color(250,255,155);
			drawStr(g, "Enemies last: ", 25, h + 75, enemy == 0 ? yellow : Color.GRAY, Color.BLACK);
			drawStr(g, "Time: ", 25, h + 100,  !(time > target_time) ? yellow : Color.GRAY, Color.BLACK);
			drawStr(g, "HP: ", 25, h + 125,  hp > 333 ? yellow : Color.GRAY, Color.BLACK);

			drawStr(g, "" + enemy, w, h + 75, enemy == 0 ? yellow : Color.GRAY, Color.BLACK);
			drawStr(g, time + "/" + target_time , w, h + 100, !(time > target_time) ? yellow : Color.GRAY, Color.BLACK);
			drawStr(g, "HP: " + hp/10 + "/33%" , w, h + 125, hp > 333 ? yellow : Color.GRAY, Color.BLACK);
		
			for (int j = 0; j < options.length; j++) {
				String str = j == currentChoice ? "> " + options[j] + " <": options[j];
				drawStr(g, str,
						(GamePanel.WIDTH/3)*(j+1) - g.getFontMetrics().stringWidth(str)/2,
						h*3 - 5, Color.WHITE, Color.BLACK);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void keyPressed(KeyEvent k) {
		switch (k.getKeyCode()) {
		case KeyEvent.VK_RIGHT:
			currentChoice = currentChoice == 0 ?  1 : 0;
			break;
		case KeyEvent.VK_LEFT:
			currentChoice = currentChoice == 0 ?  1 : 0;
			break;
		case KeyEvent.VK_ENTER:
			int lvl = Integer.parseInt(UserData.getData("level"));
			if(currentChoice == 1)
				UserData.writeData("level", (lvl + 1) + "");
			gsm.setState(GameStateManager.LEVEL1STATE);
			break;
		default:
			break;
		}
	}

	@Override
	public void keyReleased(int k) {
		
	}
	
	private void drawStar(Graphics2D g, int x, int y, double size, boolean isComplited) {
		BufferedImage img = isComplited ? star : unstar;
		int w = (int) (img.getWidth()  * size);
		int h = (int) (img.getHeight() * size);
		g.drawImage(img, x - w/2, y - h/2, w, h, null);
	}
	
	private void drawStr(Graphics2D g, String str, int x, int y, Color c1, Color c2) {
		g.setColor(c2);
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
}

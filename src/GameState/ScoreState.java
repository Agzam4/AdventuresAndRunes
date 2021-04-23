package GameState;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import Data.UserData;
import Main.GamePanel;

public class ScoreState extends GameState {
	
	private BufferedImage bg = getImage("/Backgrounds/" + Level1State.tilesetsName[Level1State.nameID] + ".png");
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
	
	double cosTime;
	
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
		cosTime+=0.1;
	}

	@Override
	public void draw(Graphics2D g) {
		try {
//			double plusCos = Math.cos(cosTime*0.5)*5;

//			GamePanel.fullImage = new BufferedImage(
//					GamePanel.d.width, GamePanel.d.height,
//					BufferedImage.TYPE_INT_ARGB
////					);
			Graphics2D fg = (Graphics2D) GamePanel.fullImage.getGraphics();
//			fg.setComposite(composite);
//			fg.setColor(new Color(0, 0, 0, 0));
//			fg.fillRect(0, 0, GamePanel.fullImage.getWidth(), GamePanel.fullImage.getHeight());
//			fg.setComposite(composite2);
			GameStateManager.fullIImgIsClear = false;
			for (int i = -1; i < 2; i++) {
				drawStar(fg, (GamePanel.WIDTH/2 + 50*i)*GamePanel.SCALE,
						(int) ((GamePanel.HEIGHT/3 +
								Math.cos(cosTime*(stars>i+1?0.5:0.4))*(i==0?7:5)
								)*GamePanel.SCALE),
						(i==0 ? 2 : 1.5)*GamePanel.SCALE, stars > i+1);
//				drawStar(fg, GamePanel.WIDTH/2*GamePanel.SCALE, 		(int) ((GamePanel.HEIGHT/3+plusCos)*GamePanel.SCALE), 2*GamePanel.SCALE, stars > 1);
//				drawStar(fg, (GamePanel.WIDTH/2 + 50)*GamePanel.SCALE,	(int) ((GamePanel.HEIGHT/3+plusCos)*GamePanel.SCALE), 1.5*GamePanel.SCALE, stars > 2);
			}
			fg.dispose();
			

			g.drawImage(bg, 0, 0, GamePanel.WIDTH, GamePanel.HEIGHT, null);
			
			int h = GamePanel.HEIGHT/3;
			int w = GamePanel.WIDTH/2;
			Color yellow = new Color(250,255,155);

			String starInfoStr[] = {"Enemies last: ","Time: ", "HP: "};
			String starInfo2Str[] = {enemy + "",time + "/"+target_time, "HP: " + hp/10 + "/33%"};
			boolean bools[] = {enemy == 0,!(time > target_time), hp > 333};
			int gb = (int) (Math.cos(cosTime)*40 + 75);
			Color gray = new Color(255, gb, gb);
			Color yellow2 = new Color(255, 255, gb);
			for (int i = 0; i < starInfoStr.length; i++) {
				drawStr(g, starInfoStr[i], 25, h + 75 + i*25, bools[i] ? yellow : gray, Color.BLACK);
				drawStr(g, starInfo2Str[i], w, h + 75+ i*25,  bools[i] ? yellow : gray, Color.BLACK);
			}
		
			for (int j = 0; j < options.length; j++) {
				String str = j == currentChoice ? "> " + options[j] + " <": options[j];
				drawStr(g, str,
						(GamePanel.WIDTH/3)*(j+1) - g.getFontMetrics().stringWidth(str)/2,
						h*3 - 5, (j == 0) ?
								(stars < 3) ? gray : Color.WHITE
								: 
								(stars > 2) ? yellow2 : Color.WHITE, Color.BLACK);
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

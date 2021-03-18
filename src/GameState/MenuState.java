package GameState;

import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFileChooser;

import Audio.AudioPlayer;
import Main.Game;
import Main.GamePanel;

public class MenuState extends GameState {
	
	private Background bg;
	
	private int currentChoice = 0;
	private String[] options = {
		"Start",
		"Load Level",
		"Credits",
		"Quit"
	};
	
	private Color titleColor;
	private Font titleFont;
	private int dark = 0;
	
	private Font font;
	
	protected static AudioPlayer audioPlayer;
	
	private int light[] = {65,65,65,65};
	
	public static boolean isMusicPlay = false;
	
	public MenuState(GameStateManager gsm) {
		
		this.gsm = gsm;
		dark = 255;
		
		try {
			
			bg = new Background("/Backgrounds/menubg.png", 1);
			bg.setVector(-0.2, 0);
			
			titleColor = new Color(128, 0, 0);
			titleFont = new Font("Tahoma",Font.BOLD,22);
			
			font = new Font("Arial", Font.PLAIN, 12);

			if(!isMusicPlay) {
				audioPlayer = new AudioPlayer("/Music/menu.mp3");
				audioPlayer.play(-1);
				isMusicPlay = true;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void init() {}
	
	public void update() {
		bg.update();
	}
	
	public void draw(Graphics2D g) {
		
		// draw bg
		bg.draw(g, true);
		
		// draw title
		g.setColor(titleColor);
		g.setFont(titleFont);
		String title = "Adventures & Runes";
//		g.drawString(title, GamePanel.WIDTH/2 - g.getFontMetrics().stringWidth(title)/2, );

		drawStr(g, title, (int) (GamePanel.HEIGHT / 4 * 1.2), Color.WHITE, Color.BLACK);
		// draw menu options
		g.setFont(font);
		for(int i = 0; i < options.length; i++) {
			if(i == currentChoice && light[i] < 250)
				light[i]+=10;
			else if(light[i] > 65)
				light[i]-=10;
			drawStr(g, i == currentChoice ? "> " +  options[i] + " <" : options[i], GamePanel.HEIGHT / 4 * 2 + i * 15,
					new Color(light[i],light[i],light[i]), Color.BLACK);
//			g.drawString(options[i], GamePanel.WIDTH/2 - g.getFontMetrics().stringWidth(options[i])/2, GamePanel.HEIGHT / 4 * 2 + i * 15);
		}
		
			if(dark > 5)
				dark-=5;
			g.setColor(new Color(0,0,0,dark));
			g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
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
	
	private void select() {
		if(currentChoice == 0) {
			if(audioPlayer != null) {
				audioPlayer.close();
				isMusicPlay = false;
			}
			gsm.setState(GameStateManager.LEVEL1STATE);
		}
		if(currentChoice == 2) {
			gsm.setState(GameStateManager.CREDITS);
		}
		if(currentChoice == 3) {
			audioPlayer.close();
			System.exit(0);
		}
		if(currentChoice == 1) {
			JFileChooser fc = new JFileChooser(new File(System.getProperty("user.dir")));
			if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
				Level1State.loadYouLevel = true;
				Level1State.youLevelURL = fc.getSelectedFile() + "";
				if(audioPlayer != null) {
					audioPlayer.close();
					isMusicPlay = false;
				}
				gsm.setState(GameStateManager.LEVEL1STATE);
			}
			
		}
	}
	
	String code = "";
	
	public void keyPressed(KeyEvent k) {
		code += Character.toUpperCase(k.getKeyChar());
		if(code.length() > 10) {
			code = code.substring(code.length()-10, code.length());
		}
		if(code.equals("/LEVELLOAD") || code.equals("/LOADLEVEL")) {
			options = new String[] {
					"Start",
					"Credits",
					"Quit",
					"Load Level"
			};
		}
		
		if(k.getKeyCode() == KeyEvent.VK_ENTER){
			select();
		}
		if(k.getKeyCode() == KeyEvent.VK_UP) {
			currentChoice--;
			if(currentChoice == -1) {
				currentChoice = options.length - 1;
			}
		}
		if(k.getKeyCode() == KeyEvent.VK_DOWN) {
			currentChoice++;
			if(currentChoice == options.length) {
				currentChoice = 0;
			}
		}
	}
	public void keyReleased(int k) {}
	
}











package GameState;

import Main.GamePanel;
import TileMap.*;
import Entity.*;
import Entity.Enemies.*;
import Audio.AudioPlayer;
import Data.UserData;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.concurrent.Exchanger;

public class Level1State extends GameState {
	
	public boolean nextLevel = false;
	
	public int level = Integer.parseInt(UserData.getData("level"));
	
	public long startingTime = 0;
	
	private TileMap tileMap;
	private Background bg;
	
	private boolean isPaused = false;
	
	public Player player;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;
	
	private HUD hud;
	
	private AudioPlayer bgMusic;
	
	public Level1State(GameStateManager gsm) {
		MenuState.audioPlayer.close();
		startingTime = 0;
		this.gsm = gsm;
		init();
	}
	public static boolean loadYouLevel = false;
	public static String youLevelURL = "";
	
	public void init() {
		
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/forest.png");
		if(loadYouLevel)
			tileMap.loadYouMap(youLevelURL);
		else
		tileMap.loadMap("/Maps/level" + level + ".map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		bg = new Background("/Backgrounds/grassbg1.png", 0.1);
		
		player = new Player(tileMap);
		String[] start = tileMap.getStringArr(tileMap.formData, "start");
		player.setPosition(Integer.parseInt(start[0]),Integer.parseInt(start[1]));
		
		populateEnemies();
		
		explosions = new ArrayList<Explosion>();
		
		hud = new HUD(player);
		
		bgMusic = new AudioPlayer("/Music/level1-1.mp3");
		bgMusic.play(-1);
		
	}
	
	private void populateEnemies() {
		
		enemies = new ArrayList<Enemy>();
		
		Enemy s;
		String pos[] = tileMap.getStringArr(tileMap.formData, "enemys_pos");
		String types[] = tileMap.getStringArr(tileMap.formData, "enemys_type");
		try {
			Point[] points = new Point[pos.length];
			for (int i = 0; i < points.length; i++) {
				String[] pos2 = pos[i].split(":");
				points[i] = new Point(Integer.parseInt(pos2[0]), Integer.parseInt(pos2[1]));
			}
			
			for(int i = 0; i < points.length; i++) {
				try {
					switch (types[i].toLowerCase()) {
					case "goblin":
						s = new Goblin(tileMap);
						break;
					case "goblin_archer":
						s = new GoblinArcher(tileMap, player);
						break;
					default:
						s = new Enemy(tileMap);
						break;
					}
					s.setPosition(points[i].x, points[i].y);
					enemies.add(s);
				} catch (NullPointerException e) {
				}
			}
		} catch (NumberFormatException e) {
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
	public void update() {
		if(isPaused) {
			// set background
			
			//drawStr(g, "", GamePanel.HEIGHT/2, c1, c2);
		}else {
			// update player
			nextLevel = player.update();
			
			tileMap.setPosition(
				GamePanel.WIDTH / 2 - player.getx(),
				GamePanel.HEIGHT / 2 - player.gety()
			);
			
			// set background
			bg.setPosition(tileMap.getx(), tileMap.gety());
			
			// attack enemies
			player.checkAttack(enemies);
			
			// update all enemies
			for(int i = 0; i < enemies.size(); i++) {
				Enemy e = enemies.get(i);
				e.update();
				if(e.isDead()) {
					enemies.remove(i);
					i--;
					explosions.add(
						new Explosion(e.getx(), e.gety()));
				}
			}
			
			// update explosions
			for(int i = 0; i < explosions.size(); i++) {
				explosions.get(i).update();
				if(explosions.get(i).shouldRemove()) {
					explosions.remove(i);
					i--;
				}
			}
			
			if(player.getHealth() < 1) { // TODO
				bgMusic.close();
				gsm.setState(GameStateManager.LEVEL1STATE);
			}
			
			if(nextLevel) {
//				if(!loadYouLevel) {
//					UserData.writeData("level", (level+1) + "");
//				}
				bgMusic.close();
				gsm.setScoreState(startingTime, enemies.size(), player.getHealth(), tileMap.formData);//TODO
			}
			startingTime++;
		}
	}
	
	public void draw(Graphics2D g) {
		
		// draw bg
		bg.draw(g, true);
		
		// draw tilemap
		tileMap.draw(g);
		
		// draw player
		player.draw(g);
		
		// draw enemies
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}
		
		// draw explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).setMapPosition(
				(int)tileMap.getx(), (int)tileMap.gety());
			explosions.get(i).draw(g);
		}
		
		// draw hud
		hud.draw(g);
		long timelost = (startingTime)/6;
		String timing = timelost/10 + "." +  timelost%10;
			g.drawString(timing, GamePanel.WIDTH - g.getFontMetrics().stringWidth(timing) - 5, 15);
		
		if(isPaused) {
			g.setColor(new Color(0,0,0,50));
			g.fillRect(0, 0, GamePanel.WIDTH,  GamePanel.HEIGHT);
			for (int i = 0; i < options.length; i++) {
				drawStr(g, i == selectedOption ? "> " + options[i] + " <" : options[i], GamePanel.HEIGHT/2 + (i-1)*15 , new Color(optionsColors[i],optionsColors[i],optionsColors[i]), Color.BLACK);
				if(i == selectedOption) {
					if(optionsColors[i] < 250)
						optionsColors[i] += 10;
				} else if(optionsColors[i] > 65) {
					optionsColors[i] -= 10;
				}
			}
		}
		
	}
	private int selectedOption = 0;
	private String[] options = {"Continue","Restart", "Menu"};
	private int[] optionsColors = {255,65,65};
	
	public void keyPressed(KeyEvent k) {
		if(k.getKeyCode() == KeyEvent.VK_LEFT) player.setLeft(true);
		if(k.getKeyCode() == KeyEvent.VK_RIGHT) player.setRight(true);
		if(k.getKeyCode() == KeyEvent.VK_W) player.setUp(true);
		if(k.getKeyCode() == KeyEvent.VK_DOWN) { 
			if(isPaused)
				selectedOption = (selectedOption+1)%options.length;
			else 
				player.setDown(true);
		}
		if(k.getKeyCode() == KeyEvent.VK_UP) {
			if(isPaused)
				selectedOption = selectedOption < 1 ? options.length-1 :selectedOption-1;
			else 
				player.setJumping(true);
		}
		if(k.getKeyCode() == KeyEvent.VK_E) player.setGliding(true);
		if(k.getKeyCode() == KeyEvent.VK_R) player.setScratching();
		if(k.getKeyCode() == KeyEvent.VK_F) player.setFiring();
		if(k.getKeyCode() == KeyEvent.VK_ESCAPE) isPaused = !isPaused;
		if(k.getKeyCode() == KeyEvent.VK_ENTER) {
			if(selectedOption == 0)
				isPaused = false;
			if(selectedOption == 1) {
				bgMusic.close();
				gsm.setState(GameStateManager.LEVEL1STATE);
			}
			if(selectedOption == 2) {
				bgMusic.close();
				gsm.setState(GameStateManager.MENUSTATE);
			}
		}
	}
	
	public void keyReleased(int k) {
		if(k == KeyEvent.VK_LEFT) player.setLeft(false);
		if(k == KeyEvent.VK_RIGHT) player.setRight(false);
		if(k == KeyEvent.VK_W) player.setUp(false);
		if(k == KeyEvent.VK_DOWN) player.setDown(false);
		if(k == KeyEvent.VK_UP) player.setJumping(false);
		if(k == KeyEvent.VK_E) player.setGliding(false);
	}
	
}













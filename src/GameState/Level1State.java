package GameState;

import Main.GamePanel;
import TileMap.Background;
import TileMap.TileMap;
import Audio.AudioPlayer;
import Data.UserData;
import Entity.Enemy;
import Entity.Explosion;
import Entity.HUD;
import Entity.Player;
import Entity.Rune;
import Entity.Enemies.Frog;
import Entity.Enemies.Goblin;
import Entity.Enemies.GoblinArcher;
import Entity.Enemies.GoblinBoss;
import Entity.Enemies.GoblinWizard;
import Entity.Enemies.Plant;
import Entity.Enemies.SwampCreature;
import Entity.Enemies.UpperPlant;
import Entity.Enemies.WitchBoss;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Level1State extends GameState {
	
	public boolean nextLevel = false;

	public static int screenDarknes = 0;
	private  double screenDarknes2 = 0;
	
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
	

	public static final String[] tilesetsName = {"forest", "swamp"};
	public static int nameID = 0;
	
	public void init() {
		nameID = (int) Math.floor((level)/11.0);
		System.out.println("\nLevel: " + level + " ID: " + nameID);
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/" + tilesetsName[nameID] + ".png");
		if(loadYouLevel)
			tileMap.loadYouMap(youLevelURL);
		else
		tileMap.loadMap("/Maps/level" + level + ".map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		bg = new Background("/Backgrounds/" + tilesetsName[nameID] + ".png", 0.1);
		
		player = new Player(tileMap);
		String[] start = tileMap.getStringArr(tileMap.formData, "start");
		player.setPosition(Integer.parseInt(start[0]),Integer.parseInt(start[1]));
		
		populateEnemies();
		
		explosions = new ArrayList<Explosion>();
		
		hud = new HUD(player);
		if(level%11 < 10) {
			bgMusic = new AudioPlayer("/Music/level" + (nameID + 1) + ".mp3");
			bgMusic.play(-1);
		} else if(level%11 == 10) {
			bgMusic = new AudioPlayer("/Music/boss" + (nameID + 1) + ".mp3");
			bgMusic.setVolume(0.1f);
		}
		
	}
	
	float vol = 0;
	
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
					switch (types[i].toLowerCase()) { // TODO
					case "goblin":
						s = new Goblin(tileMap);
						break;
					case "goblin_archer":
						s = new GoblinArcher(tileMap, player);
						break;
					case "goblin_wizard":
						s = new GoblinWizard(tileMap, player);
						break;
					case "goblinboss":
						s = new GoblinBoss(tileMap, player);
						System.err.println("BOSS");
						break;
					
					// SWAMP

					case "swamp_creature":
						s = new SwampCreature(tileMap);
						break;
					case "frog":
						s = new Frog(tileMap, player);
						break;
					case "plant":
						s = new Plant(tileMap);
						break;
					case "up_plant":
						s = new UpperPlant(tileMap, player);
						break;
					case "witchboss":
						System.out.println("WB");
						s = new WitchBoss(tileMap, player);
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
	
	boolean itemIsDropped = false;
	
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
		try {
		if(level == 10 && !bgMusic.isPlaying() && player.gety()/30 > 10) {
			bgMusic.play(-1);
			vol = 0.1f;
		}
		float v1 = vol;
		if(level == 10 && enemies.size() > 0) {
			vol += 0.01;
			if(vol > 1)
				vol = 1;
		}else {
			vol -= 0.01;
			if(vol < 0)
				vol = 0;
		}
		if(v1 != vol) {
			bgMusic.setVolume(vol);
		}
		screenDarknes = 0;
		if(isPaused) {
			
			//drawStr(g, "", GamePanel.HEIGHT/2, c1, c2);
		}else {
			nextLevel = player.update();
			
			tileMap.setPosition(
				GamePanel.WIDTH / 2 - player.getx(),
				GamePanel.HEIGHT / 2 - player.gety()
			);
			
			bg.setPosition(tileMap.getx(), tileMap.gety());
			
			player.checkAttack(enemies);
			
			for(int i = 0; i < enemies.size(); i++) {
				Enemy e = enemies.get(i);
				e.update();
				if(e.isDead()) {
					if(itemIsDropped) {
						if(level % 11 == 10) {
							if(((Rune)e).timee < 1)
							nextLevel = true;
						}
					}else {
						if(level % 11 == 10) {
							if(enemies.size()==1) {
								Rune rune = new Rune(tileMap, Rune.FIREBALL, this);// FIXME
								rune.setPosition(enemies.get(i).getx(), enemies.get(i).gety());
								enemies.remove(i);
								enemies.add(rune);
								itemIsDropped = true;
							}
						}else {
							enemies.remove(i);

						}
						i--;
						explosions.add(
								new Explosion(e.getx(), e.gety()));
					};
					
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
			
			if(nextLevel || GamePanel.code.equals("/SKIPLEVEL")) {
				GamePanel.code = "";
				bgMusic.close();
				gsm.setScoreState(startingTime, enemies.size(), player.getHealth(), tileMap.formData);//TODO
			}
			startingTime++;
		
		}	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	double timeW = 0;
	
	public void draw(Graphics2D g) {
		
		bg.draw(g, false);
		if(screenDarknes < 0)
			screenDarknes = 0;
		if(screenDarknes > 200)
			screenDarknes = 200;
		screenDarknes2 = (screenDarknes2 - screenDarknes)/1.1+ screenDarknes;
		g.setColor(new Color(0,0,0,(int) screenDarknes2));
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		
		
		tileMap.draw(g);
		
		player.draw(g);
		
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}
		
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).setMapPosition(
				(int)tileMap.getx(), (int)tileMap.gety());
			explosions.get(i).draw(g);
		}
		
		hud.draw(g);
		long timelost = (startingTime)/6;
		int sw = (int) timeW;
		g.setColor(new Color(0,0,0,100));
		g.fillRect(GamePanel.WIDTH - sw - 25, 5, (int) (timeW + 25), 25);
		g.setColor(new Color(222,222,222));
		String timing = timelost/10 + "." +  timelost%10;
			g.drawString(timing, GamePanel.WIDTH - sw - 10, 22);
			g.drawLine(GamePanel.WIDTH - sw - 20, 25, GamePanel.WIDTH - sw - 18, 25);
			g.drawLine(GamePanel.WIDTH - sw - 15, 25, GamePanel.WIDTH - 	 10, 25);
			g.drawLine(GamePanel.WIDTH -	  7, 25, GamePanel.WIDTH -       3, 25);
			
			timeW = (timeW - g.getFontMetrics().stringWidth(timing))/2 + g.getFontMetrics().stringWidth(timing);
			
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













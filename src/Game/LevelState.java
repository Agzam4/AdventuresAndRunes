package Game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.IOException;

import Entity.Player;
import Main.GameJPanel;
import TileMap.Background;
import TileMap.Tile;

public class LevelState extends GameState {

	private TileMap tileMap;
	private Background bg;
	private Player player;
	
	public LevelState() {
		this.gsm = gsm;
		init();
	}
	
	
	
	public LevelState(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}


	public void init() {
		tileMap = new TileMap(30); 
		try {
			tileMap.loadTiles("/Tilesets/blocks.png");
			tileMap.loadMap("/Maps/level1-1.map");
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
		tileMap.setPosition(0, 0);
		
		try {
			bg = new Background("/Backgrounds/grassbg1.gif", 0.1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		player = new Player(tileMap);
		player.setPosition(100, 100);
	}

	@Override
	public void update() {
		player.update();

	}

	@Override
	public void draw(Graphics2D g) {
//		g.setColor(Color.BLACK);
//		g.fillRect(0, 0, GameJPanel.WIDTH, GameJPanel.HEIGHT);
		bg.draw(g);
		
		tileMap.draw(g);
		
		player.draw(g);
	}

	public void keyPressed(int k) {
		if(k == KeyEvent.VK_LEFT) player.setLeft(true);
		if(k == KeyEvent.VK_RIGHT) player.setRight(true);
		if(k == KeyEvent.VK_W) player.setUp(true);
		if(k == KeyEvent.VK_DOWN) player.setDown(true);
		if(k == KeyEvent.VK_UP) player.setJumping(true);
		if(k == KeyEvent.VK_E) player.setGliding(true);
		if(k == KeyEvent.VK_R) player.setScratching();
		if(k == KeyEvent.VK_F) player.setFiring();
	}
	
	public void keyReleased(int k) {
		if(k == KeyEvent.VK_LEFT) player.setLeft(false);
		if(k == KeyEvent.VK_RIGHT) player.setRight(false);
		if(k == KeyEvent.VK_W) player.setUp(false);
		if(k == KeyEvent.VK_DOWN) player.setDown(false);
		if(k == KeyEvent.VK_UP) player.setJumping(false);
		if(k == KeyEvent.VK_E) player.setGliding(false);
	}
	
	private void setKeys(int k, boolean kkk) {
		switch (k) {
		case KeyEvent.VK_RIGHT:
			player.setLeft(kkk);
			break;
		case KeyEvent.VK_LEFT:
			player.setRight(kkk);
			break;
		case KeyEvent.VK_UP:
			player.setJumping(kkk);
			break;
		case KeyEvent.VK_DOWN:
			player.setDown(kkk);
			break;
		case KeyEvent.VK_W:
			player.setUp(kkk);
			break;
		case KeyEvent.VK_E:
			player.setGliding(kkk);
			break;
		case KeyEvent.VK_R:
			if(kkk)
			player.setScratching();
			break;
		case KeyEvent.VK_F:
			if(kkk)
			player.setFiring();
			break;
		default:
			break;
		}
	}

}

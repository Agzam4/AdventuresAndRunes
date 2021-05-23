package Main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;

import javax.swing.JPanel;

import Data.UserData;
import GameState.GameStateManager;

public class GamePanel extends JPanel 
	implements Runnable, KeyListener{
	
	private static final long serialVersionUID = 1L;
	// dimensions
	public static final Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
	public static final int SCALE = calScale();
	public static int WIDTH;
	public static int HEIGHT;
	
	
	private static int calScale() {
		int s = (int) Math.ceil(Math.ceil(d.height/8)/30);
		WIDTH = d.width/s;
		HEIGHT = d.height/s;
		return s;
	}
	
	// game thread
	private Thread thread;
	private boolean running;
	private int FPS = 60;
	private long targetTime = 1000 / FPS;
	
	// image
	private BufferedImage image;
	public static BufferedImage fullImage;
	public Graphics2D g;
	
	// game state manager
	private GameStateManager gsm;
	
	public GamePanel() {
		super();
		setBackground(Color.BLACK);
		setPreferredSize(
			new Dimension(d.width, d.height));
		setFocusable(true);
		requestFocus();
	}
	
	public void addNotify() {
		super.addNotify();
		if(thread == null) {
			thread = new Thread(this);
			thread.start();
			addKeyListener(this);
		}
	}
	
	private void init() {

		image = new BufferedImage(
					WIDTH, HEIGHT,
					BufferedImage.TYPE_INT_RGB
				);

		fullImage = new BufferedImage(
					d.width, d.height,
					BufferedImage.TYPE_INT_ARGB
				);
		g = (Graphics2D) image.getGraphics();getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		running = true;
		
		gsm = new GameStateManager();
		
	}
	
	public void run() {
		
		init();
		
		long start;
		long elapsed;
		long wait;
		
		// game loop
		while(running) {
			
			start = System.nanoTime();
			
			update();
			draw();
			drawToScreen();
			
			elapsed = System.nanoTime() - start;
			
			wait = targetTime - elapsed / 1000000;
			if(wait < 0) wait = 5;
			
			try {
				Thread.sleep(wait);
			} catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	private void update() {
		gsm.update();
	}
	private void draw() {
		gsm.draw(g);
	}
	private void drawToScreen() {
		BufferedImage all = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_RGB);

		Graphics g1 = all.getGraphics();
		g1.drawImage(image, 0, 0,
		WIDTH * SCALE, HEIGHT * SCALE,
		null);
		g1.drawImage(fullImage, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		g1.dispose();
		

		Graphics g2 = getGraphics();
		if(UserData.isAprilFoolsDay()) {
			int ah = all.getHeight();
			int aw = all.getWidth();
			drawAprilImg(g2, all, 0, 0, aw, ah);
			drawAprilImg(g2, all, 0, (int)(ah/1.5), aw, ah);
			drawAprilImg(g2, all, 0, (int)(ah/-1.5), aw, ah);
		}else {
			g2.drawImage(all, 0, 0, null);
		}
		g2.dispose();
	}
	
	private void drawAprilImg(Graphics g, BufferedImage all, int x, int y, int aw, int ah) {
		g.drawImage(all, aw/2+x,	ah/2+y, 	aw/-3,	ah/3, null);
		g.drawImage(all, aw/2+x, 	ah/2+y, 	aw/3,	ah/3, null);
		g.drawImage(all, aw/2+x, 	ah/2+y, 	aw/3,	ah/-3, null);
		g.drawImage(all, aw/2+x, 	ah/2+y, 	aw/-3,	ah/-3, null);

	}
	
	public void keyTyped(KeyEvent key) {
		code += Character.toUpperCase(key.getKeyChar());
		if(code.length() > 10) {
			code = code.substring(code.length()-10, code.length());
		}
	}
	
	public static String code = "";
	
	public void keyPressed(KeyEvent key) {
		gsm.keyPressed(key);
	}
	public void keyReleased(KeyEvent key) {
		gsm.keyReleased(key.getKeyCode());
	}

}

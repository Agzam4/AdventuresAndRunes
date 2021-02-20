package Main;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import Game.GameStateManager;

public class GameJPanel extends JPanel implements Runnable, KeyListener {

	public static int WIDTH = 320;
	public static int HEIGHT = 240;
	public static int SCALE = 2;
	

	private Thread thread;
	private boolean isRunning;
	private int fps = 60;
	private long targetTime = 1000 / fps;
	
	
	private BufferedImage image;
	private Graphics2D g;
	
	private GameStateManager gsm;
	
	
	
	public GameJPanel() {
		super();
		setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		setFocusable(true);
		requestFocus();
	}

	@Override
	public void addNotify() {
		super.addNotify();
		if (thread == null) {
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		gsm.keyPressed(e.getKeyCode());
	}


	@Override
	public void keyReleased(KeyEvent e) {
		gsm.keyReleased(e.getKeyCode());
	}


	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void run() {
		init();

		long start = System.nanoTime();
		long elapsed;
		long wait;
		
		while (isRunning) {
			update();
			draw();
			drawToScreen();
			
			elapsed = System.nanoTime() - start;
			wait = targetTime - elapsed/1_000_000;
			try {
				Thread.sleep(100);//TODO/wait);
			} catch (InterruptedException e) {
			}
		}
	}
	private void drawToScreen() {
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
		g2.dispose();
	}

	private void draw() {
		gsm.draw(g);
	}

	public void update() {
		gsm.update();
	}
	
	private void init() {
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		isRunning = true;
		gsm = new GameStateManager();
	}
}

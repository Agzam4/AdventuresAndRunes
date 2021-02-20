package Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import TileMap.Background;

public class MenuState extends GameState {

	private Background bg;
	private Color tileColor; 
	private Font titleFont;
	private Font font;
	
	private int currentChoice = 0;
	private String[] options = {
			"Start",
			"Help",
			"Quit"
			};
	
	public MenuState(GameStateManager gsm) {
		this.gsm = gsm;
		
		try {
			bg = new Background("/Background/menubg.jpg", 1);
			bg.setVector(-0.1, 0);
			tileColor = new Color(128,0,0);
			titleFont = new Font("Consolas", Font.PLAIN, 28);
			font = new Font("Arial", Font.PLAIN, 12);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void init() {

	}

	@Override
	public void update() {

	}

	@Override
	public void draw(Graphics2D g) {
		bg.draw(g);

		g.setColor(tileColor);
		g.setFont(titleFont);
		g.drawString("Test", 80, 70); // TODO
		
		g.setFont(font);;
		for (int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
				g.setColor(Color.WHITE);
			}else {
				g.setColor(Color.GRAY);
			}
			g.drawString(options[i], 145, 140 + i*15);
			
		}
	}

	private void select() {
		switch (currentChoice) {
		case 0:
			gsm.setState(GameStateManager.LVL1STATE);
			break;
		case 1:
			// help
			break;
		case 2:
			System.exit(0);
			break;

		default:
			break;
		}
	}
	
	@Override
	public void keyPressed(int k) {
		switch (k) {
		case KeyEvent.VK_ENTER:
			select();
			break;
		case KeyEvent.VK_UP:
			currentChoice--;
			if(currentChoice < 0)
				currentChoice = options.length - 1;
			break;		
		case KeyEvent.VK_DOWN:
			currentChoice++;
			if(currentChoice > options.length-1)
				currentChoice = 0;
			break;
		default:
			break;
		}
	}

	@Override
	public void keyReleased(int k) {

	}

}

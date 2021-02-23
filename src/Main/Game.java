package Main;

import javax.swing.JFrame;

public class Game {
	
	public static GamePanel gamePanel;
	
	public static void main(String[] args) {
		
		JFrame window = new JFrame("Adventures & Runes");
		window.setUndecorated(true);
		window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		gamePanel = new GamePanel();
		window.setContentPane(gamePanel);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		window.setResizable(false);
		window.pack();
		window.setVisible(true);
		
	}
	
}

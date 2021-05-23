package Main;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import Data.UserData;

public class Game {
	
	public static GamePanel gamePanel;
	
	public static void main(String[] args) {
		for (String string : args) {
			System.out.print(string + " ");
		}
		System.out.println("\n");
		UserData.setArgs(args);
		System.out.println("\n");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
		}
		JFrame window = new JFrame("Adventures & Runes");
		window.setUndecorated(true);
		window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		gamePanel = new GamePanel();
		window.setContentPane(gamePanel);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setVisible(true);
	}
	
}

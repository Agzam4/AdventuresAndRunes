package Main;
import javax.swing.JFrame;

public class Main extends JFrame {

	public static void main(String[] args) {
		Main main = new Main();
		main.setContentPane(new GameJPanel());
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.setResizable(false);
		main.pack();
		main.setVisible(true);
	}
	
	public Main() {
		setLocation(250, 50);
	}

}

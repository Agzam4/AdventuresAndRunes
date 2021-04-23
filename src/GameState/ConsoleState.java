package GameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import Main.GamePanel;

public class ConsoleState extends GameState {
	
	/**
	 * Это своеобразный "easter egg" - при запуске программы в день программиста,
	 * сначало булет запущена эта "State", а уже потом меню (при вводе "RUN")
	 */

	private final String C_HELP = "BG #<HEX>- Set background (BG #000)\nEXIT - Shuts down this application\nFG #<HEX> - Set foreground (FG #FFF)\nHELP - Displays reference information about commands\nRUN - Run game\n";
	private final String LOC = System.getProperty("user.dir");
	
	private String console = C_HELP;
	private String inputText = "";
	
	GameStateManager gameStateManager;
	
	public ConsoleState(GameStateManager gameStateManager) {
		this.gameStateManager = gameStateManager;
	}
	
	@Override
	public void init() {
	}

	int timer = 0;
	
	@Override
	public void update() {
		timer++;
	}
	Font f = new Font("Arial", Font.PLAIN, 14);

	public void draw(Graphics2D g) {
		g.setColor(bg);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
//		g.dispose();
		Graphics2D g1 = (Graphics2D) GamePanel.fullImage.getGraphics();
		g1.setFont(f);
		String[] lines = (console + LOC + ">" + inputText +
				((timer/20)%2==0 ? "":"_")).split("\n");
		if(lines.length*15 + 30 > GamePanel.d.height) {
			console = console.substring(console.indexOf('\n')+1, console.length());
			lines = (console + LOC + ">" + inputText +
					((timer/20)%2==0 ? "":"_")).split("\n");
		}
		int x = 0;
		int l = g1.getFontMetrics().stringWidth(console.split("\n")[0] + LOC + ">" + inputText);
		
		if(l+5 > GamePanel.d.width) {
			x = GamePanel.d.width - l;
		}
		g1.setColor(fg);
		for (int i = 0; i < lines.length; i++) {
			g1.drawString(lines[i], x+5, 25+i*15);
		}
		g1.dispose();
		
		GameStateManager.fullIImgIsClear = false;
	}

	Color bg = Color.BLACK;
	Color fg = Color.WHITE;
	
	@Override
	public void keyPressed(KeyEvent k) {
		if(k.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			if(inputText.length() > 0)
			inputText = inputText.substring(0, inputText.length()-1);
		}else if(k.getKeyCode() == KeyEvent.VK_ENTER) {
			console += LOC + ">" + inputText + "\n";
			String in = inputText.toUpperCase();
			switch (in) {
			case "HELP":
				console += C_HELP;
				break;
			case "EXIT":
				System.exit(0);
				break;
			case "RUN":
				gameStateManager.setState(GameStateManager.MENUSTATE);
				break;
			case "HELLO WORLD":
				console += "#_________________________#\n"
						+  "| \u2007\u2007\u2007\u2007\u2007\u2007\u2007\u2007\u2007\u2007\u2007\u2007\u2007\u2007\u2007\u2007\u2007\u2007\u2007\u2007\u2007\u2007\u2007\u2007\u2007 |\n"
						+  "| HAPPY PROGRAMMER'S DAY! |\n"
						+  "#_________________________#\n"
						+  "###########################\n";
				break;
			case "PI":
				console += "3.1415926535 8979323846 2643383279 5028841971 6939937510\n"
						+ "  5820974944 5923078164 0628620899 8628034825 3421170679\n"
						+ "  8214808651 3282306647 0938446095 5058223172 5359408128\n"
						+ "  4811174502 8410270193 8521105559 6446229489 5493038196\n"
						+ "  4428810975 6659334461 2847564823 3786783165 2712019091\n"
						+ "  4564856692 3460348610 4543266482 1339360726 0249141273\n"
						+ "  7245870066 0631558817 4881520920 9628292540 9171536436\n"
						+ "  7892590360 0113305305 4882046652 1384146951 9415116094\n"
						+ "  3305727036 5759591953 0921861173 8193261179 3105118548\n"
						+ "  0744623799 6274956735 1885752724 8912279381 8301194912\n"
						+ "  9833673362 4406566430 8602139494 6395224737 1907021798\n"
						+ "  6094370277 0539217176 2931767523 8467481846 7669405132\n"
						+ "  0005681271 4526356082 7785771342 7577896091 7363717872\n"
						+ "  1468440901 2249534301 4654958537 1050792279 6892589235\n"
						+ "  4201995611 2129021960 8640344181 5981362977 4771309960\n"
						+ "  5187072113 4999999837 2978049951 0597317328 1609631859\n"
						+ "  5024459455 3469083026 4252230825 3344685035 2619311881\n"
						+ "  7101000313 7838752886 5875332083 8142061717 7669147303\n"
						+ "  5982534904 2875546873 1159562863 8823537875 9375195778\n"
						+ "  1857780532 1712268066 1300192787 6611195909 2164201989\n";
				break;
			default:
				System.out.println(in.length());
				System.out.println(in.substring(0, 1));
				if(in.length() > 1) {
					if(in.substring(0, 2).equals("BG") || in.substring(0, 2).equals("FG")) {
						try {
							String hex = in.substring(in.indexOf("#"), in.length());
							if(hex.length() == 4) {
								char[] c = hex.toCharArray();
								hex = "#";
								for (int i = 1; i < c.length; i++) {
									hex += c[i] + "" + c[i];
								}
							}
							System.out.println(hex);
							if(in.substring(0, 2).equals("BG"))
								bg = Color.decode(hex);
							else
								fg = Color.decode(hex);
								
						} catch (Exception e) {
							if(in.substring(0, 2).equals("BG"))
								console += "Uncorrect command!\nExemple:\nBG #000\nBG #00010F\n";
							else
								console += "Uncorrect command!\nExemple:\nFG #FFF\nFG #00FF90\n";
								
						}
					}
				}
				break;
			}
			inputText = "";
		} else if(!Character.isISOControl(k.getKeyChar()) && Character.isDefined(k.getKeyChar())) {//Character.isUpperCase(Character.toUpperCase(k.getKeyChar())) || k.getKeyChar() == ' ') {
			inputText += k.getKeyChar();
		}
	}

	@Override
	public void keyReleased(int k) {
	}
}

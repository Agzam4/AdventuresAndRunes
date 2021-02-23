package Editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Entity.Enemy;
import Entity.Enemies.Goblin;
import TileMap.Tile;
import TileMap.TileMap;

public class EditorJPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	int width = 8;
	int height = 20;
	Tile tiles[][];

	int scrollX = 0;
	int scrollY = 0;
	double vScrollX = 0;
	double vScrollY = 0;
	
	boolean ctrl = false;
	
	int[][] map;
	public BufferedImage[] imgs;
	TileMap tileMap;

	public EditorJPanel() {
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/forest.png");
		tileMap.loadMap("/Maps/level" + 0 + ".map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		map = new int[height][width];
		try {
			BufferedImage tileset = ImageIO.read(getClass().getResourceAsStream("/Tilesets/forest.png"));
			int numTilesAcross = tileset.getWidth() / 30;
			tiles = new Tile[3][numTilesAcross];
			imgs = new BufferedImage[3*21];
			BufferedImage subimage;
			for(int col = 0; col < numTilesAcross; col++) {
				// 0
				subimage = tileset.getSubimage(col * 30,0,30,30);
				imgs[col] = subimage;
				// 1
				subimage = tileset.getSubimage(col * 30,30,30,30);
				imgs[col+21] = subimage;
				// 2
				subimage = tileset.getSubimage(col * 30,30*2,30,30);
				imgs[col+42] = subimage;
			}
		} catch (IOException e) {
		}
		
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				requestFocus();
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				requestFocus();
				if(selected < 0)
					addEnemy(e.getX(), e.getY());
				else
				replaceBlock(e.getX(), e.getY());
			}
		});
		
		addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent arg0) {
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				if(selected > -1) {
					requestFocus();
					replaceBlock(e.getX(), e.getY());	
				}			
			}
		});
		
		setFocusable(true);
		requestFocus();
		addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					vScrollY = 0;
					break;
				case KeyEvent.VK_DOWN:
					vScrollY = 0;
					break;
				case KeyEvent.VK_LEFT:
					vScrollX = 0;
					break;
				case KeyEvent.VK_RIGHT:
					vScrollX = 0;
					break;
				case KeyEvent.VK_D:
					ctrl = false;
					break;
				default:
					break;
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_UP:
					vScrollY = -1;
					break;
				case KeyEvent.VK_DOWN:
					vScrollY = 1;
					break;
				case KeyEvent.VK_LEFT:
					vScrollX = -1;
					break;
				case KeyEvent.VK_RIGHT:
					vScrollX = 1;
					break;
				case KeyEvent.VK_L:
					setSize(width, height+1);
					break;
				case KeyEvent.VK_J:
					setSize(width, height-1);
					break;
				case KeyEvent.VK_I:
					setSize(width+1, height);
					break;
				case KeyEvent.VK_K:
					setSize(width-1, height);
					break;
				case KeyEvent.VK_F1:
					export();
					break;
				case KeyEvent.VK_D:
					ctrl = true;
					break;
				default:
					break;
				}
			}

			private void export() {
				String mapdata = "<size>" + height + "," + width + "</size><map>";
				for (int x = 0; x < width; x++) {
					for (int y = 0; y < height; y++) {
						mapdata += map[y][x];
						mapdata += ",";
					}
				};
				mapdata = mapdata.substring(0, mapdata.length()-1);
				mapdata += "</map>\n<start>100,100</start>\n";
				mapdata += "<enemys_type>";
				for (int i = 0; i < enemies.size(); i++) {
					mapdata += enemies.get(i).getName();
					if(i != enemies.size()-1)
						mapdata += ",";
				}
				mapdata +="</enemys_type>\n<enemys_pos>";
				for (int i = 0; i < enemies.size(); i++) {
					mapdata += enemies.get(i).getPos();
					if(i != enemies.size()-1)
						mapdata += ",";
				}
				mapdata +="</enemys_pos>\n<enemys_drop_item>";
				for (int i = 0; i < enemies.size(); i++) {
					mapdata += "null";
					if(i != enemies.size()-1)
						mapdata += ",";
				}
				mapdata +="</enemys_drop_item>";
				//<enemys_pos>100:100,100:100</enemys_pos><enemys_drop_item>null,null</enemys_drop_item>";
				try {
					FileWriter writer = new FileWriter(new File("level.map"));
					writer.write(mapdata);
					writer.flush();
					writer.close();
				} catch (IOException e1) {
				}
			}
		});
		
		Thread updating = new Thread() {
			public void run() {
				while(true) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
					repaint();
					updateData();
				}
			};
		};
		updating.start();
	}
	
	private void updateData() {
		scrollX += vScrollX*-15;
		scrollY += vScrollY*-15;
	}
	
	public void setSize(int w, int h) {
		if(w < 8)
			return;
		if(h < 8)
			return;
		int[][] map2 = map.clone();
		map = new int[h][w];
		
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				try {
					map[y][x] = map2[y][x+width-w];
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}
		}
		
		width = w;
		height = h;
	}
	
	@Override
	public void paint(Graphics gg) {
		Graphics2D g = (Graphics2D) gg;
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.WHITE);
		g.drawRect(scrollX-1, scrollY-1, height*30 + 1, width*30+1);
		g.setColor(new Color(255,255,255,25));
		g.fillRect(scrollX-1, scrollY-1, height*30 + 1, width*30+1);
		for (int x = scrollX/30; x < (getWidth())/30 + 1; x++) {
			for (int y = scrollY/30; y < (getHeight())/30 + 1; y++) {
				try {
					g.setColor(new Color(255,255,255,(x+y)%2 == 0 ? 10: 25));
					g.fillRect(x*30+scrollX%30, y*30+scrollY%30, 30, 30);
					g.setColor(Color.WHITE);
					if((x-scrollX/30)%5 == 0 && (y-scrollY/30)%5 == 0)
						g.drawString(x-scrollX/30+":"+(y-scrollY/30), x*30+scrollX%30, y*30+scrollY%30 + 10);
					g.drawImage(imgs[map[x-scrollX/30][y-scrollY/30]],
							x*30+scrollX%30,
							y*30+scrollY%30,
							null);
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}
		}
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g, scrollX, scrollY);
			if(ctrl)
				if(enemies.get(i).tryDelite(getMousePosition().x - scrollX,getMousePosition().y - scrollY))
					enemies.remove(i);
		}
	}
	
	public int selected = 21;
	
	private void replaceBlock(int x, int y) {
		try {
			map[(x-scrollX)/30][(y-scrollY)/30] = selected;
		} catch (ArrayIndexOutOfBoundsException e) {
		}
	}

	ArrayList<EnemyImgs> enemies = new ArrayList<EnemyImgs>();

	public static final String[] ENEMYS_NAMES = {"goblin","enemy"};
	public final BufferedImage[] ENEMYS_IMGS = getImgs();
	public static final int GOBLIN = 0;
	public static final int ENEMY = 1;
	
	private void addEnemy(int x, int y) {
		if(ctrl)
			return;
		EnemyImgs e = new EnemyImgs(ENEMYS_IMGS[-selected-1], x-15-scrollX, y-15-scrollY, ENEMYS_NAMES[-selected-1]);
		enemies.add(e);
	}
	
	public BufferedImage[] getImgs() {
		BufferedImage[] imgs = new BufferedImage[ENEMYS_NAMES.length];
		for (int i = 0; i < imgs.length; i++) {
			try {
				imgs[i] = getEnemyImage(ENEMYS_NAMES[i]);
			} catch (IOException e) {
				imgs[i] = new BufferedImage(30, 30, BufferedImage.TYPE_INT_RGB);
			}
		}
		return imgs;
	}
	private BufferedImage getEnemyImage(String type) throws IOException {
		return ImageIO.read(
				getClass().getResourceAsStream(
						"/Sprites/Enemies/" + type + ".png"
						)).getSubimage(0, 0, 30, 30);
	}
}


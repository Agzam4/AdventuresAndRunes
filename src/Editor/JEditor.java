package Editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import TileMap.Tile;

import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.CardLayout;
import javax.swing.BoxLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class JEditor extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JEditor frame = new JEditor();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
		 */
	public JEditor() {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("New menu");
		menuBar.add(mnNewMenu);
		
		JMenuItem export = new JMenuItem("Export");
		mnNewMenu.add(export);
		
		JMenu mnNewMenu_1 = new JMenu("Settings");
		mnNewMenu.add(mnNewMenu_1);
		
		JMenu mnNewMenu_2 = new JMenu("Map");
		mnNewMenu_1.add(mnNewMenu_2);
		
		JPanel panel_1 = new JPanel();
		panel_1.setPreferredSize(new Dimension(150, 30));
		mnNewMenu_2.add(panel_1);
		
		JLabel lblNewLabel = new JLabel("Wight: ");
		panel_1.add(lblNewLabel);
		
		final JSpinner w = new JSpinner();
		w.setModel(new SpinnerNumberModel(100, 25, 500, 1));
		panel_1.add(w);
		
		JPanel panel_11 = new JPanel();
		mnNewMenu_2.add(panel_1);
		
		JPanel panel_2 = new JPanel();
		panel_2.setPreferredSize(new Dimension(150, 30));
		mnNewMenu_2.add(panel_2);
		
		JLabel lblHeight = new JLabel("Height:");
		panel_2.add(lblHeight);
		
		final JSpinner h = new JSpinner();
		h.setModel(new SpinnerNumberModel(8, 8, 500, 1));
		panel_2.add(h);
		
		JLabel lblNewLabell = new JLabel("New label");
		panel_11.add(lblNewLabell);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	
		JPanel panel2 = new JPanel();
		getContentPane().add(panel2, BorderLayout.EAST);
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.Y_AXIS));
		
		final JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		final JLabel iv = new JLabel(new ImageIcon(main));
		scrollPane.setViewportView(iv);

		JScrollPane scrollPane2 = new JScrollPane();
		scrollPane2.setPreferredSize(new Dimension(300, 60));
		contentPane.add(scrollPane2, BorderLayout.SOUTH);
		JPanel panel = new JPanel();
		scrollPane2.setViewportView(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		images();
		
		int id = 0;
		for (int x = 0; x < 3; x++) {
			for (int y = 0; y < 20; y++) {
				final JButton b = new JButton(new ImageIcon(tiles[x][y].getImage()));
				panel.add(b);
				b.setMargin(new Insets(0, 0, 0, 0));
				b.setName(id + "");
				b.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						selected = Integer.parseInt(b.getName());
						System.out.println(selected);
					}
				});
				id++;
				
			}
		}
		
		
		Thread draw = new Thread() {
			@Override
			public void run() {
				while (true) {
					redraw();
					iv.repaint();
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
					}
				}
			}
		};
		draw.start();
		
		iv.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				try {
					map[e.getX()/30]
					[e.getY()/30]
							    = selected;
				} catch (Exception e2) {
				}
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		
		iv.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				try {
					map[e.getX()/30]
					[e.getY()/30] 
							    = selected;
				} catch (Exception e2) {
				}
			}
		});
		
		w.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				width = Integer.parseInt(w.getValue()+"");
				main = new BufferedImage(width*30, height*30, BufferedImage.TYPE_INT_RGB);
				iv.setIcon(new ImageIcon(main));
				iv.repaint();
				int[][] map2 = new int[width][height];
				for (int x = 0; x < map.length; x++) {
					for (int y = 0; y < map[x].length; y++) {
						try {
							map2[x][y] = map[x][y];
						} catch (ArrayIndexOutOfBoundsException e2) {
						}
					}
				}
				map = map2.clone();
			}
		});
		h.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				height = Integer.parseInt(h.getValue()+"");
				main = new BufferedImage(width*30, height*30, BufferedImage.TYPE_INT_RGB);
				iv.setIcon(new ImageIcon(main));
				iv.repaint();
				int[][] map2 = new int[width][height];
				for (int x = 0; x < map.length; x++) {
					for (int y = 0; y < map[x].length; y++) {
						try {
							map2[x][y] = map[x][y];
						} catch (ArrayIndexOutOfBoundsException e2) {
						}
					}
				}
				map = map2.clone();
			}
		});
		
		export.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String mapdata = "<size>" + width + "," + height + "</size><map>";
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						mapdata += map[x][y];
						mapdata += ",";
					}
				};
				mapdata = mapdata.substring(0, mapdata.length()-1);
				mapdata += "</map><start>100,100</start>";
				mapdata += "<enemys_type>goblin,goblin</enemys_type><enemys_pos>100:100,100:100</enemys_pos><enemys_drop_item>null,null</enemys_drop_item>";
				try {
					FileWriter writer = new FileWriter(new File("level.map"));
					writer.write(mapdata);
					writer.flush();
					writer.close();
				} catch (IOException e1) {
				}
			}
		});
	}
	
	int selected = 0;
	
	private void redraw() {
		Graphics2D g = (Graphics2D) main.getGraphics();
		g.setColor(new Color(50,50,255));
		g.fillRect(0, 0, main.getWidth(), main.getHeight());
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				try {
					g.drawImage(tiles[map[x][y]/20][map[x][y]%20].getImage(), x*30, y*30, null);
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}
		}
		g.dispose();
	}

	int width = 150;
	int height = 100;
	
	int[][] map;
	BufferedImage main = new BufferedImage(width*30, height*30, BufferedImage.TYPE_INT_RGB);
	Tile tiles[][];
	
	private void images() {
		map = new int[width][height];
		BufferedImage tileset;
		try {
			tileset = ImageIO.read(
					getClass().getResourceAsStream("/Tilesets/forest.png")
				);
			int numTilesAcross = tileset.getWidth() / 30;
			tiles = new Tile[3][numTilesAcross];
			
			BufferedImage subimage;
			for(int col = 0; col < numTilesAcross; col++) {
				// 0
				subimage = tileset.getSubimage(col * 30,0,30,30);
				tiles[0][col] = new Tile(subimage, Tile.NORMAL);
				// 1
				subimage = tileset.getSubimage(col * 30,30,30,30);
				tiles[1][col] = new Tile(subimage, Tile.NORMAL);
				// 2
				subimage = tileset.getSubimage(col * 30,30*2,30,30);
				tiles[2][col] = new Tile(subimage, Tile.NORMAL);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

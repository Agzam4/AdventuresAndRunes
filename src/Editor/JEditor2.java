package Editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;

import Entity.Enemy;
import Entity.Enemies.Goblin;

import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class JEditor2 extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panel_1;
	private EditorJPanel editorJPanel;
	private Color bg = new Color(0,5,15);
	private JButton[] buttons;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JEditor2 frame = new JEditor2();
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
	public JEditor2() {
		
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(bg);
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(15000, 60));
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(25);
		contentPane.add(scrollPane, BorderLayout.SOUTH);
		
		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
//		panel.setPreferredSize(new Dimension(0, 0));
		panel.setMaximumSize(new Dimension(99999, 30));
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		editorJPanel = new EditorJPanel();
		contentPane.add(editorJPanel, BorderLayout.CENTER);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		contentPane.add(scrollPane_1, BorderLayout.EAST);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(new EmptyBorder(0,0,0,0));
		scrollPane_1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollPane_1.setBorder(new EmptyBorder(0,0,0,0));
		scrollPane.setBackground(bg);
		scrollPane_1.setBackground(bg);
		panel.setBackground(bg);
		scrollPane.getHorizontalScrollBar().setUI(new BasicScrollBarUI() {   

	        @Override
	        protected JButton createDecreaseButton(int orientation) {
	        	 JButton jbutton = new JButton();
	        	 jbutton.setBackground(new Color(10,10,25));
	            return createZeroButton();
	        }

	        @Override    
	        protected JButton createIncreaseButton(int orientation) {
	            return createZeroButton();
	        }

	        private JButton createZeroButton() {
	            JButton jbutton = new JButton();
	            jbutton.setPreferredSize(new Dimension(15, 15));
	            jbutton.setMinimumSize(new Dimension(0, 0));
	            jbutton.setMaximumSize(new Dimension(0, 0));
	            jbutton.setBackground(new Color(30,30,75));
	            jbutton.setBorderPainted(false);
	            return jbutton;
	        }
	        
	        @Override
		    protected void paintThumb( Graphics g, JComponent c, Rectangle tb ) {
		        g.setColor(new Color(61,61,153, 100));
		        g.fillRect(tb.x+1, tb.y+1, tb.width-2, tb.height-2);
		        g.setColor(new Color(30,30,75));
		        g.drawRect(tb.x+1, tb.y+1, tb.width-3, tb.height-3);
		        g.drawRect(tb.x, tb.y, tb.width-1, tb.height-1);
				g.setColor(Color.WHITE);
				g.dispose();
	        }
		    
	        @Override
		    protected void paintTrack(Graphics g, JComponent c, Rectangle tb) {
		        g.setColor(new Color(10,10,25));
		        g.fillRect(tb.x, tb.y, tb.width, tb.height );
		    }
	    });
		
		panel_1 = new JPanel();
		scrollPane_1.setViewportView(panel_1);
		panel_1.setBackground(bg );
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
		
		
		buttons = new JButton[editorJPanel.imgs.length];
		for (int i = 0; i < editorJPanel.imgs.length; i++) {
			buttons[i] = new JButton(new ImageIcon(editorJPanel.imgs[i]));
			panel.add(buttons[i]);
//			b.setBorderPainted(false);
			buttons[i].setBackground(bg);
			buttons[i].setMargin(new Insets(0, 0, 0, 0));
			buttons[i].setBorder(new LineBorder(new Color(100,100,100)));
			buttons[i].setName(i + "");
			
			final JButton b = buttons[i];
			buttons[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					editorJPanel.selected = Integer.parseInt(b.getName());
					for (JButton jButton : buttons) {
						jButton.setBackground(bg);
						jButton.setBorder(new LineBorder(new Color(100,100,100)));
					}
					b.setBackground(new Color(100,100,100));
					b.setBorder(new LineBorder(Color.WHITE));
				}
			});
		}

		for (int i = 0; i < EditorJPanel.ENEMYS_NAMES.length; i++) {
			addEnemyButton(EditorJPanel.ENEMYS_NAMES[i], i);
		}
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
	}
	
	
	private void addEnemyButton(String type, int id) {
		final JButton button = new JButton();
		button.setIcon(new ImageIcon(editorJPanel.ENEMYS_IMGS[id]));
		panel_1.add(button);
		button.setName(id + "");
		button.setBackground(bg);
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setBorder(new LineBorder(new Color(100,100,100)));
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				editorJPanel.selected = -Integer.parseInt(button.getName())-1;
			}
		});
	}
	

}

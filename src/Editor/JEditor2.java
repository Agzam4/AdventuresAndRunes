package Editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Entity.Enemy;
import Entity.Enemies.Goblin;

import javax.swing.JScrollPane;

public class JEditor2 extends JFrame {

	private JPanel contentPane;
	private JPanel panel_1;
	private EditorJPanel editorJPanel;

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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
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
		
		panel_1 = new JPanel();
		scrollPane_1.setViewportView(panel_1);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));
		
		for (int i = 0; i < editorJPanel.imgs.length; i++) {
			final JButton b = new JButton(new ImageIcon(editorJPanel.imgs[i]));
			panel.add(b);
			b.setMargin(new Insets(0, 0, 0, 0));
			b.setName(i + "");
			b.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					editorJPanel.selected = Integer.parseInt(b.getName());
				}
			});
		}

		for (int i = 0; i < EditorJPanel.ENEMYS_NAMES.length; i++) {
			addEnemyButton(EditorJPanel.ENEMYS_NAMES[i], i);
		}
	}
	
	
	private void addEnemyButton(String type, int id) {
		final JButton button = new JButton();
		button.setIcon(new ImageIcon(editorJPanel.ENEMYS_IMGS[id]));
		panel_1.add(button);
		button.setName(id + "");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				editorJPanel.selected = Integer.parseInt(button.getName())-1;
			}
		});
	}
	

}

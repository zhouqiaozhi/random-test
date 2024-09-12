package com.zhou.tetris.frame;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.zhou.tetris.component.Board;

public class TetrisFrame extends JFrame implements KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Board board;
	public TetrisFrame() {
		
		setTitle("Tetris");
		setSize(200, 440);
		setUndecorated(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		var container = new JPanel();
		setContentPane(container);
		
		container.setLayout(null);
		
		board = new Board(() -> {
			var label = new JLabel("Thank for playing!!!");
			label.setForeground(Color.CYAN);
			label.setOpaque(true);
			label.setBackground(new Color(51, 51, 51));
			container.add(label);
			label.setBounds(40, 200, 120, 20);
			new Thread(() -> {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
				}
				dispose();
			}).start();
			
			return true;
		});
		container.add(board);
		board.setBounds(0, 0, 200, 440);
		
		setResizable(false);
		addKeyListener(this);
	}
	
	

	@Override
	public void keyTyped(KeyEvent e) {
		board.keyTyped(e);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		board.keyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		board.keyReleased(e);
	}
}

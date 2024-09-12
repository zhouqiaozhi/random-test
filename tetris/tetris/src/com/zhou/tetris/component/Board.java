package com.zhou.tetris.component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.function.Supplier;

import javax.swing.JPanel;

import com.zhou.tetris.shape.Shape;
import com.zhou.tetris.util.NumUtils;

public class Board extends JPanel implements KeyListener  {

	private static final long serialVersionUID = 1L;
	int m, n;
	private int[][][] cur;
	private int[][][] next;
	private int selected, selectedNext;
	private int[][] board;
	private int X, Y;
	
	private KeyEvent prev;
	
	private boolean complete;
	
	public Board(Supplier<Boolean> c) {
		complete = false;
		m = 22;
		n = 10;
		board = new int[m][n];
		next();
		next();
		initPosition();
		new Thread(() -> {
			while(true) {
				int s = 100;
				while(true) {
					try {
						Thread.sleep(s);
					} catch (InterruptedException e) {
					}
					if(!down()) break;
					s = 1000;
				}
				if(!checkResult()) {
					break;
				}
			}
			complete = c.get();
		}).start();
	}
	
	private boolean checkResult() {
		update();
		if(cur != null && Y < cur[selected].length) return false;
		next();
		initPosition();
		repaint();
		return true;
	}
	
	private void update() {
		var shape = cur[selected];
		int base = -1;
		int k = 0;
		int[] move = new int[22];
		int sum = 0;
		for(int a = shape.length - 1; a >= 0 && Y - (shape.length - 1 - a) >= 0; a--) {
			int line = Y - (shape.length - 1 - a);
			sum = 0;
			int y2 = X + shape[0].length - 1;
			for(int i = 0; i < n; i++) {
				if(i >= X && i <= y2 && shape[a][i - X] == 1) board[line][i] = shape[a][i - X];
				sum += board[line][i];
			}
			if(sum == 10) {
				Arrays.fill(board[line], 0);
				k++;
				if(base == -1) base = line;
			} else {
				move[line] = k;
			}
		}
		if(base != -1) {
			int next;
			int up = 0;
			for(int line = Y - shape.length; line >= 0; line--) {
				next = 0;
				for(int i = 0; i < n; i++) {
					next += board[line][i];
				}
				if(next == 0 && sum == 0) {
					move[line + 1] = 0;
					up = line + 2;
					break;
				} else {
					move[line] = k;
				}
				sum = next;
			}
			for(int line = base; line >= up; line--) {
				if(move[line] != 0) {
					int[] t = board[line + move[line]];
					board[line + move[line]] = board[line];
					board[line] = t;
				}
			}
			if(Y < shape.length - 1) {
				if(Y - (shape.length - 1) + k < 0) return;
				int size = shape.length - 1 - Y;
				for(int i = 0; i < size; i++) {
					for(int j = 0; j < shape[0].length; j++) {
						board[Y - (shape.length - 1 - i) + k][X + j] = shape[i][j];
					}
				}
				cur = null;
			}
		}
	}

	private void initPosition() {
		X = n / 2 - (cur[selected][0].length + 1) / 2;
		Y = -1;
	}
	
	private void next() {
		cur = next;
		selected = selectedNext;
		next = Shape.random();
		selectedNext = NumUtils.randomInt(next.length);
	}

	@Override
	protected void paintComponent(Graphics g) {
		if(complete) return;
		super.paintComponent(g);
		g.setColor(new Color(51, 51, 51));
		for(int i = 0; i < m; i++) {
			for(int j = 0; j < n; j++) {
				if((board[i][j] | shape(i, j)) == 1) {
					g.fillRect(j * 20, i * 20, 20, 20);
				}
			}
		}
	}
	
	private int shape(int i, int j) {
		j -= X;
		i -= Y - cur[selected].length + 1;
		return i >= 0 && i < cur[selected].length && j >= 0 && j < cur[selected][0].length ? cur[selected][i][j] : 0;
	}
	
	private boolean end() {
		while(down()) {};
		return true;
	}
	
	private synchronized boolean down() {
		return move(X, Y + 1, selected);
	}
	
	private boolean left() {
		return move(X - 1, Y, selected);
	}
	
	private boolean right() {
		return move(X + 1, Y, selected);
	}
	
	private boolean rotate() {
		if(cur.length == 1) return false;
		return move(X, Y, (selected + 1) % cur.length);
	}
	
	private boolean move(int x, int y, int s) {
		if(!check(x, y, s)) return false;
		X = x;
		Y = y;
		selected = s;
		repaint();
		return true;
	}
	
	private boolean check(int X, int Y, int selected) {
		var shape = cur[selected];
		if(X + shape[0].length > n || X < 0 || Y == m) return false;
		for(int a = shape.length - 1; a >= 0; a--) {
			int line = Y - (shape.length - 1 - a);
			for(int b = 0; b < shape[0].length; b++) {
				if(shape[a][b] == 1 && line >= 0 && X + b < m && board[line][X + b] == 1) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(complete) return;
		if(isDoubleClick(e)) {
			end();
			prev = null;
			return;
		}
		prev = e;
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			right();
		} else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			left();
		} else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			down();
		} else if(e.getKeyCode() == KeyEvent.VK_UP) {
			rotate();
		} else {
			return;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
	
	private boolean isDoubleClick(KeyEvent e) {
		return prev != null && e.getKeyCode() == KeyEvent.VK_DOWN && e.getKeyCode() == prev.getKeyCode() && e.getWhen() - prev.getWhen() > 50 && e.getWhen() - prev.getWhen() < 200;
	}

}

package com.zhou.tetris;

import com.zhou.tetris.frame.TetrisFrame;

public class Main {
	// m * n < 64
	public static void main(String[] args) {
		init();
		
	}
	
	private static void init() {
		var frame = new TetrisFrame();
		frame.setVisible(true);
	}
}

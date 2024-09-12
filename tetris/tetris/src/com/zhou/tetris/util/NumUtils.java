package com.zhou.tetris.util;

import java.util.Random;

public class NumUtils {
	private final static Random random = new Random();
	public static int randomInt(int bound) {
		return random.nextInt(bound);
	}
}

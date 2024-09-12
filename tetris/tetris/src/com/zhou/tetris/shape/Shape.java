package com.zhou.tetris.shape;

import java.util.Arrays;

import com.zhou.tetris.util.NumUtils;

public class Shape {
	private static int[][][][] shapes;
	private static Integer size;
	
	static {
		init();
	}
	
	private static void init() {
		prepare();
		size = shapes.length;
	}
	
	private static void prepare() {
		shapes = ShapeGenerator.generate();
	}

	public static int[][][] random() {
		int select = NumUtils.randomInt(size);
		return shapes[select];
	}
	
	public static int[][][] choose(int x) {
		return shapes[x];
	}
	
	public static void print() {
		for(var shape: shapes) {
			System.out.println("###");
			for(var x: shape) {
				System.out.println("---");
				print(x);
			}
		}
	}
	
	public static void print(int[][] shape) {
		for(int i = 0; i < shape.length; i++) {
			System.out.println(Arrays.toString(shape[i]));
		}
	}
}

package com.zhou.tetris.shape;

import java.util.HashSet;
import java.util.Set;

public class ShapeGenerator {
	public static int[][][][] generate() {
		var res = generateShape3x2();
		res[res.length - 1] = new int[][][]{ new int[][] {{1, 1}, {1, 1}} }; // 2x2
		res[res.length - 2] = new int[][][]{ new int[][] {{1}, {1}, {1}, {1}}, new int[][] {{1, 1, 1, 1}} }; // 1x4 4x1
		return res;
	}
	
	public static int[][][][] generateShape3x2() {
		var set = generateBitMap();
		int[][][][] res = new int[set.size() + 2][][][];
		int k = 0;
		for(var x: set) {
			int[][] s = new int[3][2];
			for(int i = 0; i < 3; i++) {
				for(int j = 0; j < 2; j++) {
					s[i][j] = (x & (1 << (i * 2 + j))) == 0 ? 0 : 1; 
				}
			}
			int g = (((x & 48) >> 4) ^ (x & 3)) == 3 ? 2 : 4;
			res[k] = new int[g][][];
			int r = 0;
			res[k][r] = s;
			while(r < g - 1) {
				r++;
				s = rotate(s);
				res[k][r] = s;
			}
			k++;
		}
		return res;
	}
	
	private static int[][] rotate(int[][] O) {
		int m = O[0].length;
		int n = O.length;
		int[][] res = new int[m][n];
		for(int i = 0; i < m; i++) {
			for(int j = 0; j < n; j++) {
				res[i][j] = O[n - j - 1][i];
			}
		}
		return res;
	}
	
	private static Set<Integer> generateBitMap() {
		Set<Integer> set = new HashSet<>();
		int m = 6;
		int k = 4;
		
		int state = (1 << k) - 1;            
		while (state < (1 << m)) {
			if(valid(state, set)) {
				set.add(state);
			}
			int c = state & -state;
			int r = state + c;
		    state = (((r ^ state) >> 2) / c) | r;
		}
		return set;
	}

	private static boolean valid(int state, Set<Integer> set) {
		if((state & 48) == 0) return false;
		if(set.contains(reverse(state))) return false;
		return tryToCreate(state, 0) == 0 || tryToCreate(state, 1) == 0;
	}
	
	private static int tryToCreate(int state, int i) {
		if(i < 0 || i > 5 || (state & (1 << i)) == 0) return state;
		state ^= 1 << i;
		state = tryToCreate(state, i + 2);
		if(i % 2 != 1) state = tryToCreate(state, i + 1);
		state = tryToCreate(state, i - 2);
		if(i % 2 != 0) state = tryToCreate(state, i - 1);
		return state;
	}

	private static int reverse(int x) {
        int b = 0;
        while (x != 0) {
            b <<= 1;
            b |= (x & 1);
            x >>= 1;
        }
        return b;
    }
}

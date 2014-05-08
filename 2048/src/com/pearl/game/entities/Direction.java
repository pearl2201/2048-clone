package com.pearl.game.entities;

public enum Direction {
	UP(-1, 0, "up"), DOWN(1, 0, "down"), LEFT(0, -1, "left"), RIGHT(0, 1, "right");
	/*
	 * board 
	 * 
	 * (0,0) (0,1) (0,2)...
	 * (1,0) (1,1) (1,2)...
	 * ....
	 * 
	 * screen
	 * 
	 * ...
	 * (1,0) (1,1) (1,2)...
	 * (0,0( (0,1) (0,2)...
	 */

	private int x;
	private int y;
	private String name;

	public String getName() {
		return name;
	}

	Direction(int x, int y, String name) {
		this.x = x;
		this.y = y;
		this.name = name;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}

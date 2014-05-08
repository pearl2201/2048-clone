package com.pearl.game.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.pearl.game.utils.Constants;
import com.pearl.game.utils.DrawCell;

public class Cell extends Actor {

	private int value;
	//coordinate in screen 
	private int screenX;
	private int screenY;
	// coordinate in board
	private int boardX;
	private int boardY;

	public Cell(int value, int boardX, int boardY) {
		super();
		this.value = value;
		convertBoardToScreen(boardX, boardY);
		setPosition(screenX, screenY);
		setSize(Constants.CELL_WIDTH, Constants.CELL_WIDTH);
		setScale(0.8f);
		addAction(Actions.scaleTo(1, 1, 0.035f));
	}

	public void moveTo(int boardX, int boardY, int duration) {
		convertBoardToScreen(boardX, boardY);
		this.addAction(Actions.moveTo(screenX, screenY, 0.1f / duration, Interpolation.linear));
	}

	public boolean hasMoved() {
		return (screenX == getX() && screenY == getY());
	}

	private void convertBoardToScreen(int boardX, int boardY) {
		this.boardX = boardX;
		this.boardY = boardY;
		screenX = Constants.BOARD_POS_BOT + Constants.BOARD_GRID_WIDTH * (this.boardY + 1) + this.boardY * Constants.CELL_WIDTH;
		screenY = Constants.BOARD_POS_BOT  + Constants.BOARD_GRID_WIDTH * (Constants.BOARD_COL - this.boardX) + (Constants.BOARD_COL -1 - this.boardX) * Constants.CELL_WIDTH;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getBoardX() {
		return boardX;
	}

	public void setBoardX(int boardX) {
		this.boardX = boardX;
	}

	public int getBoardY() {
		return boardY;
	}

	public void setBoardY(int boardY) {
		this.boardY = boardY;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		// TODO Auto-generated method stub
		DrawCell.instance.draw(value, getX(), getY(), batch, getScaleX());
	}

	public void doubleValue() {
		value *= 2;
		addAction(Actions.sequence(Actions.scaleTo(1.12f, 1.12f, 0.035f), Actions.scaleTo(1, 1, 0.035f)));
	}

}

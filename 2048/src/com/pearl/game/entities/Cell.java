package com.pearl.game.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.pearl.game.utils.DrawCell;

public class Cell extends Actor {

	private int value;
	private int coorX;
	private int coorY;
	private int boardX;
	private int boardY;

	public Cell(int value, int boardX, int boardY) {
		super();
		this.value = value;
		convertBoardToCoor(boardX, boardY);
		setPosition(coorX, coorY);
		setSize(90, 90);
		setScale(0.8f);
		addAction(Actions.scaleTo(1, 1, 0.035f));
	}

	public void moveTo(int boardX, int boardY, int duration) {
		convertBoardToCoor(boardX, boardY);
		this.addAction(Actions.moveTo(coorX, coorY, 0.1f / duration, Interpolation.linear));
	}

	public boolean hasMoved() {
		return (coorX == getX() && coorY == getY());
	}

	private void convertBoardToCoor(int boardX, int boardY) {
		this.boardX = boardX;
		this.boardY = boardY;
		coorX = 30 + 12 * (this.boardY + 1) + this.boardY * 90;
		coorY = 30 + 12 * (4 - this.boardX) + (3 - this.boardX) * 90;
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

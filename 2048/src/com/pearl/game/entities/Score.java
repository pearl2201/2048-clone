package com.pearl.game.entities;

import com.pearl.game.utils.Prefs;

public class Score {

	public static Score instance = new Score();

	private int score;
	private int best;

	public Score() {
		score = Prefs.instance.score;
		best = Prefs.instance.best;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
		if (score>best)
		{
			best = score;
			
		}
	}

	public void addScore(int score) {
		this.score += score;
		this.best = (this.score > best) ? this.score : best;
	}

	public int getBest() {
		return best;
	}
}

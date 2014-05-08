package com.pearl.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.pearl.game.entities.Score;

public class Prefs {
	public static Prefs instance = new Prefs();
	private static Preferences pref;
	public int score;
	public int best;
	public boolean isLaunchNewGame;
	public String board;

	private Prefs() {
		pref = Gdx.app.getPreferences("pearl-2048");

		// create pref if not exit
		if (!pref.contains("score")) {
			pref.putInteger("score", 0);
			pref.putInteger("best", 0);
			pref.putBoolean("isFirstLaunchGame", true);
			pref.putString("board", "");
			pref.flush();
		}
		load();
	}

	private void load() {
		score = pref.getInteger("score", 0);
		best = pref.getInteger("best", 0);
		isLaunchNewGame = pref.getBoolean("isFirstLaunchGame", true);
		board = pref.getString("board", "");
		// isLaunchNewGame = true;
	}

	public void saveScore() {
		pref.putInteger("score", Score.instance.getScore());
		pref.putInteger("best", Score.instance.getBest());
		pref.flush();
	}

	public void saveBoard(String board, boolean isLauchNewGame) {
		if (!isLauchNewGame) {
			pref.putString("board", board);

			Gdx.app.log("Save Board1", board);
		}

		pref.putBoolean("isFirstLaunchGame", isLauchNewGame);
		pref.flush();

	}
}

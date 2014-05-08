package com.pearl.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.pearl.game.assets.Assets;
import com.pearl.game.screen.GameScreen;
import com.pearl.game.utils.DrawCell;

public class Pearl extends Game {

	@Override
	public void create() {
		// TODO Auto-generated method stub
		Assets.instance.load(new AssetManager());
		DrawCell.instance.init();
		setScreen(new GameScreen(this));

	}

}

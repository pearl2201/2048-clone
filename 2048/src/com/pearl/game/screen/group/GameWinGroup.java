package com.pearl.game.screen.group;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.pearl.game.assets.Assets;
import com.pearl.game.screen.GameScreen;

public class GameWinGroup extends Group {

	private GameScreen gameScreen;

	public GameWinGroup(GameScreen gameScreen) {
		// TODO Auto-generated constructor stub
		super();
		this.gameScreen = gameScreen;
		init();
	}

	private void init() {
		// Don't ask for why it has this coordinate =))
		{
			Actor tryagainActor = new Actor();
			tryagainActor.setBounds(100, 200, Assets.instance.tryAgainL.getRegionWidth(), Assets.instance.tryAgainL.getRegionHeight());
			;
			tryagainActor.addListener(new ClickListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					// TODO Auto-generated method stub
					Gdx.app.log("has been touch", "");
					gameScreen.restartGame();
					gameScreen.getStage().getRoot().removeActor(gameScreen.getGameWinGroup());
					return super.touchDown(event, x, y, pointer, button);
				}
			});
			addActor(tryagainActor);
		}

		{
			Actor exitActor = new Actor();
			exitActor.setBounds(272, 200, Assets.instance.tryAgainL.getRegionWidth(), Assets.instance.tryAgainL.getRegionHeight());
			;
			exitActor.addListener(new ClickListener() {

				@Override
				public void clicked(InputEvent event, float x, float y) {
					// TODO Auto-generated method stub
					// gameScreen.getGame().dispose();
					Gdx.app.exit();
				}
			});
			addActor(exitActor);
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		// TODO Auto-generated method stub
		batch.draw(Assets.instance.opacityLayer, 0, 0);
		Assets.instance.font.setColor(Color.BLACK);
		Assets.instance.font.setScale(1.5f);
		// Don't ask for why it has this coordinate =))
		Assets.instance.font.draw(batch, "Won", 240 - Assets.instance.font.getBounds("Won").width / 2, 400);
		batch.draw(Assets.instance.tryAgainL, 100, 200);
		batch.draw(Assets.instance.exitL, 272, 200);
	}

}

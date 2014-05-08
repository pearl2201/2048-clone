package com.pearl.game.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.pearl.game.utils.Constants;

public class Assets implements Disposable{
	
	public static Assets instance = new Assets();
	
	private AssetManager assetM;
	public TextureRegion backgroundL;
	public TextureRegion exitL;
	public TextureRegion gameOverL;
	public TextureRegion opacityLayer;
	public TextureRegion restartL;
	public TextureRegion tryAgainL;
	public TextureRegion newL;
	public BitmapFont font;
	
	public void load(AssetManager assetM)
	{
		this.assetM = assetM;
		assetM.load(Constants.GAME_TEXTURE_ATLAS_FILE, TextureAtlas.class);
		assetM.load(Constants.GAME_BITMAPFONT_FILE, BitmapFont.class);
		assetM.finishLoading();
		
		
		//Filter
		TextureAtlas atlas = assetM.get(Constants.GAME_TEXTURE_ATLAS_FILE, TextureAtlas.class);
		for (Texture texture: atlas.getTextures())
		{
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		
		backgroundL = atlas.findRegion("background");
		exitL = atlas.findRegion("exit");
		gameOverL =atlas.findRegion("game-over");
		opacityLayer = atlas.findRegion("opacity");
		restartL = atlas.findRegion("restart");
		tryAgainL = atlas.findRegion("try-again");
		newL = atlas.findRegion("new");
		
		font = assetM.get(Constants.GAME_BITMAPFONT_FILE, BitmapFont.class);
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
				
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		font.dispose();
		assetM.dispose();
	}

}

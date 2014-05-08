package com.pearl.game.utils;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Disableable;
import com.badlogic.gdx.utils.Disposable;
import com.pearl.game.assets.Assets;

public class DrawSquare implements Disposable {

	class Square {

		private int key;
		private TextureRegion smallTexture;
		private Color color;
		private float scale;

		public Square(int key, int r, int g, int b) {
			this.key = key;
			this.color = new Color(((float) r) / 255f, ((float) g) / 255f, ((float) b) / 255f, 1.0f);
			if (Integer.toString(key).length() <= 4)
				this.scale = 1;
			else
				this.scale = 4f / Integer.toString(key).length();

			this.smallTexture = createBackground(color);
		}

		public Square(int key, float r, float g, float b) {
			this.key = key;
			this.color = new Color(r, g, b, 1.0f);
			if (Integer.toString(key).length() <= 4)
				this.scale = 1;
			else
				this.scale = 4f / Integer.toString(key).length();
			this.smallTexture = createBackground(color);
		}

		public Square(int key, Color color) {
			this.key = key;
			this.color = color;
			if (Integer.toString(key).length() <= 4)
				this.scale = 1;
			else
				this.scale = 4f / Integer.toString(key).length();

			this.smallTexture = createBackground(color);
		}

		private TextureRegion createBackground(Color color) {

			if (key >= 128) {

				Pixmap pixmap = new Pixmap(90, 90, Format.RGBA8888);
				pixmap.setColor(Color.WHITE);
				pixmap.drawRectangle(0, 0, 90, 90);
				pixmap.setColor(color);
				pixmap.fillRectangle(1, 1, 88, 88);
				// pixmap.setColor(color.r, color.g, color.b, 0.6f);
				// pixmap.fillRectangle(0, 0, 100, 100);

				Texture texture = new Texture(pixmap);
				texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
				TextureRegion region = new TextureRegion(texture);
				// texture.dispose();
				pixmap.dispose();
				return region;
			} else {
				Pixmap pixmap = new Pixmap(90, 90, Format.RGBA8888);

				pixmap.setColor(color);
				pixmap.fillRectangle(0, 0, 90, 90);

				Texture texture = new Texture(pixmap);
				TextureRegion region = new TextureRegion(texture);
				// texture.dispose();
				pixmap.dispose();
				return region;
			}
		}

		public int getKey() {
			return key;
		}

		public void setKey(int key) {
			this.key = key;
		}

		public TextureRegion getTexture() {
			return smallTexture;
		}

		public void setTexture(TextureRegion texture) {
			this.smallTexture = texture;
		}

		public Color getColor() {
			return color;
		}

		public void setColor(Color color) {
			this.color = color;
		}

		public float getScale() {
			return scale;
		}

		public void setScale(float scale) {
			this.scale = scale;
		}
	}

	public static DrawSquare instance = new DrawSquare();
	private Map<Integer, Square> squares;

	private DrawSquare() {

	}

	public void init() {

		squares = new HashMap<Integer, Square>();

		squares.put(2, new Square(2, 238, 228, 218));
		squares.put(4, new Square(4, 237, 205, 123));
		squares.put(8, new Square(8, 255, 131, 30));
		squares.put(16, new Square(16, 255, 129, 8));
		squares.put(32, new Square(32, 237, 88, 52));
		squares.put(64, new Square(64, 217, 54, 17));
		squares.put(128, new Square(128, 255, 220, 102));
		squares.put(256, new Square(256, 255, 215, 80));
		squares.put(512, new Square(512, 255, 210, 60));
		squares.put(1024, new Square(1024, 255, 211, 1));
		squares.put(2048, new Square(2048, 255, 195, 0));
	}

	public void draw(int key, float x, float y, Batch batch, float f) {

		if (!squares.containsKey(key)) {

			Color lastColor = squares.get(key / 2).getColor();
			lastColor.r = lastColor.r + (1 - lastColor.r) * 0.15f;
			lastColor.g = lastColor.g + (1 - lastColor.g) * 0.15f;
			lastColor.b = lastColor.b - (lastColor.b - 0) * 0.15f;
			squares.put(key, new Square(key, lastColor));
		}

		Assets.instance.font.setScale(squares.get(key).getScale() * f);

		batch.draw(squares.get(key).getTexture(), x , y , 45f, 45f, 90f, 90f, f, f, 0);
		x = x + 45 - Assets.instance.font.getBounds(Integer.toString(key)).width / 2;
		y = y + 45 + Assets.instance.font.getBounds("9985").height + 40 * squares.get(key).getScale() * f;
		if (key > 4)
			Assets.instance.font.setColor(Color.WHITE);
		else
			Assets.instance.font.setColor(Color.GRAY);
		Assets.instance.font.draw(batch, Integer.toString(key), x, y);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		int d = 2;
		while (squares.containsKey(d)) {
			squares.get(d).getTexture().getTexture().dispose();
			squares.remove(d);
			d *= 2;
		}
	}

}

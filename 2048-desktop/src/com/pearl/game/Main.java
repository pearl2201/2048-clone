package com.pearl.game;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "2048";
		cfg.width = 480;
		cfg.height = 640;
		
		new LwjglApplication(new Pearl(), cfg);
	}
}

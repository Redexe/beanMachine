package com.redexe.games;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.ArrayMap;

import engine.AndroidHelper;
import managers.ScreenManager;
import screens.MainMenu;

public class Booter extends Game {
	private final AndroidHelper androidHelper;

	public Booter(AndroidHelper androidHelper) {
		this.androidHelper = androidHelper;
	}

    @Override
	public void create () {
		ScreenManager manager = ScreenManager.getInstance();
		ScreenManager.init(this,androidHelper);

		ArrayMap<String, Class<?>> data = new ArrayMap<String, Class<?>>();
		manager.loadScreen(data,new MainMenu());
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
	}
}

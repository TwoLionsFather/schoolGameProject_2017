package com.JanTlk.BesseresHearthstone.secondTry;

public class TextureLoader
{
	private GameDataContainer game;

	public TextureLoader(GameDataContainer gameSetup)
	{
		this.game = gameSetup;
		loadBackground();
		createCardImages();
	}

}

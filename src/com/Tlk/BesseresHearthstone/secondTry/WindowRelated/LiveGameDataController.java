package com.Tlk.BesseresHearthstone.secondTry.WindowRelated;

import com.Tlk.BesseresHearthstone.secondTry.MainGameClass.STATE;
import com.Tlk.BesseresHearthstone.secondTry.Startup.GameDataContainer;

public class LiveGameDataController implements GameDataContainer, GameStateController
{
	private final String TITLE = "Gwent";
	private final double WIDTH;
	private final double HEIGHT;
	private STATE gameState;
	private final boolean debugMode;
	private final boolean easyMode;

	public LiveGameDataController(GameDataContainer gameSetup)
	{
		this.WIDTH = gameSetup.getWIDTH();
		this.HEIGHT = gameSetup.getHEIGHT();
		this.gameState = gameSetup.getGameState();
		this.debugMode = gameSetup.isDebugMode();
		this.easyMode = gameSetup.isEasyMode();
	}

	@Override
	public double getWIDTH()
	{
		return WIDTH;
	}

	@Override
	public double getHEIGHT()
	{
		return HEIGHT;
	}

	@Override
	public boolean isDebugMode()
	{
		return debugMode;
	}

	@Override
	public boolean isEasyMode()
	{
		return easyMode;
	}

	@Override
	public String getTITLE()
	{
		return this.TITLE;
	}

	@Override
	public void setGameState(STATE gameState)
	{
		this.gameState = gameState;
		SceneController.getSceneController().update();
	}

	@Override
	public STATE getGameState()
	{
		return gameState;
	}

}

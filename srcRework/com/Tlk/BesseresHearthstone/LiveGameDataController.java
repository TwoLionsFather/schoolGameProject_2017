package com.Tlk.BesseresHearthstone;

import com.Tlk.BesseresHearthstone.MainGameClass.STATE;

import java.awt.Dimension;
import java.awt.Toolkit;

import com.Tlk.BesseresHearthstone.ErrorHandling.ErrorHandler;
import com.Tlk.BesseresHearthstone.WindowRelated.SceneController;

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

	public boolean isLargeSize()
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		return (this.WIDTH >= screenSize.getWidth()) && (this.HEIGHT >= screenSize.getHeight());
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
	public void setGameState(STATE newState)
	{
		try {
			SceneController.getSceneController().update(this.gameState, newState);
			this.gameState = newState;
		} catch (Exception e) {
			ErrorHandler.displayErrorMessage(e.getMessage());
		}

	}

	@Override
	public STATE getGameState()
	{
		return gameState;
	}

}

package com.Tlk.BesseresHearthstone.secondTry.WindowRelated;

import com.Tlk.BesseresHearthstone.secondTry.MainGameClass.STATE;

public interface GameStateController
{
	public void setGameState(STATE gameState);
	public STATE getGameState();
}

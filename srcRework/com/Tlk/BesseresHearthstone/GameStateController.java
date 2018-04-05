package com.Tlk.BesseresHearthstone;

import com.Tlk.BesseresHearthstone.MainGameClass.STATE;

public interface GameStateController
{
	public void setGameState(STATE gameState);
	public STATE getGameState();
}

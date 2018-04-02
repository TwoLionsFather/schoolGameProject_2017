package com.Tlk.BesseresHearthstone.secondTry.Startup;

import com.Tlk.BesseresHearthstone.secondTry.MainGameClass.STATE;

public interface GameDataContainer
{
	public String getTITLE();
	public double getWIDTH();
	public double getHEIGHT();
	public STATE getGameState();
	public boolean isDebugMode();
	public boolean isEasyMode();
}

package com.Tlk.BesseresHearthstone;

import com.Tlk.BesseresHearthstone.MainGameClass.STATE;

public interface GameDataContainer
{
	public String getTITLE();
	public double getWIDTH();
	public double getHEIGHT();
	public STATE getGameState();
	public boolean isDebugMode();
	public boolean isEasyMode();
}

package com.Tlk.BesseresHearthstone.secondTry.Startup;

import com.Tlk.BesseresHearthstone.secondTry.MainGameClass.STATE;

public interface GameDataContainer
{
	public double getWIDTH();
	public double getHEIGHT();
	public STATE getGAMESTATE();
	public boolean isDebugMode();
	public boolean isEasyMode();
}

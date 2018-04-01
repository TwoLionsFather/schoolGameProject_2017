package com.JanTlk.BesseresHearthstone.secondTry;

import com.JanTlk.BesseresHearthstone.secondTry.MainGameClass.STATE;

public interface GameDataContainer
{
	public double getWIDTH();
	public double getHEIGHT();
	public STATE getGAMESTATE();
	public boolean isDebugMode();
	public boolean isEasyMode();
}

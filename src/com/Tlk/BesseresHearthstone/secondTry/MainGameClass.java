package com.Tlk.BesseresHearthstone.secondTry;

import com.Tlk.BesseresHearthstone.secondTry.Startup.SetupController;

public class MainGameClass
{
	public enum STATE
	{
		MENU()
		, END()
		, GAME()
		, HELP()
		, BEATEN()
		, RESETGAME();
	}

	public MainGameClass()
	{
		new SetupController();
	}

	public static void main(String[] args)
	{
		new MainGameClass();
	}
}

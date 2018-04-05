package com.Tlk.BesseresHearthstone.secondTry.WindowRelated;

import com.Tlk.BesseresHearthstone.secondTry.MainGameClass.STATE;

public class SceneNotChangedException extends Exception
{
	private static final long serialVersionUID = 2606503144559454523L;

	public SceneNotChangedException() {
		super("State hasn`t changed");
	}

	public SceneNotChangedException(STATE unloadedState)
	{
		super("The Scene for Game State: " + unloadedState.toString() + " has not been loaded");
	}

}

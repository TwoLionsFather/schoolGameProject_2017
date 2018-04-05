package com.Tlk.BesseresHearthstone.secondTry.Startup;

import com.Tlk.BesseresHearthstone.secondTry.MainGameClass.STATE;
import com.Tlk.BesseresHearthstone.secondTry.WindowRelated.LiveGameDataController;
import com.Tlk.BesseresHearthstone.secondTry.WindowRelated.SceneContainer;

public class GameUI extends SceneContainer
{

	public GameUI(LiveGameDataController liveGameData)
	{
		this.linkWithState(STATE.GAME);
	}

}

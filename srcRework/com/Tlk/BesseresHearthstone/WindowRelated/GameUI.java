package com.Tlk.BesseresHearthstone.WindowRelated;

import com.Tlk.BesseresHearthstone.LiveGameDataController;
import com.Tlk.BesseresHearthstone.MainGameClass.STATE;

public class GameUI extends SceneContainer
{

	public GameUI(LiveGameDataController liveGameData)
	{
		this.linkWithState(STATE.GAME);
	}

}

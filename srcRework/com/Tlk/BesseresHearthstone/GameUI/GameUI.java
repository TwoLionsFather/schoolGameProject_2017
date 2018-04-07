package com.Tlk.BesseresHearthstone.GameUI;

import com.Tlk.BesseresHearthstone.LiveGameDataController;
import com.Tlk.BesseresHearthstone.MainGameClass.STATE;
import com.Tlk.BesseresHearthstone.WindowRelated.SceneContainer;

public class GameUI extends SceneContainer
{

	public GameUI(LiveGameDataController liveGameData)
	{
		this.linkWithState(STATE.GAME);
	}

}

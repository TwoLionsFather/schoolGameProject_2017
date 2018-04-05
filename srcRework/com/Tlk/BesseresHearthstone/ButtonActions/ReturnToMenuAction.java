package com.Tlk.BesseresHearthstone.ButtonActions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.Tlk.BesseresHearthstone.GameStateController;
import com.Tlk.BesseresHearthstone.MainGameClass.STATE;

public class ReturnToMenuAction extends  AbstractAction
{
	private static final long serialVersionUID = 1L;
	private GameStateController stateController;

	public ReturnToMenuAction(GameStateController liveGameData)
	{
		this.stateController = liveGameData;
		this.putValue(NAME, "Return to Menu");
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		stateController.setGameState(STATE.MENU);
	}

}

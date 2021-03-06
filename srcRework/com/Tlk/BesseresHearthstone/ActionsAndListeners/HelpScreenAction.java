package com.Tlk.BesseresHearthstone.ActionsAndListeners;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.Tlk.BesseresHearthstone.GameStateController;
import com.Tlk.BesseresHearthstone.MainGameClass.STATE;

public class HelpScreenAction extends AbstractAction
{
	private static final long serialVersionUID = -2175910140063436578L;
	private GameStateController stateController;

	public HelpScreenAction(GameStateController liveGameData)
	{
		this.stateController = liveGameData;
		this.putValue(SHORT_DESCRIPTION, "Show Help");
		this.putValue(NAME, "Help");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		stateController.setGameState(STATE.HELP);
	}

}

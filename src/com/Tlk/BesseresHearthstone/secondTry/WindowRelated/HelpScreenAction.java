package com.Tlk.BesseresHearthstone.secondTry.WindowRelated;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.Tlk.BesseresHearthstone.secondTry.MainGameClass.STATE;

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

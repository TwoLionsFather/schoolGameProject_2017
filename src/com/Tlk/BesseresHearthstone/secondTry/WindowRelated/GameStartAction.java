package com.Tlk.BesseresHearthstone.secondTry.WindowRelated;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import com.Tlk.BesseresHearthstone.secondTry.MainGameClass.STATE;
import com.Tlk.BesseresHearthstone.secondTry.TextureController;

public class GameStartAction extends AbstractAction
{
	private static final long serialVersionUID = 1L;
	private GameStateController stateController;

	public GameStartAction(GameStateController liveGameData)
	{
		this.stateController = liveGameData;
		this.putValue(SHORT_DESCRIPTION, "Starts Game");
		this.putValue(NAME, "Start New Game");
		this.putValue(SMALL_ICON, new ImageIcon(TextureController.getTexture("GameIcon.png")));
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		stateController.setGameState(STATE.GAME);
	}

}

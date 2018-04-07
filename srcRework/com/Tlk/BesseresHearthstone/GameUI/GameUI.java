package com.Tlk.BesseresHearthstone.GameUI;

import javax.swing.JButton;

import com.Tlk.BesseresHearthstone.GameController;
import com.Tlk.BesseresHearthstone.MainGameClass.STATE;
import com.Tlk.BesseresHearthstone.ActionsAndListeners.AcceptTurnAction;
import com.Tlk.BesseresHearthstone.CardRelated.Card;
import com.Tlk.BesseresHearthstone.WindowRelated.SceneContainer;

public class GameUI extends SceneContainer implements UserInterface
{

	private GameController controller;
	public GameUI(GameController controller)
	{
		this.linkWithState(STATE.GAME);

		JButton nextTurn = new JButton();
		nextTurn.setLocation(10, 10);
		nextTurn.setAction(new AcceptTurnAction(this));

		this.getPanel().add(nextTurn);
	}

	@Override
	public void setFocusedCard(Card focusedCard)
	{

	}

	@Override
	public void unsetFocusedCard()
	{

	}

}

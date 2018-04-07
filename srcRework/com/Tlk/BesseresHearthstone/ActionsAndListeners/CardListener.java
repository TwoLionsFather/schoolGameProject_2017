package com.Tlk.BesseresHearthstone.ActionsAndListeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.Tlk.BesseresHearthstone.CardRelated.Card;
import com.Tlk.BesseresHearthstone.GameUI.GameUI;
import com.Tlk.BesseresHearthstone.GameUI.UserInterface;

public class CardListener implements MouseListener
{
	private final Card card;
	private final UserInterface ui;

	public CardListener(Card card, GameUI ui)
	{
		this.card = card;
		this.ui = ui;
	}

	@Override
	public void mouseClicked(MouseEvent arg0)
	{
		this.ui.setFocusedCard(this.card);
	}

	@Override
	public void mouseEntered(MouseEvent arg0)
	{

	}

	@Override
	public void mouseExited(MouseEvent arg0)
	{

	}

	@Override
	public void mousePressed(MouseEvent arg0)
	{

	}

	@Override
	public void mouseReleased(MouseEvent arg0)
	{

	}

}

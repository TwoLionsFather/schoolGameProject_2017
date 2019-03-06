package com.Tlk.BesseresHearthstone.GameUI;

import javax.swing.JPanel;

import com.Tlk.BesseresHearthstone.ActionsAndListeners.CardListener;
import com.Tlk.BesseresHearthstone.CardRelated.Card;

public class CardHolder
{
	private Card card;
	private JPanel display;

	public CardHolder()
	{
		this.display = new JPanel();
	}

	public Card getCard()
	{
		return card;
	}

	public void setCard(Card card)
	{
		CardDisplayRepresentation cardRepresentation = new CardDisplayRepresentation(card);
		this.display = cardRepresentation.getCardDisplay();
		this.display.addMouseListener(new CardListener(card));
	}

	public void removeCard()
	{
		this.card = null;
		this.display = new JPanel();
	}

	public JPanel getDisplay()
	{
		return display;
	}
}

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

	public void setCard(Card card, FieldRepresentation field)
	{
		CardRepresentation cardRepresentation = new CardRepresentation(card);
		this.display = cardRepresentation.getCardDisplay();
		this.display.addMouseListener(new CardListener(card));
	}

	public JPanel getDisplay()
	{
		return display;
	}
}

package com.Tlk.BesseresHearthstone.CardRelated;

import java.util.ArrayList;

import com.Tlk.BesseresHearthstone.Player;

public class CardLocationController
{
	private CardLocations cardLocations;

	public CardLocationController(Player player)
	{
		this.cardLocations = new CardLocations();

		for(Card tempCard : player.getDeck().getCards())
			this.cardLocations.moveToStack(tempCard);
	}

	public void resetCards(Deck deck)
	{
		for(Card tempCard : deck.getCards())
		{
			this.cardLocations.moveToStack(tempCard);
			tempCard.resetCard();
		}
	}

	public void drawCard()
	{
		Card drawnCard = this.cardLocations.getStack().get(0);
		this.cardLocations.moveToHand(drawnCard);
	}

	public void killCard(Card card)
	{
		this.cardLocations.moveToGrave(card);
	}

	public void playCard(Card playedCard)
	{
		this.cardLocations.moveToField(playedCard);
	}

	public int getGraveSize()
	{
		return this.cardLocations.getGrave().size();
	}

	public int getStackSize()
	{
		return this.cardLocations.getStack().size();
	}

	public ArrayList<Card> getCardOnHand()
	{
		return this.cardLocations.getHand();
	}

	public ArrayList<Card> getCardOnField()
	{
		return this.cardLocations.getField();
	}

}

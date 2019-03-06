package com.Tlk.BesseresHearthstone;

import java.util.ArrayList;

import com.Tlk.BesseresHearthstone.CardRelated.Card;
import com.Tlk.BesseresHearthstone.CardRelated.CardLocationController;
import com.Tlk.BesseresHearthstone.CardRelated.Deck;

public class Player
{
	private final String name;
	private final CardLocationController cardController;
	private Deck deck;
	private int mana;
	private int life;

	public Player (String name, Deck deck)
	{
		this.deck = deck;
		this.name = name;
		this.cardController = new CardLocationController(this);
		this.mana = 0;
		this.life = 20;
	}

	public void playCard(Card playedCard)
	{
		this.cardController.playCard(playedCard);
	}

	public void drawCard()
	{
		this.cardController.drawCard();
	}

	public void checkCard(Card card)
	{
		if (card.getLife() <= 0)
			this.killCard(card);
	}

	private void killCard(Card card)
	{
		this.cardController.killCard(card);
	}

	public void reset()
	{
		this.mana = 0;
		this.life = 20;
		this.deck.shuffle();
		this.cardController.resetCards(this.deck);
	}

	public ArrayList<Card> getCardsOnField()
	{
		return this.cardController.getCardOnField();
	}

	public ArrayList<Card> getCardsOnHand()
	{
		return this.cardController.getCardOnHand();
	}

	public int getSizeOfGrave()
	{
		return this.cardController.getGraveSize();
	}

	public int getSizeOfStack()
	{
		return this.cardController.getStackSize();
	}

	public int getMana()
	{
		return mana;
	}

	public void setMana(int mana)
	{
		this.mana = mana;
	}

	public int getLife()
	{
		return life;
	}

	public void setLife(int life)
	{
		this.life = life;
	}

	public Deck getDeck()
	{
		return deck;
	}

	public void setDeck(Deck deck)
	{
		this.deck = deck;
	}

	public String getName()
	{
		return name;
	}
}

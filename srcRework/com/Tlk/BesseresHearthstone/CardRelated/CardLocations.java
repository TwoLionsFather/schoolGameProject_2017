package com.Tlk.BesseresHearthstone.CardRelated;

import java.util.ArrayList;

public class CardLocations
{
	private final ArrayList<Card> stack = new ArrayList<Card>();
	private final ArrayList<Card> hand = new ArrayList<Card>();
	private final ArrayList<Card> field = new ArrayList<Card>();
	private final ArrayList<Card> grave = new ArrayList<Card>();

	public void moveToStack(Card card)
	{
		if (card.getCurrentLocation() == stack)
			return;
		if (card.getCurrentLocation() != null)
			card.getCurrentLocation().remove(card);

		card.setCurrentLocation(stack);
		stack.add(card);
	}

	public ArrayList<Card> getStack()
	{
		return stack;
	}

	public void moveToHand(Card card)
	{
		if (card.getCurrentLocation() == hand)
			return;
		if (card.getCurrentLocation() != null)
			card.getCurrentLocation().remove(card);

		card.setCurrentLocation(hand);
		hand.add(card);
	}

	public ArrayList<Card> getHand()
	{
		return hand;
	}

	public void moveToField(Card card)
	{
		if (card.getCurrentLocation() == field)
			return;
		if (card.getCurrentLocation() != null)
			card.getCurrentLocation().remove(card);

		card.setCurrentLocation(field);
		field.add(card);
	}

	public ArrayList<Card> getField()
	{
		return field;
	}

	public void moveToGrave(Card card)
	{
		if (card.getCurrentLocation() == grave)
			return;
		if (card.getCurrentLocation() != null)
			card.getCurrentLocation().remove(card);

		card.setCurrentLocation(grave);
		grave.add(card);
	}

	public ArrayList<Card> getGrave()
	{
		return grave;
	}

}

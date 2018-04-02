package com.Tlk.BesseresHearthstone.secondTry.CardRelated;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

public class Deck
{
	private ArrayList<Card> cards;

	public Deck (ArrayList<Card> cards)
	{
		this.cards = cards;
	}

	public void shuffle()
	{
		Random r = new SecureRandom();
		ArrayList<Card> shuffeled = new ArrayList<Card>();

		int initLength = cards.size();
		ArrayList<Double> posValue = new ArrayList<Double>(initLength);

		//__Preshuffle__
		//every Card gets assigned a value based on its mana and some random number, lower mana -> higher score
		for (Card tempCard : cards)
		{
			//the double gets added to improve randomness among cards with the same mana
			posValue.add(1 / (tempCard.getMana()  + r.nextInt(7)) + r.nextDouble());
		}

		do {
			//the highest valued card gets added first
			int idxHigh = 0;
			for (int idx = 1; idx < posValue.size(); idx++)
			{
				if (posValue.get(idx) > posValue.get(idxHigh))
				{
					idxHigh = idx;
				}
			}

//			idx out of bounds ErrorMESSAGEErrorMESSAGEErrorMESSAGEErrorMESSAGEErrorMESSAGEErrorMESSAGEErrorMESSAGEErrorMESSAGEErrorMESSAGEErrorMESSAGEErrorMESSAGEErrorMESSAGEErrorMESSAGEErrorMESSAGEErrorMESSAGEErrorMESSAGEErrorMESSAGEErrorMESSAGE
			shuffeled.add(cards.get(idxHigh));
			cards.remove(idxHigh);
			posValue.remove(idxHigh);
		} while (shuffeled.size() < initLength * 0.15);	//15% of the deck get stacked that way


		//the rest gets shuffled randomly
		for(int counter = 0; counter < cards.size(); counter++)
		{
			int randomIdx = r.nextInt(cards.size());
			shuffeled.add(cards.get(randomIdx));
			cards.remove(randomIdx);
		}

		this.cards = shuffeled;
	}

	public ArrayList<Card> getCards()
	{
		return this.cards;
	}

}

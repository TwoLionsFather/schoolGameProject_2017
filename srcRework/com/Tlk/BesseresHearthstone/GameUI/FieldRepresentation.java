package com.Tlk.BesseresHearthstone.GameUI;

import java.util.ArrayList;

import com.Tlk.BesseresHearthstone.GameController;
import com.Tlk.BesseresHearthstone.CardRelated.Card;
import com.Tlk.BesseresHearthstone.ErrorHandling.ErrorHandler;
import com.Tlk.BesseresHearthstone.ErrorHandling.NoFreeSpaceException;

public class FieldRepresentation
{
	private CardHolder[] cardsOne;
	private CardHolder[] cardsTwo;

	public FieldRepresentation(ArrayList<Card> cardsOne, ArrayList<Card> cardsTwo, GameController controller)
	{
		this.cardsOne = new CardHolder[5];
		this.cardsTwo = new CardHolder[this.cardsOne.length];

		for(CardHolder tempCardHolder : this.cardsOne)
		{
			tempCardHolder.getDisplay().setSize();
		}


		for(Card tempCard : cardsOne)
		{
			try {
				this.cardsOne[this.getFreeLocation(this.cardsOne)].setCard(tempCard, this);
			} catch(NoFreeSpaceException e) {
				ErrorHandler.displayErrorMessage("No fieldspace for card: " + tempCard.toString() + " -> Card lost!");
				break;
			}
		}

		for(Card tempCard : cardsTwo)
		{
			try {
				this.cardsTwo[this.getFreeLocation(this.cardsTwo)].setCard(tempCard, this);
			} catch(NoFreeSpaceException e) {
				ErrorHandler.displayErrorMessage("No fieldspace for card: " + tempCard.toString() + " -> Card lost!");
				break;
			}
		}
	}

	private int getFreeLocation(CardHolder[] cardHolder) throws NoFreeSpaceException
	{
		int freeID = cardHolder.length / 2;
		int circle = 0;

		while(cardHolder[freeID].getCard() != null)
		{
			circle *= -1;
			freeID = freeID + ((circle < 0) ? --circle : ++circle);

			if(freeID >= cardHolder.length)
				throw new NoFreeSpaceException();
		}

		return freeID;
	}

}

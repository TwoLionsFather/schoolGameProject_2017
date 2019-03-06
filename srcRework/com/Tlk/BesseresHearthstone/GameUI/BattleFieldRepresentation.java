package com.Tlk.BesseresHearthstone.GameUI;

import com.Tlk.BesseresHearthstone.GameController;
import com.Tlk.BesseresHearthstone.Player;
import com.Tlk.BesseresHearthstone.CardRelated.Card;
import com.Tlk.BesseresHearthstone.ErrorHandling.NoFreeSpaceException;

public class BattleFieldRepresentation
{
	private final int AMMOUNT_OF_HORIZONTAL_CARDS = 5;

	private CardHolder[] cardsOne;
	private CardHolder[] cardsTwo;
	private Player p1;
	private Player p2;


	public BattleFieldRepresentation(GameController controller)
	{
		this.cardsOne = new CardHolder[AMMOUNT_OF_HORIZONTAL_CARDS];
		this.cardsTwo = new CardHolder[AMMOUNT_OF_HORIZONTAL_CARDS];

		for(int i = 0; i < AMMOUNT_OF_HORIZONTAL_CARDS; i++)
		{
			this.cardsOne[i] = new CardHolder();
			this.cardsTwo[i] = new CardHolder();
		}

		this.p1 = controller.getP1();
		this.p2 = controller.getP2();
	}

	public void addCardForPlayer(Card card, Player player) throws NoFreeSpaceException
	{
		if(player.equals(p1))
		{
			this.cardsOne[getFreeLocation(p1)].setCard(card);
		}

		else if(player.equals(p2))
		{
			this.cardsTwo[getFreeLocation(p2)].setCard(card);
		}

	}

	private int getFreeLocation(Player player) throws NoFreeSpaceException
	{
		CardHolder[] cardHolder;

		if(player.equals(p1))
			cardHolder = this.cardsOne;
		else
			cardHolder = this.cardsTwo;


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

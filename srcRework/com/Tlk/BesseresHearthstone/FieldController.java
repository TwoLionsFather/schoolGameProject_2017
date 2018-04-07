package com.Tlk.BesseresHearthstone;

import com.Tlk.BesseresHearthstone.CardRelated.Card;

public class FieldController
{
	private GameController controller;

	public FieldController(GameController controller)
	{
		this.controller = controller;
	}

	public void attackCard(Card cardAttack, Card cardDefend)
	{
		if(cardDefend == null)
		{
			this.controller.attackPlayer(cardAttack);
		}
		this.controller.cardFight(cardAttack, cardDefend);
	}

}

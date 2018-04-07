package com.Tlk.BesseresHearthstone;

import java.util.Random;

import com.Tlk.BesseresHearthstone.MainGameClass.STATE;
import com.Tlk.BesseresHearthstone.CardRelated.Card;

public class GameController
{
	private final Player p1;
	private final Player p2;
	private Player atPlay;

	private GameStateController controller;

	public GameController(Player p1, Player p2, GameStateController controller)
	{
		this.controller = controller;
		this.p1 = p1;
		this.p2 = p2;

		for(int i = 0; i < 5; i++)
		{
			p1.drawCard();
			p2.drawCard();
		}

		Random r = new Random();
		if(r.nextBoolean())
			this.setTurn(this.p1);
		else
			this.setTurn(this.p2);
	}

	public void cardFight(Card cardOne, Card cardTwo)
	{
		cardOne.setLife(cardOne.getLife() - cardTwo.getDamage());
		cardTwo.setLife(cardTwo.getLife() - cardOne.getDamage());
		p1.checkCard(cardOne);
		p2.checkCard(cardTwo);
	}

	public void attackPlayer(Card card)
	{
		if(this.atPlay == this.p1)
			this.p2.setLife(this.p2.getLife() - card.getDamage());
		else
			this.p1.setLife(this.p1.getLife() - card.getDamage());

		if(this.p1.getLife() <= 0)
			this.controller.setGameState(STATE.END);
		else if(this.p2.getLife() <= 0)
			this.controller.setGameState(STATE.BEATEN);
	}

	private void setTurn(Player player)
	{
		player.setMana(player.getMana() + 1);
		player.drawCard();
		this.atPlay = player;
	}

	public void nextTurn()
	{
		if (this.atPlay == this.p1)
			setTurn(this.p2);
		else
			setTurn(this.p1);
	}

	public Player getP1()
	{
		return p1;
	}

	public Player getP2()
	{
		return p2;
	}

}

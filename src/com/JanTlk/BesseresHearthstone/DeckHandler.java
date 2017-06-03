package com.JanTlk.BesseresHearthstone;

import java.awt.Component;
import java.io.FileNotFoundException;
import java.util.LinkedList;

import com.JanTlk.BesseresHearthstone.Karten.Karte;
import com.JanTlk.BesseresHearthstone.Karten.Status;

/**
 * In this class every card should be added to the game and to a deck
 * this deck will get added to a Spielfeld, which will then take controll of the deck
 * @author Gaming
 *
 */
public class DeckHandler 
{

	private Deck player;
	private Deck pc;
	
	/**
	 * creates the two Decks of both Player and a clone of that for the PC
	 * @param c
	 */
	public DeckHandler(Component c)
	{		
		player = new Deck("Player");
		
		CardCreator cC;
		try {
			cC = new CardCreator(c);
		} catch (FileNotFoundException e) {
			System.err.println("Card creator File not Found! \nGame cannot be started!");
			System.err.println("Please create a Card Creator Text Document.");
			return;
		} 
		
		for(int i = 1; i <= 32; i++)
		{
			player.addKarte(cC.nextCard());
			player.getKarten().getLast().setDeck(player);
		}
		
		pc = player.clone(new Deck("PC")); 
		
		player.mischenA();
		pc.mischenA();
		
//		real random Card shuffel, results in 50/50 Games
//		player.mischen();
//		pc.mischen();
		
		
		//Starthand ziehen
		for(int startKartren = 0; startKartren < 5; startKartren ++)
		{
			player.ziehen();
			pc.ziehen();                   
		}
		
	}
	
	/**
	 * both Player and PC draw 5 cards, only to use in Debug Mode
	 */
	public void refillHands()
	{
		for(int startKartren = 0; startKartren < 6; startKartren ++)
		{
			player.ziehen();
			pc.ziehen();                   
		}
	}
	
	/**
	 * resets the game
	 */
	public void reset()
	{	
		for (Karte tCard : getAllCards())
		{
			tCard.setStatus(Status.STAPEL);
			tCard.setAttacked(false);
			tCard.attackedCard(null);
			tCard.setDisplayed(false);
			tCard.setLeben(tCard.getLebenInit());
			tCard.setSchaden(tCard.getSchadenInit());
		}
		
		player.setDrawCounter(0);
		pc.setDrawCounter(0);
		player.mischenA();
		pc.mischenA();
	}
	
	public LinkedList<Karte> getAllCards()
	{
		LinkedList<Karte> allCards = new LinkedList<Karte>();
		allCards.addAll(player.getKarten());
		allCards.addAll(pc.getKarten());
		return allCards;
	}


	public Deck getPlayerDeck() 
	{
		return player;
	}

	public Deck getPCDeck() 
	{
		return pc;
	}
}

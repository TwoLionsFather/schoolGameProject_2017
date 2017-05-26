package com.JanTlk.BesseresHearthstone;

import java.awt.Component;
import java.io.FileNotFoundException;

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
			cC = new CardCreator("Karten.txt", c);
		} catch (FileNotFoundException e) {
			System.err.println("Card creator File not Found! \nGame cannot be started!");
			System.err.println("Please create a Card Creator Text Document.");
			return;
		} 
		
		for(int i = 1; i <= 13; i++)
		{
			player.addKarte(cC.nextCard());
			player.getKarten().getLast().setDeck(player);
		}
		
		pc = player.clone(new Deck("pc")); 
		
		player.mischen();
		pc.mischen();
		
		//Starthand ziehen
		for(int startKartren = 0; startKartren < 3; startKartren ++)
		{
			player.ziehen();
			pc.ziehen();
		}
		
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

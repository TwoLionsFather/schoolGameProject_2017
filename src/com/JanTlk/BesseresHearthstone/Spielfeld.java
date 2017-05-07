package com.JanTlk.BesseresHearthstone;

import java.awt.Graphics;

import com.JanTlk.BesseresHearthstone.Karten.Karte;

public class Spielfeld 
{

	private Deck dPL;
	private Deck dPC;
	
	public Spielfeld()
	{
		DeckHandler dH = new DeckHandler();	
		
		dPL = dH.getPlayerDeck();
		dPC = dH.getPCDeck();
	}
	
	/**
	 * this draws the Image of every Card, at the moment at 0|0
	 * @param g
	 */
	public void render(Graphics g) 
	{
		for(int i = 0; i < dPL.getAnzKarten(); i++)
		{
			Karte tempKarte = dPL.getKarten().get(i);
			tempKarte.drawCard(0 + i * 105, 200, g);
		}
		
		for(int i = 0; i < dPC.getAnzKarten(); i++)
		{
			Karte tempKarte = dPC.getKarten().get(i);
			tempKarte.drawCard(0 + i * 105, 0, g);
		}
	}

	public void tick() 
	{
		
	}
	
}

package com.JanTlk.BesseresHearthstone;

import java.awt.Graphics;

import com.JanTlk.BesseresHearthstone.Karten.Karte;

public class Spielfeld 
{

	private Deck dPL;
	private Deck dPC;
	
	private int kartenCount;
	
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
		for(Karte tempKarte : dPL.getKarten())
		{
			tempKarte.drawCard(0 + 55 * kartenCount++, 200, g);
		}
		kartenCount = 0;
		
		for(Karte tempKarte : dPC.getKarten())
		{
			tempKarte.drawCard(0 + 55 * kartenCount++, 0, g);
		}
		kartenCount = 0;
	}

	public void tick() 
	{
		
	}
	
}

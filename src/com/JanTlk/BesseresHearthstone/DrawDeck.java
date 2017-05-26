package com.JanTlk.BesseresHearthstone;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.JanTlk.BesseresHearthstone.Karten.Karte;
import com.JanTlk.BesseresHearthstone.Karten.Status;

public class DrawDeck 
{
	private int anzRectInR = 15;		//How many rectangles are there from left to right
	private float rectHoehe = Hearthstone.HOEHE / 12 * 3;
	private Rectangle [][] kartenFelder;
	private Deck dPL;
	private Deck dPC;

	private int abblageCountPL;
	private int abblageCountPC;
	private int stapelCountPL;
	private int stapelCountPC;
	
	public DrawDeck(Deck dPL, Deck dPC) 
	{
		this.dPL = dPL;
		this.dPC = dPC;
		kartenFelder = new Rectangle [anzRectInR][2];
		
		for(int playerPC = 0; playerPC < 2; playerPC++)
		{
			for(int spalte = 0; spalte < kartenFelder.length; spalte++)
			{
				kartenFelder[spalte][playerPC] = new Rectangle((int) Hearthstone.BREITE / anzRectInR * spalte
															, (int) (Hearthstone.HOEHE / 2 - ((playerPC > 0) ? 0 : rectHoehe)) //If Rectangle is on Playerside, do not substract the rectangles height
															, (int) (Hearthstone.BREITE / anzRectInR)
															, (int) rectHoehe);	
			}
			
		}	
		
	}

	public void render(Graphics g) 
	{
		abblageCountPL = 0;
		stapelCountPL = 0;
		abblageCountPC = 0;
		stapelCountPC = 0;
		
		ArrayList<Karte> handKartenPL = new ArrayList<Karte>();
		ArrayList<Karte> handKartenPC = new ArrayList<Karte>();

		ArrayList<Karte> fieldKarten = new ArrayList<Karte>();
		
		
		//the order of these for loops is relevant
		//It decides which deck is int the Fore/Background
		for(Karte tCard : dPC.getKarten())
		{
			if(tCard.getStatus() == Status.Hand)
			{
				handKartenPC.add(tCard);
			}
			
			else if(tCard.getStatus() == Status.Abblage) {
				abblageCountPC++;	
			}
			
			else if(tCard.getStatus() == Status.Stapel) {
				stapelCountPC++;	
			}
			
			else if(tCard.getStatus() == Status.Feld
			|| tCard.getStatus() == Status.Attack)
			{
				fieldKarten.add(tCard);
			}			
		}
		
		for(Karte tCard : dPL.getKarten())
		{
			if(tCard.getStatus() == Status.Hand)
			{
				handKartenPL.add(tCard);
			}
			
			else if(tCard.getStatus() == Status.Abblage) {
				abblageCountPL++;	
			}
			
			else if(tCard.getStatus() == Status.Stapel) {
				stapelCountPL++;	
			}
			
			else if(tCard.getStatus() == Status.Feld
			|| tCard.getStatus() == Status.Attack)
			{
				fieldKarten.add(tCard);
			}			
		}
		
		drawField(fieldKarten, g);
		drawHand(true, handKartenPL, g);
		drawHand(true, handKartenPC, g);
	}
	
	/**
	 * draws handCards, thus the cards both players have on their hand
	 * @param player if player is set, the cards are visible and on the bottom of the screen
	 * @param handCards these cards are on the hand of a player and need to be drawn
	 * @param g used to draw the graphics of the card on
	 */
	public void drawHand(boolean player, ArrayList<Karte> handCards, Graphics g)
	{
		int kartenCount = 0;
		for(Karte tCard : handCards)
		{
			tCard.drawCard((int) (Hearthstone.BREITE / 2 
														- ((tCard.getBounds().getWidth() - 55) * handCards.size()) / 2
														+ 55 * kartenCount++)
						, (int) ((player) ? Hearthstone.HOEHE - (Hearthstone.HOEHE / 5) : 0)
						, g
						, false);
		}
	}
	
	/**
	 * draws cards on Field and in Battle
	 * these Cards know their location  themselfes
	 * @param fieldCards is a list of Cards that need to be drawn
	 * @param g used to draw the graphics of the card on
	 */
	public void drawField(ArrayList<Karte> fieldCards, Graphics g)
	{
		for(Karte tempC : fieldCards)
		{
			tempC.drawCard((int) tempC.getBounds().getX()
						, (int) tempC.getBounds().getY()
						, g
						, false);
		}
	}
	
	public int getAbblageCountPL() 
	{
		return abblageCountPL;
	}

	public int getAbblageCountPC() 
	{
		return abblageCountPC;
	}

	public int getStapelCountPL() 
	{
		return stapelCountPL;
	}

	public int getStapelCountPC() 
	{
		return stapelCountPC;
	}

	public Rectangle[][] getKartenFelder() {
		return kartenFelder;
	}

}

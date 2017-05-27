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
	
	private DeckHandler deckHandler;
	private Deck dPL;
	private Deck dPC;

	private int abblageCountPL;
	private int abblageCountPC;
	private int stapelCountPL;
	private int stapelCountPC;
	
	public DrawDeck(DeckHandler dH) 
	{
		this.dPL = dH.getPlayerDeck();
		this.dPC = dH.getPCDeck();
		this.deckHandler = dH;
		
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

	/**
	 * this displays all Cards on the Game
	 * @param playersMove if this is true, players Cards will get displayed on top
	 * @param g no comment needed
	 */
	public void render(boolean playersMove, Graphics g) 
	{
		//reset counter to start counting while checking Status of every Card
		abblageCountPL = 0;
		stapelCountPL = 0;
		abblageCountPC = 0;
		stapelCountPC = 0;
		
		//Collection of Cards that get displayed on top/bottom of the screen
		ArrayList<Karte> handKartenPL = new ArrayList<Karte>();
		ArrayList<Karte> handKartenPC = new ArrayList<Karte>();

		//these lists collect all field cards and displays them
		ArrayList<Karte> fieldKartenPL = new ArrayList<Karte>();
		ArrayList<Karte> fieldKartenPC = new ArrayList<Karte>();		
		
		//checks status of every card in the game and adds its to the correct list
		for(Karte tCard : deckHandler.getAllCards())
		{
			switch (tCard.getStatus()) 
			{
			case Hand:
				if (tCard.getDeck() == dPL)
				{
					handKartenPL.add(tCard);
				}
				
				else if (tCard.getDeck() == dPC)
				{
					handKartenPC.add(tCard);
				}
				break;
				
			case Abblage:
				abblageCountPC++;
				break;
				
			case Stapel:
				stapelCountPC++;
				break;
				
			case Feld:
			case Attack:
			case Layed:
				if (tCard.getDeck() == dPL)
				{
					fieldKartenPL.add(tCard);
				}
				
				else if (tCard.getDeck() == dPC)
				{
					fieldKartenPC.add(tCard);
				}
				break;
			}			
		}
		
		if (playersMove)
		{
			drawField(fieldKartenPC, g);
			drawField(fieldKartenPL, g);
		}
		
		else
		{
			drawField(fieldKartenPL, g);
			drawField(fieldKartenPC, g);
		}
		
		drawHand(true, handKartenPL, g);
		drawHand(false, handKartenPC, g);		
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
	
	public int getHorizontal() 
	{
		return anzRectInR;
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

	public Rectangle[][] getKartenFelder() 
	{
		return kartenFelder;
	}

}

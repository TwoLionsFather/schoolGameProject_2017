package com.JanTlk.BesseresHearthstone;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.JanTlk.BesseresHearthstone.Hearthstone.STATE;
import com.JanTlk.BesseresHearthstone.Karten.Karte;
import com.JanTlk.BesseresHearthstone.Karten.Status;

public class DrawDeck 
{
	private int anzRectInR = 7;		//How many rectangles are there from left to right
	private float rectHoehe = Hearthstone.HOEHE / 12 * 3;
	private Rectangle [][] kartenFelder;
	
	private BufferedImage cardBack;
	
	private DeckHandler deckHandler;
	private Deck dPL;
	private Deck dPC;
	
	public DrawDeck(DeckHandler dH) 
	{
		this.dPL = dH.getPlayerDeck();
		this.dPC = dH.getPCDeck();
		this.deckHandler = dH;
		
		try {
			this.cardBack = Hearthstone.rescaledBufferedimage(ImageIO.read(Hearthstone.allImportedFiles[4])
															, (Hearthstone.BREITE < 1920) ? 70 : 100
															, (Hearthstone.BREITE < 1920) ? 140 : 200);
		} catch (IOException e) {
			e.printStackTrace();
			cardBack = null;
			System.err.println("No Cardback File found");
		}
		
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
	public void render(int[] gameStats, boolean playersMove, Graphics g) 
	{
		if ((gameStats[0] <= 0)
		|| (gameStats[3] <= 0)
		|| gameStats[7] >= dPL.getAnzKarten()
		|| gameStats[9] >= dPC.getAnzKarten())
		{
			Hearthstone.gameState = STATE.BEATEN;
			dPC.repaint();
			return;
		}
		
		//reset counter to start counting while checking Status of every Card
		gameStats[6] = 0;
		gameStats[7] = 0;
		gameStats[8] = 0;
		gameStats[9] = 0;
		
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
			case HAND:
				if (tCard.getDeck() == dPL)
				{
					handKartenPL.add(tCard);
				}
				
				else if (tCard.getDeck() == dPC)
				{
					handKartenPC.add(tCard);
				}
				break;
				
			case STAPEL:
			case ABBLAGE:
				if (tCard.getDeck() == dPL)
				{
					gameStats[(tCard.getStatus() == Status.STAPEL) ? 6 : 7]++;
				}
				
				else if (tCard.getDeck() == dPC)
				{
					gameStats[(tCard.getStatus() == Status.STAPEL) ? 8 : 9]++;
				}
				break;
				
			case FELD:
			case ATTACKC:
			case ATTACKP:
			case LAYED:
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
		
		if (!player 
		&& !Hearthstone.isDebugMode()
		&& cardBack != null)
		{
			for(Karte tCard : handCards)
			{
				g.drawImage(cardBack
						, (int) (Hearthstone.BREITE / 2 
														- ((tCard.getBounds().getWidth() - ((Hearthstone.BREITE < 1920) ? 40 : 55)) * handCards.size()) / 2
														+ 55 * kartenCount++)
						, 0
						, null);
			}
			return;
		}
		
		for(Karte tCard : handCards)
		{
			tCard.drawCard((int) (Hearthstone.BREITE / 2 
														- ((tCard.getBounds().getWidth() - ((Hearthstone.BREITE < 1920) ? 40 : 55)) * handCards.size()) / 2
														+ 55 * kartenCount++)
						, (int) ((player) ? Hearthstone.HOEHE - tCard.getBounds().getHeight() - ((Hearthstone.BREITE < 1920) ? 30 : -10) : 0) // oder /5
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
			
			//Es ist nicht ostern, doch ostereier suchen macht immer Spaß
			if (tempC.getCardID() == 16
			&& !Hearthstone.isDrawhelpActive()
			&& !Hearthstone.isDebugMode()
			&& !tempC.isDisplayed()
			&& tempC.getStatus() == Status.LAYED
			&& kartenFelder[(anzRectInR / 2) + ((anzRectInR % 2 == 0) ? 1 : 0)][1].contains(tempC.getHome()))
			{
				g.setColor(Color.red);
				g.setFont(new Font("Arial", Font.PLAIN, 20));
				
				String EasterEgg = "Fixed it";
				g.drawString(EasterEgg
							, (int) tempC.getHome().getX()
							, (int) tempC.getHome().getY() - 10);
				
				System.out.println("_________________┌∩┐(ಠ_ಠ)┌∩┐_________________ Achivement unlocked _________________┌∩┐(ಠ_ಠ)┌∩┐_________________");
			}
		}
	}
	
	
	
	public Rectangle[][] getKartenFelder() 
	{
		return kartenFelder;
	}

	public int getAnzRectInR() 
	{
		return anzRectInR;
	}

}

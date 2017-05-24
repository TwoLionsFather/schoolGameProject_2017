package com.JanTlk.BesseresHearthstone;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import com.JanTlk.BesseresHearthstone.Karten.Karte;
import com.JanTlk.BesseresHearthstone.Karten.Status;

public class Spielfeld 
{
	private Deck dPL;
	private Deck dPC;
	private int idxMovedC;
	
	private boolean playersMove;
	private int lifePlayer;
	private int manaPlayer;
	private int manaPlayerMax;
	private int lifePC;
	private int manaPC;
	private Rectangle nextRoundB;
	
	private int anzRectInR = 15;		//How many rectangles are there from left to right
	private float rectHoehe = Hearthstone.HOEHE / 12 * 3;
	Rectangle [][] kartenFelder;
	Karte [][] kartenAufFelder;
	
	/**
	 * used to set up the game
	 * @param c used to redraw
	 */
	public Spielfeld(Component c)
	{		
		DeckHandler dH = new DeckHandler(c);	
		
		kartenFelder = new Rectangle [anzRectInR][2];
		kartenAufFelder = new Karte [anzRectInR][2];
		nextRoundB = new Rectangle((int) (Hearthstone.BREITE - 50)
								, 50
								, 30
								, 20);
		
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
		
		dPL = dH.getPlayerDeck();
		dPC = dH.getPCDeck();
		
		//used for test of fighting system
		int spalte = 0;
		for(Karte tempC : dPC.getKarten())
		{
			kartenAufFelder[spalte][0] = tempC;
			Rectangle tempRect = kartenFelder[spalte][0];
			tempC.setNewPos(new Rectangle((int) (tempRect.getX() + (tempRect.getWidth() - tempC.getBounds().getWidth()) / 2)
										, (int) (tempRect.getY() + (tempRect.getHeight() - tempC.getBounds().getHeight()) / 2)
										, (int) tempC.getBounds().getWidth()
										, (int) tempC.getBounds().getHeight()));

			tempC.setStatus(Status.Feld);
			spalte++;
		}
		
		lifePlayer = 20;
		lifePC = 20;
		manaPlayer = 1;		
		manaPlayerMax = 1;
		
		playersMove = true;
		dPL.mischen();
		dPC.mischen();
	}
	
	/**
	 * used to set up next Round
	 * @param playerPC if true, its the players turn
	 */
	public void nextRound(boolean player)
	{		
		//only needs to check for players cards, PC/KI handles attacks on its own
		for(Karte tempC : dPL.getKarten())
		{
			//if a card has attacked, it gets moved back to its origin position
			if(tempC.getStatus() == Status.Attack)
			{
				tempC.damageTick();	
				tempC.placeHome();
			}
		}
		
//		for(Karte tempC : dPC.getKarten())
//		{
//			tempC.setAttacked(false);
//		}
		
		if (!player)
		{
			playersMove = false;
			manaPC++;
			dPC.ziehen();
			//ki.nextRound(dPC, kartenFelder, kartenAufFelder, manaPC, lifePlayer);
			return;
		}

		manaPlayerMax++;
		manaPlayer = manaPlayerMax;
		dPL.ziehen();
		playersMove = true;
		
		dPL.getKarten().getLast().getComponent().repaint();
	}
	
	/**
	 * this draws the Image of every Card, at the moment at 0|0
	 * @param g the Graphics that every Card in the Game gets drawn with
	 */
	public void render(Graphics g) 
	{
		drawDeck(dPC, g, false);
		drawDeck(dPL, g, true);
		
		drawHud(g);
	}
	
	/**
	 * used to display buttons for next round, mana, live, ...
	 * @param g graphics component that graphics get drwn on
	 */
	private void drawHud(Graphics g)
	{
		for(int playerPC = 0; playerPC < 2; playerPC++)
		{
			for(int spalte = 0; spalte < kartenFelder.length; spalte++)
			{
				Rectangle temp = kartenFelder[spalte][playerPC];
				g.setColor((playerPC > 0) ? Color.green : Color.red);
				g.drawRect((int) temp.getX()
						, (int) temp.getY()
						, (int) temp.getWidth()
						, (int) temp.getHeight());
			}
		}
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("Info", Font.BOLD , 12));
		g.drawString("Mana Player: " + manaPlayer
				, (int) Hearthstone.BREITE - 120
				, (int) Hearthstone.HOEHE - 40);
		
		g.setColor((playersMove) ? Color.green : Color.red);
		g.drawRect((int) nextRoundB.getX()
				, (int) nextRoundB.getY()
				, (int) nextRoundB.getWidth()
				, (int) nextRoundB.getHeight());
	}
	
	/**
	 * draws a deck of cards based on their status
	 * uses the Graphics class to do so
	 * @param deck the deck of cards that needs to get drawn
	 * @param g the Graphics parameter used to draw the cards on
	 * @param player this ich true if the card belongs to the pc, if a card does not belong to the pc
	 */
	private void drawDeck(Deck deck, Graphics g, boolean player)
	{
		int kartenCount = 0;
		int abblageCount = 0;
		int stapelCount = 0;
		
		for(Karte tempKarte : deck.getKarten())
		{
			if(tempKarte.getStatus() == Status.Abblage) {
				abblageCount++;	
			}
			
			else if(tempKarte.getStatus() == Status.Stapel) {
				stapelCount++;	
			}
			
			else if(tempKarte.getStatus() == Status.Hand)
			{
				if(player)
				{
					tempKarte.drawCard((int) (Hearthstone.BREITE / 2 
																	- (dPL.getAnzKarten() * (dPL.getKarten().get(0).getBounds().getWidth() - 50) / 2)
																	+ 55 * kartenCount++)
									, (int) (Hearthstone.HOEHE - (Hearthstone.HOEHE / 5))
									, g
									, false);
				}
				
				else 
				{
					tempKarte.drawCard((int) (Hearthstone.BREITE / 2 - (dPC.getAnzKarten() * (dPC.getKarten().get(0).getBounds().getWidth() - 50) / 2) + 55 * kartenCount++)
									, 0
									, g
									, false);
				}
			}
			
			else if(tempKarte.getStatus() == Status.Feld
			|| tempKarte.getStatus() == Status.Attack)
			{
				tempKarte.drawCard((int) tempKarte.getBounds().getX()
								, (int) tempKarte.getBounds().getY()
								, g
								, false);
			}
		}
		
		/**
		 * since this info is related to the specific deck it is not displayed on the hud
		 */
		g.setFont(new Font("Info", Font.BOLD , 12));
		if(player)
		{
			g.setColor(Color.green);
			g.drawString("" + stapelCount
					, 20
					, (int) Hearthstone.HOEHE - 40);
		
			g.drawString("" + abblageCount
					, (int) Hearthstone.BREITE - 25
					, (int) Hearthstone.HOEHE - 40);
		}
			
		else
		{
			g.setColor(Color.red);
			g.drawString("" + stapelCount
						, 15
						, 15);
			
			g.drawString("" + abblageCount
					, (int) Hearthstone.BREITE - 25
					, 15);
		}
			
		kartenCount = 0;
		abblageCount = 0;
		stapelCount = 0;
	}

	/**
	 * This is used to check if a valid card has been clicked on and is responsible for updating its position
	 * @param xyChange a Mouse event to get the starting (and finishing) Coordinates 
	 */
	public void moveCard(int[] xyChange) 
	{
		Karte movedC = dPL.getKarten().get(idxMovedC);
		
		System.out.println(movedC.toString());
		System.out.println(xyChange[0] + " in X und " + xyChange[1] + " in Y");
		
		movedC.setChange(xyChange);
		movedC.setStatus(Status.Feld);
		movedC.getComponent().repaint();
		
		dPL.cardPlayed(idxMovedC);		
	}
	
	/**
	 * This is used to check if the recorded Point of the moujseevent is within the boarders of a card's Rectangle
	 * @param toTest the point of the mouseevent
	 * @param borders borders of card or onscreen rectangel
	 * @return true if the point is in the boarders of the rectangle
	 */
	private boolean inBounds(Point toTest, Rectangle borders)
	{
		if(borders == null)
		{
			return false;
		}
		
		return toTest.getX() > borders.getX()						//boolsche Abfrage, sonst if(...)
				&& toTest.getX() < borders.getX() + borders.getWidth()
				&& toTest.getY() > borders.getY()
				&& toTest.getY() < (borders.getY() + borders.getHeight());
	}

	
	/**
	 * to check if there is a card at the clicked point, saves index of card on top to be the moved card
	 * @param arg0 the mousevent that needs to be checked
	 * @return true if the event happened on a card
	 */
	public boolean cardAt(MouseEvent arg0) 
	{
		Point cEvent = arg0.getPoint();
		int highestID = -1;
		
		for(int i = 0; i < dPL.getAnzKarten(); i++)
		{
			Karte tKarte = dPL.getKarten().get(i);
			
			if(inBounds(cEvent, tKarte.getBounds())) 
			{
				highestID = i;
			}
		}
		
		if(highestID == -1)
		{
			return false;
		}
		
		idxMovedC = highestID;
		return true;
	}
	
	/**
	 * main use is to move cards around and handle attacks of cards
	 * this is used to check in/out cards from card array of this class. 
	 * playerPC > 0 if players cards/rectangles
	 * @param arg0 MouseEvent used to check if there is a Rectangle at its location
	 * @return true if card has been placed in a rectangle
	 */
	public boolean cardRectAt(MouseEvent arg0) 
	{
		Point rEvent = arg0.getPoint();
		
		for(int playerPC = 0; playerPC < 2; playerPC++)
		{
			for(int spalte = 0; spalte < kartenFelder.length; spalte++)
			{				
				Rectangle tempRect = kartenFelder[spalte][playerPC];
				Karte cardAtRect = kartenAufFelder[spalte][playerPC];
				Karte movedC = dPL.getKarten().get(idxMovedC);
				
				//if the moved card was attacking another card this needs to be resetted
				if(movedC.getAttackCard() != null)
				{
					movedC.getAttackCard().setAttacked(false);
				}
				
				/**
				 * moved from to a field and sets default location to the selected rectangle 
				 */
				if(inBounds(rEvent, tempRect.getBounds())
				&& cardAtRect == null
				&& ((movedC.getStatus() == Status.Hand) ? manaPlayer - movedC.getMana() >= 0 : true)
				&& playerPC > 0
				&& playersMove) 
				{
					//in case this card owned a rectangle before, now it no longer does so
					remCardFromRectangles(movedC);
					if (movedC.getStatus() == Status.Hand)
					{
						manaPlayer -= movedC.getMana();
					}
					
					//sets this cards default location to Recctangles center
					movedC.setHome(new Rectangle((int) (tempRect.getX() + (tempRect.getWidth() - movedC.getBounds().getWidth()) / 2)
												, (int) (tempRect.getY() + (tempRect.getHeight() - movedC.getBounds().getHeight()) / 2)
												, (int) movedC.getBounds().getWidth()
												, (int) movedC.getBounds().getHeight()));
					
					cardAtRect = movedC;
					movedC.setStatus(Status.Feld);
					return true;
				}
				
				/**
				 * if move is used to attack another card
				 */
				else if(inBounds(rEvent, tempRect.getBounds())
				&& ((cardAtRect != null) ? !cardAtRect.isAttacked() : false)
				&& playerPC <= 0
				&& movedC.getStatus() != Status.Hand
				&& playersMove) 
				{
					movedC.setNewPos(new Rectangle((int) (tempRect.getX() + (tempRect.getWidth() - movedC.getBounds().getWidth()) / 2)
												, (int) tempRect.getY()
												, (int) movedC.getBounds().getWidth()
												, (int) movedC.getBounds().getHeight()));
					
					movedC.attacks(cardAtRect);
					movedC.setStatus(Status.Attack);
					return true;
				}
				
			}
			
		}					
		return false;
	}

	/**
	 * checks nextRoundButton on clickevent
	 * @param arg0 clickevent to be tested
	 * @return true if next round
	 */
	public boolean clickNR(MouseEvent arg0)
	{
		Point cEvent = arg0.getPoint();
		playersMove = !playersMove;
		nextRound(playersMove);
		return inBounds(cEvent, nextRoundB);
	}
	
	/**
	 * checks if moved card is checked in on any of the rectangles and delets it from the array
	 * @param remC the card choosen to get removed
	 */
	private void remCardFromRectangles(Karte remC) 
	{
		for(int playerPC = 0; playerPC < 2; playerPC++)
		{
			for(int i = 0; i < kartenFelder.length; i++)
			{
				if(kartenAufFelder[i][playerPC] == remC)
				{
					kartenAufFelder[i][playerPC] = null;
				}
			}
		}
	}
	
	public boolean isPlayersMove() 
	{
		return playersMove;
	}
}

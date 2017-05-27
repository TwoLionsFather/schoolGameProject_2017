package com.JanTlk.BesseresHearthstone;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.io.File;

import com.JanTlk.BesseresHearthstone.Karten.Karte;
import com.JanTlk.BesseresHearthstone.Karten.Status;

public class Spielfeld 
{
	private Deck dPL;
	private Deck dPC;
	private int idxMovedC;
	
	private boolean playersMove;	
	private int[] gameStats;
	
	private Karte detailedCard;
	private Rectangle nextRoundB;
	
	private Rectangle [][] kartenFelder;
	private Karte [][] kartenAufFelder;
	private DrawDeck deckDrawer;
	private DrawHud hudDrawer;

	private boolean drawHelp = false;
	
	/**
	 * used to set up the game
	 * @param c used to redraw
	 */
	public Spielfeld(Component c)
	{				
		DeckHandler dH = new DeckHandler(c);
		
		dPL = dH.getPlayerDeck();
		dPC = dH.getPCDeck();
		
		nextRoundB = new Rectangle((int) (Hearthstone.BREITE - 50), 50
								, 30, 20);
		
		hudDrawer = new DrawHud(new File("Graphics\\HudPlayer.png"), nextRoundB);
		deckDrawer = new DrawDeck(dPL, dPC);
		
		kartenFelder = deckDrawer.getKartenFelder();
		kartenAufFelder = new Karte [deckDrawer.getHorizontal()][2];
		
		/**
		 * 0: playersLife, Leben des Spieler
		 * 1: playersMana, Mana des Spieler
		 * 2: playersManaCap, max Mana des Spieler
		 * 3: pcsLife, Leben des PC
		 * 4: pcsManaCap, max Mana des Spieler
		 * ***********************************
		 * 5: playersStapelCount, anzahl NachziehKarten Player
		 * 6: playersAbblageCount, anzahl AbblageKarten Player
		 * 7: pcsStapelCode, anzahl NachziehKarten PC
		 * 8: pcsAbblageCode, anzahl AbblageKarten PC
		 */
		gameStats = new int[9];	
		gameStats[0] = 20;
		gameStats[3] = 20;
		gameStats[1] = 1;		
		gameStats[2] = 1;
		gameStats[4] = 1;
		
		playersMove = true;
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
			
			//if card gets killed its "corps" gets removed
			if(tempC.getStatus() == Status.Abblage)
			{
				remCardFromRectangles(tempC);
			}
		}
		
		if (!player)
		{
			playersMove = false;
			gameStats[4]++;
			dPC.ziehen();
			//ki.nextRound(dPC, kartenFelder, kartenAufFelder, manaPCMax, lifePlayer);
			return;
		}

		gameStats[2]++;
		gameStats[1] = gameStats[2];
		dPL.ziehen();
		playersMove = true;
	}
	
	/**
	 * this draws the Image of every Card, at the moment at 0|0
	 * @param g the Graphics that every Card in the Game gets drawn with
	 */
	public void render(Graphics g) 
	{		
		deckDrawer.render(playersMove, g);
//		long timer = System.nanoTime();
		
		gameStats[5] = deckDrawer.getStapelCountPL();
		gameStats[6] = deckDrawer.getAbblageCountPL();
		gameStats[7] = deckDrawer.getStapelCountPC();
		gameStats[8] = deckDrawer.getAbblageCountPC();
		
		hudDrawer.render(playersMove, detailedCard, gameStats, g);
//		System.out.println("Spielfeld.render time diff " + (System.nanoTime() - timer) * 0.001 + "us");
		
		if(drawHelp)
		{
			drawGuideLines(g);
		}
		
	}
	
	/**
	 *  used to draw lines around rectangles that cards use to get displayed/layed out
	 * @param g graphics component that graphics get drwn on
	 */
	private void drawGuideLines(Graphics g)
	{		
//		Rectangles to guide userinput
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
	 * This is used to check if the recorded Point of the mouseevent is within the boarders of a card's Rectangle
	 * @param toTest the point of the mouseevent
	 * @param borders borders of card or onscreen rectangel
	 * @return true if the point is in the boarders of the rectangle
	 */
	public boolean inBounds(Point toTest, Rectangle borders)
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
				
				//shows details of moved card
				if (!movedC.isDisplayed())
				{
					if (detailedCard != null)
					{
						detailedCard.setDisplayed(false);
						detailedCard = null;
					}
					
					movedC.setDisplayed(true);
					detailedCard = movedC;			
				}
				
				//if the moved card was attacking another card this needs to be resetted
				if(movedC.getAttackCard() != null)
				{
					movedC.getAttackCard().setAttacked(false);
				}
				
//				debugg only!
//				if (cardAtRect != null)
//				{
//					System.out.println(cardAtRect.toString());
//					System.out.println("at: " + spalte + " of " + playerPC);
//				}
				
				/**
				 * moved from to a field and sets default location to the selected rectangle 
				 */
				if(inBounds(rEvent, tempRect.getBounds())
				&& cardAtRect == null
				&& ((movedC.getStatus() == Status.Hand) ? gameStats[1] - movedC.getMana() >= 0 : true)
				&& playerPC > 0
				&& playersMove) 
				{
					if (movedC.getStatus() == Status.Hand)
					{
						gameStats[1] -= movedC.getMana();
					}
					else
					{
						//in case this card owned a rectangle before, now it no longer does so
						remCardFromRectangles(movedC);
					}
					
					//sets this cards default location to Recctangles center
					movedC.setHome(new Rectangle((int) (tempRect.getX() + (tempRect.getWidth() - movedC.getBounds().getWidth()) / 2)
												, (int) (tempRect.getY() + (tempRect.getHeight() - movedC.getBounds().getHeight()) / 2)
												, (int) movedC.getBounds().getWidth()
												, (int) movedC.getBounds().getHeight()));
					
					//not working with reference
					kartenAufFelder[spalte][playerPC] = movedC;
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
	 * selects card to be displayed in detail on hud and repaints canvas
	 * @param arg0 mous position
	 */
	public void cardDetailsAt(MouseEvent arg0) 
	{
		Point cEvent = arg0.getPoint();
		int idxDetailedCard = -1;
		boolean playersDeck = false;
		
		for(int i = 0; i < dPL.getAnzKarten(); i++)
		{
			Karte tKarte = dPL.getKarten().get(i);
			
			if(inBounds(cEvent, tKarte.getBounds())) 
			{
				idxDetailedCard = i;
				playersDeck = true;
			}
		}
		
		if (idxDetailedCard == -1)
		{
			for(int i = 0; i < dPC.getAnzKarten(); i++)
			{
				Karte tKarte = dPC.getKarten().get(i);
				
				if(inBounds(cEvent, tKarte.getBounds())) 
				{
					idxDetailedCard = i;
					playersDeck = false;
				}
			}
		}		
		
		if(idxDetailedCard == -1)
		{
			if (detailedCard != null)
			{
				detailedCard.setDisplayed(false);
				detailedCard = null;
			}
			return;
		}
		
		Karte atDisplay = (playersDeck) ? dPL.getKarten().get(idxDetailedCard) : dPC.getKarten().get(idxDetailedCard);
		
		if (!atDisplay.isDisplayed())
		{
			if (detailedCard != null)
			{
				detailedCard.setDisplayed(false);
				detailedCard = null;
			}
			
			atDisplay.setDisplayed(true);
			detailedCard = atDisplay;			
			detailedCard.getComponent().repaint();
		}
	}
	
	/**
	 * checks nextRoundButton on clickevent
	 * @param arg0 clickevent to be tested
	 * @return true if next round
	 */
	public boolean clickNR(MouseEvent arg0)
	{
		Point cEvent = arg0.getPoint();
		if (inBounds(cEvent, nextRoundB))
		{
			playersMove = !playersMove;
			nextRound(playersMove);
			dPL.getKarten().getLast().getComponent().repaint();
		}
		return inBounds(cEvent, nextRoundB);
	}
	
	/**
	 * checks if moved card is checked in on any of the rectangles and delets it from the array
	 * @param remC the card choosen to get removed
	 */
	private void remCardFromRectangles(Karte remC) 
	{
		if (remC == null)
		{
			return;
		}
		
		for(int playerPC = 0; playerPC < 2; playerPC++)
		{
			for(int i = 0; i < kartenAufFelder.length; i++)
			{
				if ((kartenAufFelder[i][playerPC] != null) 
				&& kartenAufFelder[i][playerPC] == remC)
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

	
	public Karte getDetailedCard() 
	{
		return detailedCard;
	}
	

	public void setDetailedCard(Karte detailedCard) 
	{
		this.detailedCard = detailedCard;
	}

}

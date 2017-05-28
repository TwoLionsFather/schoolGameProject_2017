package com.JanTlk.BesseresHearthstone;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.io.File;

import com.JanTlk.BesseresHearthstone.Hearthstone.STATE;
import com.JanTlk.BesseresHearthstone.Karten.Karte;
import com.JanTlk.BesseresHearthstone.Karten.Status;

public class Spielfeld 
{
	private DeckHandler dH;
	private Deck dPL;
	private Deck dPC;
	private int idxMovedC;
	
	private boolean playersMove;	
	private boolean playerFirstMove = false;
	private boolean attackUpdate;
	private int[] gameStats;
	
	private Karte detailedCard;
	private Rectangle nextRoundB;
	
	private Rectangle [][] kartenFelder;
	private Karte [][] kartenAufFelder;
	private DrawDeck deckDrawer;
	private DrawHud hudDrawer;
	private KI pcController;

	private boolean drawHelp = false;
	
	
	/**
	 * used to set up the game
	 * @param c used to redraw
	 */
	public Spielfeld(Component c)
	{				
		dH = new DeckHandler(c);
		
		dPL = dH.getPlayerDeck();
		dPC = dH.getPCDeck();
		
		nextRoundB = new Rectangle((int) (Hearthstone.BREITE - 50), 50
								, 30, 20);
		
		hudDrawer = new DrawHud(new File("Graphics\\HudPlayer.png"), nextRoundB);
		deckDrawer = new DrawDeck(dH);
		
		kartenFelder = deckDrawer.getKartenFelder();
		kartenAufFelder = new Karte [deckDrawer.getAnzRectInR()][2];
		
		/**
		 * 0: playersLife, Leben des Spieler
		 * 1: playersMana, Mana des Spieler
		 * 2: playersManaCap, max Mana des Spieler
		 * 3: pcsLife, Leben des PC
		 * 4: pcsMana, Mana des PC
		 * 5: pcsManaCap, max Mana des Spieler
		 * ***********************************
		 * 6: playersStapelCount, anzahl NachziehKarten Player
		 * 7: playersAbblageCount, anzahl AbblageKarten Player
		 * 8: pcsStapelCode, anzahl NachziehKarten PC
		 * 9: pcsAbblageCode, anzahl AbblageKarten PC
		 */
		gameStats = new int[10];	
		gameStats[0] = 20;
		gameStats[3] = 20;
		
		gameStats[1] = (playerFirstMove) ? 1 : 0;		
		gameStats[2] = (playerFirstMove) ? 1 : 0;
		
		gameStats[4] = (!playerFirstMove) ? 1 : 0;
		gameStats[5] = (!playerFirstMove) ? 1 : 0;
		
		pcController = new KI(deckDrawer.getAnzRectInR(), kartenFelder, dH);
		playersMove = playerFirstMove;
		
		if(!playersMove)
		{
			pcController.nextRound(kartenAufFelder, gameStats);
		}
		
	}
	
	/**
	 * used to set up next Round
	 * this is a project for the future
	 * @param toggelRound 
	 * @param playerPC if true, its the players turn
	 */
	public void nextRound()
	{		
		//atacking cards get moved back to their origin position
		for (Karte tempC : dH.getAllCards()) 
		{
			switch(tempC.getStatus())
			{
			case ATTACKC:
				tempC.damageTick();
				tempC.placeHome();
				break;
				
			case ATTACKP:
				tempC.attackPlayer((tempC.getDeck() == dPC), gameStats);
				break;
				
			case LAYED:
				tempC.setStatus(Status.FELD);
				break;
				
			default:
				break;
			}

		}
		
		remDeadCardsFromRectangles();
		
		if(attackUpdate)
		{
			attackUpdate = false;
			
			if (playersMove)
				playersMove = false;
			else 
				playersMove = true;
			
			dPL.repaint();
			nextRound();
			return;
		}
		
		//if it is PCs move
		if (playersMove == false)
		{
			gameStats[5]++;					//increase PC Mana Pool by one
			gameStats[4] = gameStats[5]; 	//set Mana Pool PC to max Mana
			dPC.ziehen();					//draws new Card from Deck		
			gameStats = pcController.nextRound(kartenAufFelder, gameStats);		//updates gameStats after PK played
			attackUpdate = true;
			nextRound();
		}
		
		else if (playersMove)
		{
			gameStats[2]++;
			gameStats[1] = gameStats[2];
			dPL.ziehen();
			dPL.repaint();
		}

	}
	
	/**
	 * this draws the Image of every Card, at the moment at 0|0
	 * @param g the Graphics that every Card in the Game gets drawn with
	 */
	public void render(Graphics g) 
	{		
		deckDrawer.render(gameStats, playersMove, g);
		hudDrawer.render(playersMove, detailedCard, gameStats, g);
		
		if(drawHelp)
		{
			drawGuideLines(g);
		}
		
		if (gameStats[0] <= 0
		|| gameStats[3] <= 0
		|| gameStats[7] >= dPL.getAnzKarten()
		|| gameStats[9] >= dPC.getAnzKarten())
		{
			Hearthstone.gameState = STATE.BEATEN;
			dPC.repaint();
		}
		
	}
	
	/**
	 *  used to draw lines around rectangles that cards use to navigate
	 * @param g graphics component that graphics get drwn on
	 */
	private void drawGuideLines(Graphics g)
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
	public boolean playableCardAt(MouseEvent arg0) 
	{
		Point cEvent = arg0.getPoint();
		int highestID = -1;
		
		//needs to check all cards, to always pick the one on top
		for(int i = 0; i < dPL.getAnzKarten(); i++)
		{
			Karte tKarte = dPL.getKarten().get(i);
			
			if (inBounds(cEvent, tKarte.getBounds())
			&& (tKarte.getStatus() != Status.ABBLAGE)
			&& (tKarte.getStatus() != Status.STAPEL)
			&& ((tKarte.getStatus() == Status.HAND) ? (gameStats[1] - tKarte.getMana() >= 0) : true)
			&& playersMove) 
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
	public boolean moveCardAtRect(MouseEvent arg0) 
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
					movedC.attackedCard(null);
				}
				
				/**
				 * moved from to a field and sets default location to the selected rectangle 
				 */
				if(inBounds(rEvent, tempRect.getBounds())
				&& ((cardAtRect == movedC) ? true : cardAtRect == null)
				&& playerPC > 0) 
				{
					//Update Players Mana pool and sets Status so card can't attack same Round it's played 
					if (movedC.getStatus() == Status.HAND)
					{
						gameStats[1] -= movedC.getMana();
						movedC.setStatus(Status.LAYED);
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
					
					//updates Location of Card in card filed
					kartenAufFelder[spalte][playerPC] = movedC;
					return true;
				}
				
				/**
				 * if move is used to attack another card
				 */
				else if(inBounds(rEvent, tempRect.getBounds())
				&& ((cardAtRect != null) ? !cardAtRect.isAttacked() : false)
				&& playerPC == 0
				&& movedC.getStatus() != Status.HAND
				&& movedC.getStatus() != Status.LAYED) 
				{
					movedC.setNewPos(new Rectangle((int) (tempRect.getX() + (tempRect.getWidth() - movedC.getBounds().getWidth()) / 2)
												, (int) tempRect.getY()
												, (int) movedC.getBounds().getWidth()
												, (int) movedC.getBounds().getHeight()));
					
					movedC.attackedCard(cardAtRect);
					movedC.setStatus(Status.ATTACKC);
					return true;
				}
				
				/**
				 * if move is used to attack Player
				 */
				else if(inBounds(rEvent, tempRect.getBounds())
				&& cardAtRect == null
				&& allCardsUnderAttack(0)
				&& playerPC == 0
				&& movedC.getStatus() != Status.HAND
				&& movedC.getStatus() != Status.LAYED) 
				{
					movedC.setNewPos(new Rectangle((int) (tempRect.getX() + (tempRect.getWidth() - movedC.getBounds().getWidth()) / 2)
												, (int) tempRect.getY()
												, (int) movedC.getBounds().getWidth()
												, (int) movedC.getBounds().getHeight()));
					
					movedC.setStatus(Status.ATTACKP);
					return true;
				}
				
			}
			
		}					
		return false;
	}

	/**
	 * checks if it is possible to attack the enemy player
	 * @param playerPC if 0, cards on PCs half get checked
	 * @return true if all cards are under attack
	 */
	private boolean allCardsUnderAttack(int playerPC) 
	{
		for (int i = 0; i < kartenAufFelder.length; i++)
		{
			if ((kartenAufFelder[i][playerPC] != null)
			&& !kartenAufFelder[i][playerPC].isAttacked())
			{
				return false;
			}
		}
			
		return true;
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
	public boolean clickedNR(MouseEvent arg0)
	{
		Point cEvent = arg0.getPoint();
		if (inBounds(cEvent, nextRoundB))
		{
			playersMove = !playersMove;
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
					return;
				}
			}
		}
	}
	
	/**
	 * checks if moved card is checked in on any of the rectangles and delets it from the array
	 * @param remC the card choosen to get removed
	 */
	private void remDeadCardsFromRectangles() 
	{
		for(int playerPC = 0; playerPC < 2; playerPC++)
		{
			for(int i = 0; i < kartenAufFelder.length; i++)
			{
				if ((kartenAufFelder[i][playerPC] != null) 
				&& kartenAufFelder[i][playerPC].getStatus() == Status.ABBLAGE)
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

	public boolean getAttackUpdate()
	{
		return attackUpdate;
	}

	public void setAttackUpdate(boolean attackUpdate) 
	{
		this.attackUpdate = attackUpdate;
	}

}

package com.JanTlk.BesseresHearthstone;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Random;

import com.JanTlk.BesseresHearthstone.Hearthstone.STATE;
import com.JanTlk.BesseresHearthstone.Karten.Karte;
import com.JanTlk.BesseresHearthstone.Karten.Status;

public class Spielfeld 
{
	private DeckHandler dH;
	private Deck dPL;
	private Deck dPC;
	
	private boolean PCFirstMove;
	private int[] gameStats;
	
	private int idxMovedC;
	private boolean attackUpdate;
	private Karte detailedCard;
	private Rectangle nextRoundB;
	private Rectangle skipAttacks;
	
	private Rectangle [][] kartenFelder;
	private Karte [][] kartenAufFelder;
	private DrawDeck deckDrawer;
	private DrawHud hudDrawer;
	private KI pcController;

	/**
	 * used to set up the game
	 * @param c used to redraw
	 */
	public Spielfeld(Component c)
	{				
		dH = new DeckHandler(c);
		
		dPL = dH.getPlayerDeck();
		dPC = dH.getPCDeck();
		
		nextRoundB = new Rectangle((int) (Hearthstone.BREITE - ((Hearthstone.isDrawhelpActive()) ? 80 : 70))
								, 70
								, (Hearthstone.isDrawhelpActive()) ? 50 : 30
								, (Hearthstone.isDrawhelpActive()) ? 30 : 20);
		
		skipAttacks =  new Rectangle((int) (Hearthstone.BREITE - 70)
									, (int) (Hearthstone.HOEHE - 70)
									, 30
									, 20);
		
		hudDrawer = new DrawHud();
		deckDrawer = new DrawDeck(dH);
		
		kartenFelder = deckDrawer.getKartenFelder();
		kartenAufFelder = new Karte [deckDrawer.getAnzRectInR()][2];
		
		Random r = new Random();
		PCFirstMove = r.nextBoolean();
		UIInput.setPlayersMove(!PCFirstMove);
		
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
		
		gameStats[1] = 0;		
		gameStats[2] = 0;
		
		gameStats[4] = 0;
		gameStats[5] = 0;
		
		pcController = new KI(deckDrawer.getAnzRectInR(), kartenFelder, dH);
		
		nextRound(!PCFirstMove);
		
	}
	
	/**
	 * used to set up next Round
	 * this is a project for the future
	 * @param playerPC if true, its the players turn
	 */
	public void nextRound(boolean playersTurn)
	{	
		//Reset detailed Card
		if (detailedCard != null)
		{
			detailedCard.setDisplayed(false);
			detailedCard = null;
		}
		
		if(playersTurn)
		{
			if (gameStats[2] < 5)
			{
				gameStats[2]++;
			}
			
			gameStats[1] = gameStats[2];
			dPL.ziehen();
			dPL.repaint();
			return;
		} 
		
		attackUpdate();
		
		if (gameStats[5] < 5)
		{
			gameStats[5]++;					//increase PC Mana Pool by one
		}
		
		gameStats[4] = gameStats[5]; 	//set Mana Pool PC to max Mana
		dPC.ziehen();					//draws new Card from Deck		
		attackUpdate = pcController.nextRound(kartenAufFelder, gameStats);		//updates gameStats after PK played
		
		if (!attackUpdate)
		{
			UIInput.setPlayersMove(true);
			nextRound(true);			
		}
		
		return;		
	}
	
	/**
	 * this method is used to performe the attack of every card
	 */
	public void attackUpdate() 
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
		
		attackUpdate = false;
		updateCardRectangles();
		dPC.repaint();
	}
	
	/**
	 * this draws the Image of every Card, at the moment at 0|0
	 * @param g the Graphics that every Card in the Game gets drawn with
	 */
	public void render(Graphics g) 
	{	
		if (Hearthstone.gameState == STATE.RESETGAME)
		{
			resetGame();			
			return;
		}
		
		if (Hearthstone.isDrawhelpActive())
		{
			drawGuideLines(g);
		}
		
		if (Hearthstone.isDebugMode()) 
		{
			drawDebugInfo(g);
		}		

		drawButtons(g);
		drawHelpHud(g);
		deckDrawer.render(gameStats, UIInput.isPlayersMove(), g);
		hudDrawer.render(UIInput.isPlayersMove(), detailedCard, gameStats, g);
		
	}
	
	/**
	 * used to reset Game into start condition
	 */
	private void resetGame()
	{
		removeAllCards();
		dH.reset();
		
		if (detailedCard != null)
		{
			detailedCard.setDisplayed(false);
			detailedCard = null;
		}
		
		//life
		gameStats[0] = 20;
		gameStats[3] = 20;
		//manaPlayer
		gameStats[1] = 0;		
		gameStats[2] = 0;
		//manaPC
		gameStats[4] = 0;
		gameStats[5] = 0;
		
		for (int i = 0; i < 5; i++)
		{
			dPC.ziehen();
			dPL.ziehen();
		}
				
		Hearthstone.gameState = STATE.MENU;
		
		Random r = new Random();
		PCFirstMove = r.nextBoolean();
		nextRound(!PCFirstMove);
		
		dPL.repaint();			
		return;
	}
	
	/**
	 * the card "Map" gets reset
	 */
	private void removeAllCards() 
	{
		for (int playerPC = 0; playerPC < 2; playerPC++) 
		{
			for (int i = 0; i < kartenAufFelder.length; i++) 
			{
				kartenAufFelder[i][playerPC] = null;
			}
		}
	}

	/**
	 * this draws additional Card info to debug game
	 * @param g
	 */
	private void drawDebugInfo(Graphics g) 
	{
		for (int playerPC = 0; playerPC < 2; playerPC++) 
		{
			for (int i = 0; i < kartenAufFelder.length; i++) 
			{
				if (kartenAufFelder[i][playerPC] != null)
				{
					Karte tempC = kartenAufFelder[i][playerPC];
					
					int rimC = 6;
					g.setColor(Color.black);
					g.drawRect((int) tempC.getHome().getX() - rimC / 2
							, (int) tempC.getHome().getY() - rimC / 2
							, (int) tempC.getHome().getWidth() + rimC
							, (int) tempC.getHome().getHeight() + rimC);
					
					if (tempC.getHome() != null)
					{
						int rimH = 2;
						g.setColor(Color.white);
						g.fillRect((int) tempC.getHome().getX() - rimH / 2
								, (int) tempC.getHome().getY() - rimH / 2
								, (int) tempC.getHome().getWidth() + rimH
								, (int) tempC.getHome().getHeight() + rimH);
						
					}
					
				}
			}
		}

	}

	/**
	 * draws Card Details
	 * and other usefull info
	 * @param g
	 */
	private void drawHelpHud(Graphics g) 
	{	
		if ((detailedCard != null)
		&& detailedCard.getStatus() != Status.ABBLAGE)
		{
			Rectangle dChome = detailedCard.getBounds();
			int rimWidth = 3;
			
			g.setColor(Color.orange);
			g.fillRect((int) dChome.getX() - rimWidth
					, (int) dChome.getY() - rimWidth
					, (int) dChome.getWidth() + rimWidth * 2
					, (int) dChome.getHeight() + rimWidth * 2);
		}
		
		for (Karte tCard : dH.getAllCards())
		{
			Rectangle dcHome = null;
			int rimWidth = 3;
			
			if (tCard.getHome() != null)
			{
				dcHome = tCard.getHome();
			}
			
			switch (tCard.getStatus())
			{
			case ATTACKC:
			case ATTACKP:				
				g.setColor(Color.darkGray);
				g.drawRect((int) dcHome.getX() - rimWidth
						, (int) dcHome.getY() - rimWidth
						, (int) dcHome.getWidth() + rimWidth * 2
						, (int) dcHome.getHeight() + rimWidth * 2);
				
				g.drawLine((int) dcHome.getX()
						, (int) dcHome.getY()
						, (int) (dcHome.getX() + dcHome.getWidth())
						, (int) (dcHome.getY() + dcHome.getHeight()));
				break;
				
			case FELD:				
				g.setColor(Color.orange);
				g.drawRect((int) dcHome.getX() - rimWidth
						, (int) dcHome.getY() - rimWidth
						, (int) dcHome.getWidth() + rimWidth * 2
						, (int) dcHome.getHeight() + rimWidth * 2);
				break;
				
			default:
				break;
			
			}
			
		}
	}
	
	/**
	 * as the name implies, this is used to draw the Buttons on screen
	 * @param g
	 */
	private void drawButtons(Graphics g)
	{
		g.setColor((UIInput.isPlayersMove()) ? Color.green : Color.red);
		
		//next Round Button
		if (Hearthstone.isDrawhelpActive()
		|| Hearthstone.isDebugMode())
		{
			g.setFont(new Font("Century", Font.PLAIN, 12));
			g.drawString("Spieler"
					, (int) (nextRoundB.getX() + nextRoundB.getWidth() / 2 - "Spieler".length() * 3)
					, (int) (nextRoundB.getY() + nextRoundB.getHeight() / 2 + 6)); 
		}
		
		g.setColor((UIInput.isPlayersMove()) ? Color.green : Color.red);
		g.drawRect((int) nextRoundB.getX()
				, (int) nextRoundB.getY()
				, (int) nextRoundB.getWidth()
				, (int) nextRoundB.getHeight());
		
		//skip PCs Attacks Button
		if (!Hearthstone.isDrawhelpActive()
		|| Hearthstone.isDebugMode())
		{
			g.setFont(new Font("Arial", Font.PLAIN, 10));
			g.drawString("Skip"
					, (int) (skipAttacks.getX() + nextRoundB.getWidth() / 2 - "Skip".length() * 2.5)
					, (int) (skipAttacks.getY() + nextRoundB.getHeight() / 2 + 6));
			
			g.setColor((UIInput.isPlayersMove()) ? Color.green : Color.red);
			g.drawRect((int) skipAttacks.getX()
					, (int) skipAttacks.getY()
					, (int) skipAttacks.getWidth()
					, (int) skipAttacks.getHeight());
		}
		
		//cross out if attackUpdate
		if (attackUpdate)
		{
			g.drawLine((int) nextRoundB.getX()
					, (int) nextRoundB.getY()
					, (int) (nextRoundB.getX() + nextRoundB.getWidth())
					, (int) (nextRoundB.getY() + nextRoundB.getHeight()));
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
			&& ((tKarte.getStatus() == Status.HAND) ? (gameStats[1] - tKarte.getMana() >= 0) : true)) 
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
					
					if (movedC.getStatus() == Status.ATTACKC)
					{
						remAttackers();
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
	 * removes Cards that attack the pc and sends them back home
	 */
	private void remAttackers() 
	{
		for(int playerPC = 0; playerPC < 2; playerPC++)
		{
			for(int i = 0; i < kartenAufFelder.length; i++)
			{
				if ((kartenAufFelder[i][playerPC] != null)
				&& kartenAufFelder[i][playerPC].getStatus() == Status.ATTACKP)
				{
					kartenAufFelder[i][playerPC].placeHome();
					kartenAufFelder[i][playerPC].setStatus(Status.FELD);
				}
			}
		}

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
		
		int idx = -1;
		for (Karte tCard : dH.getAllCards())
		{
			idx++;
			
			if(inBounds(cEvent, tCard.getBounds())
			&& tCard.getStatus() != Status.ABBLAGE
			&& tCard.getStatus() != Status.STAPEL) 
			{
				if (detailedCard != tCard)
				{
					if (detailedCard != null)
					{
						detailedCard.setDisplayed(false);
					}
					
					detailedCard = dH.getAllCards().get(idx);
					detailedCard.setDisplayed(true);
					detailedCard.getComponent().repaint();
				}
				
			}
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
		return inBounds(cEvent, nextRoundB);
	}
	
	/**
	 * checks nextRoundButton on clickevent
	 * @param arg0 clickevent to be tested
	 * @return true if next round
	 */
	public boolean clickedSkipA(MouseEvent arg0)
	{
		if (Hearthstone.isDrawhelpActive()
		&& !Hearthstone.isDebugMode())
		{
			return false;
		}
		
		Point cEvent = arg0.getPoint();
		return inBounds(cEvent, skipAttacks);
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
	 * delets old cards from rect.
	 */
	public void updateCardRectangles() 
	{
		for (Karte tCard : dH.getAllCards())
		{
			if (tCard.getStatus() == Status.ABBLAGE)
			{
				remCardFromRectangles(tCard);
			}
			
			if (dPC.isInDeck(tCard)
			&& tCard.getStatus() == Status.FELD)
			{
				for (int idx = 0; idx < kartenAufFelder.length; idx++)
				{
					if(kartenAufFelder[idx][1] == tCard)
					{
						kartenAufFelder[idx][1] = null;
					}
					
				}

			}
			
		}
			
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

	public int[] getGameStats() 
	{
		return gameStats;
	}

	public DeckHandler getdH() 
	{
		return dH;
	}

}

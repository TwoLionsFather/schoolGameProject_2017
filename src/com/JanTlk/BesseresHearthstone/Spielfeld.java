package com.JanTlk.BesseresHearthstone;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.Random;

import com.JanTlk.BesseresHearthstone.Karten.Karte;
import com.JanTlk.BesseresHearthstone.Karten.Status;

public class Spielfeld 
{

	private Deck dPL;
	private Deck dPC;
	
	public Spielfeld()
	{
		DeckHandler dH = new DeckHandler();	
		
		dPL = dH.getPlayerDeck();
		dPC = dH.getPCDeck();
		
		dPL.setKarten(mischen(dPL.getKarten()));
		dPC.setKarten(mischen(dPC.getKarten()));
	}
	
	/**
	 * this draws the Image of every Card, at the moment at 0|0
	 * @param g the Graphics that every Card in the Game gets drawn with
	 */
	public void render(Graphics g) 
	{
		drawDeck(dPL, g, true);
		drawDeck(dPC, g, false);
	}

	public void tick() 
	{
		
	}
	
	/**
	 * draws a deck of cards based on their status
	 * uses the Graphics class to do so
	 * @param deck the deck of cards that needs to get drawn
	 * @param g the Graphics parameter used to draw the cards on
	 * @param player this ich true if the card belongs to the player, if a card does not belong to the player it is not shown to him 
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
									, g);
				}
				
				else 
				{
					tempKarte.drawCard((int) (Hearthstone.BREITE / 2 - (dPC.getAnzKarten() * (dPC.getKarten().get(0).getBounds().getWidth() - 50) / 2) + 55 * kartenCount++)
									, 0
									, g);
				}
			}
			
			else if(tempKarte.getStatus() == Status.Feld)
			{
				tempKarte.drawCard(tempKarte.getBounds().width
								, tempKarte.getBounds().height
								, g);
			}
		}
		
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
	 * this class brings the cards from the linked list in a random order
	 * @param linkedList this is the list of cards that is getting  a shuffeled return
	 * @return the linked list gets returned as a list in a random order but with the same cards
	 */
	private LinkedList<Karte> mischen(LinkedList<Karte> linkedList) 
	{
		Random r = new SecureRandom();							//Random Objekt anlegen
		LinkedList<Karte> temp = new LinkedList<Karte>();		//neue temporäre Liste um gemischtes Deck zu speichern
		
		int anzKarten = linkedList.size();					//Anzahl an zu mischenden Karten
		
		for(int i = 0; i < anzKarten; i++) 					//solange Karten zu mischen sind
		{
			int randomZahl = r.nextInt(linkedList.size());	//neue Zufallszahl im Bereich der noch zu sortierenden Karten
			
			temp.add(linkedList.get(randomZahl));			//fügt dem temp(gemischten) Stapel die Karte an der Zufälligen Position zu
			linkedList.remove(randomZahl);					//entfernt die Karte aus dem Stapel
		}
		return temp;
	}

	/**
	 * This is used to check if a valid card has been clicked on and is responsible for updating its position
	 * @param arg0 a Mouse event to get the starting (and finishing) Coordinates 
	 */
	public void moveCard(MouseEvent arg0) 
	{
		Point cEvent = arg0.getPoint();
		
		for(int i = 0; i < dPL.getAnzKarten(); i++)
		{
			Karte tKarte = dPL.getKarten().get(i);
			Karte nKarte = null;
			
			if(i < dPL.getAnzKarten() - 1)
			{
				nKarte = dPL.getKarten().get(i + 1);
			}
			
			if(inBounds(cEvent, tKarte.getBounds()))
			{
				System.out.println("2 TestErfolgreich " + tKarte.toString());
				if(! (inBounds(cEvent, nKarte.getBounds())))
				{
					System.out.println("TestErfolgreich " + tKarte.toString());
				}
			}
		}
		
	}
	
	/**
	 * This is used to check if the recorded Point of the moujseevent is within the boarders of a card's Rectangle
	 * @param toTest the point of the mouseevent
	 * @param borders borders of card or onscreen rectangel
	 * @return true if the point is in the boarders of the rectangle
	 */
	private boolean inBounds(Point toTest, Rectangle borders)
	{

		if(toTest.getX() > borders.getX()
		&& toTest.getX() < borders.getX() + borders.getWidth())
		{
			if(toTest.getY() > borders.getY()
					&& toTest.getY() < borders.getY() + borders.getHeight())
			{
				return true;
			}	
		}
		return false;
	}
	
}

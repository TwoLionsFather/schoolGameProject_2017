package com.JanTlk.BesseresHearthstone;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
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
	 * @param g
	 */
	public void render(Graphics g) 
	{
		drawDeck(dPL, g, true);
		drawDeck(dPC, g, false);
	}

	public void tick() 
	{
		
	}
	
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
					tempKarte.drawCard((int) (Hearthstone.BREITE / 2 - (dPL.getAnzKarten() * dPL.getKarten().get(0).getBounds().getWidth() / 2) + 55 * kartenCount++)
							, (int) (Hearthstone.HOEHE - (Hearthstone.HOEHE / 5))
							, g);
				}
				
				else 
				{
					tempKarte.drawCard((int) (Hearthstone.BREITE / 2 - (dPC.getAnzKarten() * dPC.getKarten().get(0).getBounds().getWidth() / 2) + 55 * kartenCount++)
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
			g.setColor(Color.blue);
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
	
	private LinkedList<Karte> mischen(LinkedList<Karte> linkedList) 
	{
		Random r = new Random();							//Random Objekt anlegen
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
	
}

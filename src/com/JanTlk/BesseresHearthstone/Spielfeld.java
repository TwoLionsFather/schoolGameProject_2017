package com.JanTlk.BesseresHearthstone;

import java.awt.Graphics;
import java.util.LinkedList;
import java.util.Random;

import com.JanTlk.BesseresHearthstone.Karten.Karte;
import com.JanTlk.BesseresHearthstone.Karten.Status;

public class Spielfeld 
{

	private Deck dPL;
	private Deck dPC;
	
	private int kartenCount;
	private int abblageCount;
	private int stapelCount;
	
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
		for(Karte tempKarte : dPL.getKarten())
		{
			if(tempKarte.getStatus() == Status.Abblage)
			{
				abblageCount++;	
			}
			else if(tempKarte.getStatus() == Status.Stapel)
			{
				stapelCount++;	
			}
			else if(tempKarte.getStatus() == Status.Hand)
			{
				tempKarte.drawCard(55 * kartenCount++
								, 200
								, g);
			}
			else if(tempKarte.getStatus() == Status.Feld)
			{
				tempKarte.drawCard(tempKarte.getBounds().width
								, tempKarte.getBounds().height
								, g);
			}			
		}
		kartenCount = 0;
		abblageCount = 0;
		stapelCount = 0;
		
		
		for(Karte tempKarte : dPC.getKarten())
		{
			if(tempKarte.getStatus() == Status.Abblage)
			{
				abblageCount++;	
			}
			else if(tempKarte.getStatus() == Status.Stapel)
			{
				stapelCount++;	
			}
			else if(tempKarte.getStatus() == Status.Hand)
			{
				tempKarte.drawCard(55 * kartenCount++
								, 200
								, g);
			}
			else if(tempKarte.getStatus() == Status.Feld)
			{
				tempKarte.drawCard(tempKarte.getBounds().width
								, tempKarte.getBounds().height
								, g);
			}
		}
		kartenCount = 0;
	}

	public void tick() 
	{
		
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

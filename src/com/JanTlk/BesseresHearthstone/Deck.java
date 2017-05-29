package com.JanTlk.BesseresHearthstone;

import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.Random;

import com.JanTlk.BesseresHearthstone.Karten.Karte;
import com.JanTlk.BesseresHearthstone.Karten.Status;

public class Deck 
{

	private LinkedList<Karte> karten = new LinkedList<Karte>();
	private int drawCounter = 0;

	private String name;
	
	/**
	 * If a Deck is created...
	 * @param name ...the name oft the owner needs to be set
	 */
	public Deck(String name) 
	{
		this.name = name;
	}

	@Override
	public String toString() 
	{
		String deckString = "Deck gehört [ " + name + " ]\n";
		
		for(Karte tCard : karten)
		{
			deckString += tCard.toString() + "\n";
		}
		
		return deckString;
	}

	/**
	 * sets next cards status to hand and repaints canvas
	 */
	public void ziehen()
	{
		Karte dCard = this.getKarten().get(drawCounter++);
		drawCounter = (int) Hearthstone.clamp(drawCounter, 0, karten.size() - 1);
		
		if(dCard.getStatus() == Status.STAPEL)
		{
			dCard.setStatus(Status.HAND);
		}
		
		return;			
	}
	
	/**
	 * redraws game
	 */
	public void repaint() 
	{
		karten.get(0).getComponent().repaint();
	}
	
	/**
	 * creates a new Deck with clones of every card
	 * @param name the name of the new decks owner
	 * @param cloneDeck this is the new Deck that schal be added
	 * @return a clone of the original deck
	 */
	public Deck clone(Deck cloneDeck)
	{
		for(Karte temp : karten)
		{
			cloneDeck.addKarte(temp.clone());
			cloneDeck.getKarten().getLast().setDeck(cloneDeck);
		}
		return cloneDeck;
	}
	
	/**
	 * this class brings the cards from a deck in a random order
	 * @param zuMischen the deck that needs to be shuffeled
	 * @return the shuffeled deck
	 */
	public void mischen()
	{
		Random r = new SecureRandom();							//Random Objekt anlegen
		LinkedList<Karte> old = this.getKarten();
		LinkedList<Karte> temp = new LinkedList<Karte>();		//neue temporäre Liste um gemischtes Deck zu speichern
		
		int anzKarten = old.size();					//Anzahl an zu mischenden Karten
		
		for(int i = 0; i < anzKarten; i++) 					//solange Karten zu mischen sind
		{
			int randomZahl = r.nextInt(old.size());	//neue Zufallszahl im Bereich der noch zu sortierenden Karten
			
			temp.add(old.get(randomZahl));			//fügt dem temp(gemischten) Stapel die Karte an der Zufälligen Position zu
			old.remove(randomZahl);					//entfernt die Karte aus dem Stapel
		}
		
		this.setKarten(temp);
	}
	
	public boolean isInDeck(Karte cKarte)
	{
		return karten.contains(cKarte);
	}
	
	public int getAnzKarten() 
	{
		return karten.size();
	}

	public LinkedList<Karte> getKarten() 
	{
		return karten;
	}

	/*
	 * resorts the order of cards to make last played card the first card to be picked up again
	 */
	public void cardPlayed(int idxMovedC) 
	{
		Karte playedC = karten.get(idxMovedC);
		this.karten.remove(idxMovedC);
		this.karten.add(playedC);	
	}
	
	public void setDrawCounter(int drawCounter) 
	{
		this.drawCounter = drawCounter;
	}

	public void setKarten(LinkedList<Karte> karten) 
	{
		this.karten = karten;
	}
	
	public void addKarte(Karte karte)
	{
		this.karten.add(karte);
	}

}

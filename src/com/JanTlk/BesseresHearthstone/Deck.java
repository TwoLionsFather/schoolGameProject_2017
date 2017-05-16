package com.JanTlk.BesseresHearthstone;

import java.util.LinkedList;

import com.JanTlk.BesseresHearthstone.Karten.Karte;

public class Deck {

	private LinkedList<Karte> karten = new LinkedList<Karte>();
	
	public int getAnzKarten() 
	{
		return karten.size();
	}

	public LinkedList<Karte> getKarten() 
	{
		return karten;
	}

	public void cardPlayed(int idxMovedC) 
	{
		Karte playedC = karten.get(idxMovedC);
		this.karten.remove(idxMovedC);
		this.karten.add(playedC);	
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

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
	
	public Karte[] getKartenArray()
	{
		return karten.toArray(new Karte[getAnzKarten()]);
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

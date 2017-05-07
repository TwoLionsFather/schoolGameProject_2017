package com.JanTlk.BesseresHearthstone;

import java.util.LinkedList;

import com.JanTlk.BesseresHearthstone.Karten.Karte;

public class Deck {

	private LinkedList<Karte> karten = new LinkedList<Karte>();
	
	public Deck(LinkedList<Karte> karten)
	{
		this.setKarten(karten);
	}
	
	public int getAnzKarten() 
	{
		return karten.size();
	}

	public LinkedList<Karte> getKarten() 
	{
		return karten;
	}

	public void setKarten(LinkedList<Karte> karten) 
	{
		this.karten = karten;
	}
}

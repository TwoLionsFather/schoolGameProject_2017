package com.JanTlk.BesseresHearthstone;

import java.awt.Graphics;

import com.JanTlk.BesseresHearthstone.Karten.Karte;

public class Spielfeld 
{

	private Deck dPL;
	private Deck dPC;
	
	public Spielfeld(Deck d1, Deck d2)
	{
		this.dPL = d1;
		this.dPC = d2;
	}
	
	public void render(Graphics g) 
	{
		//anzeige Spielerkarten
		for(int i = 0; i < dPL.getAnzKarten(); i++)
		{
			Karte tempKarte = dPL.getKarten().get(i);
			System.out.println(tempKarte.toString());
		}
		
		for(int i = 0; i < dPC.getAnzKarten(); i++)
		{
			Karte tempKarte = dPC.getKarten().get(i);
			System.out.println(tempKarte.toString());
		}
	}

	public void tick() 
	{
		// TODO Auto-generated method stub
		for(int i = 0; i < dPL.getAnzKarten(); i++)
		{
			Karte tempKarte = dPL.getKarten().get(i);
			tempKarte.tick();
		}
	}
	
}

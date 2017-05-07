package com.JanTlk.BesseresHearthstone;

import java.awt.Graphics;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.JanTlk.BesseresHearthstone.Karten.Karte;
import com.JanTlk.BesseresHearthstone.Karten.Typ;

public class Spielfeld 
{

	private Deck dPL;
	private Deck dPC;
	
	public Spielfeld()
	{
		dPL = new Deck();
		dPC = new Deck();
		
		//used to debug
		Karte tempK = new Karte("FireStarter", Typ.Monster, 5, 25, 3);
		Karte tempK2 = new Karte("MotherOfD", Typ.Monster, 5, 25, 25);
		Karte tempK3 = new Karte("BigBuuudy", Typ.Monster, 5, 5, 53);
		Karte tempK4 = new Karte("ManyOfAss", Typ.Monster, 5, 23, 23);
		
		try {
			tempK.setCardImage(ImageIO.read(new File("CardBluePrint.png")));
			tempK2.setCardImage(ImageIO.read(new File("CardBluePrint.png")));
			tempK3.setCardImage(ImageIO.read(new File("CardBluePrint.png")));
			tempK4.setCardImage(ImageIO.read(new File("CardBluePrint.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		dPL.addKarte(tempK);
		dPL.addKarte(tempK2);
		dPL.addKarte(tempK3);
		dPL.addKarte(tempK4);
	}
	
	/**
	 * this draws the Image of every Card, at the moment at 0|0
	 * @param g
	 */
	public void render(Graphics g) 
	{
		for(int i = 0; i < dPL.getAnzKarten(); i++)
		{
			Karte tempKarte = dPL.getKarten().get(i);
			tempKarte.drawCard(0 + i * 105, 200, g);
		}
		
		for(int i = 0; i < dPC.getAnzKarten(); i++)
		{
			Karte tempKarte = dPC.getKarten().get(i);
			tempKarte.drawCard(0 + i * 105, 0, g);
		}
	}

	public void tick() 
	{
		
	}
	
}

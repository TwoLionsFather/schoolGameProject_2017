package com.JanTlk.BesseresHearthstone;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.JanTlk.BesseresHearthstone.Karten.Karte;
import com.JanTlk.BesseresHearthstone.Karten.Typ;

public class DeckHandler {

	private Deck player;
	private Deck pc;
	
	public DeckHandler()
	{
		player = new Deck();
		
		player.addKarte(new Karte("FireStarter", Typ.Monster, 5, 25, 3));
		player.addKarte(new Karte("MotherOfD", Typ.Monster, 5, 25, 25));
		player.addKarte(new Karte("BigBuuudy", Typ.Monster, 5, 5, 53));
		player.addKarte(new Karte("ManyOfAss", Typ.Monster, 5, 23, 23));
		
		
		try {
			for(int i = 0; i < player.getAnzKarten(); i++)
			{
				Karte tempK = player.getKarten().get(i);
				tempK.setCardImage(ImageIO.read(new File("CardBluePrint.png")));
			}				
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		pc = player;
	}

	public Deck getPlayerDeck() {
		// TODO Auto-generated method stub
		return player;
	}

	public Deck getPCDeck() {
		// TODO Auto-generated method stub
		return pc;
	}
}

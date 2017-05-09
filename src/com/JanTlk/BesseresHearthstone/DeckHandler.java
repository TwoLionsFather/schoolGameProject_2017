package com.JanTlk.BesseresHearthstone;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.JanTlk.BesseresHearthstone.Karten.Karte;
import com.JanTlk.BesseresHearthstone.Karten.Typ;

/**
 * In this class every card should be added to the game and to a deck
 * this deck will get added to a Spielfeld, which will then take controll of the deck
 * @author Gaming
 *
 */
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
		player.addKarte(new Karte("ManyOfAss", Typ.Monster, 5, 23, 23));
		player.addKarte(new Karte("FireStarter", Typ.Monster, 5, 25, 3));
		player.addKarte(new Karte("MotherOfD", Typ.Monster, 5, 25, 25));
		player.addKarte(new Karte("BigBuuudy", Typ.Monster, 5, 5, 53));
		player.addKarte(new Karte("ManyOfAss", Typ.Monster, 5, 23, 23));
		
		try {
			for(Karte tempK : player.getKarten())
			{
				tempK.setCardImage(ImageIO.read(new File("CardBluePrint.png")));
			}				
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		pc = player;
	}

	public Deck getPlayerDeck() 
	{
		return player;
	}

	public Deck getPCDeck() 
	{
		return pc;
	}
}

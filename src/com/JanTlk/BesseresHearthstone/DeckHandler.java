package com.JanTlk.BesseresHearthstone;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
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

	private int[] spaltenZeilenPic;
	private Rectangle cardProps;
	private Deck player;
	private Deck pc;
	
	public DeckHandler(Component c)
	{
		cardProps = new Rectangle(100, 164);	//this defines the image size of identical pictures right next to each other
		spaltenZeilenPic = new int[]{10, 2};	//this defines how many collums and rows the composed image has
												//these values must be choosen carefully!
		
		player = new Deck();
		
		player.addKarte(new Karte("FireStarter", Typ.Monster, 5, 25, 3));
		player.addKarte(new Karte("MotherOfD", Typ.Monster, 5, 25, 25));
		player.addKarte(new Karte("BigBuuudy", Typ.Monster, 5, 5, 53));
		player.addKarte(new Karte("ManyOfAss", Typ.Monster, 5, 23, 23));
		
		try {
		      for(Karte tempK : player.getKarten())
		      {
		        tempK.setCardImage(ImageIO.read(new File("CardBluePrint.png")));
		        tempK.setComponent(c);
		      }       
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		
		pc = player.clone();
	}
	
	/**
	 * In the future Cards will get their images from this class
	 * not put to work until errorproof
	 * @param allGraphics This is the file in which all cards texturess are stored
	 * @param c used for rendering in later stages
	 */
	private void setCardsImages(File allGraphics, Component c)
	{
		try {
			BufferedImage bigPic = ImageIO.read(allGraphics);
			
			for(int zeile = 0; zeile < spaltenZeilenPic[1]; zeile++)	//there needs to be a graphic for every Card
			{																				
				for(int spalte = 0; spalte < spaltenZeilenPic[0]; spalte++)
				{
					if(spalte + zeile * spaltenZeilenPic[0] >= player.getAnzKarten())
					{
						break;
					}
						
					Karte tempK = player.getKarten().get(spalte + spalte * zeile);
					
					BufferedImage cardImage = bigPic.getSubimage((int) cardProps.getWidth() * zeile
																, (int) cardProps.getHeight() * spalte
																, (int) cardProps.getWidth()
																, (int) cardProps.getHeight());
					
					tempK.setCardImage(cardImage);
					tempK.setComponent(c);
				}
			}				
		} catch (IOException e) {
			e.printStackTrace();
		}
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

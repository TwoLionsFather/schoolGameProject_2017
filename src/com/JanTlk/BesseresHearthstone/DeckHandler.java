package com.JanTlk.BesseresHearthstone;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
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
public class DeckHandler 
{

	private int[] spaltenZeilenPic;
	private Rectangle cardProps;
	private Deck player;
	private Deck pc;
	
	public DeckHandler(Component c)
	{
		cardProps = new Rectangle(220, 414);	//this defines the image size of identical pictures right next to each other
		spaltenZeilenPic = new int[]{14, 1};	//this defines how many collums and rows the composed image has
												//these values must be choosen carefully!
		
		player = new Deck();
		
		player.addKarte(new Karte("Geralt", Typ.Monster, 1, 25, 3));
		player.addKarte(new Karte("Ciri", Typ.Monster, 1, 25, 25));
		player.addKarte(new Karte("Vesemir", Typ.Monster, 25, 5, 53));
		player.addKarte(new Karte("Mother", Typ.Monster, 15, 25, 50));
		
		try {
			
			setCardsImages(new File("Graphics\\allCards.png"), c);
//		      for(Karte tempK : player.getKarten())
//		      {
//		        tempK.setCardImage(ImageIO.read(new File("CardBluePrint.png")));
//		        tempK.setComponent(c);
//		      }       
//		    } catch (IOException e) {
//		      e.printStackTrace();
		    } catch (NullPointerException e) {
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
					
					BufferedImage subImage = bigPic.getSubimage((int) cardProps.getWidth() * spalte
																, (int) cardProps.getHeight() * zeile
																, (int) cardProps.getWidth()
																, (int) cardProps.getHeight());
					
					subImage = toBufferedImage(subImage.getScaledInstance(100, 200, Image.SCALE_DEFAULT));
					tempK.setCardImage(subImage);
					tempK.setComponent(c);
				}
			}				
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * used to convert scaled subimage to cardimage
	 * @param img the image object that will get converted
	 * @return a new buffered image of the image
	 */
	public static BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
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

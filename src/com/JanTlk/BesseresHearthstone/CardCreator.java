package com.JanTlk.BesseresHearthstone;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

import com.JanTlk.BesseresHearthstone.Karten.Karte;
import com.JanTlk.BesseresHearthstone.Karten.Typ;

public class CardCreator 
{
	private Component mainComponent;
	
	private String cardName;
	private Typ cardTyp;
	private boolean cardLeg;
	private int cardMana;
	private int cardAttack;
	private int cardLife;	
	private BufferedImage cardTextur;
	private Karte nextCard;
	
	private int[] spaltenZeilenPic;
	private int[] spalteZeile;
	private Rectangle cardProps;
	private BufferedImage allCards;
	private BufferedReader br;
	private String line;
	private Scanner s;
	
	
	public CardCreator(String pathCardCretorFile, Component c) throws FileNotFoundException 
	{
		mainComponent = c;
		cardProps = new Rectangle(220, 414);
		spaltenZeilenPic = new int[]{14, 1};
		spalteZeile = new int[]{0, 0};
		
		try {
			allCards = ImageIO.read(new File("Graphics\\allCards.png"));
		} catch (IOException e) {
			System.err.println("no Graphics File");
			e.printStackTrace();
		}
		br = new BufferedReader(new FileReader(pathCardCretorFile));
		
	}

	/**
	 * creates next card from next line in Text File
	 * @return a new card with properties read from the File Karten.txt
	 */
	public Karte nextCard()
	{
		try {
			line = br.readLine();
			if((line == null)
			|| line.isEmpty())
			{
				System.out.println("Card Creator File ran out of lines");
				return null;
			}
		} catch (IOException e) {
			System.err.println("Card cound't be created");
			e.printStackTrace();
			return null;
		}
		
		s = new Scanner (line);
		s.useDelimiter(";");	
		
		for (int data = 0; data <= 6 && s.hasNext(); data++)
		{
			switch (data)
			{
			case 0:
				cardName = s.next();
				break;

			case 1:
				cardTyp = (s.next().equalsIgnoreCase("M") ? Typ.Monster : Typ.Zauber);
				break;

			case 2:
				cardLeg = (s.next().equalsIgnoreCase("L") ? true : false);
				break;

			case 3:
				cardMana = s.nextInt();
				break;

			case 4:
				cardAttack = s.nextInt();
				break;
			case 5:
				cardLife = s.nextInt();
				break;

			default:
				System.err.println("Input Error during Card creation");
				break;
			}
		}

		if(spalteZeile[0] <= spaltenZeilenPic[0]
		&& spalteZeile[1] <= spaltenZeilenPic[1])
		{
			BufferedImage subImage = allCards.getSubimage((int) cardProps.getWidth() * spalteZeile[0]
														, (int) cardProps.getHeight() * spalteZeile[1]
														, (int) cardProps.getWidth()
														, (int) cardProps.getHeight());
			
			spalteZeile[0]++;
			if(spalteZeile[0] > spaltenZeilenPic[0])
			{
				spalteZeile[0] = 0;
				spalteZeile[1]++;				
			}
			
			
			cardTextur = toBufferedImage(subImage.getScaledInstance(100, 200, Image.SCALE_DEFAULT));
		}
		
		else
		{
			try {
				cardTextur = ImageIO.read(new File("Graphics\\CardBluePrint.png"));
			} catch (IOException e) {
				System.err.println("The Default BluePrint for emergencies got lost!");
			}
		}
		
		nextCard = new Karte(cardName
							, cardTyp
							, cardLeg
							, cardMana
							, cardAttack
							, cardLife);
		
		nextCard.setCardImage(cardTextur);
		nextCard.setComponent(mainComponent);
		
		System.out.println(nextCard.toString());
		return nextCard;
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

}

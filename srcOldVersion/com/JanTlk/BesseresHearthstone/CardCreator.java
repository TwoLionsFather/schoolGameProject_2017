package com.JanTlk.BesseresHearthstone;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
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
	private BufferedImage cardTexture;
	private Karte nextCard;

	private int[] spaltenZeilenPic;
	private int[] spalteZeile;
	private Rectangle cardProps;
	private BufferedImage allCards;
	private BufferedReader br;
	private String line;
	private Scanner s;

	public CardCreator(Component c) throws FileNotFoundException
	{
		mainComponent = c;
		cardProps = new Rectangle(220, 414);
		spaltenZeilenPic = new int[]{21, 2};
		spalteZeile = new int[]{0, 0};

		try {
			allCards = ImageIO.read(Hearthstone.allImportedFiles[3]);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("no Graphics File");
		}

		br = new BufferedReader(new FileReader(Hearthstone.allImportedFiles[2]));
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
			e.printStackTrace();
			System.err.println("Card cound't be created");
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
		&& spalteZeile[1] <= spaltenZeilenPic[1]
		&& allCards != null)
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


			cardTexture = Hearthstone.rescaledBufferedimage(subImage
															, (Hearthstone.BREITE < 1920) ? 70 : 100
															, (Hearthstone.BREITE < 1920) ? 140 : 200);
		}

		else
		{
			try {
				cardTexture = ImageIO.read(Hearthstone.allImportedFiles[1]);
			} catch (IOException e) {
				System.err.println("The Default BluePrint for emergencies got lost!");
			}
		}

		nextCard = new Karte(cardName
							, cardTyp
							, cardLeg
							, cardMana
							, cardAttack
							, cardLife
							, null);

		nextCard.setCardImage(cardTexture);
		nextCard.setComponent(mainComponent);

		//Debugg output
//		System.out.println(nextCard.toString());
		return nextCard;
	}

}

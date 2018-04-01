package com.JanTlk.BesseresHearthstone.secondTry;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import com.JanTlk.BesseresHearthstone.Karten.Typ;


public class DeckBuilder
{
	public ArrayList<Card> cards;

	public DeckBuilder()
	{
		cards = new ArrayList<Card>();
		importCards();
	}

	public Deck getDeckFromCards()
	{
		Deck newDeck = new Deck(cards);
		newDeck.shuffle();
		return newDeck;
	}

	private void importCards()
	{
		try {
			BufferedReader fileInput = new BufferedReader(new FileReader(FileController.getFile("Karten.txt")));
			String line = fileInput.readLine();
			Scanner scanner;
			while((line != null)
			&& !line.isEmpty())
			{
				CardCreator cardCreator = new CardCreator();
				scanner = new Scanner(line);
				scanner.useDelimiter(";");
				for (int dataPoint = 0; dataPoint < 4 && scanner.hasNext(); dataPoint++)
				{
					switch (dataPoint)
					{
					case 0:
						cardCreator.setName(scanner.next());
						break;

					case 1:
						cardCreator.setTyp(scanner.next().equalsIgnoreCase("M") ? Typ.Monster : Typ.Zauber);
						break;

					case 2:
						cardCreator.setLegendary (scanner.next().equalsIgnoreCase("L") ? true : false);
						break;

					case 3:
						cardCreator.setMana(scanner.nextInt());
						cardCreator.setInitDamage(scanner.nextInt());
						cardCreator.setInitLife(scanner.nextInt());
						break;
					default:
						System.err.println("DeckBuilder.importCards Input Error during Card creation");
						break;
					}
				}
				scanner.close();
				cards.add(cardCreator.createCard());

				line = fileInput.readLine();
			}

			fileInput.close();
		} catch (IOException e) {
			System.err.println("DeckBuilder.importCards Error Reading File: \"Karten.txt\"");
		}
	}
}

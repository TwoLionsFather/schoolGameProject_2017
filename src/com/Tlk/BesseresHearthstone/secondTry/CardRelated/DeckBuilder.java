package com.Tlk.BesseresHearthstone.secondTry.CardRelated;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import com.JanTlk.BesseresHearthstone.Karten.Typ;
import com.Tlk.BesseresHearthstone.secondTry.ErrorHandler;
import com.Tlk.BesseresHearthstone.secondTry.FileController;


public class DeckBuilder implements DeckDataContainer
{
	public ArrayList<Card> cards;

	public DeckBuilder()
	{
		cards = new ArrayList<Card>();
		importCards();
	}

	public Deck getDeckFromCards()
	{
		Deck newDeck = new Deck(cloneCards());
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
				CardBluePrint cardCreator = new CardBluePrint();
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
						ErrorHandler.displayErrorMessage("Input Error during Card creation");
						break;
					}
				}
				scanner.close();
				cards.add(cardCreator.createCard());

				line = fileInput.readLine();
			}

			fileInput.close();
		} catch (IOException e) {
			ErrorHandler.displayErrorMessage("Error Reading File: \"Karten.txt\"");
		}
	}

	private ArrayList<Card> cloneCards()
	{
		ArrayList<Card> clonedCards = new ArrayList<Card>();
		for(Card tempCard : cards)
		{
			clonedCards.add(new Card(tempCard.getName()
									, tempCard.isLegendary()
									, tempCard.getTyp()
									, tempCard.getMana()
									, tempCard.getInitDamage()
									,tempCard.getInitLife()));
		}
		return clonedCards;
	}

	@Override
	public ArrayList<String> getCardNames()
	{
		ArrayList<String> cardNames = new ArrayList<String>();
		for(Card tempCard : cards)
			cardNames.add(tempCard.getName());
		return cardNames;
	}

	@Override
	public int getDeckSize()
	{
		return this.cards.size();
	}
}

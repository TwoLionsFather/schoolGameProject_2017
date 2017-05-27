package com.JanTlk.BesseresHearthstone;

import java.awt.Rectangle;
import java.util.LinkedList;

import com.JanTlk.BesseresHearthstone.Karten.Karte;
import com.JanTlk.BesseresHearthstone.Karten.Status;

public class KI 
{
	private DeckHandler deckHandler;
	private Deck deck;
	
	private Rectangle[][] kartenFelder;
	private int nextPlayField;
	
	
	
	public KI(Rectangle[][] kartenFelder, DeckHandler dH) 
	{
		this.deck = dH.getPCDeck();
		this.deckHandler = dH;
		
		this.kartenFelder = kartenFelder;
		this.nextPlayField = 0;
	}

	
	/**
	 * plays next Round for PC
	 * @param kartenAufFelder these cards are on the field rN
	 * @param gameStats
	 * @return
	 */
	public int[] nextRound(Karte[][] kartenAufFelder, int[] gameStats) 
	{
		LinkedList<Karte> playableCs = new LinkedList<Karte>();
		
		LinkedList<Karte> ownCs = new LinkedList<Karte>();
		LinkedList<Karte> enemysCs = new LinkedList<Karte>();
		
		for(Karte tCard : deckHandler.getAllCards())
		{
			switch(tCard.getStatus())
			{
			case Hand: 
				if ((tCard.getDeck() == deck)
				&& (gameStats[4] - tCard.getMana() >= 0))
				{
					System.out.println(tCard.toString());
					playableCs.add(tCard);
				}
				break;
				
			case Feld:
			case Attack:
			case Layed:
				if (tCard.getDeck() == deck)
				{
					ownCs.add(tCard);
				}
				
				else if (tCard.getDeck() == deckHandler.getPlayerDeck())
				{
					enemysCs.add(tCard);
				}
				break;
				
			default:
				break;
			}
		}
		
		gameStats = layNextCard(playableCs, kartenAufFelder, gameStats);
		
		return gameStats;		
		
	}

	/**
	 * lays next card 
	 * @param playableCs
	 * @param kartenAufFelder
	 * @param gameStats
	 * @return
	 */
	private int[] layNextCard(LinkedList<Karte> playableCs, Karte[][] kartenAufFelder, int[] gameStats) 
	{
		if (!playableCs.isEmpty())
		{
			Karte lastCard = playableCs.get(playableCs.size() - 1);
			
			lastCard.setStatus(Status.Layed);
			gameStats[4] -= lastCard.getMana();
			
//			debug info
//			System.out.println(lastCard.toString());
			
			Rectangle tempRect = kartenFelder[nextPlayField][0];
			kartenAufFelder[nextPlayField][0] = lastCard;
			nextPlayField++;
			
			lastCard.setHome(new Rectangle((int) (tempRect.getX() + (tempRect.getWidth() - lastCard.getBounds().getWidth()) / 2)
										, (int) (tempRect.getY() + (tempRect.getHeight() - lastCard.getBounds().getHeight()) / 2)
										, (int) lastCard.getBounds().getWidth()
										, (int) lastCard.getBounds().getHeight()));
			
			this.nextRound(kartenAufFelder, gameStats);
		}
		
		else return gameStats;
		
		return gameStats;
	}

	/**
	 * not in Use Currently
	 * @param field all games played cards
	 * @param playerPC if 1 players cards, if 0 pcs cards
	 * @return list of cards on these rectangels
	 */
	public LinkedList<Karte> getCardsOnFiled(Karte[][] field, int playerPC)
	{
		LinkedList<Karte> onField = new LinkedList<Karte>();
		
		for(int spalte = 0; spalte < kartenFelder.length; spalte++)
		{
			Karte tCard = field[spalte][playerPC];
			
			if (tCard != null)
			{
				onField.add(field[spalte][playerPC]);
			}
			
		}
		
		return onField;
	}

}

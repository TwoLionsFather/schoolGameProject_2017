package com.JanTlk.BesseresHearthstone;

import java.awt.Rectangle;
import java.util.LinkedList;

import com.JanTlk.BesseresHearthstone.Karten.Karte;
import com.JanTlk.BesseresHearthstone.Karten.Status;

public class KI 
{
	private Rectangle[][] kartenFelder;
	private int nextPlayField;
	private Deck deck;
	
	public KI(Rectangle[][] kartenFelder, Deck dPC) 
	{
		this.kartenFelder = kartenFelder;
		this.deck = dPC;
		
		this.nextPlayField = 0;
	}

	public int[] nextRound(Karte[][] kartenAufFelder, int[] gameStats) 
	{
		LinkedList<Karte> playableCs = new LinkedList<Karte>();
		
		LinkedList<Karte> ownCs = new LinkedList<Karte>();
		LinkedList<Karte> enemysCs = new LinkedList<Karte>();
		
		for(Karte tCard : deck.getKarten())
		{
			switch(tCard.getStatus())
			{
			case Hand: 
				if (gameStats[4] - tCard.getMana() >= 0)
				{
					playableCs.add(tCard);
				}
				break;
				
			case Feld:
			case Attack:
				ownCs.add(tCard);
				break;
				
			default:
				break;
			}
		}
		
		enemysCs = getCardsOnFiled(kartenAufFelder, 1);
		
		gameStats = layNextCard(playableCs, kartenAufFelder, gameStats);
		
		for(Karte tCard : ownCs)
		{
			System.out.println(tCard.toString());
		}
		return gameStats;		
		
	}

	private int[] layNextCard(LinkedList<Karte> playableCs, Karte[][] kartenAufFelder, int[] gameStats) 
	{
		if (!playableCs.isEmpty())
		{
			Karte lastCard = playableCs.get(playableCs.size() - 1);
			lastCard.setStatus(Status.Feld);;
			gameStats[4] -= lastCard.getMana();
			
			Rectangle tempRect = kartenFelder[nextPlayField][0];
			nextPlayField++;
			lastCard.setHome(new Rectangle((int) (tempRect.getX() + (tempRect.getWidth() - lastCard.getBounds().getWidth()) / 2)
										, (int) (tempRect.getY() + (tempRect.getHeight() - lastCard.getBounds().getHeight()) / 2)
										, (int) lastCard.getBounds().getWidth()
										, (int) lastCard.getBounds().getHeight()));
			
			kartenAufFelder[nextPlayField][0] = lastCard;
					
			playableCs.clear();
			nextRound(kartenAufFelder, gameStats);
		}
		
		else return gameStats;
		
		return gameStats;
	}

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

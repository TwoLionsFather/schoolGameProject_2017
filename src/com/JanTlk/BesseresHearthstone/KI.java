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
	private int maxCardRow;
	
	
	
	public KI(int maxCardRow, Rectangle[][] kartenFelder, DeckHandler dH) 
	{
		this.deck = dH.getPCDeck();
		this.deckHandler = dH;
		
		this.kartenFelder = kartenFelder;
		this.nextPlayField = maxCardRow/2;
		this.maxCardRow = maxCardRow;
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
	 * lays all possible cards
	 * @param playableCs lays cards of this cards list
	 * @param kartenAufFelder checks the layed card into this card map
	 * @param gameStats updates mana using the games stats
	 * @return returns gameStats after card is played
	 */
	
	private int[] layNextCard(LinkedList<Karte> playableCs, Karte[][] kartenAufFelder, int[] gameStats) 
	{
		if (!playableCs.isEmpty())
		{
			Karte cardToPlay = chooseCardToPlay(playableCs, gameStats);
			
			cardToPlay.setStatus(Status.Layed);
			gameStats[4] -= cardToPlay.getMana();
			
			nextPlayField = nextPlayField(kartenAufFelder);
			
			Rectangle tempRect = kartenFelder[nextPlayField][0];
			kartenAufFelder[nextPlayField][0] = cardToPlay;
			
			
			
			cardToPlay.setHome(new Rectangle((int) (tempRect.getX() + (tempRect.getWidth() - cardToPlay.getBounds().getWidth()) / 2)
										, (int) (tempRect.getY() + (tempRect.getHeight() - cardToPlay.getBounds().getHeight()) / 2)
										, (int) cardToPlay.getBounds().getWidth()
										, (int) cardToPlay.getBounds().getHeight()));
			
			this.nextRound(kartenAufFelder, gameStats);
		}
		
		else return gameStats;
		
		return gameStats;
	}

	/**
	 * this is where the magic happenes
	 * If player has more life than pc defensive cards get played
	 * and vise versa
	 * @param playableCs
	 * @param gameStats
	 * @return
	 */
	private Karte chooseCardToPlay(LinkedList<Karte> playableCs, int[] gameStats) 
	{
		float[] idx_CardRating = new float[playableCs.size()];
		int idxTop = 0;
		
		for (int idx = 0; idx < playableCs.size(); idx++)
		{
			Karte cardToValue = playableCs.get(idx);
			int cA = cardToValue.getSchaden();
			int cL = cardToValue.getLeben();
			
			//offensive Card if PLLife < Own life
			if (gameStats[0] <= gameStats[3])
			{
				idx_CardRating[idx] = (float) ((gameStats[3] / gameStats[0]) * cL + 1.001 * cA);
			}
			
			//deffensive Card is played if PLLife > OwnLife
			else 
			{
				idx_CardRating[idx] = (float) ((gameStats[3] / gameStats[0]) * cA + 1.001 * cL);
			}
			
			//debugg output to check for smart choices
			System.out.printf("KI.chooseCard %20s value of: %.3f", cardToValue.getName(), idx_CardRating[idx]);
			System.out.printf("It deals %d damage and has %d life", cardToValue.getSchaden(), cardToValue.getLeben());
		}
		
		for (int idx = 0; idx < idx_CardRating.length; idx++)
		{
			if (idx_CardRating[idx] > idx_CardRating[idxTop])
			{
				idxTop = idx;
			}
		}
		
		
		System.out.println(playableCs.get(idxTop).toString());
		return playableCs.get(idxTop);
	}


	/**
	 * sets the next spot a Card will get placed on
	 * @param kartenAufFelder
	 * @return
	 */
	public int nextPlayField(Karte[][] kartenAufFelder) 
	{		
		for (int i = 0; i < maxCardRow - 1; i++)
		{
			if (kartenAufFelder[maxCardRow / 2 + i][0] == null)
			{
				nextPlayField = maxCardRow / 2 + i;
				return nextPlayField;
			}
			else if (kartenAufFelder[maxCardRow / 2 - i][0] == null)
			{
				nextPlayField = maxCardRow / 2 - i;
				return nextPlayField;
			}
		}
		
		return -1;
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

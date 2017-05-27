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
		
		chooseCardToAttack(enemysCs, ownCs, gameStats);
		
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
	 * chooses which cards will attack which other card
	 * @param enemysCs the enemys cards, from which opponents get chosen
	 * @param ownCards the cards that an opponent is searched for
	 * @param gameStats the game stats, to determine strategy and who to attack
	 */
	private void chooseCardToAttack(LinkedList<Karte> enemysCs, LinkedList<Karte> ownCards, int[] gameStats)
	{
		boolean offensive = gameStats[0] <= gameStats[3];
		float topCardRating = (float) 0.0;
		int idxTop = 0;
		
		System.out.println("KI.chooseCard Attacking Card Search ------------------------------------------------------------------------------------------------");
		
		for (Karte ownCard : ownCards)
		{
			int oL = ownCard.getLeben();
			int oA = ownCard.getSchaden();
			
			for (int idxE = 0; idxE < enemysCs.size(); idxE++)
			{
				Karte potTarget = enemysCs.get(idxE);
				
				int eL = potTarget.getLeben();
				int eA = potTarget.getSchaden();
				
				if (offensive)
				{
					float tempRating = (float) ((oA - eL) + 0.1 * (oL - eA));
					
					if (tempRating > topCardRating
					|| idxE == 0)
					{
						idxTop = idxE;
						topCardRating = tempRating;
					}						
					
				}
				
				else
				{
					float tempRating = (float) ((oL - eA) + 0.1 * (oA - eL));
					
					if (tempRating > topCardRating
					|| idxE == 0)
					{
						idxTop = idxE;
						topCardRating = tempRating;
					}	
				}
			}
			

			Karte chosenCard = enemysCs.get(idxTop);
			
			chosenCard.setNewPos(new Rectangle((int) (chosenCard.getBounds().getX() + (chosenCard.getBounds().getWidth() - ownCard.getBounds().getWidth()) / 2)
										, (int) chosenCard.getBounds().getY()
										, (int) ownCard.getBounds().getWidth()
										, (int) ownCard.getBounds().getHeight()));
			
			ownCard.attackedCard(chosenCard);
			ownCard.setStatus(Status.Attack);
			
			System.out.printf("%-20s attacking card %s\n", "", ownCard.getName());
			System.out.printf("%-20s value of: %.3f\n", enemysCs.get(idxTop).getName(), topCardRating);
			enemysCs.remove(idxTop);
//			ownCard.attackedCard(enemysCs.get(idxTop));
			idxTop = 0;
		}
		
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
		boolean offensive = gameStats[0] <= gameStats[3];
		float topCardRating = (float) 0.0;
		int idxTop = 0;
		
		System.out.println("KI.chooseCard Card to Play ------------------------------------------------------------------------------------------------");
		
		for (int idx = 0; idx < playableCs.size(); idx++)
		{
			Karte cardToValue = playableCs.get(idx);
			int cA = cardToValue.getSchaden();
			int cL = cardToValue.getLeben();

			//offensive Card if PLLife < Own life
			if (offensive)
			{
				float tempRating = (float) ((gameStats[3] / gameStats[0]) * cL + 1.001 * cA);
				
				if (tempRating > topCardRating
				|| idx == 0)
				{
					idxTop = idx;
					topCardRating = tempRating;
				}
			}
			
			//deffensive Card is played if PLLife > OwnLife
			else 
			{
				float tempRating = (float) ((gameStats[3] / gameStats[0]) * cA + 1.001 * cL);
				
				if (tempRating > topCardRating
				|| idx == 0)
				{
					idxTop = idx;
					topCardRating = tempRating;
				}
			}
			
			//debugg output to check for smart choices
			System.out.printf("%-20s value of: %.3f\n", cardToValue.getName(), topCardRating);
			System.out.printf("%-20s It deals %d damage and has %d life\n", "", cardToValue.getSchaden(), cardToValue.getLeben());
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

}

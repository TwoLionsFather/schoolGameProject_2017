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
	public void nextRound(Karte[][] kartenAufFelder, int[] gameStats) 
	{
		LinkedList<Karte> playableCs = new LinkedList<Karte>();
		
		LinkedList<Karte> ownCs = new LinkedList<Karte>();
		LinkedList<Karte> enemysCs = new LinkedList<Karte>();
		
		for(Karte tCard : deckHandler.getAllCards())
		{
			switch(tCard.getStatus())
			{
			case HAND: 
				if ((tCard.getDeck() == deck)
				&& (gameStats[4] - tCard.getMana() >= 0))
				{
					playableCs.add(tCard);
				}
				break;
				
			case FELD:
			case ATTACKC:
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
		
		layNextCard(playableCs, kartenAufFelder, gameStats);
		chooseCardToAttack(enemysCs, ownCs, gameStats);
		
		if (allCardsUnderAttack(kartenAufFelder))
		{
			attackPlayer(ownCs);
		}		
		
	}

	/**
	 * all cards that have no target, attack the player
	 * @param ownCs
	 */
	private void attackPlayer(LinkedList<Karte> ownCs) 
	{
		for (Karte tempC : ownCs)
		{
			if (tempC.getStatus() == Status.FELD)
			{
				tempC.setStatus(Status.ATTACKP);
			}
		}
	}


	/**
	 * lays all possible cards
	 * @param playableCs lays cards of this cards list
	 * @param kartenAufFelder checks the layed card into this card map
	 * @param gameStats updates mana using the games stats
	 * @return returns gameStats after card is played
	 */
	
	private void layNextCard(LinkedList<Karte> playableCs, Karte[][] kartenAufFelder, int[] gameStats) 
	{
		if (!playableCs.isEmpty())
		{
			Karte cardToPlay = chooseCardToPlay(playableCs, gameStats);
			
			if (cardToPlay == null)
			{
				return;
			}
			
			cardToPlay.setStatus(Status.LAYED);
			gameStats[4] -= cardToPlay.getMana();
			
			nextPlayField = nextPlayField(kartenAufFelder);
			
			Rectangle tempRect = kartenFelder[nextPlayField][0];
			kartenAufFelder[nextPlayField][0] = cardToPlay;
			
			
			
			cardToPlay.setHome(new Rectangle((int) (tempRect.getX() + (tempRect.getWidth() - cardToPlay.getBounds().getWidth()) / 2)
										, (int) (tempRect.getY() + (tempRect.getHeight() - cardToPlay.getBounds().getHeight()) / 2)
										, (int) cardToPlay.getBounds().getWidth()
										, (int) cardToPlay.getBounds().getHeight()));
			
			playableCs.remove(cardToPlay);
			
			this.layNextCard(playableCs, kartenAufFelder, gameStats);
		}

	}
	
	/**
	 * chooses which cards will attack which other card
	 * @param enemysCs the enemys cards, from which opponents get chosen
	 * @param ownCards the cards that an opponent is searched for
	 * @param gameStats the game stats, to determine strategy and who to attack
	 */
	private void chooseCardToAttack(LinkedList<Karte> enemysCs, LinkedList<Karte> ownCards, int[] gameStats)
	{
		float topCardRating = (float) 0.0;
		int idxTop;
		
		System.out.println("KI.chooseCard Attacking Card Search ------------------------------------------------------------------------------------------------");
		
		for (Karte ownCard : ownCards)
		{
			int oL = ownCard.getLeben();
			int oA = ownCard.getSchaden();
			idxTop = -1;
			
			for (int idxE = 0; idxE < enemysCs.size(); idxE++)
			{
				Karte potTarget = enemysCs.get(idxE);
				
				int eL = potTarget.getLeben();
				int eA = potTarget.getSchaden();

				float tempRating = (float) (-1.0/((eL - oA) * 2 + 1));
				
				if (tempRating > topCardRating
				|| idxE == 0
				&& enemysCs.get(idxE) != null)
				{
					idxTop = idxE;
					topCardRating = tempRating;
				}	
			}
			
			if (idxTop < 0)
			{
				return;
			}

			Karte chosenCard = enemysCs.get(idxTop);
			
			chosenCard.setNewPos(new Rectangle((int) (chosenCard.getBounds().getX() + (chosenCard.getBounds().getWidth() - ownCard.getBounds().getWidth()) / 2)
										, (int) chosenCard.getBounds().getY()
										, (int) ownCard.getBounds().getWidth()
										, (int) ownCard.getBounds().getHeight()));
			
			ownCard.attackedCard(chosenCard);
			ownCard.setStatus(Status.ATTACKC);
			
			System.out.printf("attacking card %s\n", ownCard.getName());
			System.out.printf("attacks %-15s with value of: %.3f\n", enemysCs.get(idxTop).getName(), topCardRating);
			enemysCs.remove(idxTop);
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
		if (gameStats[4] == 0)
		{
			return null;
		}
		
		boolean offensive = gameStats[0] <= gameStats[3];
		float topCardRating = (float) 0.0;
		int idxTop = -1;
		
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
			System.out.printf("It deals %d damage and has %d life\n", cardToValue.getSchaden(), cardToValue.getLeben());
		}
		
		if (idxTop < 0)
		{
			return null;
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
	 * checks if it is possible to attack the enemy player
	 * same function as in Spielfeld but for pc
	 * @param playerPC if 0, cards on PCs half get checked
	 * @return true if all cards are under attack
	 */
	private boolean allCardsUnderAttack(Karte[][] kartenAufFelder) 
	{
		for (int i = 0; i < kartenAufFelder.length; i++)
		{
			if ((kartenAufFelder[i][1] != null)
			&& !kartenAufFelder[i][1].isAttacked())
			{
				return false;
			}
		}
			
		return true;
	}

}

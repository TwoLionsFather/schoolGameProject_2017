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
	private Karte[][] kartenAufFelder;
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
	 * @return true if a card has attacked
	 */
	public boolean nextRound(Karte[][] kartenAufFelder, int[] gameStats) 
	{
		boolean attacksUpdated = false;
		boolean attacksOnPlayer = false;
		
		LinkedList<Karte> playableCs = new LinkedList<Karte>();
		LinkedList<Karte> ownCs = new LinkedList<Karte>();
		LinkedList<Karte> enemysCs = new LinkedList<Karte>();
		
		this.kartenAufFelder = kartenAufFelder;
		
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
			case ATTACKP:
				if (deck.isInDeck(tCard))
				{
					ownCs.add(tCard);
				}
				
				else if (deckHandler.getPlayerDeck().isInDeck(tCard))
				{
					enemysCs.add(tCard);
				}
				break;
				
			default:
				break;
			}
		}
		
		layNextCard(playableCs, gameStats);
		attacksUpdated = chooseCardToAttack(enemysCs, ownCs, gameStats);
		
		if (noCardsToAttack()
		||allCardsUnderAttack())
		{
			attacksOnPlayer = attackPlayer(ownCs);
		}		
		
		if (attacksUpdated || attacksOnPlayer)
		{
			return true;
		}
		
		return false;
		
	}

	/**
	 * checks enemy side for cards
	 * @param kartenAufFelder this map is used to do so
	 * @return true if no cards where found
	 */
	private boolean noCardsToAttack() 
	{		
		return (nextAttackField() == -1);
	}

	/**
	 * all cards that have no target, attack the player
	 * @param ownCs the list of cards, that attacks the player
	 * @param kartenAufFelder this is to determin space for the attacking card
	 * @return true if at least one card is attacking the player
	 */
	private boolean attackPlayer(LinkedList<Karte> ownCs) 
	{
		boolean attacked = false;
		for (Karte tempC : ownCs)
		{
			if (tempC.getStatus() == Status.FELD)
			{
				int posAttack = nextAttackField();
								
				kartenAufFelder[posAttack][1] = tempC;
				Rectangle attackPos = kartenFelder[posAttack][1];
				
				if (Hearthstone.isDebugMode())
				{
					System.out.println("KI.attackPlayer Reminder for positioning");
				}
				tempC.setNewPos(new Rectangle((int) (attackPos.getX() + (attackPos.getWidth() - tempC.getBounds().getWidth()) / 2)
											, (int) attackPos.getY() - 60
											, (int) tempC.getBounds().getWidth()
											, (int) tempC.getBounds().getHeight()));
				
				tempC.setStatus(Status.ATTACKP);
				attacked = true;
				System.out.printf("%20s attacks player for %d\n", tempC.getName(), tempC.getSchaden());
			}
		}
		return attacked;
	}

	/**
	 * lays all possible cards
	 * @param playableCs lays cards of this cards list
	 * @param kartenAufFelder checks the layed card into this card map
	 * @param gameStats updates mana using the games stats
	 * @return true if at least one card has been played
	 */
	private boolean layNextCard(LinkedList<Karte> playableCs, int[] gameStats) 
	{
		boolean cardPlayed = false;
		if (!playableCs.isEmpty())
		{
			Karte cardToPlay = chooseCardToPlay(playableCs, gameStats);
			
			//if PC has no Hand cars or not enough Mana, secound catch
			if (cardToPlay == null
			|| gameStats[4] - cardToPlay.getMana() < 0)
			{
				return cardPlayed;
			}
			
			nextPlayField();
			
			//If there is no space, no card will get played
			if (nextPlayField == -1)
			{
				return cardPlayed;
			}
			
			Rectangle tempRect = kartenFelder[nextPlayField][0];
			kartenAufFelder[nextPlayField][0] = cardToPlay;
			cardPlayed = true;
			
			cardToPlay.setHome(new Rectangle((int) (tempRect.getX() + (tempRect.getWidth() - cardToPlay.getBounds().getWidth()) / 2)
										, (int) (tempRect.getY() + (tempRect.getHeight() - cardToPlay.getBounds().getHeight()) / 2)
										, (int) cardToPlay.getBounds().getWidth()
										, (int) cardToPlay.getBounds().getHeight()));
			
			gameStats[4] -= cardToPlay.getMana();
			playableCs.remove(cardToPlay);
			cardToPlay.setStatus(Status.LAYED);			
			this.layNextCard(playableCs, gameStats);
		}
		return cardPlayed;
	}
	
	/**
	 * chooses which cards will attack which other card
	 * @param enemysCs the enemys cards, from which opponents get chosen
	 * @param ownCards the cards that an opponent is searched for
	 * @param gameStats the game stats, to determine strategy and who to attack
	 * @return true if at least one card has attacked
	 */
	private boolean chooseCardToAttack(LinkedList<Karte> enemysCs, LinkedList<Karte> ownCards, int[] gameStats)
	{
		float topCardRating = (float) 0.0;
		int idxTop;
		boolean attacked = false;
		
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
				return attacked;
			}

			Karte targetCard = enemysCs.get(idxTop);
			
			if (Hearthstone.isDebugMode())
			{
				System.out.println("KI.chooseAttack Reminder for positioning <------------------------------------------------------------------------------");
				System.out.printf("Attacking card %s\n", ownCard.getName());
				System.out.printf("attacks %-15s with value of: %.3f\n", enemysCs.get(idxTop).getName(), topCardRating);
			}
			
			ownCard.setNewPos(new Rectangle((int) (targetCard.getBounds().getX() + (targetCard.getBounds().getWidth() - ownCard.getBounds().getWidth()) / 2)
												, (int) targetCard.getBounds().getY() - 60
												, (int) ownCard.getBounds().getWidth()
												, (int) ownCard.getBounds().getHeight()));
			attacked = true;
			ownCard.attackedCard(targetCard);
			ownCard.setStatus(Status.ATTACKC);
			enemysCs.remove(idxTop);
			idxTop = 0;
		}
		
		System.out.println();
		return attacked;
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
		//no need to look for a card, if mana reached zero
		if (gameStats[4] <= 0)
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
				float tempRating = (float) ((gameStats[3] / gameStats[0]) * cL + 1.01 * cA);
				
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
				float tempRating = (float) ((gameStats[3] / gameStats[0]) * cA + 1.01 * cL);
				
				if (tempRating > topCardRating
				|| idx == 0)
				{
					idxTop = idx;
					topCardRating = tempRating;
				}
			}
			
			//debugg output to check plays
			if (Hearthstone.isDebugMode())
			{
				System.out.printf("%-20s value of: %.3f\n", cardToValue.getName(), topCardRating);
				System.out.printf("It deals %d damage and has %d life\n", cardToValue.getSchaden(), cardToValue.getLeben());
			}
			
		}
		
		if (idxTop < 0)
		{
			return null;
		}
		
		System.out.println(playableCs.get(idxTop).toString());
		return playableCs.get(idxTop);
	}


	/**
	 * looks for next free spot, close to the mid
	 * @param kartenAufFelder
	 * @return free place in row
	 */
	public void nextPlayField() 
	{		
		for (int i = 0; i < maxCardRow / 2 + 1; i++)
		{
			if (kartenAufFelder[maxCardRow / 2 + i][0] == null)
			{
				this.nextPlayField = maxCardRow / 2 + i;
				return;
			}
			else if (kartenAufFelder[maxCardRow / 2 - i][0] == null)
			{
				this.nextPlayField = maxCardRow / 2 - i;
				return;
			}
		
		}
		
		nextPlayField = -1;
	}
	
	/**
	 * searches players Rectangels for next free spot
	 * @param kartenAufFelder uses this cardmap to determin this spot
	 * @return the idx of next attack fild and -1 if non found
	 */
	public int nextAttackField() 
	{		
		for (int i = 0; i < maxCardRow / 2 + 1; i++)
		{
			if (kartenAufFelder[maxCardRow / 2 + i][1] == null)
			{
				return maxCardRow / 2 + i;
			}
			else if (kartenAufFelder[maxCardRow / 2 - i][1] == null)
			{
				return maxCardRow / 2 - i;
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
	private boolean allCardsUnderAttack() 
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

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
		attacksUpdated = chooseCardToAttack(enemysCs, ownCs);
		
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
											, (int) attackPos.getY() - ((Hearthstone.BREITE < 1920) ? 30 : 60)
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
			
			playableCs.remove(cardToPlay);
			cardToPlay.setStatus(Status.LAYED);		
			gameStats[4] -= cardToPlay.getMana();
			this.layNextCard(playableCs, gameStats);
		}
		return cardPlayed;
	}
	
	/**
	 * chooses which cards will attack which other card
	 * @param enemysCs the enemys cards, from which opponents get chosen
	 * @param ownCs the cards that an opponent is searched for
	 * @param gameStats the game stats, to determine strategy and who to attack
	 * @return true if at least one card has attacked
	 */
	private boolean chooseCardToAttack(LinkedList<Karte> enemysCs, LinkedList<Karte> ownCs)
	{
		System.out.println("KI.chooseCard Attacking Card Search ------------------------------------------------------------------------------------------------");
		
		if (Hearthstone.isDrawhelpActive())
		{
			return basicC(enemysCs, ownCs);
		}
		
		return advancedC(enemysCs, ownCs);
	}
	
	/**
	 * first implemented combat system
	 * therefore decisions is based of simple onetime calculation
	 * @param enemysCs enemys cards that need to get attacked
	 * @param ownCs cards that can attack
	 * @return true if a card has attacked
	 */
	private boolean basicC(LinkedList<Karte> enemysCs, LinkedList<Karte> ownCs)
	{
		float topCardRating = (float) 0.0;
		int idxTop;
		boolean attacked = false;
		
		for (Karte ownCard : ownCs)
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
												, (int) targetCard.getBounds().getY() - ((Hearthstone.BREITE < 1920) ? 30 : 60)
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
	 * this method checks for every enemy Card to find the best attacking own Card
	 * still needs better attack algorithm, other then that, works fine
	 * @param enemysCs every one of these gets a enemy, as long as there are own attack Cards to attack
	 * @param ownCs these cards attack in a way, that every enemy card gets attacked by the highest scoring own card
	 * @return true if there has been an attack
	 */
	private boolean advancedC(LinkedList<Karte> enemysCs, LinkedList<Karte> ownCs)
	{
		//if there is no card to attack or there is no attacking card
		if (enemysCs.size() <= 0
		|| ownCs.size() <= 0)
		{
			return false;
		}
		
		boolean attacked = false;
		boolean[][] isBest = new boolean[ownCs.size()][enemysCs.size()];
		float[][] score = new float[ownCs.size()][enemysCs.size()];
		int idxHighestOwn = -1;	//should throw an exception in case of error
		
		//setup of score array, this is final and won't get changed after init
		//for every enemy
		for (int idxEnemy = 0; idxEnemy < enemysCs.size(); idxEnemy++)
		{
			Karte enemy = enemysCs.get(idxEnemy);
			
			//there is a own card, that could attack
			for (int idxOwnC = 0; idxOwnC < ownCs.size(); idxOwnC++)
			{
				Karte own = ownCs.get(idxOwnC);
				
				int oL = own.getLeben();
				int oA = own.getSchaden();
				int eL = enemy.getLeben();
				int eA = enemy.getSchaden();
				
				//the score of this attack gets stored in the score array
				score[idxOwnC][idxEnemy] = (float) ((-1.0/((eL - oA) * 2 + 1)) 
													+ oL - eA
													+ Math.pow(eA, (oL - eA <= 0 && eL - oA <= 0) ? 1 : 0));
				
				//if the score is very high or there is no top score yet 
				if (idxOwnC == 0
				|| score[idxOwnC][idxEnemy] >= score[idxHighestOwn][idxEnemy])
				{
					idxHighestOwn = idxOwnC;
				}
			}
			
			//the highest scoreing own card for this enemy gets stored
			isBest[idxHighestOwn][idxEnemy] = true;
		}
		
		isBest = findBestA(score, isBest, enemysCs.size(), ownCs.size());
		
		attacked = attackA(isBest, score, enemysCs, ownCs);
		
		System.out.println();
		return attacked;
	}
	
	/**
	 * the attacks get set up
	 * @param isBest the array to check for the best attacking card
	 * @param score
	 * @param enemysCs cards that will get attacked
	 * @param ownCs cards that will attack
	 * @return true if a card has attacked
	 */
	private boolean attackA(boolean[][] isBest, float[][] score, LinkedList<Karte> enemysCs, LinkedList<Karte> ownCs)
	{
		boolean attacked = false;
		
		//setsup attack with isBest array
		//check for every Enemy
		for (int idxEnemy = 0; idxEnemy < enemysCs.size(); idxEnemy++)
		{
			//for potential own attackCards
			boolean[] potOwnC = new boolean[ownCs.size()];
			
			for (int idxOwnC = 0; idxOwnC < ownCs.size(); idxOwnC++)
			{
				//if this own cards best enemy is this enemy
				if (isBest[idxOwnC][idxEnemy])
				{
					//it is a potential enemy for this card
					potOwnC[idxOwnC] = true;
					
					//if there are other potential Cards there needs to be another check
					for (int i = 0; i < potOwnC.length; i++)
					{
						//if there is another own card, that would want to attack this card...
						if(potOwnC[i] && i != idxOwnC)
						{
							//if the other Card has a higher score for the card
							if (score[i][idxEnemy] > score[idxOwnC][idxEnemy])
							{
								//the potential own card is no longer a valid choice
								potOwnC[idxOwnC] = false;
							}
							//if the score is lower, the card is not an alternative
							else 
							{
								potOwnC[i] = false;
							}
						}
					}
				}
			}
			
			//search for highest scoring card and setup attack
			for (int idxFinalChoice = 0; idxFinalChoice < potOwnC.length; idxFinalChoice++)
			{
				if (potOwnC[idxFinalChoice])
				{
					Karte attackingCard = ownCs.get(idxFinalChoice);
					Karte attackedCard = enemysCs.get(idxEnemy);
					
					if (Hearthstone.isDebugMode())
					{
						System.out.println("KI.chooseAttack Reminder for positioning <------------------------------------------------------------------------------");
						System.out.printf("Attacking card %s\n", attackingCard.getName());
						System.out.printf("attacks %-15s with value of: %.3f\n", enemysCs.get(idxEnemy).getName(), score[idxFinalChoice][idxEnemy]);
					}
					
					attackingCard.setNewPos(new Rectangle((int) (attackedCard.getBounds().getX() + (attackedCard.getBounds().getWidth() - attackingCard.getBounds().getWidth()))
														, (int) attackedCard.getBounds().getY() - 60
														, (int) attackingCard.getBounds().getWidth()
														, (int) attackingCard.getBounds().getHeight()));
					attacked = true;
					attackingCard.attackedCard(attackedCard);
					attackedCard.setAttacked(true);
					attackingCard.setStatus(Status.ATTACKC);
					ownCs.remove(attackingCard);
					enemysCs.remove(attackedCard);
					
					advancedC(enemysCs, ownCs);
				}
			}
		}
		
		return attacked;
	}
	
	/**
	 * cleanup of isBest array, only one card should remain to attack any enemy
	 * @param score this is used in case, there are multiple cards
	 * @param isBest the field in wich the highest scoring cards are marked for every enemy
	 * @param amEnemyCard amout of enemy cards
	 * @param amOwnCard amount of own cards
	 * @return the field, but for every own card, only one ememy is left
	 */
	private boolean[][] findBestA(float[][] score, boolean[][] isBest, int amEnemyCard, int amOwnCard)
	{
		int countLeak = -1;
		
		//for evey own card, there might be multiple enemy cards, that this card would top score against
		for (int idxOwnC = 0; idxOwnC < amOwnCard; idxOwnC++)
		{
			//if thete is another enemy card that might get attacked by this card
			boolean[] potEnemys = new boolean[amEnemyCard];
			
			//so every enemy gets checked
			for (int idxEnemy = 0; idxEnemy < amEnemyCard; idxEnemy++)
			{
				//if the own card is the best enemy for this eCard
				if (isBest[idxOwnC][idxEnemy])
				{
					//it is best, so it is a potential enemy for this card
					potEnemys[idxEnemy] = true;
					
					//there needs to be another check if the own card is best for only this enemy
					for (int i = 0; i < potEnemys.length; i++)
					{
						//if there is another enemy card, that the own card should attack
						if(potEnemys[i] && i != idxOwnC)
						{
							//and the attack score is higher
							if (score[idxOwnC][i] > score[idxOwnC][idxEnemy])
							{
								//the potential enemy is is no longer a valid choice
								potEnemys[idxEnemy] = false;
								
								int idxSecoundHighest = -1;		//this should throw exception if something goes wrong
								float highest = score[0][idxEnemy];
								//but there needs to be a replacement for this card
								for (int idxReplacement = 0; idxReplacement < amOwnCard; idxReplacement++)
								{
									//so if the score is higher than top score and its not the already top scoreing card
									if (score[idxReplacement][idxEnemy] >= highest
									&& idxReplacement != idxOwnC)
									{
										idxSecoundHighest = idxReplacement;
										highest = score[idxReplacement][idxEnemy];
										//since there have been changes to the best enemy card to attack, every card needs to be checked again
										if (countLeak < 5)
										{
											idxEnemy = 0;	
										}
										countLeak++;
									}
								}
								
								if (idxSecoundHighest < 0)
								{
									break;
								}
								
								isBest[idxSecoundHighest][idxEnemy] = true;
							}
						}
					}
				}
			}
		}
		
		return isBest;
	}

	/**
	 * this is where the magic happenes
	 * If player has more life than pc defensive cards get played
	 * and vise versa
	 * @param playableCs
	 * @param gameStats
	 * @return chosen Card
	 */
	private Karte chooseCardToPlay(LinkedList<Karte> playableCs, int[] gameStats) 
	{
		System.out.println("KI.chooseCard Card to Play ------------------------------------------------------------------------------------------------");
		
		//no need to look for a card, if mana reached zero
		if (gameStats[4] <= 0)
		{
			return null;
		}	
		
		boolean offensive = gameStats[0] <= gameStats[3];
		float topCardRating = (float) 0.0;
		int idxTop = -1;
		
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

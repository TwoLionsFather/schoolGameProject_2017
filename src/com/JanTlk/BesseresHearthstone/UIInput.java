package com.JanTlk.BesseresHearthstone;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.JanTlk.BesseresHearthstone.Hearthstone.STATE;

public class UIInput extends KeyAdapter implements MouseMotionListener, MouseListener
{
	private Spielfeld spielfeld;
	private Menu menu;
	private Hearthstone hs;
	private boolean cardMoved;
	private static boolean playersMove = false;
	
	/** User Interactive Input
	 * this is used to handle mouseinput
	 * dictates spiefeld how to handle mouseevent
	 * @param spielfeld this is used to convert mouseevents into actions
	 * @param menu 
	 */
	public UIInput(Spielfeld spielfeld, Menu menu, Hearthstone hearthstone) 
	{
		this.spielfeld = spielfeld;
		this.menu = menu;
		this.hs = hearthstone;
		this.cardMoved = false;
	}
	
	@Override
	public void keyPressed(KeyEvent e) 
	{
		int keyCode = e.getKeyCode();

		switch (keyCode)
		{
		case KeyEvent.VK_ESCAPE:
			System.exit(0);
			break;	
		
		case KeyEvent.VK_ENTER:
			
			if (Hearthstone.gameState == STATE.GAME
			&& !Hearthstone.isDrawhelpActive()
			|| Hearthstone.isDebugMode())
			{
				if (playersMove)
				{
					spielfeld.attackUpdate();
					spielfeld.nextRound(!playersMove);
					if (spielfeld.getAttackUpdate())
					{
						spielfeld.attackUpdate();
						spielfeld.nextRound(playersMove);
					}
				}
				
				else if (spielfeld.getAttackUpdate())
				{
					spielfeld.attackUpdate();
					playersMove = !playersMove;
					spielfeld.nextRound(playersMove);
				}
				break;
			}
			
		case KeyEvent.VK_DELETE:
			if (Hearthstone.gameState == STATE.GAME
			&& Hearthstone.isDebugMode())
			{
				Hearthstone.gameState = STATE.RESETGAME;
				hs.repaint();
			}
			break;
			
		case KeyEvent.VK_SHIFT:
			if (Hearthstone.isDebugMode())
			{
				spielfeld.getdH().refillHands();
				hs.repaint();
			}
		}
		
		e.consume();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
		switch (Hearthstone.gameState)
		{
		case BEATEN:
			Hearthstone.gameState = STATE.RESETGAME;
			hs.repaint();
			break;
			
		case END:
			System.exit(0);
			break;
			
		case GAME:
			if (spielfeld.clickedNR(arg0))
			{
				if(playersMove)
				{
					spielfeld.attackUpdate();
				}
					
				else if (spielfeld.getAttackUpdate())
				{
					spielfeld.attackUpdate();
				}
				
				playersMove = !playersMove;
				spielfeld.nextRound(playersMove);
				return;
			}
			
			if (spielfeld.clickedSkipA(arg0)
			&& !Hearthstone.isDrawhelpActive())
			{
				if (playersMove)
				{
					spielfeld.attackUpdate();
					spielfeld.nextRound(!playersMove);
					spielfeld.attackUpdate();
					spielfeld.nextRound(playersMove);
				}
				
				else if (spielfeld.getAttackUpdate())
				{
					spielfeld.attackUpdate();
					playersMove = !playersMove;
					spielfeld.nextRound(playersMove);
				}
			}
			break;
			
		case HELP:
			Hearthstone.gameState = STATE.MENU;
			hs.repaint();
			break;
			
		case MENU:
			for(int i = 0; i < menu.getButtons().length; i++)
			{
				if (spielfeld.inBounds(arg0.getPoint(), menu.getButtons()[i]))
				{
					switch (i)
					{
					case 0: DJ.getInstance().enqueue("adapt start");
						DjukeBox.playSFX("CardShuffling");
						Hearthstone.gameState = STATE.GAME;
						break;
						
					case 1: Hearthstone.gameState = STATE.HELP;
						break;
						
					case 2: Hearthstone.gameState = STATE.END;
						break;
					}
					
					hs.repaint();
				}
				
			}
			break;
			
		default:
			break;
		
		}
		
		arg0.consume();
	}
	
	@Override
	public void mousePressed(MouseEvent arg0) 
	{
		if (Hearthstone.gameState == STATE.GAME)
		{
			spielfeld.cardDetailsAt(arg0);
			
			if (arg0.getButton() == MouseEvent.BUTTON1
			&& spielfeld.playableCardAt(arg0)
			&& playersMove)
			{
				cardMoved = true;	
			}
		}	
		
		arg0.consume();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) 
	{
		if(cardMoved)
		{
			spielfeld.moveCardAtRect(arg0);
			
			cardMoved = false;
			arg0.consume();
		}
		
	}
	
	@Override
	public void mouseDragged(MouseEvent arg0) 
	{
		arg0.consume();	
	}

	@Override
	public void mouseMoved(MouseEvent arg0) 
	{
		arg0.consume();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) 
	{
		arg0.consume();
	}

	@Override
	public void mouseExited(MouseEvent arg0) 
	{
		arg0.consume();
	}

	public static boolean isPlayersMove() 
	{
		return playersMove;
	}

	public static void setPlayersMove(boolean playersMove) 
	{
		UIInput.playersMove = playersMove;
	}
}

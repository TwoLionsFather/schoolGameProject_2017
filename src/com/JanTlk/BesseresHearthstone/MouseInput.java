package com.JanTlk.BesseresHearthstone;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.JanTlk.BesseresHearthstone.Hearthstone.STATE;

public class MouseInput implements MouseMotionListener, MouseListener
{
	private Spielfeld spielfeld;
	private Menu menu;
	private Hearthstone hs;
	private boolean cardMoved;
	private static boolean playersMove = true;
	
	/**
	 * this is used to handle mouseinput
	 * dictates spiefeld how to handle mouseevent
	 * @param spielfeld this is used to convert mouseevents into actions
	 * @param menu 
	 */
	public MouseInput(Spielfeld spielfeld, Menu menu, Hearthstone hearthstone) 
	{
		this.spielfeld = spielfeld;
		this.menu = menu;
		this.hs = hearthstone;
		this.cardMoved = false;
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
	public void mouseClicked(MouseEvent arg0) 
	{
		spielfeld.cardDetailsAt(arg0);
		
		if (Hearthstone.gameState == STATE.END
		|| Hearthstone.gameState == STATE.BEATEN)
		{
			System.exit(0);
		}
		
		else if (Hearthstone.gameState == STATE.GAME)
		{
			if(spielfeld.clickedNR(arg0))
			{
				if(playersMove)
				{
					spielfeld.attackUpdate();
				}
					
				if (spielfeld.getAttackUpdate())
				{
					spielfeld.attackUpdate();
				}
				
				playersMove = !playersMove;
				spielfeld.nextRound(playersMove);
				
				
				return;
			}
			
		}			
		
		else if(Hearthstone.gameState == STATE.HELP)
		{
			Hearthstone.gameState = STATE.MENU;
			hs.repaint();
		}
		
		else if(Hearthstone.gameState == STATE.MENU)
		{
			for(int i = 0; i < menu.getButtons().length; i++)
			{
				if (spielfeld.inBounds(arg0.getPoint(), menu.getButtons()[i]))
				{
					switch (i)
					{
					case 0: Hearthstone.gameState = STATE.GAME;
						break;
						
					case 1: Hearthstone.gameState = STATE.HELP;
						break;
						
					case 2: Hearthstone.gameState = STATE.END;
						break;
					}
					
					hs.repaint();
				}
				
			}
			
		}
		
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

	@Override
	public void mousePressed(MouseEvent arg0) 
	{
		if (Hearthstone.gameState == STATE.GAME)
		{
			if (arg0.getButton() == MouseEvent.BUTTON1
			&& spielfeld.playableCardAt(arg0)
			&& playersMove)
			{
				cardMoved = true;	
			}
			
			else
			{
				spielfeld.cardDetailsAt(arg0);
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
	
	public static boolean isPlayersMove() 
	{
		return playersMove;
	}
}

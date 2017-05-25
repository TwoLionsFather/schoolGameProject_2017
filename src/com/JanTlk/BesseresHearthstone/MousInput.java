package com.JanTlk.BesseresHearthstone;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import com.JanTlk.BesseresHearthstone.Hearthstone.STATE;

public class MousInput implements MouseMotionListener, MouseListener
{
	private Spielfeld spielfeld;
	private Menu menu;
	private Hearthstone hs;
	private int[] xyChange;		//alternativ als Point
	private Point oldPoint = null;
	
	/**
	 * this is used to handle mouseinput
	 * dictates spiefeld how to handle mouseevent
	 * @param spielfeld this is used to convert mouseevents into actions
	 * @param menu 
	 */
	public MousInput(Spielfeld spielfeld, Menu menu, Hearthstone hearthstone) 
	{
		this.spielfeld = spielfeld;
		this.menu = menu;
		this.hs = hearthstone;
		xyChange = new int[2];
	}
	
	@Override
	public void mouseDragged(MouseEvent arg0) 
	{
		if(oldPoint != null)
		{
			Point temp = arg0.getPoint();			
			
			xyChange[0] += (int) (temp.getX() - oldPoint.getX()); //x
			xyChange[1] += (int) (temp.getY() - oldPoint.getY()); //y
			
			oldPoint = temp;
			arg0.consume();
		}		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) 
	{
		arg0.consume();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) 
	{
		if (hs.getGameState() == STATE.End)
		{
			System.exit(0);
		}
		
		else if (hs.getGameState() == STATE.Game)
		{
			spielfeld.clickNR(arg0);
		}
		
		else if(hs.getGameState() == STATE.Help)
		{
			hs.setGameState(STATE.Menu);
			hs.repaint();
		}
		
		else if(hs.getGameState() == STATE.Menu)
		{
			for(int i = 0; i < menu.getButtons().length; i++)
			{
				if (spielfeld.inBounds(arg0.getPoint(), menu.getButtons()[i]))
				{
					if(i == 0)
					{
						hs.setGameState(STATE.Game);
					}
					
					else if(i == 1)
					{
						hs.setGameState(STATE.Help);
					}
					
					else if(i == 2)
					{
						hs.setGameState(STATE.End);
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
		if (hs.getGameState() == STATE.Game)
		{
			if (arg0.getButton() == MouseEvent.BUTTON1)
			{
				if(spielfeld.cardAt(arg0))
				{
					oldPoint = arg0.getPoint();		
				}
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
		if(oldPoint != null)
		{
			spielfeld.cardRectAt(arg0);
//			if(!spielfeld.cardRectAt(arg0))
//			{
//				Point temp = arg0.getPoint();
//				
//				xyChange[0] += (int) (oldPoint.getX() - temp.getX());
//				xyChange[1] += (int) (oldPoint.getY() - temp.getY());	
//				spielfeld.moveCard(xyChange);
//			}
			
			oldPoint = null;
			xyChange[0] = 0;
			xyChange[1] = 0;
			arg0.consume();
		}
		
	}
}

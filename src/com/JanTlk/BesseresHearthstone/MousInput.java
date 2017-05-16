package com.JanTlk.BesseresHearthstone;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MousInput implements MouseMotionListener, MouseListener
{
	private Spielfeld spielfeld;
	private int[] xyChange;		//alternativ als Point
	private Point oldPoint = null;
	
	
	
	public MousInput(Spielfeld spielfeld) 
	{
		this.spielfeld = spielfeld;
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
		if(spielfeld.cardAt(arg0))
		{
			oldPoint = arg0.getPoint();		
		}
		arg0.consume();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) 
	{
		if(oldPoint != null)
		{
			Point temp = arg0.getPoint();
			
			xyChange[0] += (int) (oldPoint.getX() - temp.getX());
			xyChange[1] += (int) (oldPoint.getY() - temp.getY());			
			oldPoint = null;
			
			spielfeld.moveCard(xyChange);			
			
			xyChange[0] = 0;
			xyChange[1] = 0;
			arg0.consume();
		}
		
	}
}

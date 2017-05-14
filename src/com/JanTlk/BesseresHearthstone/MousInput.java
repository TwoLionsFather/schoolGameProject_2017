package com.JanTlk.BesseresHearthstone;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MousInput implements MouseMotionListener, MouseListener
{
	private Spielfeld spielfeld;
	private boolean isBeingMoved = false;
	
	
	public MousInput(Spielfeld spielfeld) 
	{
		this.spielfeld = spielfeld;
	}
	
	@Override
	public void mouseDragged(MouseEvent arg0) 
	{
		if(!isBeingMoved)
		{
			System.out.println("Debugg");
			spielfeld.moveCard(arg0);
			isBeingMoved = true;
		}
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
		arg0.consume();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) 
	{
		System.out.println("Test");
		isBeingMoved = false;
		arg0.consume();
	}
}

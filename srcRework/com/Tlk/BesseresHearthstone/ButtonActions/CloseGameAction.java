package com.Tlk.BesseresHearthstone.ButtonActions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class CloseGameAction extends AbstractAction
{
	private static final long serialVersionUID = 5506574180035057416L;

	public CloseGameAction()
	{
		this.putValue(NAME, "Exit");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		System.exit(0);
	}


}

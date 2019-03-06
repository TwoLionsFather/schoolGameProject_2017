package com.Tlk.BesseresHearthstone.ActionsAndListeners;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.Tlk.BesseresHearthstone.GameUI.GameUI;

public class AcceptTurnAction extends AbstractAction
{
	private static final long serialVersionUID = 5506574180035057416L;

	private GameUI controller;
	public AcceptTurnAction(GameUI ui)
	{
		this.controller = ui;
		this.putValue(NAME, "Done");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{

	}


}

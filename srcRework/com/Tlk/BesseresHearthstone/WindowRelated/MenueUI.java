package com.Tlk.BesseresHearthstone.WindowRelated;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import com.Tlk.BesseresHearthstone.GameStateController;
import com.Tlk.BesseresHearthstone.ButtonActions.GameStartAction;
import com.Tlk.BesseresHearthstone.ButtonActions.HelpScreenAction;
import com.Tlk.BesseresHearthstone.MainGameClass.STATE;

public class MenueUI extends SceneContainer
{
	public MenueUI(GameStateController stateController)
	{
		this.getPanel().setLayout(new FlowLayout(FlowLayout.CENTER));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.LIGHT_GRAY);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
		buttonPanel.setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.LOWERED, Color.DARK_GRAY, Color.BLACK));
		this.getPanel().add(buttonPanel);

		buttonPanel.setSize(250, 500);
		JButton startGame = new JButton();
		startGame.setAction(new GameStartAction(stateController));
		startGame.setBackground(Color.DARK_GRAY);
		startGame.setForeground(Color.YELLOW);
		buttonPanel.add(startGame);

		JButton helpButton = new JButton();
		helpButton.setAction(new HelpScreenAction(stateController));
		helpButton.setBackground(Color.DARK_GRAY);
		helpButton.setForeground(Color.YELLOW);
		helpButton.setSize(startGame.getSize());
		buttonPanel.add(helpButton);

		this.linkWithState(STATE.MENU);
	}

}

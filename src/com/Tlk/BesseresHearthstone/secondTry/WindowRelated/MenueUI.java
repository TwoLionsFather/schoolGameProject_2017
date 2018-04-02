package com.Tlk.BesseresHearthstone.secondTry.WindowRelated;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import com.Tlk.BesseresHearthstone.secondTry.MainGameClass.STATE;

public class MenueUI implements SceneContainer
{
	private JPanel menuPanel;

	public MenueUI(LiveGameDataController stateController)
	{
		this.menuPanel = new JPanel();
		this.menuPanel.setSize((int) stateController.getWIDTH(), (int) stateController.getHEIGHT());
		this.menuPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.LIGHT_GRAY);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
		buttonPanel.setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.LOWERED, Color.DARK_GRAY, Color.BLACK));
		this.menuPanel.add(buttonPanel);

		buttonPanel.setSize(250, 500);
//		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));

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

		SceneController.getSceneController().addScene(STATE.MENU, this);
	}

	@Override
	public JPanel getScene()
	{
		return this.menuPanel;
	}

}

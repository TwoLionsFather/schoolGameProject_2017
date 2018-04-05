package com.Tlk.BesseresHearthstone.secondTry.WindowRelated;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import com.Tlk.BesseresHearthstone.secondTry.MainGameClass.STATE;
import com.Tlk.BesseresHearthstone.secondTry.TextureController;

public class HelpUI extends SceneContainer
{
	public HelpUI(GameStateController stateController)
	{
//		expand more complex user help interface
		JLabel helpSheetLabel = new JLabel();
		helpSheetLabel.setIcon(new ImageIcon(TextureController.getTexture("helpSheet.png")));
		helpSheetLabel.setSize(helpSheetLabel.getSize());

		JButton returnToMenu = new JButton();
		returnToMenu.setAction(new ReturnToMenuAction(stateController));

		this.getPanel().add(returnToMenu);
		this.getPanel().add(helpSheetLabel);
		this.linkWithState(STATE.HELP);
	}

}

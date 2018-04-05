package com.Tlk.BesseresHearthstone.secondTry.WindowRelated;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.Tlk.BesseresHearthstone.secondTry.MainGameClass.STATE;
import com.Tlk.BesseresHearthstone.secondTry.TextureController;

public class HelpUI implements SceneContainer
{
	private SceneController sceneController = SceneController.getSceneController();
	private JPanel helpSheet;

	public HelpUI()
	{
		loadHelpSheet();
	}

	private void loadHelpSheet()
	{
		this.helpSheet = new JPanel();
		this.helpSheet.setSize(sceneController.getMaxSceneSize());
		JLabel helpSheetLabel = new JLabel();
		helpSheetLabel.setIcon(new ImageIcon(TextureController.getTexture("helpSheet.png")));
		helpSheetLabel.setSize(helpSheetLabel.getSize());
		this.helpSheet.add(helpSheetLabel);
		this.helpSheet.setVisible(false);
		this.sceneController.addScene(STATE.HELP, this);
	}

	@Override
	public JPanel getPanel()
	{
		return this.helpSheet;
	}

	@Override
	public void activate()
	{
		this.helpSheet.setVisible(true);
	}

	@Override
	public void deactivate()
	{
		this.helpSheet.setVisible(false);
	}


}

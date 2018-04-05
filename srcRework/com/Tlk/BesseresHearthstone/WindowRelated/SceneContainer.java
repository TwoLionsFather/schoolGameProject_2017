package com.Tlk.BesseresHearthstone.WindowRelated;

import javax.swing.JPanel;

import com.Tlk.BesseresHearthstone.MainGameClass.STATE;

public abstract class SceneContainer
{
	private JPanel scene;

	/**
	 * default SceneSize is full windowSize
	 */
	protected SceneContainer()
	{
		scene = new JPanel();
		scene.setVisible(false);
		scene.setSize(SceneController.getSceneController().getMaxSceneSize());
	}

	protected void linkWithState(STATE state)
	{
		SceneController.getSceneController().addScene(state, this);
	}

	public JPanel getPanel()
	{
		return scene;
	}

	public void activate()
	{
		scene.setVisible(true);
	}

	public void deactivate()
	{
		scene.setVisible(false);
	}
}

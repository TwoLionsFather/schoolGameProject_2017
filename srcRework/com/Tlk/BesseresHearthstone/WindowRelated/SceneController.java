package com.Tlk.BesseresHearthstone.WindowRelated;

import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.JLayeredPane;

import com.Tlk.BesseresHearthstone.GameDataContainer;
import com.Tlk.BesseresHearthstone.ErrorHandling.SceneNotChangedException;
import com.Tlk.BesseresHearthstone.MainGameClass.STATE;

public class SceneController
{
	//Use one Instance of Scene Controller for all Operations
	private static SceneController sceneControllerSingelton = new SceneController();

	private JLayeredPane panels;
	private HashMap<STATE, SceneContainer> linkedStateScene;

	private SceneController()
	{
		panels = new JLayeredPane();
		panels.setLayout(null);
		linkedStateScene = new HashMap<STATE, SceneContainer>();
	}

	public static SceneController getSceneController()
	{
		return sceneControllerSingelton;
	}


	public void resizeJLayeredPane(GameDataContainer gameSetup)
	{
		this.panels.setSize((int) gameSetup.getWIDTH(), (int) gameSetup.getHEIGHT());
	}

	public void update(STATE oldState, STATE newState) throws Exception
	{
		if (oldState == newState)
			throw new SceneNotChangedException();

		SceneContainer currentScene = this.linkedStateScene.get(oldState);
		if (currentScene != null)
			currentScene.deactivate();

		try {
			SceneContainer newScene = linkedStateScene.get(newState);
			newScene.activate();
			panels.moveToFront(newScene.getPanel());
		} catch (NullPointerException e) {
			if (currentScene != null)
				currentScene.activate();
			else
				throw new Exception("No scenes have been found");
			throw new SceneNotChangedException(newState);
		}

	}

	//only this package should add and create scenes
	public void addScene(STATE state, SceneContainer sceneContainer)
	{
		linkedStateScene.put(state, sceneContainer);
		panels.add(sceneContainer.getPanel());
	}

	public Dimension getMaxSceneSize()
	{
		return this.panels.getSize();
	}

	public JLayeredPane getLayeredPane()
	{
		return this.panels;
	}

	public HashMap<STATE, SceneContainer> getSceneMap()
	{
		return linkedStateScene;
	}


}

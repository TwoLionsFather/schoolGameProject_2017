package com.Tlk.BesseresHearthstone.secondTry.WindowRelated;

import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.JLayeredPane;

import com.Tlk.BesseresHearthstone.secondTry.ErrorHandler;
import com.Tlk.BesseresHearthstone.secondTry.MainGameClass.STATE;
import com.Tlk.BesseresHearthstone.secondTry.Startup.GameDataContainer;

public class SceneController
{
	//Use one Instance of Scene Controller for all Operations
	private static SceneController sceneControllerSingelton = new SceneController();

	private JLayeredPane scenes;
	private HashMap<STATE, SceneContainer> linkedStateScene;
	private GameStateController gameStateController;
	private SceneContainer currentScene;

	private SceneController()
	{
		scenes = new JLayeredPane();
		scenes.setLayout(null);
		linkedStateScene = new HashMap<STATE, SceneContainer>();
	}

	public static SceneController getSceneController()
	{
		return sceneControllerSingelton;
	}


	public void resizeJLayeredPane(GameDataContainer gameSetup)
	{
		this.scenes.setSize((int) gameSetup.getWIDTH(), (int) gameSetup.getHEIGHT());
	}

	public void update()
	{
		if (currentScene != null)
		{
			currentScene.deactivate();
		}

		try {
			STATE newSTATE = gameStateController.getGameState();
			SceneContainer newScene = linkedStateScene.get(newSTATE);
			newScene.activate();
			scenes.moveToFront(newScene.getPanel());
			currentScene = newScene;
		} catch (NullPointerException e) {
			ErrorHandler.displayErrorMessage("The Scene for Game State: " + gameStateController.getGameState().toString() + " has not been loaded");
			currentScene.activate();
		}

	}

	//only this package should add and create scenes
	public void addScene(STATE state, SceneContainer sceneContainer)
	{
		linkedStateScene.put(state, sceneContainer);
		scenes.add(sceneContainer.getPanel());
	}

	public void setGameStateController(GameStateController liveGameData)
	{
		gameStateController = liveGameData;
	}

	public Dimension getMaxSceneSize()
	{
		return this.scenes.getSize();
	}

	public JLayeredPane getLayeredPane()
	{
		return this.scenes;
	}

	public HashMap<STATE, SceneContainer> getSceneMap()
	{
		return linkedStateScene;
	}

}

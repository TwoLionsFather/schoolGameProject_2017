package com.Tlk.BesseresHearthstone.secondTry.WindowRelated;

import java.util.HashMap;

import javax.swing.JLayeredPane;

import com.Tlk.BesseresHearthstone.secondTry.MainGameClass.STATE;
import com.Tlk.BesseresHearthstone.secondTry.Startup.GameDataContainer;

public class SceneController
{
	//Use one Instance of Scene Controller for all Operations
	private static SceneController sceneControllerSingelton = new SceneController();

	private JLayeredPane scenes;
	private HashMap<STATE, SceneContainer> linkedStateScene;
	private GameStateController gameStateController;

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
		linkedStateScene.get(gameStateController.getGameState()).getScene().setVisible(true);
		scenes.moveToFront(linkedStateScene.get(gameStateController.getGameState()).getScene());
	}

	//only this package should add and create scenes
	public void addScene(STATE state, SceneContainer sceneManager)
	{
		linkedStateScene.put(state, sceneManager);
		scenes.add(sceneManager.getScene());
	}

	public void setGameStateController(GameStateController liveGameData)
	{
		gameStateController = liveGameData;
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

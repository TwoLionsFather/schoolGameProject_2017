package com.Tlk.BesseresHearthstone.Startup;

import java.util.HashMap;

import com.Tlk.BesseresHearthstone.GameController;
import com.Tlk.BesseresHearthstone.GameDataContainer;
import com.Tlk.BesseresHearthstone.LiveGameDataController;
import com.Tlk.BesseresHearthstone.MainGameClass.STATE;
import com.Tlk.BesseresHearthstone.Player;
import com.Tlk.BesseresHearthstone.TextureController;
import com.Tlk.BesseresHearthstone.CardRelated.DeckDataContainer;
import com.Tlk.BesseresHearthstone.ErrorHandling.ErrorHandler;
import com.Tlk.BesseresHearthstone.ErrorHandling.FileLoadingIncompletException;
import com.Tlk.BesseresHearthstone.GameUI.CardDisplayRepresentation;
import com.Tlk.BesseresHearthstone.GameUI.GameUI;
import com.Tlk.BesseresHearthstone.WindowRelated.HelpUI;
import com.Tlk.BesseresHearthstone.WindowRelated.MenueUI;
import com.Tlk.BesseresHearthstone.WindowRelated.SceneContainer;
import com.Tlk.BesseresHearthstone.WindowRelated.SceneController;
import com.Tlk.BesseresHearthstone.WindowRelated.WindowCreator;

public class SetupController
{
	public SetupController()
	{
		LoadingWindowController progressWindow = new LoadingWindowController(4);
		ErrorHandler.setERROR_DISPLAY(progressWindow);

		progressWindow.displayLoadingMessage("LoadingFiles");
		try {
			new FileLoader();
		} catch (FileLoadingIncompletException e) {
			ErrorHandler.displayErrorMessage("File Loading incomplet -> Files missing...");
		}
		progressWindow.advanceProgressBar();

		progressWindow.displayLoadingMessage("Setting Game Options");
//		Implement to directly setup LiveGameDataController
		GameDataContainer gameSetup = new SetupFileInterpreter();
		SceneController.getSceneController().resizeJLayeredPane(gameSetup);
		progressWindow.advanceProgressBar();
		if(gameSetup.isDebugMode())
		{
			printGameSettingsToConsole(gameSetup);
		}

		progressWindow.displayLoadingMessage("Setup Cards");
		DeckBuilder deckBuilder = new DeckBuilder();
		progressWindow.advanceProgressBar();
		if(gameSetup.isDebugMode())
		{
			printTestCardDeck(deckBuilder);
		}

		progressWindow.displayLoadingMessage("Setup Textures");
		new TextureLoader(gameSetup, deckBuilder);
		progressWindow.advanceProgressBar();
		if(gameSetup.isDebugMode())
		{
			checkCardImages(deckBuilder);
		}

		progressWindow.displayLoadingMessage("Setup Window");
		LiveGameDataController liveGameData = new LiveGameDataController(gameSetup);
		Player p1 = new Player("Spieler", deckBuilder.getDeckFromCards());
		Player p2 = new Player("Computer Spieler", deckBuilder.getDeckFromCards());
		GameController controller = new GameController(p1, p2, liveGameData);

		//SceneCreation
		new GameUI(controller);
		new HelpUI(liveGameData);
		new MenueUI(liveGameData);
		initFirstPanel(liveGameData.getGameState());
		new WindowCreator(liveGameData);
		progressWindow.advanceProgressBar();
		if(gameSetup.isDebugMode())
		{
			checkSceneLayout();
		}
		progressWindow.closeWindow();

//		testOutCardRepresentation
		new CardDisplayRepresentation(liveGameData);
	}

	private void initFirstPanel(STATE state)
	{
		try {
			SceneController.getSceneController().getSceneMap().get(state).getPanel().setVisible(true);
		} catch (NullPointerException e) {
			ErrorHandler.displayErrorMessage("Initial Scene couldn't be loaded");
		}

	}

	private void printGameSettingsToConsole(GameDataContainer gameSetup)
	{
		System.out.println("GameSettings: ");
		System.out.println(gameSetup.getHEIGHT() + " x " + gameSetup.getWIDTH());
		System.out.println(gameSetup.getGameState().toString());
		System.out.println("EasyMode: " + ((Boolean) gameSetup.isEasyMode()).toString());
		System.out.println("--------------------------");
	}

	private void printTestCardDeck(DeckDataContainer deckData)
	{
		System.out.println("Cards in Play: ");
		for (int idx = 0; idx < deckData.getDeckSize(); idx++)
		{
			System.out.print(deckData.getCardNames().get(idx) + ((idx % 4 == 0) ? "\n" : ", "));
		}
		System.out.println();
	}

	private void checkCardImages(DeckDataContainer deckData)
	{
		boolean allTexturesExist = true;
		for(String cardname : deckData.getCardNames())
		{
			if (TextureController.getTexture(cardname) == null)
			{
				ErrorHandler.displayErrorMessage("Loading was incomplete for: " + cardname);
				allTexturesExist = false;
			}
		}

		if (allTexturesExist)
		{
			System.out.println("Every Card also has a texture Object");
			System.out.println("----------------------------------------------------------------");
		}
		else
		{
			ErrorHandler.displayErrorMessage("There is a Problem with the card's textures");
		}
	}

	private void checkSceneLayout()
	{
		HashMap<STATE, SceneContainer> stateSceneMap = SceneController.getSceneController().getSceneMap();
		if(checkStateMissing(STATE.MENU, stateSceneMap))
			ErrorHandler.displayErrorMessage("No MenuScene created");

		if(checkStateMissing(STATE.HELP, stateSceneMap))
			ErrorHandler.displayErrorMessage("No HelpScene created");

		if(checkStateMissing(STATE.GAME, stateSceneMap))
			ErrorHandler.displayErrorMessage("No GameScene created");

		if(checkStateMissing(STATE.BEATEN, stateSceneMap))
			ErrorHandler.displayErrorMessage("No WinningScreen created");

		if(checkStateMissing(STATE.END, stateSceneMap))
			ErrorHandler.displayErrorMessage("No LoosingScreen created");
	}

	private boolean checkStateMissing(STATE state, HashMap<STATE, SceneContainer> stateSceneMap)
	{
		return (!stateSceneMap.containsKey(state)
				|| (stateSceneMap.get(STATE.MENU).getPanel() == null));
	}
}

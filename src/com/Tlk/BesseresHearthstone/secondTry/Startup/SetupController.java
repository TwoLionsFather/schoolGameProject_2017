package com.Tlk.BesseresHearthstone.secondTry.Startup;

import com.Tlk.BesseresHearthstone.secondTry.ErrorHandler;
import com.Tlk.BesseresHearthstone.secondTry.MainGameClass.STATE;
import com.Tlk.BesseresHearthstone.secondTry.TextureController;
import com.Tlk.BesseresHearthstone.secondTry.CardRelated.DeckBuilder;
import com.Tlk.BesseresHearthstone.secondTry.CardRelated.DeckDataContainer;
import com.Tlk.BesseresHearthstone.secondTry.WindowRelated.LiveGameDataController;
import com.Tlk.BesseresHearthstone.secondTry.WindowRelated.LoadingWindowController;
import com.Tlk.BesseresHearthstone.secondTry.WindowRelated.MenueUI;
import com.Tlk.BesseresHearthstone.secondTry.WindowRelated.SceneContainer;
import com.Tlk.BesseresHearthstone.secondTry.WindowRelated.SceneController;
import com.Tlk.BesseresHearthstone.secondTry.WindowRelated.HelpUI;
import com.Tlk.BesseresHearthstone.secondTry.WindowRelated.WindowCreator;

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
//			Forward to window for error messages
			ErrorHandler.displayErrorMessage("File Loading incomplet -> Files missing...");
		}
		progressWindow.advanceProgressBar();

		progressWindow.displayLoadingMessage("Setting Game Options");
		GameDataContainer gameSetup = new SetupFileInterpreter();	//Todo: Implement to directly setup LiveGameDataController
		SceneController.getSceneController().resizeJLayeredPane(gameSetup);
		progressWindow.advanceProgressBar();
		if(gameSetup.isDebugMode())
		{
			printGameSettingsToConsole(gameSetup);
		}

		progressWindow.displayLoadingMessage("Setup Cards");
		DeckDataContainer deckBuilder = new DeckBuilder();
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
		SceneController.getSceneController().setGameStateController(liveGameData);
		new HelpUI();
		new MenueUI(liveGameData);
		new WindowCreator(liveGameData);
		progressWindow.advanceProgressBar();
		if(gameSetup.isDebugMode())
		{
			checkSceneLayout();
		}

		progressWindow.closeWindow();
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
		if (SceneController.getSceneController().getSceneMap().isEmpty())
			ErrorHandler.displayErrorMessage("No scenes have been created");
		System.out.println(((SceneContainer) SceneController.getSceneController().getSceneMap().get(STATE.MENU)).getPanel().toString());
	}

}
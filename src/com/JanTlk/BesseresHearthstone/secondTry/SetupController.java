package com.JanTlk.BesseresHearthstone.secondTry;

public class SetupController
{
	public SetupController()
	{
		LoadingWindowController progressWindow = new LoadingWindowController(4);

		progressWindow.displayLoadingMessage("LoadingFiles");
		try {
			new FileLoader();
		} catch (FileLoadingIncompletException e) {
//			Forward to window for error messages
			System.err.println("File Loading incomplet -> Files missing...");
		}
		progressWindow.advanceProgressBar();

		progressWindow.displayLoadingMessage("Setting Game Options");
		GameDataContainer gameSetup = new SetupFileInterpreter();
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

	}

	private void printGameSettingsToConsole(GameDataContainer gameSetup)
	{
		System.out.println("GameSettings: ");
		System.out.println(gameSetup.getHEIGHT() + " x " + gameSetup.getWIDTH());
		System.out.println(gameSetup.getGAMESTATE().toString());
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
				System.err.println("Loading was incomplete for: " + cardname);
				allTexturesExist = false;
			}
		}

		if (allTexturesExist)
			System.out.println("Every Card also has a texture Object");
		else
			System.err.println("There is a Problem with the card's textures");
	}

}

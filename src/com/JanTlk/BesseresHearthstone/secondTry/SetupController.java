package com.JanTlk.BesseresHearthstone.secondTry;

public class SetupController
{
	public SetupController()
	{
		LoadingWindowController progressWindow = new LoadingWindowController(4);

		progressWindow.displayLoadingMessage("LoadingFiles");
		try {
			new SetupFiles();
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
		DeckBuilder deckBuilder = new DeckBuilder();
		progressWindow.advanceProgressBar();
		if(gameSetup.isDebugMode())
		{
			printTestCardDeck(deckBuilder);
		}

		progressWindow.displayLoadingMessage("Setup Testures");
		new TextureLoader(gameSetup);
		progressWindow.advanceProgressBar();


	}

	private void printGameSettingsToConsole(GameDataContainer gameSetup)
	{
		System.out.println("GameSettings: ");
		System.out.println(gameSetup.getHEIGHT() + " x " + gameSetup.getWIDTH());
		System.out.println(gameSetup.getGAMESTATE().toString());
		System.out.println("EasyMode: " + ((Boolean) gameSetup.isEasyMode()).toString());
		System.out.println("--------------------------");
	}

	private void printTestCardDeck(DeckBuilder deckBuilder)
	{
		System.out.println("Cards in Play: ");
		int newLineCounter = 0;
		for (Card tempCard : deckBuilder.getDeckFromCards().getCards())
		{
			newLineCounter++;
			System.out.print(tempCard.getName() + ", ");
			if(newLineCounter > 4)
			{
				System.out.println();
				newLineCounter = 0;
			}

		}
		System.out.println();
	}





}

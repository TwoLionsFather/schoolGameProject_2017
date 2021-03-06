package com.Tlk.BesseresHearthstone.Startup;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import com.Tlk.BesseresHearthstone.FileController;
import com.Tlk.BesseresHearthstone.GameDataContainer;
import com.Tlk.BesseresHearthstone.ErrorHandling.ErrorHandler;
import com.Tlk.BesseresHearthstone.MainGameClass.STATE;


public class SetupFileInterpreter implements GameDataContainer
{
	private double WIDTH;
	private double HEIGHT;
	private STATE GAMESTATE;
	private boolean debugMode;
	private boolean easyMode;

	public SetupFileInterpreter()
	{
		loadGameSettingsFromFile();
	}

	private void loadDefaultGameSettings()
	{
		WIDTH = 800;
		HEIGHT = 600;
		GAMESTATE = STATE.MENU;
		debugMode = false;
		easyMode = true;
	}

	private void loadGameSettingsFromFile()
	{
		try {
			BufferedReader fileInput = new BufferedReader(new FileReader(FileController.getFile("Einstellungen.txt")));

			for (int lineNumber = 0; lineNumber < 3; lineNumber++)
			{
				String line = fileInput.readLine();

				if(lineEmpty(line))
				{
					ErrorHandler.displayErrorMessage("Config File ran out of lines -> loading Defaults");
				}

				Scanner s = new Scanner(line);
				switch (lineNumber) {
				case 0:
					WIDTH = Double.parseDouble(s.next());
					break;

				case 1:
					s.useDelimiter("/");

					double temp1 = Double.parseDouble(s.next());
					if (s.hasNext())
					{
						HEIGHT = WIDTH * Double.parseDouble(s.next()) / temp1;
					}

					else
					{
						HEIGHT = temp1;
					}

					break;

				case 2:
					String GameMode = s.next();

					GAMESTATE = STATE.MENU;
					debugMode = false;
					easyMode = true;

					if (GameMode.equalsIgnoreCase("ProMode"))
					{
						GAMESTATE = STATE.MENU;
						debugMode = false;
						easyMode = false;
					}

					else if (GameMode.equalsIgnoreCase("Debug"))
					{
						GAMESTATE = STATE.MENU;
						debugMode = true;
						easyMode = true;
					}

					else if (GameMode.equalsIgnoreCase("DebugHard"))
					{
						GAMESTATE = STATE.GAME;
						debugMode = true;
						easyMode = false;
					}

					break;
				}

				s.close();
			}

			fileInput.close();
		} catch (IOException e) {
			ErrorHandler.displayErrorMessage("No Settings File Found -> Loading Defaults");
			loadDefaultGameSettings();
		}
	}

	private boolean lineEmpty(String line)
	{
		return (line == null)
			|| line.isEmpty();
	}

	@Override
	public double getWIDTH()
	{
		return this.WIDTH;
	}

	@Override
	public double getHEIGHT() {
		return this.HEIGHT;
	}

	@Override
	public STATE getGameState()
	{
		return this.GAMESTATE;
	}

	@Override
	public boolean isDebugMode()
	{
		return this.debugMode;
	}

	@Override
	public boolean isEasyMode()
	{
		return this.easyMode;
	}

	@Override
	public String getTITLE()
	{
		return "";
	}
}

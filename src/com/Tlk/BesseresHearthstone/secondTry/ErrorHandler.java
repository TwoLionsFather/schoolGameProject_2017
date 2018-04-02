package com.Tlk.BesseresHearthstone.secondTry;

import java.util.ArrayList;

public class ErrorHandler
{
	private static ErrorDiplay ERROR_DISPLAY;
	private static ArrayList<String> ERR_MESSAGES = new ArrayList<String>();

	public static void displayErrorMessage(String message)
	{
		ERR_MESSAGES.add(message);
		ERROR_DISPLAY.outputErrorMessage(message + "\n");
	}

	public static void setERROR_DISPLAY(ErrorDiplay display)
	{
		ERROR_DISPLAY = display;
	}

	public static ArrayList<String> getErrorMessages()
	{
		return ERR_MESSAGES;
	}

	public static void clearErrorHistory()
	{
		ERR_MESSAGES = new ArrayList<String>();
	}

	public static boolean isErrorDetected()
	{
		return !ERR_MESSAGES.isEmpty();
	}
}

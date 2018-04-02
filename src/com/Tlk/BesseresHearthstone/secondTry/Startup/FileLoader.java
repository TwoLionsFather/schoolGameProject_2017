package com.Tlk.BesseresHearthstone.secondTry.Startup;

import java.io.File;

import com.Tlk.BesseresHearthstone.secondTry.FileController;

public class FileLoader
{
	/**
	 * imports all Files from array lists paths and adds them to the game files
	 * @throws FileLoadingIncompletException if not all Files have been found
	 */
	public FileLoader() throws FileLoadingIncompletException
	{
		FileController.addFilePath("Einstellungen.txt"); //
		FileController.addFilePath("Graphics\\backGround.png"); //
		FileController.addFilePath("Graphics\\CardBluePrint.png"); //
		FileController.addFilePath("Karten.txt"); //
		FileController.addFilePath("Graphics\\allCards.png"); //
		FileController.addFilePath("Graphics\\CardBack.png"); //
		FileController.addFilePath("Graphics\\HudPlayer.png"); //
		FileController.addFilePath("Graphics\\helpSheet.png"); //
		FileController.addFilePath("Graphics\\EndScreenW.png"); //
		FileController.addFilePath("Graphics\\EndScreenL.png"); //
		FileController.addFilePath("Graphics\\v_attack.png"); //
		FileController.addFilePath("Graphics\\v_life.png"); //
		FileController.addFilePath("Graphics\\GameIcon.png"); //

		importFiles();
	}

	private void importFiles() throws FileLoadingIncompletException
	{
		boolean FILE_LOADING_ERROR = false;

		for(String path : FileController.getFilePathList())
		{
			File tempFile = new File(path);
			String fileName = tempFile.getName();

			if(!tempFile.exists())
			{
				FILE_LOADING_ERROR = true;
				System.err.println("File at " + path + " expected");
			}

			FileController.addFileImport(fileName, tempFile);
		}

		if(FILE_LOADING_ERROR)
		{
			throw new FileLoadingIncompletException();
		}
	}
}

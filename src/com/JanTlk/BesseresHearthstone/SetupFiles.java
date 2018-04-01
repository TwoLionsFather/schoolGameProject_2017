package com.JanTlk.BesseresHearthstone;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

public class SetupFiles
{
	private JProgressBar fileImportBar;
	private ArrayList<String> filePaths;

	//---------------------------------------------------------------------------------------------------notUsed


	/**
	 * setsup a Loading Bar to display Progress
	 * @param loadingFrame Frame to display Loading Bar in
	 * @param filePaths list with all game Files
	 */
	public static void withProgressBar(JFrame loadingFrame, ArrayList<String> filePaths)
	{
		new SetupFiles(loadingFrame, filePaths);
	}

	/**
	 * imports all Files from array lists paths and adds them to the game files
	 * a progress bar shows progress and current ImportPath
	 */
	private SetupFiles(JFrame loadingFrame, ArrayList<String> filePaths)
	{
		this.filePaths = filePaths;

		loadingFrame.add(fileImportBar);
		fileImportBar = new JProgressBar(0, filePaths.size() - 1);
		fileImportBar.setStringPainted(true);
		importFiles();
	}

	/**
	 * imports all Files from array lists paths and adds them to the game files
	 */
	private SetupFiles(ArrayList<String> filePaths)
	{
		this.filePaths = filePaths;
		importFiles();
	}

	/**
	 * "File Path" "index in Array List"
	 * "Graphics\\backGround.png"[0];
	 * "G...\\CardBluePrint.png"[1];
	 * "Karten.txt"[2];
	 * "G...\\allCards.png"[3];
	 * "G...\\CardBack.png"[4];
	 * "G...\\HudPlayer.png"[5];
	 * "G...\\helpSheet.png"[6];
	 * "G...\\playerWin.png"[7];
	 * "G...\\pcWin.png"[8];
	 * "G...\\v_attack.png"[9];
	 * "G...\\v_life.png"[10];
	 * "G...\\GameIcon.png"[11];
	 */
	private void importFiles()
	{
		final int ERROR_NO_FILES_IN_LIST = -1;

		int fileNumber = -1;
		for (String tempFilePath : filePaths)
		{
			fileNumber++;

			if (fileImportBar != null)
			{
				fileImportBar.setValue(fileNumber);
				fileImportBar.setString(tempFilePath);
			}

			importFile(tempFilePath);
		}

		if (fileNumber == ERROR_NO_FILES_IN_LIST)
		{
			System.err.println("List of Files to Import is empty!");
			System.exit(1);
		}

		if (fileImportBar != null)
		{
			fileImportBar.setToolTipText("File import completed");
			fileImportBar.setStringPainted(false);
		}
	}

	/**
	 * tries to add a File from specified Path to GameFiles
	 * @param path files path
	 */
	private void importFile(String path)
	{
		File tempFile = new File(path);

		if (!tempFile.exists())
		{
			System.err.printf("There was no File found at: %s\n", path);
		}

	}
}

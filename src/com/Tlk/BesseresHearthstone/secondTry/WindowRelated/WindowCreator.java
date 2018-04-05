package com.Tlk.BesseresHearthstone.secondTry.WindowRelated;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import com.JanTlk.BesseresHearthstone.Hearthstone;
import com.Tlk.BesseresHearthstone.secondTry.TextureController;
import com.Tlk.BesseresHearthstone.secondTry.Startup.GameDataContainer;

public class WindowCreator
{

	public WindowCreator(GameDataContainer gameData)
	{
		JFrame gameMainFrame = new JFrame(gameData.getTITLE());

		Dimension windowSize = new Dimension((int) gameData.getWIDTH(), (int) gameData.getHEIGHT());
		gameMainFrame.setPreferredSize(windowSize);
		gameMainFrame.setMaximumSize(windowSize);
		gameMainFrame.setMinimumSize(windowSize);
		gameMainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameMainFrame.setLayout(null);
		gameMainFrame.setResizable(false);
		gameMainFrame.setLocationRelativeTo(null);
		gameMainFrame.setIconImage(TextureController.getTexture("GameIcon.png"));

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		if (Hearthstone.BREITE >= screenSize.getWidth()
		&& Hearthstone.HOEHE >= screenSize.getHeight())
		{
			gameMainFrame.setUndecorated(true);
		}

		gameMainFrame.add(SceneController.getSceneController().getLayeredPane());
		gameMainFrame.setVisible(true);
	}

}

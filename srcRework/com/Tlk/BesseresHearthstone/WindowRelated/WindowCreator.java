package com.Tlk.BesseresHearthstone.WindowRelated;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import com.Tlk.BesseresHearthstone.GameDataContainer;
import com.Tlk.BesseresHearthstone.TextureController;

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
		if (windowSize.getWidth() >= screenSize.getWidth()
		&& windowSize.getHeight() >= screenSize.getHeight())
		{
			gameMainFrame.setUndecorated(true);
		}

		gameMainFrame.add(SceneController.getSceneController().getLayeredPane());
		gameMainFrame.setVisible(true);
	}

}

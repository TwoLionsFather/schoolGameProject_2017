package com.JanTlk.BesseresHearthstone.secondTry;

import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

public class LoadingWindowController
{
	private JFrame loadingFrame;
	private JProgressBar loadingBar;

	public LoadingWindowController(int ammountOfLoadingOperations)
	{
		loadingFrame = new JFrame("Loading Gwint");
		loadingBar = new JProgressBar(0, ammountOfLoadingOperations);
		loadingBar.setStringPainted(true);
		loadingFrame.setLayout(new FlowLayout());
		loadingFrame.setSize(300, 100);
		loadingFrame.setLocationRelativeTo(null);
		loadingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loadingFrame.setResizable(false);
		loadingFrame.setVisible(true);
		loadingFrame.add(loadingBar);
	}

	public void displayLoadingMessage(String message)
	{
		loadingBar.setString(message);
	}

	public void advanceProgressBar()
	{
		loadingBar.setValue(loadingBar.getValue() + 1);
	}

	public void closeWindow()
	{
		loadingFrame.dispose();
	}
}

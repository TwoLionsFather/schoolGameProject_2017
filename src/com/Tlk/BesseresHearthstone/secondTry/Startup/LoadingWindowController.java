package com.Tlk.BesseresHearthstone.secondTry.Startup;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.Tlk.BesseresHearthstone.secondTry.ErrorDiplay;
import com.Tlk.BesseresHearthstone.secondTry.ErrorHandler;

public class LoadingWindowController implements ErrorDiplay
{
	private JFrame loadingFrame;
	private JProgressBar loadingBar;
	private JTextArea errorTextField;
	private JScrollPane errorField;

	public LoadingWindowController(int ammountOfLoadingOperations)
	{
		loadingFrame = new JFrame("Loading Gwint");
		loadingBar = new JProgressBar(0, ammountOfLoadingOperations);
		errorTextField = new JTextArea();
		errorField = new JScrollPane(errorTextField, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		errorTextField.setEditable(false);
		errorTextField.setForeground(Color.RED);
		errorTextField.setToolTipText("Fehler Anzeige");
		loadingBar.setStringPainted(true);
		loadingFrame.setLayout(new BorderLayout()); //FlowLayout
		loadingFrame.setSize(350, 100);
		loadingFrame.setLocationRelativeTo(null);
		loadingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loadingFrame.setResizable(false);
		loadingFrame.setVisible(true);
		loadingFrame.add(loadingBar, BorderLayout.NORTH);
		loadingFrame.add(errorField, BorderLayout.CENTER);
	}

	public void displayLoadingMessage(String message)
	{
		loadingBar.setString(message);
	}

	@Override
	public void outputErrorMessage(String message)
	{
		errorTextField.append(message);
	}

	public void advanceProgressBar()
	{
		loadingBar.setValue(loadingBar.getValue() + 1);
	}

	public void closeWindow()
	{
		loadingFrame.dispose();
		if(ErrorHandler.isErrorDetected())
			JOptionPane.showMessageDialog(null, errorTextField.getText(), "Setup Error Acknowlage", JOptionPane.ERROR_MESSAGE, null);
		ErrorHandler.clearErrorHistory();
	}

}

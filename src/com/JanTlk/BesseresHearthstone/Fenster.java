package com.JanTlk.BesseresHearthstone;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Fenster extends Canvas
{
	private static final long serialVersionUID = 8837313394937421756L;

	/**
	 * Fenster is used to add the Game Component to a new JFrame
	 * @param breite width of the game/frame
	 * @param hoehe hight of the game/frame
	 * @param titel the title of the game window
	 * @param spiel the game component
	 */
	public Fenster(float breite, float hoehe, String titel, Hearthstone spiel)
	{
		JFrame frame = new JFrame(titel);

		//Das Fenster kann nur noch in der festgelegten größe sein und wird normal geschlossen
		frame.setPreferredSize(new Dimension((int) breite, (int) hoehe));
		frame.setMaximumSize(new Dimension((int) breite, (int) hoehe));
		frame.setMinimumSize(new Dimension((int) breite, (int) hoehe));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		try {
			frame.setIconImage(ImageIO.read(Hearthstone.allImportedFiles[11]));
		} catch (IOException e) {	}

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		if (Hearthstone.BREITE >= screenSize.getWidth()
		&& Hearthstone.HOEHE >= screenSize.getHeight())
		{
			frame.setUndecorated(true);
		}

		frame.setAlwaysOnTop(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.add(spiel);
		frame.setVisible(true);
	}

}

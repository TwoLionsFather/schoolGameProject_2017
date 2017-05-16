package com.JanTlk.BesseresHearthstone;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Fenster extends Canvas 
{

	private static final long serialVersionUID = 8837313394937421756L;

	public Fenster(float breite, float hoehe, String titel, Hearthstone spiel)
	{
		//JFrame bekommt einen Titel für das Fenster
		JFrame frame = new JFrame(titel);
		
		//Das Fenster kann nur noch in der festgelegten größe sein und wird normal geschlossen
		frame.setPreferredSize(new Dimension((int) breite, (int) hoehe));
		frame.setMaximumSize(new Dimension((int) breite, (int) hoehe));
		frame.setMinimumSize(new Dimension((int) breite, (int) hoehe));		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.add(spiel);		
		frame.setVisible(true);		
//		spiel.render();
//		spiel.start();
	}
}

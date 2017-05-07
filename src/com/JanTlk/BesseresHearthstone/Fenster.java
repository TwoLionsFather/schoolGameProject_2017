package com.JanTlk.BesseresHearthstone;

import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Fenster extends Canvas {
	
	//benötigt um Kommunikation zwischen den richtigen Klassen sicher zu stellen
	private static final long serialVersionUID = 8837313394937421756L;

	public Fenster(float breite, float hoehe, String titel, Hearthstone spiel)
	{
		//JFrame benötigt einen Titel für das Fenster
		JFrame frame = new JFrame(titel);
		
		//Das Fenster kann nur noch in der festgelegten größe sein und wird normal geschlossen
		frame.setPreferredSize(new Dimension((int) breite, (int) hoehe));
		frame.setMaximumSize(new Dimension((int) breite, (int) hoehe));
		frame.setMinimumSize(new Dimension((int) breite, (int) hoehe));		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		//die größe ist für den Benutzer nicht mehr änderbar
		frame.setResizable(false);
		//Das Fenster ist in der Mitte des Bildschirms
		frame.setLocationRelativeTo(null);
		//Dem Fenster wird gesagt woher es die Anzeige bekommt
		frame.add(spiel);		
		//das Fenster wird sichtbar
		frame.setVisible(true);		
		//das Spiel beginnt
		spiel.start();
	}
}

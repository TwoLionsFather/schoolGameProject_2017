package com.JanTlk.BesseresHearthstone;

import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.JanTlk.BesseresHearthstone.Karten.Karte;

public class Spielfeld
{
	private Deck dPL;
	private Deck dPC;
	
	private JLabel playerDraw;
	private JLabel playerHand;
	private JLabel playerFriedhof;
	
	private JLabel pcDraw;
	private JLabel pcHand;
	private JLabel pcFriedhof;
	
	private int kartenCount;
	
	public Spielfeld() 
	{
		DeckHandler dH = new DeckHandler();	
		
		dPL = dH.getPlayerDeck();
		dPC = dH.getPCDeck();
	}
	
	public void render()
	{
		JPanel content = new JPanel();
		playerDraw = new JLabel();
		playerHand = new JLabel();
		playerFriedhof = new JLabel();
		
		pcDraw = new JLabel();
		pcHand = new JLabel();
		pcFriedhof = new JLabel();
		
		playerDraw.setBounds((int) (Hearthstone.BREITE - Karte.breite + 5)
							, (int) (Hearthstone.HOEHE - Karte.hoehe + 5)
							, (int) Karte.breite + 2
							, (int) Karte.hoehe + 2);
		
		playerDraw = dPL.getKarten().getFirst().cardToJLabel();
		content.add(playerDraw);
		
		JFrame window = new JFrame("Test Display");
		window.setSize(1280, 780);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);

		window.add(content);
	}
	
	/**
	 * this draws the Image of every Card, at the moment at 0|0
	 * @param g
	 */
	public void render(Graphics g) 
	{
		for(Karte tempKarte : dPL.getKarten())
		{
			tempKarte.drawCard(0 + 55 * kartenCount++, 200, g);
		}
		kartenCount = 0;
		
		for(Karte tempKarte : dPC.getKarten())
		{
			tempKarte.drawCard(0 + 55 * kartenCount++, 0, g);
		}
		kartenCount = 0;
	}

	public void tick() 
	{
		
	}
	
}

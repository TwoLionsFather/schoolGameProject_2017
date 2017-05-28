package com.JanTlk.BesseresHearthstone;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Hearthstone extends Canvas
{
	private static final long serialVersionUID = 2288248461332515463L;

	public enum STATE
	{
		MENU()
		, END()
		, GAME()
		, HELP()
		, BEATEN();
	}
	
	public static final String TITEL = "Hearthstone";	//Titel für das Spiel
	public static final float BREITE = 1920; 			// 1920 für Fullscreen
	public static final float HOEHE = BREITE / 16 * 9; 	// 3/4 der Breite -> Höhe
	public static STATE gameState = STATE.GAME;
	
	private BufferedImage background;
	private Spielfeld spielfeld;
	private Menu menu;
	
	public Hearthstone()
	{	
		this.setBackground(Color.black);
		
		try {
			background = ImageIO.read(allImportedFiles()[0]);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Backgound not found");
		}
		
		this.menu = new Menu();
		this.spielfeld = new Spielfeld(this);
		
		MouseInput mouseStuff = new MouseInput(spielfeld, menu, this);
		this.addMouseMotionListener(mouseStuff);
		this.addMouseListener(mouseStuff);
		
		new Fenster(BREITE, HOEHE, TITEL, this);	
		
	}
	
	
	/**
	 * "G\\backGround.png"[0]; 
	 * "G\\CardBluePrint.png"[1]; 
	 * "Karten.txt"[2]; 
	 * "G\\allCards.png"[3];
	 * "G\\CardBack.png"[4];
	 * "G\\HudPlayer.png"[5]; 
	 * @return list of listed Files
	 */
	public static File[] allImportedFiles()
	{
		ArrayList<String> paths = new ArrayList<String>();
		paths.add("Graphics\\backGround.png"); //Hearthstone.Hearthstone
		paths.add("Graphics\\CardBluePrint.png"); //CardCreator.nextCard
		paths.add("Karten.txt"); //CardCreator.CardCreator 
		paths.add("Graphics\\allCards.png"); //CardCreator.CardCreator
		paths.add("Graphics\\CardBack.png"); //DrawDeck.DrawDeck
		paths.add("Graphics\\HudPlayer.png"); //Spielfeld.Spielfeld
		
		File[] allImportedFiles = new File[paths.size()];
		
		int counter = -1;
		for (File tempF : allImportedFiles)
		{
			counter++;
			tempF = importFiles(paths.get(counter));
			
			if (tempF == null)
			{
				System.err.printf("The %d.File was not found at %s", counter + 1, paths.get(counter));
			}
			
			allImportedFiles[counter] = tempF;
		}
		
		return allImportedFiles;
	}
	
	/**
	 * this will create a new File from path
	 * @param path the Path where the File that needs to get imported is at
	 * @return the new File
	 */
	private static File importFiles(String path)
	{		
		return new File(path);
	}
	
	/**
	 * used by the Component Class to repaint the "Game" Component
	 * Updates get stored in Buffer so only changes get rendered
	 * This improves Performance.... a lot
	 */
	@Override
	public void paint(Graphics g)
	{
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(2);
			return;
		}
		
		Graphics gb = bs.getDrawGraphics();
		
		if (gameState == STATE.GAME)
		{
			gb.drawImage(background, 0, 0, null);	
			spielfeld.render(gb);
		}
		
		else
		{
			menu.render(gb);
		}
		
		gb.dispose();
		bs.show();
	}
	
	public static void main(String[] args) 
	{
		new Hearthstone();
	}

	/**
	 * used to prevent a variable from running out of bounds
	 */
	public static float clamp(float var, float min, float max)
	{
		if(var >= max)
			return var = max;
		
		else if(var <= min)
			return var = min;
		
		else 
			return var;
	}
	
}

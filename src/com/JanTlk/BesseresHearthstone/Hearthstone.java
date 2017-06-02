package com.JanTlk.BesseresHearthstone;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

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
		, BEATEN()
		, RESETGAME();
	}
	
	public static final String TITEL = "Gwint";	//Titel für das Spiel
	public static float BREITE; 			// 1920 für Fullscreen
	public static float HOEHE; 	// 3/4 der Breite -> Höhe
	public static STATE gameState;
	private static boolean debug;
	private static boolean drawHelp;
	
	private BufferedImage background;
	private Spielfeld spielfeld;
	private Menu menu;
	
	public Hearthstone()
	{	
		this.setBackground(Color.black);
		try {
			setup();
		} catch (IOException e1) {
			e1.printStackTrace();
			System.err.println("Settup failed");
		}
		
		try {
			background = rescaledBufferedimage(ImageIO.read(allImportedFiles()[0])
											, (int) BREITE
											, (int) HOEHE);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Backgound not found");
		}
		
		this.menu = new Menu();

		this.spielfeld = new Spielfeld(this);
		
		UIInput uiStuff = new UIInput(spielfeld, menu, this);
		this.addKeyListener(uiStuff);
		this.addMouseMotionListener(uiStuff);
		this.addMouseListener(uiStuff);
		
		
		new Fenster(BREITE, HOEHE, TITEL, this);	
		
	}
	
	/**
	 * "G\\backGround.png"[0]; 
	 * "G\\CardBluePrint.png"[1]; 
	 * "Karten.txt"[2]; 
	 * "G\\allCards.png"[3];
	 * "G\\CardBack.png"[4];
	 * "G\\HudPlayer.png"[5];
	 * "G\\helpSheet.png"[6]; 
	 * "G\\playerWin.png"[7]; 
	 * "G\\pcWin.png"[8];  
	 * "G\\v_attack.png"[9];
	 * "G\\v_life.png"[10];
	 * "Graphics\\GameIcon.png"[11];
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
		paths.add("Graphics\\helpSheet.png"); //Menu.Menu
		paths.add("Graphics\\EndScreenW.png"); //Menu.Menu
		paths.add("Graphics\\EndScreenL.png"); //Menu.Menu
		paths.add("Graphics\\v_attack.png"); //Karte.Karte
		paths.add("Graphics\\v_life.png"); //Karte.Karte
		paths.add("Graphics\\GameIcon.png"); //Fenster:Fenster
		
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
	 * reads and sets up Game
	 * @throws IOException
	 */
	public void setup() throws IOException 
	{
		BufferedReader br = null;
		
		try {
			br =  new BufferedReader(new FileReader(new File("Einstellungen.txt")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("No Config File Found, fatal error!");
			System.exit(1);
		}
		
		for (int i = 1; i <= 3; i++)
		{
			String line = br.readLine();
			
			if((line == null)
			|| (line.isEmpty())
			&& i != 3)
			{
				System.err.println("Config File ran out of lines");
				System.exit(1);
			}
			
			Scanner s = new Scanner(line);
			switch (i)
			{
			case 1:
				BREITE = s.nextInt();
				break;
				
			case 2:
				s.useDelimiter("/");
				
				int temp1 = s.nextInt();
				if (s.hasNext())
				{
					HOEHE = BREITE / temp1 * s.nextInt();
				}
				
				else 
				{
					HOEHE = temp1;
				}
				break;
				
			case 3:				
				String GameMode = s.next();
				
				if (GameMode.equalsIgnoreCase("ProMode")) 
				{
					gameState = STATE.MENU;
					debug = false;
					drawHelp = false;
					break;
				}
				
				else if (GameMode.equalsIgnoreCase("Debug")) 
				{
					gameState = STATE.GAME;
					debug = true;
					drawHelp = true;
					break;
				}
				
				else if (GameMode.equalsIgnoreCase("TestGame")) 
				{
					gameState = STATE.GAME;
					debug = true;
					drawHelp = false;
					break;
				}
				
				else
				{
					gameState = STATE.MENU;
					debug = false;
					drawHelp = true;
					break;
				}
			}
			
			s.close();
		}
		
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
		this.requestFocus();
		
		BufferStrategy bs = this.getBufferStrategy();
		if (bs == null) {
			this.createBufferStrategy(2);
			return;
		}
		
		Graphics gb = bs.getDrawGraphics();
		
		if (gameState == STATE.GAME
		&& gameState != STATE.BEATEN
		|| gameState == STATE.RESETGAME)
		{
			gb.drawImage(background, 0, 0, null);	
			spielfeld.render(gb);
		}
		
		else
		{
			menu.render(spielfeld.getGameStats()[0] > spielfeld.getGameStats()[3] ,gb);
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

	/**
	 * used to convert scaled image of original Buffered Image
	 * @param img the image object that will get converted
	 * @return a new buffered image with correct scale
	 */
	public static BufferedImage rescaledBufferedimage(BufferedImage bimg, int width, int height)
	{
		
		Image img = bimg.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}
	
	public static boolean isDebugMode() {
		return debug;
	}


	public static boolean isDrawhelpActive() {
		return drawHelp;
	}
	
}

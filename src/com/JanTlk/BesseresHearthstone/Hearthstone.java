package com.JanTlk.BesseresHearthstone;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
	public static STATE gameState = STATE.MENU;
	
	private BufferedImage background;
	private Spielfeld spielfeld;
	private Menu menu;
	
	public Hearthstone()
	{	
		this.setBackground(Color.black);
		
		try {
			background = ImageIO.read(new File("Graphics\\backGround.png"));
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
		
//		float tst = (float) ((-1/(3 - 5)) * 2 + 1);
//		System.out.printf("%.3f \n", tst);
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

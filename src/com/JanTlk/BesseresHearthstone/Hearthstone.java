package com.JanTlk.BesseresHearthstone;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Hearthstone extends Canvas
{
	private static final long serialVersionUID = 2288248461332515463L;

	public static final String TITEL = "Hearthstone";	//Titel für das Spiel
	public static final float BREITE = 1920; 			// 1920 für Fullscreen
	public static final float HOEHE = BREITE / 16 * 9; 	// 3/4 der Breite -> Höhe
	
	private Spielfeld spielfeld;
	
	private BufferedImage backGround;
	
	public Hearthstone()
	{		
		try {
			backGround = ImageIO.read(new File("Graphics\\backGround.png"));
		} catch (IOException e) {
			backGround = null;
			e.printStackTrace();
		}
		
		spielfeld = new Spielfeld(this);
		MousInput mouseStuff = new MousInput(spielfeld);
		this.addMouseMotionListener(mouseStuff);
		this.addMouseListener(mouseStuff);
		
		//window init
		new Fenster(BREITE, HOEHE, TITEL, this);	
	}
	
	/**
	 * used by the Component Class to repaint the "Game" Component
	 */
	public void paint(Graphics g)
	{
		if(backGround == null)
		{
			g.setColor(Color.black);
			g.fillRect(0, 0, (int) BREITE, (int) HOEHE);
		}
		g.drawImage(backGround, 0, 0, null);
		
		spielfeld.render(g);
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

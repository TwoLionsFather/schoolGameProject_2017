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
	private static final long serialVersionUID = 406005602200328868L;
	
	public static final String TITEL = "Hearthstone";
	public static final float BREITE = 1280; 			// 1920 für Fullscreen
	public static final float HOEHE = BREITE / 4 * 3; 	// 3/4 der Breite -> Höhe
	
	private Spielfeld spielfeld;
	
	private BufferedImage backGround;
	
	public Hearthstone()
	{		
		try {
			backGround = ImageIO.read(new File("backGround.png"));
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
	
	public void paint(Graphics g)
	{
		g.setColor(Color.black);
		g.fillRect(0, 0, (int) BREITE, (int) HOEHE);
		g.drawImage(backGround, 0, 0, null);
		
		spielfeld.render(g);
	}
	
	public static void main(String[] args) 
	{
		new Hearthstone();
	}
}

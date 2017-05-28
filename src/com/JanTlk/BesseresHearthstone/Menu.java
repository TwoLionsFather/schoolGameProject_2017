package com.JanTlk.BesseresHearthstone;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.JanTlk.BesseresHearthstone.Hearthstone.STATE;

public class Menu 
{
	private Rectangle[] buttons;
	private BufferedImage helpSheet;
	
	public Menu() 
	{
		try {
			helpSheet = ImageIO.read(new File("Graphics\\helpSheet.png"));
		} catch (IOException e) {
			System.err.println("Found no Help File");
			helpSheet = null;
		}
		
		buttons = new Rectangle[3];
		for(int i = 0; i < buttons.length; i++)
		{
			Rectangle button = new Rectangle((int) (Hearthstone.BREITE / 2 - Hearthstone.BREITE / 10)
										, (int) (i * Hearthstone.HOEHE / 5 + Hearthstone.HOEHE / 10 * (i + 1))
										, (int) Hearthstone.BREITE / 5
										, (int) Hearthstone.HOEHE / 5);
			buttons[i] = button;
		}

	}
	
	/**
	 * draws menu and buttons on the screen, MouseInputClass handles their effect
	 * @param g
	 */
	public void render(Graphics g)
	{
		g.setColor(Color.black);
		g.fillRect(0, 0
				, (int) Hearthstone.BREITE
				, (int) Hearthstone.HOEHE);
		
		if(Hearthstone.gameState == STATE.MENU)
		{
			for (int i = 0; i < buttons.length; i++)
			{
				Rectangle button = buttons[i];
				
				g.setColor(Color.white);
				g.drawRect((int) button.getX()
						, (int) button.getY()
						, (int) button.getWidth()
						, (int) button.getHeight());
				
				g.setFont(new Font("Menü", Font.BOLD, 25));
				
				switch(i)
				{
				case 0: 
					g.drawString("Spielen"
							, (int) (button.getX() +  button.getWidth() / 2 - "Spielen".length() * 6)
							, (int) (button.getY() +  button.getHeight() / 2 + 12));
					break;
					
				case 1: 
					g.drawString("Anleitung"
							, (int) (button.getX() +  button.getWidth() / 2 - "Anleitung".length() * 6)
							, (int) (button.getY() +  button.getHeight() / 2 + 12));
					break;
					
				case 2: 
					g.drawString("Beenden"
							, (int) (button.getX() +  button.getWidth() / 2 - "Beenden".length() * 6)
							, (int) (button.getY() +  button.getHeight() / 2 + 12));
					break;
				}
				
			}
			
		}
		
		else if(Hearthstone.gameState == STATE.END) 
		{
			g.setColor(Color.white);
			g.setFont(new Font("Ende", Font.BOLD, 50));
			g.drawString("Spiel Ende"
					, (int) Hearthstone.BREITE / 2 - "Spiel Ende".length() * 10
					, (int) Hearthstone.HOEHE / 2);
		}
		
		else if(Hearthstone.gameState == STATE.HELP
		&& helpSheet != null)
		{
			g.drawImage(helpSheet
					, (int) (Hearthstone.BREITE / 2 - helpSheet.getWidth() / 2)
					, 0
					, null);
		}
		
		else if(Hearthstone.gameState == STATE.BEATEN) 
		{
			g.setColor(Color.white);
			g.setFont(new Font("Ende", Font.BOLD, 50));
			g.drawString("Gewonnen/Verloren , cool"
					, (int) Hearthstone.BREITE / 2 - "Gewonnen/Verloren , cool".length() * 10
					, (int) Hearthstone.HOEHE / 2);
		}
		
		
	}

	public Rectangle[] getButtons() 
	{
		return buttons;
	}

}

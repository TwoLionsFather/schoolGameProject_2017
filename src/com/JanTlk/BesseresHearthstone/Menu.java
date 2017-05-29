package com.JanTlk.BesseresHearthstone;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Menu 
{
	private Rectangle[] buttons;
	private BufferedImage helpSheet;
	private BufferedImage playerWin;
	private BufferedImage pcWin;
	
	public Menu()
	{
		try {
			helpSheet = ImageIO.read(Hearthstone.allImportedFiles()[6]);

			playerWin = Hearthstone.rescaledBufferedimage(ImageIO.read(Hearthstone.allImportedFiles()[7])
														, (int) Hearthstone.BREITE
														, (int) Hearthstone.HOEHE);

			pcWin = Hearthstone.rescaledBufferedimage(ImageIO.read(Hearthstone.allImportedFiles()[8])
														, (int) Hearthstone.BREITE
														, (int) Hearthstone.HOEHE);
			
		} catch (IOException e) {
			e.printStackTrace();
			playerWin = null;
			pcWin = null;
			System.err.println("File not found for menu build.");
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
	 *  draws menu and buttons on the screen, MouseInputClass handles their effect
	 * @param gameStats this is needed to check if game is over
	 * @param g
	 */
	public void render(boolean playerW, Graphics g)
	{
		g.setColor(Color.black);
		g.fillRect(0, 0
				, (int) Hearthstone.BREITE
				, (int) Hearthstone.HOEHE);
		
		switch (Hearthstone.gameState)
		{
		case MENU:
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
			break;
		
		case END:
			g.setColor(Color.white);
			g.setFont(new Font("Ende", Font.BOLD, 50));
			g.drawString("Spiel Ende"
						, (int) Hearthstone.BREITE / 2 - "Spiel Ende".length() * 10
						, (int) Hearthstone.HOEHE / 2);
			break;
			
		case HELP:
			if (helpSheet != null)
			{
				g.drawImage(helpSheet
						, (int) (Hearthstone.BREITE / 2 - helpSheet.getWidth() / 2)
						, 0
						, null);
			}
			
			else 
			{
				g.setColor(Color.white);
				g.setFont(new Font("Arial", Font.PLAIN, 12));;
				g.drawString("No Help File"
						, (int) Hearthstone.BREITE / 2 - ("No Help File".length() * 5)
						, (int) Hearthstone.HOEHE / 2 - 6);
			}
			
			break;
			
		case BEATEN:
			if (playerW
			&& playerWin != null)
			{
				g.drawImage(playerWin, 0, 0, null);
			}
			
			else if (!playerW
			&& playerWin != null)
			{
				g.drawImage(pcWin, 0, 0, null);
						
			}
			else 
			{
				g.setColor(Color.white);
				g.setFont(new Font("Ende", Font.BOLD, 50));
				g.drawString("Gewonnen/Verloren , cool"
							, (int) Hearthstone.BREITE / 2 - "Gewonnen/Verloren , cool".length() * 10
							, (int) Hearthstone.HOEHE / 2);
			}
			break;
			
		default: 
			break;
		}
		
		
	}

	public Rectangle[] getButtons() 
	{
		return buttons;
	}

}

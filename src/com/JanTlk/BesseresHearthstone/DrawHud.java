package com.JanTlk.BesseresHearthstone;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.JanTlk.BesseresHearthstone.Karten.Karte;
import com.JanTlk.BesseresHearthstone.Karten.Status;

public class DrawHud 
{
	private BufferedImage hudtexture;
	private Rectangle hud; //all locations relative to this
	
	public DrawHud() 
	{
		try {
			hudtexture = (ImageIO.read(Hearthstone.allImportedFiles()[5]));
			
//			Testing went wrong
//			hudtexture = DrawDeck.rescaledBufferedimage((ImageIO.read(Hearthstone.allImportedFiles()[5])), 300, 170);
		} catch (IOException e) {
			hudtexture = null;
			e.printStackTrace();
		}
		
		hud = new Rectangle(50
						, (int) Hearthstone.HOEHE - hudtexture.getHeight() - 10
						, hudtexture.getWidth()
						, hudtexture.getHeight());
	}
	
	/**
	 * is used to render the hud with players, pcs and cards stats
	 * @param playersMove if the hud is rendered in the players move or not
	 * @param cardInDetail the card of which stats get shown on player hud
	 * @param gameStats Stats Array that is used to display games current stats
	 * @param g the component on that the graphics get drawn
	 */
	public void render(boolean playersMove, Karte cardInDetail, int[] gameStats, Graphics g)
	{		
		//hud texture
		g.drawImage(hudtexture
	            , (int) hud.getX()
	            , (int) hud.getY()
	            , (int) hud.getWidth()
	            , (int) hud.getHeight()
	            , null);
		
		g.setFont(new Font("Century", Font.BOLD, 14));
		//Overlay on Hud texture
		if((cardInDetail != null)
		&& cardInDetail.getStatus() != Status.ABBLAGE)
		{
			g.setColor((cardInDetail.getDeck().toString().contains("PC") ? Color.red : Color.green));
			
			//lifeDisplay Player
			String dmgCard = String.format("%2d%5s%-25s", cardInDetail.getSchaden(), "", "Schaden von " + cardInDetail.getName());
			
			g.drawString(dmgCard
					, (int) (hud.getX() + 25)
					, (int) (hud.getY() + 62));
			
			//manaDisplay Player
			String lifeCard = String.format("%2d%5s%-25s", cardInDetail.getLeben(), "", "Leben von " + cardInDetail.getName());
					
			g.drawString(lifeCard
					, (int) (hud.getX() + 25)
					, (int) (hud.getY() + 95));
		}
		
		drawInfo(gameStats, g);
		drawDeckInfo(gameStats, g);
	}
	
	/**
	 * draws ammount of cards on players/pcs Stapel/Abblage
	 * @param gameStats these stats are used fpr display
	 * @param g as in every other class
	 */
	public void drawDeckInfo(int[] gameStats, Graphics g)
	{
		//Stapel PC
		g.setColor(Color.green);
		g.drawString("" + gameStats[6]
					, 20
					, (int) Hearthstone.HOEHE - 10);
		
		//Stapel Player
		g.drawString("" + gameStats[7]
					, (int) Hearthstone.BREITE - 25
					, (int) Hearthstone.HOEHE - 10);
		
		//Abblage PC
		g.setColor(Color.red);
		g.drawString("" + gameStats[8]
					, 15
					, 15);
		  
		//Abblage Player
		g.drawString("" + gameStats[9]
					, (int) Hearthstone.BREITE - 25
					, 15);
	}
	
	/**
	 * draws Life and Mana of Player on hudtexture
	 * @param gameStats these stats are used fpr display
	 * @param g as in every other class
	 */
	public void drawInfo(int[] gameStats, Graphics g)
	{
		g.setColor(new Color(160, 151, 16)); //possibly change this to RGB of Gold for looks
		
		//lifeDisplay Player
		String lifePL = String.format("%2d%5s%-15s", gameStats[0], "", "Leben Player");
		
		g.drawString(lifePL
				, (int) (hud.getX() + 25)
				, (int) (hud.getY() + hud.getHeight() - 85));
		
		//manaDisplay Player
		String manaPL = String.format("%2d%5s%-15s", gameStats[1], "", "Mana Player");
				
		g.drawString(manaPL
				, (int) (hud.getX() + 25)
				, (int) (hud.getY() + hud.getHeight() - 53));
		
		
		//lifeDisplay PC
		String lifePC = String.format("%15s %5d", "Leben PC", gameStats[3]);
		
		g.drawString(lifePC
				, (int) (hud.getX() + hud.getWidth() - lifePC.length() * 7 - 20)
				, (int) (hud.getY() + hud.getHeight() - 85));
		
		//manaDisplay PC
		String manaPC = String.format("%15s %5d", "Mana PC Max", gameStats[5]);
		
		g.drawString(manaPC
				, (int) (hud.getX() + hud.getWidth() - manaPC.length() * 8 - 20)
				, (int) (hud.getY() + hud.getHeight() - 53));

	}
	
}

package com.JanTlk.BesseresHearthstone;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.JanTlk.BesseresHearthstone.Karten.Karte;
import com.JanTlk.BesseresHearthstone.Karten.Status;

public class DrawHud 
{
	private BufferedImage hudtexture;
	private Rectangle hud; //all locations relative to this
	private Rectangle nextRoundB;
	
	public DrawHud(File hudTextur, Rectangle nextRoundB) 
	{
		this.nextRoundB = nextRoundB;
		
		try {
			hudtexture = ImageIO.read(hudTextur);
		} catch (IOException e) {
			hudtexture = null;
			e.printStackTrace();
		}
		
		hud = new Rectangle(50
						, (int) Hearthstone.HOEHE - hudtexture.getHeight() - 40
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
		//next Round Button
		g.setColor((playersMove) ? Color.green : Color.red);
		g.drawRect((int) nextRoundB.getX()
				, (int) nextRoundB.getY()
				, (int) nextRoundB.getWidth()
				, (int) nextRoundB.getHeight());	
		
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
			g.setColor((!cardInDetail.getDeck().toString().contains("PC")) ? Color.GREEN : Color.RED);
			
			g.drawString("" + cardInDetail.getSchaden()
					, (int) (hud.getX() + ((cardInDetail.getSchaden() > 9) ? 9 : 12))
					, (int) (hud.getY() + 22));
			
			g.drawString("Schaden von " + cardInDetail.getName()
					, (int) (hud.getX() + 50)
					, (int) (hud.getY() + 22));
			
			g.drawString("" + cardInDetail.getLeben()
					, (int) (hud.getX() + ((cardInDetail.getLeben() > 9) ? 9 : 12))
					, (int) (hud.getY() + 55));
			
			g.drawString("Leben von " + cardInDetail.getName()
					, (int) (hud.getX() + 50)
					, (int) (hud.getY() + 55));

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
		g.setColor(Color.green);
		g.drawString("" + gameStats[6]
					, 20
					, (int) Hearthstone.HOEHE - 40);
		
		g.drawString("" + gameStats[7]
					, (int) Hearthstone.BREITE - 25
					, (int) Hearthstone.HOEHE - 40);
		
		g.setColor(Color.red);
		g.drawString("" + gameStats[8]
					, 15
					, 15);
		  
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
		g.setColor(Color.white); //possibly change this to RGB of Gold for looks
		
		//lifeDisplay Player
		String lifePL = String.format("%-5d %-15s", gameStats[1], "Leben Player");
		
		g.drawString(lifePL
				, (int) (hud.getX() + 12)
				, (int) (hud.getY() + hud.getHeight() - 45));
		
		//manaDisplay Player
		String manaPL = String.format("%-5d %-15s", gameStats[1], "Mana Player");
				
		g.drawString(manaPL
				, (int) (hud.getX() + 12)
				, (int) (hud.getY() + hud.getHeight() - 12));
		
		
		//lifeDisplay PC
		String lifePC = String.format("%15s %5d", "Leben PC", gameStats[3]);
		
		g.drawString(lifePC
				, (int) (hud.getX() + hud.getWidth() - lifePC.length() * 7 - 8)
				, (int) (hud.getY() + hud.getHeight() - 45));
		
		//manaDisplay PC
		String manaPC = String.format("%15s %5d", "Mana PC Max", gameStats[5]);
		
		g.drawString(manaPC
				, (int) (hud.getX() + hud.getWidth() - manaPC.length() * 8 - 8)
				, (int) (hud.getY() + hud.getHeight() - 12));

	}
	
}

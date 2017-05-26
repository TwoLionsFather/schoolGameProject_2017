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
		
		//Overlay on Hud texture
		if((cardInDetail != null)
		&& cardInDetail.getStatus() != Status.Abblage)
		{
			g.setColor((!cardInDetail.getDeck().toString().contains("PC")) ? Color.GREEN : Color.RED);
			g.drawString("" + cardInDetail.getLeben()
					, (int) (hud.getX() + ((cardInDetail.getLeben() > 9) ? 9 : 12))
					, (int) (hud.getY() + 22));
			
			g.drawString("Leben von " + cardInDetail.getName()
					, (int) (hud.getX() + 50)
					, (int) (hud.getY() + 22));

			g.drawString("" + cardInDetail.getSchaden()
					, (int) (hud.getX() + ((cardInDetail.getSchaden() > 9) ? 9 : 12))
					, (int) (hud.getY() + 55));
			
			g.drawString("Schaden von " + cardInDetail.getName()
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
		g.setFont(new Font("Info", Font.BOLD , 12));
		
		g.setColor(Color.green);
		g.drawString("" + gameStats[5]
					, 20
					, (int) Hearthstone.HOEHE - 40);
		
		g.drawString("" + gameStats[6]
					, (int) Hearthstone.BREITE - 25
					, (int) Hearthstone.HOEHE - 40);
		
		g.setColor(Color.red);
		g.drawString("" + gameStats[7]
					, 15
					, 15);
		  
		g.drawString("" + gameStats[8]
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
		//lifeDisplay
		g.drawString("" + gameStats[0]
				, (int) (hud.getX() + ((gameStats[0] > 9) ? 9 : 12))
				, (int) (hud.getY() + 87));
		
		g.drawString("Life Player"
				, (int) (hud.getX() + 50)
				, (int) (hud.getY() + 87));
		
		//manaDisplay
		g.drawString("" + gameStats[1]
				, (int) (hud.getX() + ((gameStats[1] > 9) ? 9 : 12))
				, (int) (hud.getY() + 120));
		
		g.drawString("Mana Player"
				, (int) (hud.getX() + 50)
				, (int) (hud.getY() + 120));
	}
	
}

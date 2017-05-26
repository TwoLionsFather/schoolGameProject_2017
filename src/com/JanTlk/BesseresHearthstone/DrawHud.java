package com.JanTlk.BesseresHearthstone;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.JanTlk.BesseresHearthstone.Karten.Karte;
import com.JanTlk.BesseresHearthstone.Karten.Status;

public class DrawHud 
{
	private BufferedImage hudtextur;
	private Karte cardInDetail;
	
	public DrawHud(BufferedImage hudtextur, Karte cardInDetail) 
	{
		this.hudtextur = hudtextur;
		if (cardInDetail != null)
		{
			this.cardInDetail = cardInDetail;
		}
	}
	
	public void render(int manaPlayer, int lifePlayer, int lifePC, Graphics g)
	{
		//all locations relative to this
		Rectangle hud = new Rectangle(50
									, (int) Hearthstone.HOEHE - hudtextur.getHeight() - 40
									, hudtextur.getWidth()
									, hudtextur.getHeight());
		
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
		
		g.setColor(Color.white); //possibly change this to RGB of Gold for looks
		g.drawString("" + lifePlayer
				, (int) (hud.getX() + ((lifePlayer > 9) ? 9 : 12))
				, (int) (hud.getY() + 87));
		
		g.drawString("Life Player"
				, (int) (hud.getX() + 50)
				, (int) (hud.getY() + 87));
		
		//manaDisplay
		g.drawString("" + manaPlayer
				, (int) (hud.getX() + ((manaPlayer > 9) ? 9 : 12))
				, (int) (hud.getY() + 120));
		
		g.drawString("Mana Player"
				, (int) (hud.getX() + 50)
				, (int) (hud.getY() + 120));
	}
	
	
	
}

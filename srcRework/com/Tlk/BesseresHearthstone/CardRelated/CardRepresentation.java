package com.Tlk.BesseresHearthstone.CardRelated;

import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.JanTlk.BesseresHearthstone.Hearthstone;
import com.JanTlk.BesseresHearthstone.Karten.Karte;
import com.JanTlk.BesseresHearthstone.Karten.Typ;
import com.Tlk.BesseresHearthstone.LiveGameDataController;
import com.Tlk.BesseresHearthstone.TextureController;

public class CardRepresentation
{
	private final Card card;
	private JPanel cardDisplay;

	public CardRepresentation(LiveGameDataController gameData)
	{
		card = null;
		testDisplay(gameData);
	}

	public CardRepresentation(Card displayCard, LiveGameDataController gameData)
	{
		this.card = displayCard;

		BufferedImage texture = TextureController.getTexture(this.card.getName());
		this.cardDisplay = new JPanel();
		this.cardDisplay.setLayout(null);
		this.cardDisplay.setSize(texture.getWidth(), texture.getHeight());

		JLabel mainTexture = new JLabel();
		mainTexture.setIcon(new ImageIcon(texture));
		mainTexture.setLocation(0, 0);
		mainTexture.setSize(texture.getWidth(), texture.getHeight());
		this.cardDisplay.add(mainTexture);

		JLabel attackLogo = new JLabel();
		attackLogo.setIcon(new ImageIcon(TextureController.getTexture("v_attack.png")));
		attackLogo.setLocation(((this.card.getDamage() < 10) ? 18 : 23) + (gameData.isLargeSize() ? 5 : 10)
							, (mainTexture.getHeight() - (gameData.isLargeSize() ? 27 : 36)));
		attackLogo.setSize(attackLogo.getIcon().getIconWidth(), attackLogo.getIcon().getIconHeight());
		this.cardDisplay.add(attackLogo);


		JLabel attackValue = new JLabel(this.card.getDamage() + "");
		attackValue.setFont(new Font("Century", Font.BOLD, gameData.isLargeSize() ? 16 : 18));
		attackValue.setLocation(((this.card.getDamage() < 10) ? 18 : 23) + (gameData.isLargeSize() ? 7 : 0)
							, (mainTexture.getHeight() - (gameData.isLargeSize() ? 15 : 25)));
		this.cardDisplay.add(attackValue);


		JLabel lifeLogo = new JLabel();
		lifeLogo.setVisible(true);
		lifeLogo.setIcon(new ImageIcon(TextureController.getTexture("v_life.png")));
		lifeLogo.setSize(lifeLogo.getIcon().getIconWidth(), lifeLogo.getIcon().getIconHeight());
		lifeLogo.setLocation(mainTexture.getWidth() - lifeLogo.getWidth() - ((this.card.getDamage() < 10) ? 22 : 27) + (gameData.isLargeSize() ? -3 : 1)
							, mainTexture.getHeight() - (gameData.isLargeSize() ? 27 : 36));
		this.cardDisplay.add(lifeLogo);
//		if(Hearthstone.isDrawhelpActive())
//		{
//			g.drawImage(icon_Life
//						, x + textur.getWidth() - ((Karte.this.getLeben() < 10) ? 22 : 27) - icon_Life.getWidth() - ((Hearthstone.BREITE < 1920) ? -3 : 1)
//						, y + textur.getHeight() - ((Hearthstone.BREITE < 1920) ? 27 : 36)
//						, null);
//		}
//
//		g.setColor(checkForChange(lebenInit, leben));
//		g.drawString("" + Karte.this.getLeben()
//					, x + textur.getWidth() - ((Karte.this.getLeben() < 10) ? 22 : 27) + ((Hearthstone.BREITE < 1920) ? 5 : 0)
//					, y + textur.getHeight() - ((Hearthstone.BREITE < 1920) ? 15 : 25));



		this.cardDisplay.addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseDragged(MouseEvent me) {
                me.translatePoint(me.getComponent().getLocation().x
                				, me.getComponent().getLocation().y);

                cardDisplay.setLocation(me.getX(), me.getY());
            }

        });

		this.cardDisplay.setVisible(true);
	}

	public void testDisplay(LiveGameDataController gameData)
	{
		JFrame testDisplay = new JFrame();
		JPanel testScene = new JPanel();
		testDisplay.setSize(1000, 750);
		testScene.setSize(1000, 750);
		testScene.setLayout(null);

		Card cardExample = new Card("Geralt of Riva", true, Typ.Monster, 5, 8, 9);
		CardRepresentation testCard = new CardRepresentation(cardExample, gameData);

		testScene.add(testCard.getCardDisplay());
		testScene.setVisible(true);

		testDisplay.add(testScene);
		testDisplay.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		testDisplay.setVisible(true);
	}

	public JPanel getCardDisplay()
	{
		return this.cardDisplay;
	}

}

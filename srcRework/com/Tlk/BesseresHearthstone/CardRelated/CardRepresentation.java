package com.Tlk.BesseresHearthstone.CardRelated;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import com.Tlk.BesseresHearthstone.LiveGameDataController;
import com.Tlk.BesseresHearthstone.TextureController;

public class CardRepresentation
{
	private final Card card;
	private JLayeredPane cardDisplay;

	public CardRepresentation(LiveGameDataController gameData)
	{
		card = null;
		testDisplay(gameData);
	}

	public CardRepresentation(Card displayCard, LiveGameDataController gameData)
	{
		this.card = displayCard;

		BufferedImage texture = TextureController.getTexture(this.card.getName());
		this.cardDisplay = new JLayeredPane();
		this.cardDisplay.setLayout(null);
		this.cardDisplay.setSize(texture.getWidth(), texture.getHeight());

		JLabel mainTexture = new JLabel();
		mainTexture.setIcon(new ImageIcon(texture));
		mainTexture.setLocation(0, 0);
		mainTexture.setSize(texture.getWidth(), texture.getHeight());
		this.cardDisplay.add(mainTexture);

		//----------------------------------------------------------------------
		JLabel attackLogo = new JLabel();
		attackLogo.setIcon(new ImageIcon(TextureController.getTexture("v_attack.png")));
		attackLogo.setLocation(((this.card.getDamage() < 10) ? 18 : 23) + (gameData.isLargeSize() ? 5 : 10)
							, (mainTexture.getHeight() - (gameData.isLargeSize() ? 27 : 36)));
		attackLogo.setSize(attackLogo.getIcon().getIconWidth(), attackLogo.getIcon().getIconHeight());
		this.addToOverlay(attackLogo);

		JLabel attackValue = new JLabel(this.card.getDamage() + "");
		attackValue.setFont(new Font("Century", Font.BOLD, gameData.isLargeSize() ? 16 : 18));
		attackValue.setSize(attackValue.getPreferredSize());
		attackValue.setLocation(28 + (gameData.isLargeSize() ? 7 : 0) - (int) attackValue.getPreferredSize().getWidth()
							, (mainTexture.getHeight() - (gameData.isLargeSize() ? 15 : 25)));
		this.addToOverlay(attackValue);

		JLabel lifeLogo = new JLabel();
		lifeLogo.setVisible(true);
		lifeLogo.setIcon(new ImageIcon(TextureController.getTexture("v_life.png")));
		lifeLogo.setSize(lifeLogo.getIcon().getIconWidth(), lifeLogo.getIcon().getIconHeight());
		lifeLogo.setLocation(mainTexture.getWidth() - lifeLogo.getWidth() - ((this.card.getDamage() < 10) ? 22 : 27) + (gameData.isLargeSize() ? -3 : 1)
							, mainTexture.getHeight() - (gameData.isLargeSize() ? 27 : 36));
		this.addToOverlay(lifeLogo);

		JLabel lifeValue = new JLabel(this.card.getDamage() + "");
		lifeValue.setFont(new Font("Century", Font.BOLD, gameData.isLargeSize() ? 16 : 18));
		lifeValue.setSize(lifeValue.getPreferredSize());
		lifeValue.setLocation(lifeValue.getWidth() - ((this.card.getLife() < 10) ? 22 : 27) + (gameData.isLargeSize() ? 5 : 0)
							, lifeValue.getHeight() - (gameData.isLargeSize() ? 15 : 25));
		this.addToOverlay(lifeValue);

		this.cardDisplay.setVisible(true);
	}

	private void addToOverlay(Component component)
	{
		this.cardDisplay.add(component);
		this.cardDisplay.moveToFront(component);
	}

	public void testDisplay(LiveGameDataController gameData)
	{
		JFrame testDisplay = new JFrame();
		JPanel testScene = new JPanel();
		testDisplay.setSize(1000, 750);
		testScene.setSize(1000, 750);
		testScene.setLayout(null);

		Card cardExample = new Card("Geralt of Riva", true, Typ.Monster, 5, 28, 9);
		CardRepresentation testCard = new CardRepresentation(cardExample, gameData);

		testScene.add(testCard.getCardDisplay());
		testScene.setVisible(true);

		testDisplay.add(testScene);
		testDisplay.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		testDisplay.setVisible(true);
	}

	public JPanel getCardDisplay()
	{
		JPanel cardRepresentation = new JPanel();
		cardRepresentation.setLayout(null);
		cardRepresentation.setSize(this.cardDisplay.getSize());
		cardRepresentation.add(this.cardDisplay);
		cardRepresentation.addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseDragged(MouseEvent me) {
                me.translatePoint(me.getComponent().getLocation().x
                				, me.getComponent().getLocation().y);

                cardRepresentation.setLocation(me.getX(), me.getY());
            }

        });

		cardRepresentation.setVisible(true);
		return cardRepresentation;
	}

}

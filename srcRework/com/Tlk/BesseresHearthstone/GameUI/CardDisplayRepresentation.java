package com.Tlk.BesseresHearthstone.GameUI;

import java.awt.Color;
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
import com.Tlk.BesseresHearthstone.CardRelated.Card;
import com.Tlk.BesseresHearthstone.CardRelated.Typ;

public class CardDisplayRepresentation
{
	private final Card card;
	private JLayeredPane cardDisplay;

	public CardDisplayRepresentation(LiveGameDataController gameData)
	{
		card = null;
		testDisplay(gameData);
	}

	public CardDisplayRepresentation(Card displayCard)
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


		int logoTop = (int) (texture.getHeight() * 0.83);
		int textTop = (int) (texture.getHeight() * 0.79);
		Font dataFont = new CardFont(16);

		//----------------------------------------------------------------------
		JLabel attackLogo = new JLabel(new ImageIcon(TextureController.getTexture("v_attack.png")));
		attackLogo.setSize(attackLogo.getPreferredSize());
		//Left Top Corner
		attackLogo.setLocation((int) (mainTexture.getWidth() * 0.47)
							, logoTop);
		this.addToOverlay(attackLogo);

		JLabel attackValue = new JLabel(this.card.getDamage() + "");
		attackValue.setFont(dataFont);
		attackValue.setForeground(this.chooseColor(this.card.getDamage(), this.card.getInitDamage()));
		attackValue.setSize(attackValue.getPreferredSize());
		//Right Top Corner - Width
		attackValue.setLocation((int) (mainTexture.getWidth() * 0.45) - (int) attackValue.getWidth()
							, textTop);
		this.addToOverlay(attackValue);

		JLabel lifeLogo = new JLabel(new ImageIcon(TextureController.getTexture("v_life.png")));
		lifeLogo.setSize(lifeLogo.getPreferredSize());
		//Right Top Corner - Width
		lifeLogo.setLocation((int) (mainTexture.getWidth() * 0.76) - lifeLogo.getWidth()
							, logoTop);
		this.addToOverlay(lifeLogo);

		JLabel lifeValue = new JLabel(this.card.getLife() + "");
		lifeValue.setFont(dataFont);
		lifeValue.setForeground(this.chooseColor(this.card.getLife(), this.card.getInitLife()));
		lifeValue.setSize(lifeValue.getPreferredSize());
		//Left Top Corner
		lifeValue.setLocation((int) (mainTexture.getWidth() * 0.78)
							, textTop);
		this.addToOverlay(lifeValue);

		JLabel manaValue = new JLabel(this.card.getMana() + "");
		manaValue.setForeground(this.card.isLegendary() ? Color.WHITE : Color.BLACK);
		manaValue.setFont(dataFont);
		manaValue.setSize(manaValue.getPreferredSize());
		//Right Top Corner
		manaValue.setLocation((int) (mainTexture.getWidth() * 0.22) - manaValue.getWidth()
							, (int) (mainTexture.getHeight() * 0.01));
		this.addToOverlay(manaValue);

		this.cardDisplay.setVisible(true);
	}

	private Color chooseColor(int now, int init)
	{
		if (now == init)
			return Color.BLACK;

		else if (now < init)
			return Color.RED;
		else
			return Color.GREEN;
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
		testDisplay.setSize(250, 300);
		testScene.setSize(250, 300);
		testScene.setLayout(null);

		Card cardExample = new Card("Philippa Eilhart", false, Typ.Monster, 5, 28, 9);
		CardDisplayRepresentation testCard = new CardDisplayRepresentation(cardExample);

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

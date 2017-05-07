package com.JanTlk.BesseresHearthstone.Karten;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Karte extends JPanel
{
	private static final long serialVersionUID = -4663989360172026366L;
	
	private  String name;	//Kartenname für Anzeige
	private Typ typ;		//Typ der Karte (Zauber oder Monster)
	private int mana;		//kosten bei Ausspielen der Karte
	
	private int schaden;	//Schaden bei angriff auf andere Karte
	private int leben;		//Leben, 0 Leben = Karte tot
	
	/**
	 * textur will get standardiced Graphic from a composed image, when set up
	 * bounds will hopefully be used to check if the card got clicked on
	 */
	private BufferedImage textur;
	private Rectangle bounds;
	
	/**
	 * Konstruktor um Karte Werte beim anlegen zu zu weisen
	 * @param name Sets the name of the Card
	 * @param typ It is either a Spell or a monster
	 * @param mana How much this card will cost when played
	 * @param schaden How much Damage the card deals when attacking another one
	 * @param leben How much damage a card can take before beeing moved to the graveyard :(
	 * @param textur Needs to follow a specific blueprint, A Bufferd Image that stores the Cards image
	 */
	public Karte(String name
				, Typ typ
				, int mana
				, int schaden
				, int leben) 
	{
		this.name = name;
		this.typ = typ;
		this.mana = mana;
		this.schaden = schaden;
		this.leben = leben;		
	}
	
	/**
	 * currently a wildcard
	 */
	public void tick()
	{
		
	}
	
	/**
	 * Mainmethod for Testing only!
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException 
	{
		Karte firstTestCard = new Karte("FireStarter", Typ.Monster, 5, 25, 3);
		firstTestCard.setCardImage(ImageIO.read(new File("CardBluePrint.png")));
		
		
		//Create JFrame to display the Card
		JFrame window = new JFrame("Display for a Card Object");
		window.setSize(1280, 780);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		
		
		//Creates JPanel to display card
		JPanel contentPane = new JPanel(new BorderLayout());
		
		JLabel cardLabel = firstTestCard.cardToJLabel();
		cardLabel.setSize(100, 164);		
		
		contentPane.add(cardLabel);
		window.add(contentPane);
	}
	
	/**
	 * retuns a JLabel with the Cards Graphic and current Life and Damage on it
	 * if clicked on the Label the Card clicked on will identify itself for development purpose
	 * @return
	 */
	public JLabel cardToJLabel()
	{		
		JLabel cardLabel = new JLabel()
				{
					private static final long serialVersionUID = -3561533864873217494L;

					//wird die Karte gemalt, dann mit einer Beschreibung unter sich
					protected void paintChildren(Graphics g)
					{
						super.paintChildren(g);						
						g.drawImage(textur, 0, 0, null);
						
						g.setColor(Color.red);
						g.setFont(new Font("Damage", Font.BOLD, 15));
						g.drawString("" + Karte.this.getSchaden()
									, 15
									, textur.getHeight() - 15);
						
						g.setColor(Color.green);
						g.setFont(new Font("Live", Font.BOLD, 15));
						g.drawString("" + Karte.this.getLeben()
									, textur.getWidth() - 23
									, textur.getHeight() - 15);
						
						g.setColor(Color.black);
						g.setFont(new Font("CardInfo", Font.BOLD, 11));
						g.drawString(Karte.this.getName()
									, 22
									, 22);
					}
				};		
				
				/**
				 * fügt JLabel der Karte einen MouseListener hinzu
				 * da nicht alle MausFunktionen geutzt werden wird ein Adapter benutzt
				 */
				cardLabel.addMouseListener(new MouseAdapter() 
				{
					public void mouseClicked(MouseEvent e)
					{
						System.out.println("Ich bin " + name);
					}
				});
		
		return cardLabel;
	}
	
	/**
	 * this should be used to draw a Card with Graphics class
	 * @param x position of the top left corner
	 * @param y position of the corner
	 * @param g this is what the card gets drawn on
	 */
	public void drawCard(int x, int y, Graphics g)
	{
		bounds = new Rectangle(x, y
								, textur.getWidth()
								, textur.getHeight());
		
		g.drawImage(textur, x, y, null);
		
		g.setColor(Color.red);
		g.setFont(new Font("Damage", Font.BOLD, 15));
		g.drawString("" + Karte.this.getSchaden()
					, 15 + x
					, textur.getHeight() - 15 + y);
		
		g.setColor(Color.green);
		g.setFont(new Font("Live", Font.BOLD, 15));
		g.drawString("" + Karte.this.getLeben()
					, textur.getWidth() - 23 + x
					, textur.getHeight() - 15 + y);
		
		g.setColor(Color.black);
		g.setFont(new Font("CardInfo", Font.BOLD, 11));
		g.drawString(Karte.this.getName()
					, 22 + x
					, 22 + y);
	}
	
	public String toString()
	{
		String kartenInfo = String.format("%s macht %d Schaden und hat %d leben.", name, schaden, leben);
		return kartenInfo;
	}
	
	public void setCardImage(BufferedImage textur)
	{
		
		this.textur = textur;
	}
	
	public Typ getTyp() {		//Wenn die Karte gespielt wird, überprüfe ob Zauber oder Monster
		return typ;
	}

	public void setTyp(Typ typ) {
		this.typ = typ;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public int getLeben() {
		return leben;
	}

	public void setLeben(int leben) {
		this.leben = leben;
	}

	public int getSchaden() {
		return schaden;
	}

	public void setSchaden(int schaden) {
		this.schaden = schaden;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
	
	
}

package com.JanTlk.BesseresHearthstone.Karten;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
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
	private int mana;		//kosten für ausspielen der Karte
	
	private int schaden;	//Schaden bei angriff auf andere Karte
	private int leben;	//Leben, 0 Leben = Karte tot
	
	private BufferedImage textur;		//aussehen der Karte (hoffentlich)
	
	public Karte(String name			//Konstruktor um Karte Werte beim anlegen zu zu weisen
				, Typ typ
				, int mana
				, int schaden
				, int leben
				, BufferedImage textur) 
	{
		this.name = name;
		this.typ = typ;
		this.mana = mana;
		this.schaden = schaden;
		this.leben = leben;
		this.textur = textur;
	}
	
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
		Karte firstTestCard = new Karte("FireStarter", Typ.Monster, 5, 25, 3, ImageIO.read(new File("CardBluePrint.png")));
		
		
		//Create JFrame to display the Card
		JFrame window = new JFrame("Display for a Card Object");
		window.setSize(1280, 780);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		
		
		//Creates JPanel to display card
		JPanel contentPane = new JPanel(new BorderLayout());
		
		//Create JLable like a stickynote with the Card on the content Pane
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
	
	public String toString()
	{
		String kartenInfo = String.format("%s macht %d Schaden und hat %d leben.", name, schaden, leben);
		return kartenInfo;
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
	
	public void setTexture(BufferedImage texture) {
		
	}
	
}

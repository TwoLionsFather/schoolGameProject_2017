package com.JanTlk.BesseresHearthstone.Karten;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

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
						g.setFont(new Font("CardInfo", Font.BOLD, 9));
						g.drawString(toString()
									, 0									//X Koordinate
									, textur.getHeight() + 10);			//Y Koordinate
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

package com.JanTlk.BesseresHearthstone.Karten;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Karte extends JPanel
{
	private static final long serialVersionUID = -4663989360172026366L;
	
	private  String name;	//Kartenname für Anzeige
	private Typ typ;		//Typ der Karte (Zauber oder Monster)
	private int mana;		//kosten für ausspielen der Karte
	
	private int schaden;	//Schaden bei angriff auf andere Karte
	private int leben;	//Leben, 0 Leben = Karte tot
	
	private Rectangle bounds;
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
	
	public void render(Graphics g)
	{
		JPanel 
	}
	
	public void tick(boolean clicked)
	{
		if(clicked)
		{
			bounds.x = 100;//(Mouseposition neu relativ zu Karte x ) 
			bounds.y = 100;
		}
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

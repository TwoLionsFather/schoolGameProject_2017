package com.JanTlk.BesseresHearthstone.Karten;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
	private int schadenInit;
	private int leben;		//Leben, 0 Leben = Karte tot
	private int lebenInit;
	private Status status; //this will be used to decide how the card gets handeled
	
	/**
	 * textur will get standardiced Graphic from a composed image, when set up
	 * bounds will hopefully be used to check if the card got clicked on
	 */
	private Component component;
	private BufferedImage textur;
	private Rectangle bounds;
	private boolean moved;

	private Karte attackCard;
	
	/**
	 * Konstruktor um Karte Werte beim anlegen zu zu weisen
	 * @param name Sets the name of the Card
	 * @param typ It is either a Spell or a monster
	 * @param mana How much this card will cost when played
	 * @param schaden How much Damage the card deals when attacking another one
	 * @param leben How much damage a card can take before beeing moved to the graveyard :(
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
		this.schadenInit = schaden;
		this.leben = leben;	
		this.lebenInit = leben;
		this.setStatus(Status.Hand);
	}
	
	/**
	 * used from damage dealing deck to attack another card
	 */
	public void damageTick()
	{
		if (attackCard == null)
		{
			return;
		}
		
		this.leben = this.leben - attackCard.getSchaden();
		attackCard.setLeben(attackCard.getLeben() - this.schaden);
		
		if(this.leben <= 0)
		{
			this.status = Status.Abblage;
		}
		
		if(attackCard.getLeben() <= 0)
		{
			attackCard.setStatus(Status.Abblage);
		}
		
		this.component.repaint();
		attackCard = null;
	}
	
	/**
	 * Mainmethod for Testing only!
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException 
	{
		Karte firstTestCard = new Karte("FireStarter", Typ.Monster, 12, 9, 12);
		firstTestCard.setCardImage(ImageIO.read(new File("Ves.png")));
		
		
		//Create JFrame to display the Card
		JFrame window = new JFrame("Display");
		window.setSize(400, 800);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		
		
		//Creates JPanel to display card
		JPanel contentPane = new JPanel(new BorderLayout());
		
		JLabel cardLabel = firstTestCard.cardToJLabel();
		cardLabel.setSize((int) 100, (int) 164);	
		
		contentPane.add(cardLabel);
		window.add(contentPane);
	}
	
	/**
	 * retuns a JLabel with the Cards Graphic and current Life and Damage on it
	 * if clicked on the Label the Card clicked on will identify itself for development purpose
	 * @return a JLabel that can be put on a JPanel for simple display and debug purpose
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

						/**
						 * used to debug with cards main method
						 */	
						if (Karte.this.typ == Typ.Monster)
						{
							g.setColor(checkForChange(schadenInit, schaden));			
							g.setFont(new Font("Damage", Font.BOLD, 25));
							g.drawString("" + Karte.this.getSchaden()
										, ((Karte.this.getSchaden() < 10) ? 80 : 70)
										, textur.getHeight() - 70);
							
							
							g.setColor(checkForChange(lebenInit, leben));			
							g.setFont(new Font("Live", Font.BOLD, 25));
							g.drawString("" + Karte.this.getLeben()
										, textur.getWidth() - ((Karte.this.getLeben() < 10) ? 50 : 60)
										, textur.getHeight() - 70);
						}
						
						g.setColor(Color.blue);
						g.setFont(new Font("Mana", Font.PLAIN, 35));
						g.drawString("" + Karte.this.getMana()
									, (Karte.this.getMana() < 10) ? 45 : 30
									, 57);
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
	public void drawCard(int x, int y, Graphics g , boolean drawTitle)
	{
		if(moved)
		{
			x = (int) bounds.getX();
			y = (int) bounds.getY();
		}
		
		else 
		{
			bounds = new Rectangle(x, y
					, textur.getWidth()
					, textur.getHeight());
		}
		
		g.drawImage(textur, x, y, null);
		
		if (this.typ == Typ.Monster)
		{
			g.setColor(checkForChange(schadenInit, schaden));			
			g.setFont(new Font("Damage", Font.BOLD, 15));
			g.drawString("" + Karte.this.getSchaden()
						, ((Karte.this.getSchaden() < 10) ? 15 : 10) + x
						, textur.getHeight() - 25 + y);
			
			
			g.setColor(checkForChange(lebenInit, leben));			
			g.setFont(new Font("Live", Font.BOLD, 15));
			g.drawString("" + Karte.this.getLeben()
						, textur.getWidth() - ((Karte.this.getLeben() < 10) ? 20 : 25) + x
						, textur.getHeight() - 25 + y);
		}
							
		g.setColor(Color.blue);
		g.setFont(new Font("Mana", Font.PLAIN, 15));
		g.drawString("" + Karte.this.getMana()
					, x + ((Karte.this.getMana() < 10) ? 10 : 6)
					, y + 20);
			
		if (drawTitle)
		{
			g.setColor(Color.black);
			g.setFont(new Font("CardInfo", Font.BOLD, 11));
			g.drawString(Karte.this.getName()
						, 22 + x
						, 22 + y);
		}
		
		moved = false;
	}
	
	/**
	 * clones a card, creating a new Karte object in the process
	 */
	public Karte clone()
	{
		Karte newK = new Karte(this.name, this.typ, this.mana, this.schaden, this.leben);
		newK.setCardImage(this.texturClone());
		newK.setComponent(this.component);
		return newK;
	}
	
	private BufferedImage texturClone()
	{
		BufferedImage newBI = new BufferedImage(textur.getWidth(), textur.getHeight(), textur.getType());
	   
		Graphics g = newBI.getGraphics();
	    g.drawImage(textur, 0, 0, null);
	    g.dispose();
	    return newBI;	
	}
	
	/**
	 * used to identify a Card with name and dmg/live if given
	 */
	public String toString()
	{
		String kartenInfo = "";
		if(this.typ == Typ.Monster)
			kartenInfo = String.format("%s macht %d Schaden und hat %d leben.", name, schaden, leben);
		else
			kartenInfo = String.format("%s ist ein Zauber mit STARKEM Effekt!!", name);
			
		return kartenInfo;
	}
	
	/**
	 * Methode checks what Color to use based on a change in Value (green - up; red - down)
	 * @param init the initial value that the current one is checked with
	 * @param now the value that gets checked for change
	 * @return returs either red, green or black Color code
	 */
	private Color checkForChange(int init, int now)
	{
		if(now < init)
			return Color.red;
		
		else if (now > init)
			return Color.green;
		else 
			return Color.black;
	}
	
	/**
	 * this sets up the grafik, if not setup by this, the graphic will be null
	 * @param cardImage the Buffered image used as texture when displaying the card
	 */
	public void setCardImage(BufferedImage cardImage)
	{
		bounds = new Rectangle(-1, -1	//at the point of setting Graphics, one shouldn't be drawing it
							, cardImage.getWidth()
							, cardImage.getHeight());
		this.textur = cardImage;
	}
	
	/**
	 * used for changes in DragAndDrop
	 * @param xyChange offsets Card by values defined in this array
	 */
	public void setChange(int[] xyChange) 
	{
		bounds = new Rectangle((int) (bounds.getX() + xyChange[0])
							, (int) (bounds.getY() + xyChange[1])
							, (int) bounds.getWidth()
							, (int) bounds.getHeight());
		moved = true;
	}
	
	/**
	 * used to relocate a cad to a new Rectangle
	 * @param nP
	 */
	public void setNewPos(Rectangle nP) 
	{
		bounds = new Rectangle((int) nP.getX()
							, (int) nP.getY()
							, (int) nP.getWidth()
							, (int) nP.getHeight());
		moved = true;
	}
	
	/**
	 * A Monster Card has different functionality then a spell card
	 * @return enum with the typ that card is
	 */
	public Typ getTyp() 
	{
		return typ;
	}

	public void setTyp(Typ typ) 
	{
		this.typ = typ;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public int getMana() 
	{
		return mana;
	}

	public void setMana(int mana) 
	{
		this.mana = mana;
	}

	public int getLeben() 
	{
		return leben;
	}

	public void setLeben(int leben) 
	{
		this.leben = leben;
	}

	public int getSchaden() 
	{
		return schaden;
	}

	public void setSchaden(int schaden) 
	{
		this.schaden = schaden;
	}

	public Status getStatus() 
	{
		return status;
	}

	public void setStatus(Status status) 
	{
		this.status = status;
	}

	public Rectangle getBounds() 
	{
		return bounds;
	}

	public void setBounds(Rectangle bounds) 
	{
		this.bounds = bounds;
	}
	
	public Component getComponent()
	{
		return this.component;
	}
	
	public void setComponent(Component c)
	{
		this.component = c;
	}

	public void attacks(Karte karte) 
	{
		this.attackCard = karte;
	}
}

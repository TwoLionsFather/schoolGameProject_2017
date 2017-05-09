package com.JanTlk.BesseresHearthstone.Karten;

import java.util.ArrayList;
import java.util.Random;

/* Eine Kartenverwaltung für PC und eine Für Player			
 * Die initStapel Methode nimmt ein Kartendeck entgegen und mischt es auf einen Stapel
 * 
 * Die zieheKarteHand Methode entfernt eine Karte aus dem Stapel und "nimmt Sie auf die Hand"
 * 
 * Die spieleKarte Methode soll die Vom Benutzer gewählte Karte an Das Spielfeld übergeben und Mana abziehen
 * 							Dieses übernimmt vom Punkt der übernahme an das Rendern
 * 							Die Übergabe erfolgt mit legen der Karte auf freies Feld (Zauber oder Monster)
 * 
 * Die addKarteAblageMethode nimmt tote Karten entgegen
 */

/**
 * ----------------------------------------------------------------------------------------------------------------------------------------------------------------------
 * Not used
 * @author Gaming
 */

/*
public class Kartenhand 				
{

	private ArrayList<Karte> stapel = new ArrayList<Karte>();	//Vom Stapel werden die Karten der reihe nach auf die Hand gezogen
	private ArrayList<Karte> hand = new ArrayList<Karte>();		//Von der Hand soll der Benutzer per Maus eine Karte auswählen können, die dann auf das Spielfeld gelegt wird
	private ArrayList<Karte> ablage = new ArrayList<Karte>();	//Auf Die Ablage kommen "tote" Karten,(optional) wiederbelebung/looserbonus//etc.
	
	public void initStapel(ArrayList<Karte> kartenDeck) //Zu beginn des Spiels werden vom Kartendeck auf den Stapel gemoved
	{
		this.stapel = mischen(kartenDeck);		//das gemischte Kartendeck bildet den Stapel
	}
	
	public void zieheKarteHand() 				//die oberste Karte auf dem Stapel wird auf die Hand genommen
	{
		this.hand.add(this.stapel.get(this.stapel.size() - 1)); //Fügt der Liste an Karten auf der Hand die oberste vom Stapel hinzu
		this.stapel.remove(this.stapel.size() - 1);				//Entfernt diese Vom Stapel
	}
	
	//WIP
	public Karte spieleKarte(Karte karte)	//Karte auf die die Maus klickt von Hand entfernen und zurückgeben
											//Die Kosten Der KArte sollen vom Manapool abgezogen werden
											//Karte wird an Spielfeld übergeben, dieses regelt Efekt
	{		
		Karte auswKarte = karte;			//Karte zwischenspeichen um sie aus der Hand zu löschen
		hand.remove(karte);					//Karte aus Hand entfernen
		return auswKarte;					//KartenObjekt zurückgeben
	}
	
	public void addKarteAblage(Karte karte) //Wenn eine Karte auf dem Spielfeld Stirbt wird Sie zur Ablage gemoved
	{
		this.ablage.add(karte);
	}
	
	public void render() 
	{
		//Stellt Karten auf Hand/Ablage/Stapel des Spielers dar
		//KartenHand alle Karten nebeneinander
		//KartenStapel Anzahl der Karten auf Stapel
		//Karten ablage Anzahl der Karten auf der Ablage
	}
	
	private ArrayList<Karte> mischen(ArrayList<Karte> zuMischen) 
	{
		Random r = new Random();							//Random Objekt anlegen
		ArrayList<Karte> temp = new ArrayList<Karte>();		//neue temporäre Liste um gemischtes Deck zu speichern
		
		int anzKarten = zuMischen.size();					//Anzahl an zu mischenden Karten
		
		for(int i = 0; i < anzKarten; i++) 					//solange Karten zu mischen sind
		{
			int randomZahl = r.nextInt(zuMischen.size());	//neue Zufallszahl im Bereich der noch zu sortierenden Karten
			
			temp.add(zuMischen.get(randomZahl));			//fügt dem temp(gemischten) Stapel die Karte an der Zufälligen Position zu
			zuMischen.remove(randomZahl);					//entfernt die Karte aus dem Stapel
		}
		

		return temp;
	}
}
*/

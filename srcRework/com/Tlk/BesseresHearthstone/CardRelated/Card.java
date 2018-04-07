package com.Tlk.BesseresHearthstone.CardRelated;

import java.util.ArrayList;

public class Card
{
	private final String name;
	private ArrayList<Card> currentLocation;

	private final boolean isLegendary;
	private final Typ typ;
	private final int mana;
	private final int initDamage;
	private final int initLife;
	private int damage;
	private int life;

	public Card(String name, boolean isLegendary, Typ typ, int mana, int initDamage, int initLife)
	{
		this.name = name;
		this.isLegendary = isLegendary;
		this.typ = typ;
		this.mana = mana;
		this.initDamage = initDamage;
		this.initLife = initLife;
		this.damage = initDamage;
		this.life = initLife;
	}

	public void resetCard()
	{
		this.damage = this.initDamage;
		this.life = this.initLife;
	}

	public String getName()
	{
		return name;
	}

	public ArrayList<Card> getCurrentLocation()
	{
		return currentLocation;
	}

	public void setCurrentLocation(ArrayList<Card> currentLocation)
	{
		this.currentLocation = currentLocation;
	}

	public boolean isLegendary()
	{
		return isLegendary;
	}

	public Typ getTyp()
	{
		return typ;
	}

	public int getMana()
	{
		return mana;
	}

	public int getInitDamage()
	{
		return initDamage;
	}

	public int getInitLife()
	{
		return initLife;
	}

	public int getDamage()
	{
		return damage;
	}

	public int getLife()
	{
		return life;
	}

	public void setLife(int life)
	{
		this.life = life;
	}

}

package com.JanTlk.BesseresHearthstone.secondTry;

import com.JanTlk.BesseresHearthstone.Karten.Typ;

public class Card
{
	private final String name;
	private final boolean isLegendary;
	private final Typ typ;
	private final int mana;

	private final int initDamage;
	private final int initLife;
	private int damage;
	private int life;

	public Card(String name, boolean isLegendary, Typ typ, int mana, int initDamage, int initLife)
	{
		super();
		this.name = name;
		this.isLegendary = isLegendary;
		this.typ = typ;
		this.mana = mana;
		this.initDamage = initDamage;
		this.initLife = initLife;
		this.damage = initDamage;
		this.life = initLife;
	}

	public String getName()
	{
		return name;
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


}

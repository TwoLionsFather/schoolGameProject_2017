package com.Tlk.BesseresHearthstone.secondTry.CardRelated;

import com.JanTlk.BesseresHearthstone.Karten.Typ;

public class CardBluePrint
{
	private String name;
	private boolean isLegendary;
	private Typ typ;
	private int mana;

	private int initDamage;
	private int initLife;

	public CardBluePrint() {	}

	public Card createCard()
	{
		return new Card(name, isLegendary, typ, mana, initDamage, initLife);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLegendary(boolean isLegendary) {
		this.isLegendary = isLegendary;
	}

	public void setTyp(Typ typ) {
		this.typ = typ;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public void setInitDamage(int initDamage) {
		this.initDamage = initDamage;
	}

	public void setInitLife(int initLife) {
		this.initLife = initLife;
	}


}

package com.JanTlk.BesseresHearthstone.secondTry;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class TextureController
{
	private static HashMap<String, BufferedImage> ALL_TEXTURES = new HashMap<String, BufferedImage>(35);

	private TextureController() {	}

	public static void addTexture(String name, BufferedImage texture)
	{
		ALL_TEXTURES.put(name, texture);
	}

	public static ArrayList<String> getTextureNames()
	{
		return new ArrayList<String>(ALL_TEXTURES.keySet());
	}

	public static BufferedImage getTexture(String name)
	{
		return ALL_TEXTURES.get(name);
	}
}

package com.Tlk.BesseresHearthstone.Startup;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.JanTlk.BesseresHearthstone.Hearthstone;
import com.Tlk.BesseresHearthstone.FileController;
import com.Tlk.BesseresHearthstone.GameDataContainer;
import com.Tlk.BesseresHearthstone.TextureController;
import com.Tlk.BesseresHearthstone.CardRelated.DeckDataContainer;
import com.Tlk.BesseresHearthstone.ErrorHandling.ErrorHandler;

public class TextureLoader
{
	private final Dimension cardDimension = new Dimension(220, 414);

	public TextureLoader(GameDataContainer gameSetup, DeckDataContainer deckData)
	{
		loadFileToTextureController("GameIcon.png", 30, 30);
		loadBackgrounds(gameSetup);
		createCardImages(gameSetup, deckData);
	}

	/**
	 * loads BackgroungImage to TextureController
	 */
	private void loadBackgrounds(GameDataContainer gameSetup)
	{
		int maxWidth = (int) gameSetup.getWIDTH();
		int maxHight = (int) gameSetup.getHEIGHT();

		loadFileToTextureController("backGround.png", maxWidth, maxHight);
		loadFileToTextureController("EndScreenW.png", maxWidth, maxHight);
		loadFileToTextureController("EndScreenL.png", maxWidth, maxHight);
		loadFileToTextureController("helpSheet.png", maxWidth, maxHight);
	}

	/**
	 * loads scaled Images with corresponding name to TextureController
	 */
	private void createCardImages(GameDataContainer gameSetup, DeckDataContainer deckData)
	{
		int zeilenTextur = 2;
		int spaltenTextur = 21;
		int iconSideLength = (Hearthstone.BREITE < 1920) ? 11 : 12;

		//Texture loading and cutouts
		loadFileToTextureController("allCards.png", 0, 0);

		for (int zeile = 0; zeile < zeilenTextur; zeile++)
		{
			boolean ERR_FLAG = false;
			for(int spalte = 0; spalte < spaltenTextur; spalte++)
			{
				BufferedImage cutout = getCardBluePrint(); //init in case no other texture has been found
				int namePosition = spalte + (zeile * spaltenTextur);
				if (namePosition >= deckData.getDeckSize())
					break;
				String cardname = deckData.getCardNames().get(namePosition);

				if (allCardsImageExist())
				{
					cutout = TextureController.getTexture("allCards.png").getSubimage(cardDimension.width * spalte
																					, cardDimension.height * zeile
																					, cardDimension.width
																					, cardDimension.height);
				}

				if (cutout == null) //no images have been found
				{
					ERR_FLAG = true;
					break;
				}

				cutout = rescaledBufferedimage(cutout
						, (gameSetup.getWIDTH() < 1920) ? 70 : 100
						, (gameSetup.getWIDTH() < 1920) ? 140 : 200);

				TextureController.addTexture(cardname, cutout);
			}
			if(ERR_FLAG)
				break;
		}

		//Icon Loading
		loadFileToTextureController("v_attack.png", iconSideLength, iconSideLength);
		loadFileToTextureController("v_life.png", iconSideLength, iconSideLength);
	}

	/**
	 * basic card Texture by developer
	 */
	private BufferedImage getCardBluePrint()
	{
		try {
			return ImageIO.read(FileController.getFile("CardBluePrint.png"));
		} catch (IOException e) {
			ErrorHandler.displayErrorMessage("CardBluePrint Image not found -> no texture :(");
		}
		return null;
	}

	private boolean allCardsImageExist()
	{
		return TextureController.getTexture("allCards.png") != null;
	}

	/**
	 * loads file to texture and closes file's reference
	 * @param width if > 0 image will get rescaled
	 * @param height if > 0 image will get rescaled
	 */
	private void loadFileToTextureController(String filename, int width, int height)
	{
		try {
			BufferedImage texture = ImageIO.read(FileController.getFile(filename));
			if ((width > 0)
			&& (height > 0))
			{
				texture = rescaledBufferedimage(texture, width, height);
			}
			TextureController.addTexture(filename, texture);
		} catch (IOException e) {
			ErrorHandler.displayErrorMessage(filename + " not found");
			System.err.println("TextureLoader.loadFileToTextoreController " + filename + " not found");
		} finally {
			FileController.closeFile(filename);
		}
	}

	/**
	 * inspired by MemoryNotFound from https://memorynotfound.com/java-resize-image-fixed-width-height-example/ (02.04.18,00:28)
	 * @return a rescaled version of bimg
	 */
	private BufferedImage rescaledBufferedimage(BufferedImage bimg, int width, int height)
	{

		Image img = bimg.getScaledInstance(width, height, Image.SCALE_SMOOTH);

	    // Create a bufferedImage with transparency that will take the resized graphics
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image in bufferedImage's graphics
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return scaled bufferedImage
	    return bimage;
	}


}

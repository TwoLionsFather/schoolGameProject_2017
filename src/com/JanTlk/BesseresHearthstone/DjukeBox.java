package com.JanTlk.BesseresHearthstone;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class DjukeBox implements Runnable	//later should Run under own thread
{
	private static Clip[] gameSounds;
	private static Clip backgroundMusic;
	
	/**
	 * all Audio and SoundFiles get imported and setup to be played later on
	 */
	public DjukeBox() 
	{
		ArrayList<File> audioFiles = new ArrayList<File>();
		audioFiles.add(soundFile("backGround"));		//First File is set to be background Music
		audioFiles.add(soundFile("CardShuffling"));		//UIInput.mouseClicked

		audioImport(audioFiles);
		
	}
	
	/**
	 * this is used to setup the Sound System
	 * @param audioFiles
	 */
	private void audioImport(ArrayList<File> audioFiles) 
	{
		gameSounds = new Clip[audioFiles.size() - 1];
		AudioInputStream ais;
		AudioFormat format;
		DataLine.Info info;
		FloatControl gainControl;
		try {
			ais = AudioSystem.getAudioInputStream(audioFiles.get(0));
			format = ais.getFormat();
			info = new DataLine.Info(Clip.class, format);
			backgroundMusic = (Clip) AudioSystem.getLine(info);
			backgroundMusic.open(ais);
			gainControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(-10);
			
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		};
		
		for (int idx = 1; idx < audioFiles.size(); idx++)
		{
			try {
				ais = AudioSystem.getAudioInputStream(audioFiles.get(idx));
				format = ais.getFormat();
				info = new DataLine.Info(Clip.class, format);
				gameSounds[idx - 1] = (Clip) AudioSystem.getLine(info);
				gameSounds[idx - 1].open(ais);
				
				gainControl = (FloatControl) gameSounds[idx - 1].getControl(FloatControl.Type.MASTER_GAIN);
				//to soften SFX if needed
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Error Loading: " + audioFiles.get(idx).getAbsolutePath());
			}
		}
	}

	/**
	 * used to import sound File in wav Format from SoundsFolder
	 * @param name name of Imported File
	 * @return new File with name specified by parameter
	 */
	private File soundFile(String name)
	{
		return new File("Sounds\\" + name + ".wav");
	}
	
	/**
	 * starts Background Loop
	 */
	public static void playBackGround()
	{
		backgroundMusic.start();
		backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	/**
	 * stops Background loop
	 */
	public static void stopBackGround()
	{
		backgroundMusic.stop();
		backgroundMusic.setFramePosition(0); 
	}
	
	/**
	 * plays soundEffect at ID
	 * @param id
	 */
	public static void playSFX(int id)
	{
		gameSounds[id].start();
	}

	@Override
	public void run() 
	{
		
	}
}

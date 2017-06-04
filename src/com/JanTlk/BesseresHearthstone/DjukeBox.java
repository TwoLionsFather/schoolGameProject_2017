package com.JanTlk.BesseresHearthstone;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

import com.JanTlk.BesseresHearthstone.Hearthstone.STATE;

public class DjukeBox implements Runnable	//later should Run under own thread
{
	private static HashMap<String, Clip> sfx;
	private static HashMap<String, ArrayList<Clip>> musicMap;
	private Spielfeld spielfeld;
	
	/**
	 * all Audio and SoundFiles get imported and setup to be played later on
	 * @param spielfeld this is used to determine current game situation, to adpot music accordingly
	 */
	public DjukeBox(Spielfeld spielfeld) 
	{
		this.spielfeld = spielfeld;
		
		ArrayList<String> audioFiles = new ArrayList<String>();
		audioFiles.add("sfx_CardShuffling");			//UIInput.mouseClicked
		audioFiles.add("menu_TheFirstSteps");		//First File is set to be background Music
		audioFiles.add("losing_EmhyrVarEmreis");
		audioFiles.add("losing_RedInBlack");
		audioFiles.add("menu_CantinaRag");
		audioFiles.add("menu_MythOfParzival");
		audioFiles.add("playing_HuntersPath");
		audioFiles.add("playing_KaerMorhen");
		audioFiles.add("start_AdventureDarling");
		audioFiles.add("start_Trouba");
		audioFiles.add("winning_TheTrail");
		
		audioImport(audioFiles);
		playBackGround();
	}
	
	/**
	 * this is used to setup the Sound System
	 * @param audioFiles
	 */
	private void audioImport(ArrayList<String> audioFiles) 
	{
		ArrayList<String> sfx_Paths = new ArrayList<String>();
		ArrayList<String> music_Paths = new ArrayList<String>();
		
		for(String tPath : audioFiles)
		{
			if (tPath.contains("sfx_"))
			{
				sfx_Paths.add(tPath);
			}
			
			else 
			{
				music_Paths.add(tPath);	
			}
					
		}
		
		sfx = new HashMap<String, Clip>(sfx_Paths.size());
		musicMap = new HashMap<String, ArrayList<Clip>>(5);		//for the 5 different "playlists"
		
		if (Hearthstone.isDebugMode())
		{
			System.out.println("Imported Audio Files");
			for (String path : music_Paths)
			{
				System.out.println(path);
			}
			System.out.println();
		}
		
		sortImportMusic(music_Paths);
		importSFX(sfx_Paths);
		
		
	}
	
	/**
	 * used to get the AudioFiles
	 * @param path the audioFiles path
	 * @return a new AudioFile from that path
	 */
	private File getAudioFile(String path)
	{
		return new File("Sounds\\GwintAudio_" + path + ".wav");
	}
	
	/**
	 * imports Soundeffects and adds their name and Clip to the Hashmap
	 * @param sfx_Paths these paths get used for the import
	 * @param sfx_Names these are the names of the imported files
	 */
	private void importSFX(ArrayList<String> sfx_Paths) 
	{
		for (int idx = 0; idx < sfx_Paths.size(); idx++)
		{
			try {
				AudioInputStream ais = AudioSystem.getAudioInputStream(getAudioFile(sfx_Paths.get(idx)));
				AudioFormat format = ais.getFormat();
				DataLine.Info info = new DataLine.Info(Clip.class, format);
				
				sfx.put(sfx_Paths.get(idx), (Clip) AudioSystem.getLine(info));
				sfx.get(sfx_Paths.get(idx)).open(ais);
				
//				to soften SFX if needed
//				FloatControl gainControl = (FloatControl) sfx.get(sfx_Names.get(idx)).getControl(FloatControl.Type.MASTER_GAIN);
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Error Loading: " + sfx_Paths.get(idx));
			}
		}
	}

	/**
	 * sorts music imports to different "Playlists"
	 * @param music_Paths
	 */
	private void sortImportMusic(ArrayList<String> music_Paths) 
	{
		ArrayList<Clip> menu = new ArrayList<Clip>();
		ArrayList<Clip> start = new ArrayList<Clip>();
		ArrayList<Clip> play = new ArrayList<Clip>();
		ArrayList<Clip> los = new ArrayList<Clip>();
		ArrayList<Clip> win = new ArrayList<Clip>();
		
		for (String path : music_Paths)
		{
			try {
				AudioInputStream ais = AudioSystem.getAudioInputStream(getAudioFile(path));
				AudioFormat format = ais.getFormat();
				DataLine.Info info = new DataLine.Info(Clip.class, format);
				
				if (path.contains("menu_"))
				{
					menu.add((Clip) AudioSystem.getLine(info));
					menu.get(menu.size() - 1).open(ais);
				}
				
				else if (path.contains("start_"))
				{
					start.add((Clip) AudioSystem.getLine(info));
					start.get(start.size() - 1).open(ais);
				}
				
				else if (path.contains("playing_"))
				{
					play.add((Clip) AudioSystem.getLine(info));
					play.get(play.size() - 1).open(ais);
				}
				
				else if (path.contains("losing_"))
				{
					los.add((Clip) AudioSystem.getLine(info));
					los.get(los.size() - 1).open(ais);
				}
				
				else if (path.contains("winning_"))
				{
					win.add((Clip) AudioSystem.getLine(info));
					win.get(win.size() - 1).open(ais);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Error Loading: " + path);
			}
		}
		
		musicMap.put("menu", menu);
		musicMap.put("start", start);
		musicMap.put("playing", play);
		musicMap.put("losing", los);
		musicMap.put("winning", win);
		
	}

	/**
	 * starts Background Loop
	 */
	public static void playBackGround()
	{
		Clip playing;
		if (Hearthstone.gameState == STATE.MENU)
		{
			Random r = new Random();
			playing = musicMap.get("menu").get(r.nextInt(musicMap.get("menu").size()));
			playing.start();
			
			playing.addLineListener(new LineListener()
			{
				@Override
				public void update(LineEvent arg0) 
				{
					if (arg0.getType() == LineEvent.Type.STOP)
					{
						playing.stop();
						playing.setFramePosition(0);
						playBackGround();
					}
				}
			});
		}
		
		else if (Hearthstone.gameState == STATE.GAME)
		{
			Random r = new Random();
			playing = musicMap.get("playing").get(r.nextInt(musicMap.get("playing").size()));
			playing.start();
			
			playing.addLineListener(new LineListener()
			{
				@Override
				public void update(LineEvent arg0) 
				{
					if (arg0.getType() == LineEvent.Type.STOP)
					{
						playing.stop();
						playing.setFramePosition(0);
						playBackGround();
					}
				}
			});
		}
	}
	
	/**
	 * stops Background loop
	 */
	public static void stopBackGround()
	{
		musicMap.get("menu").get(0).stop();
		musicMap.get("menu").get(0).setFramePosition(0); 
	}
	
	/**
	 * plays soundEffect at ID resets this sound once played to be played again
	 * @param name uses this name to get specific clip
	 */
	public static void playSFX(String name)
	{
		Clip effect = sfx.get("sfx_" + name);
		
		if (effect == null)
		{
			return;
		}
		
		effect.start();
		effect.addLineListener(new LineListener()
		{

			@Override
			public void update(LineEvent arg0) 
			{
				if (arg0.getType() == LineEvent.Type.STOP)
				{
					effect.stop();
					effect.setFramePosition(0);
				}
			}
			
		});
	}

	@Override
	public void run() 
	{
		
	}
}

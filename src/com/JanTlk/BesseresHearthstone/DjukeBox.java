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
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class DjukeBox	//later should Run under own thread
{
	private static HashMap<String, Clip> sfx;
	private static HashMap<String, ArrayList<String>> musicFiles;
	private static Clip playingBG;
	private static Clip preloadedBG;

	private static LineListener nextSongOnEnd;

	/**
	 * all Audio and SoundFiles get imported and setup to be played later on
	 * @param spielfeld this is used to determine current game situation, to adpot music accordingly
	 */
	public DjukeBox()
	{
		ArrayList<String> audioFiles = new ArrayList<String>();
		audioFiles.add("sfx_CardShuffling");
		audioFiles.add("menu_TheFirstSteps");
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
		DJ.getInstance().start();
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
		musicFiles = new HashMap<String, ArrayList<String>>(5);

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
	private static File getAudioFile(String path)
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
		musicFiles.put("menu", new ArrayList<String>());
		musicFiles.put("start", new ArrayList<String>());
		musicFiles.put("playing", new ArrayList<String>());
		musicFiles.put("losing", new ArrayList<String>());
		musicFiles.put("winning", new ArrayList<String>());

		for (String path : music_Paths)
		{
			try {
				String typ = path.substring(0, path.indexOf("_"));
				if (musicFiles.containsKey(typ))
				{
					musicFiles.get(typ).add(path);
				}
				else
				{
					System.err.printf("DjukeBox.sortImprtMusic %s of Typ %s not assinged", path, typ);
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Error Loading: " + path);
			}
		}

	}

	/**
	 * stops Background loop
	 */
	public static void stopBackGround()
	{
		if (playingBG == null)
		{
			return;
		}

		//overrides old LineListener
		if (nextSongOnEnd != null)
		{
			playingBG.removeLineListener(nextSongOnEnd);
			nextSongOnEnd = null;
		}

		playingBG.stop();
		playingBG.setFramePosition(0);
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

	/**
	 * loads next Clip to be played
	 * @param nextPlayList next song is from this playlist
	 */

	public static void preloadFromPlayList(String nextPlayList)
	{
		ArrayList<String> paths = musicFiles.get(nextPlayList);
		Random r = new Random();

		if ((playingBG != null)
		&& (nextSongOnEnd != null))
		{
			playingBG.removeLineListener(nextSongOnEnd);
			nextSongOnEnd = null;
		}

		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(getAudioFile(paths.get(r.nextInt(paths.size()))));
			AudioFormat format = ais.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			preloadedBG = (Clip) AudioSystem.getLine(info);
			preloadedBG.open(ais);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error Loading next song from Playlist: " + nextPlayList);
		}
	}

	/**
	 * sets current volume (-50db ... 0db)
	 * @param soundLevel between 0 - 100%
	 */
	public static void setSoundLevel(int soundLevel)
	{
		if (playingBG != null)
		{
			System.out.printf("DjukeBox.setSoundLevel(%d)\n", soundLevel);
			float fadeCurve = (float) (Math.sin(0.005 * soundLevel * Math.PI / 2.0));
			FloatControl gainControl = (FloatControl) playingBG.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue((float) (-50 + 50.0 * fadeCurve));
		}
	}

	/**
	 * switches to next preloaded song if there is one
	 */
	public static void switchPlayList(int soundLevel)
	{
		if (preloadedBG != null)
		{
			if (playingBG != null)
			{
				if (nextSongOnEnd != null)
				{
					playingBG.removeLineListener(nextSongOnEnd);
					nextSongOnEnd = null;
				}

				playingBG.close();
			}

			playingBG = preloadedBG;

			setSoundLevel(soundLevel);
			playingBG.start();

			nextSongOnEnd = new LineListener()
			{
				@Override
				public void update(LineEvent arg0)
				{
					if (arg0.getType() == LineEvent.Type.STOP)
					{
						DJ.getInstance().enqueue("next");
					}
				}
			};

			playingBG.addLineListener(nextSongOnEnd);

			preloadedBG = null;
		}
	}

	/**
	 * @return true if song is playing
	 */
	public static boolean isPlaying()
	{
		return playingBG != null;
	}
}

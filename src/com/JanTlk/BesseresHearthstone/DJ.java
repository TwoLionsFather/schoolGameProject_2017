package com.JanTlk.BesseresHearthstone;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DJ extends Thread
{
	private static DJ djSingleton = new DJ();
	
	private final BlockingQueue<String> queue;
	private String currentPlayList;
	private String nextPlayList;
	private int soundLevel;
	
	/**
	 * nowhere but in this call a DJ can be created
	 */
	private DJ() 
	{	
		queue = new LinkedBlockingQueue<String>();
		soundLevel = 100;
		
		if (Hearthstone.isDebugMode())
		{
			System.out.println("A new DJ created");
		}
	}
	
	/**
	 * kann jederzeit einer Klasse/Methode den current DJ Booss geben
	 * @return the one and only DJ ;)
	 */
	public static DJ getInstance()
	{
		return djSingleton;
	}
	
	/**
	 * main control method
	 * @param command uses this command for control
	 */
	public void enqueue(String command)
	{
		queue.add(command);
	}
	
	@Override
	public void run() 
	{
		while (true)
		{
			try {
				String totalCommand = queue.take();
				Scanner tS = new Scanner(totalCommand);
				String cmd = tS.next();
				
				if (true || Hearthstone.isDebugMode())
				{
					System.out.printf("DJ gets command to: %s!\n", totalCommand);
				}
				
				if (cmd.equalsIgnoreCase("exit"))
				{
					break;
				}
				
				else if (cmd.equalsIgnoreCase("next"))
				{
					DjukeBox.preloadFromPlayList(currentPlayList);
					DjukeBox.switchPlayList(soundLevel);
				}
				
				else if (cmd.equalsIgnoreCase("adapt"))
				{
					String playList = tS.next();
					
					if (!playList.equalsIgnoreCase(currentPlayList))
					{
						nextPlayList = playList;
						DjukeBox.preloadFromPlayList(nextPlayList);
						if (DjukeBox.isPlaying())
						{
							enqueue("fadeout");
						}
						
						else
						{
							soundLevel = 100;
							DjukeBox.switchPlayList(soundLevel);
						}
					}
				}
				
				else if (cmd.equalsIgnoreCase("fadeout"))
				{
					if (soundLevel > 0)
					{
						soundLevel -= 10;
						DjukeBox.setSoundLevel(soundLevel);
						sleep(100);
						enqueue("fadeout");
					}
					
					else 
					{
						currentPlayList = nextPlayList;
						DjukeBox.switchPlayList(soundLevel);
						enqueue("fadein");
						if (Hearthstone.isDebugMode())
						{
							System.out.printf("DJ.run Changeing Playlist to %s\n", nextPlayList);
						}
					}
				}
				
				else if (cmd.equalsIgnoreCase("fadein"))
				{
					if (soundLevel < 100)
					{
						soundLevel += 10;
						DjukeBox.setSoundLevel(soundLevel);
						sleep(100);
						enqueue("fadein");
					}
				}
				
				tS.close();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}

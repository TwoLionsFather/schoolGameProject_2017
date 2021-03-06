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
	private int fadeIncrement;
	
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
				
				if (Hearthstone.isDebugMode())
				{
					System.out.printf("DJ gets command to: %s!\n", totalCommand);
				}
				
				if (cmd.equalsIgnoreCase("exit"))
				{
					break;
				}
				
				else if (cmd.equalsIgnoreCase("next"))
				{
					if (tS.hasNext())
					{
						String selectedPL = tS.next();
						if (!selectedPL.equalsIgnoreCase(currentPlayList))
						{
							currentPlayList = selectedPL;
							DjukeBox.preloadFromPlayList(currentPlayList);
						}
					}
					
					else
					{
						DjukeBox.preloadFromPlayList(currentPlayList);
						DjukeBox.switchPlayList(soundLevel);
					}
					
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
							fadeIncrement = -5;
							enqueue("fadeout");
						}
						
						else
						{
							fadeIncrement = 0;
							soundLevel = 100;
							DjukeBox.switchPlayList(soundLevel);
						}
					}
				}
				
				else if (cmd.equalsIgnoreCase("fadeout"))
				{
					if (fadeIncrement >= 0)
					{
						continue;
					}
					
					if (soundLevel > 0)
					{
						soundLevel += fadeIncrement;
						DjukeBox.setSoundLevel(soundLevel);
						sleep(100);
						enqueue("fadeout");
					}
					
					else 
					{
						soundLevel = 0;
						fadeIncrement = 10;
						currentPlayList = nextPlayList;
						DjukeBox.switchPlayList(soundLevel);
						enqueue("fadein");
					}
				}
				
				else if (cmd.equalsIgnoreCase("fadein"))
				{
					if (fadeIncrement <= 0)
					{
						continue;
					}
					
					if (soundLevel < 100)
					{
						soundLevel += fadeIncrement;
						DjukeBox.setSoundLevel(soundLevel);
						sleep(100);
						enqueue("fadein");
					}
					else
					{
						soundLevel = 100;
						fadeIncrement = 0;
					}
				}
				
				tS.close();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

	public String getCurrentPL() 
	{
		return currentPlayList;
	}
	
}

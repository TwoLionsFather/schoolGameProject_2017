package com.JanTlk.BesseresHearthstone;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Hearthstone extends Canvas implements Runnable 
{
	private static final long serialVersionUID = 406005602200328868L;
	
	public static final String TITEL = "Hearthstone";
	public static final float BREITE = 1280; 			// 1920 für Fullscreen
	public static final float HOEHE = BREITE / 4 * 3; 	// 3/4 der Breite -> Höhe
	
	private Thread thread;
	private Spielfeld spielfeld;
	
	private BufferedImage backGround;
	
	private boolean running = false;
	
	public Hearthstone()
	{		
		try {
			backGround = ImageIO.read(new File("backGround.png"));
		} catch (IOException e) {
			backGround = null;
			e.printStackTrace();
		}
		
		spielfeld = new Spielfeld();
		MousInput mouseStuff = new MousInput(spielfeld);
		this.addMouseMotionListener(mouseStuff);
		this.addMouseListener(mouseStuff);

		
		//window init
		new Fenster(BREITE, HOEHE, TITEL, this);	
	}
	
	/**
	 * Gameloop that ticks and renders 60 times per secound
	 */
	public void run() 
	{
		this.requestFocus();
		
		double target = 60.0;
		double nsPerTick = 1000000000 / target;
		double unprocessed = 0.0;
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		int fps = 0;
		int tps = 0;
		boolean canRender = false;
		
		while(running)
		{
			long now = System.nanoTime();
			unprocessed += (now - lastTime) / nsPerTick;
			lastTime = now;
			
			if(unprocessed >= 1.0)
			{
				tick();
				unprocessed--;
				tps++;
				canRender = true;
			}
			else
				canRender = false;
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(canRender) 
			{
				fps++;
				render();
			}
			
			if(System.currentTimeMillis() - timer > 1000)
			{
				timer += 1000;
				System.out.printf("FPS: %d | TPS: %d\n", fps, tps);
				fps = 0;
				tps = 0;
			}
		}
		stop();	
	}
	
	/**
	 * currently just a wildcard
	 */
	public void tick()
	{
		//spielfeld.tick();
	}
	
	public void render()
	{
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null)
		{
			this.createBufferStrategy(3); //3 bilder Buffern
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		
		
		//Graphics g = this.getGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, (int) BREITE, (int) HOEHE);
		g.drawImage(backGround, 0, 0, null);
		
		spielfeld.render(g);
		
		g.dispose();
		bs.show();
	}

	
	public void start() 
	{
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	
	public synchronized void stop()
	{
		try{
			thread.join();
			running = false;
		}catch(Exception e) {
			e.printStackTrace();
		}			
	}
	
	public static void main(String[] args) 
	{
		new Hearthstone();
	}
}

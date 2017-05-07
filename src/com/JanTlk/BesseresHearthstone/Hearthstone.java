package com.JanTlk.BesseresHearthstone;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import com.JanTlk.BesseresHearthstone.Karten.Karte;
import com.JanTlk.BesseresHearthstone.Karten.Typ;

public class Hearthstone extends Canvas implements Runnable 
{
	//benötigt um Kommunikation zwischen den richtigen Klassen sicher zu stellen
	private static final long serialVersionUID = 406005602200328868L;
	
	public static final String TITEL = "Hearthstone";
	public static final float BREITE = 900; //1920 für Fullscreen
	public static final float HOEHE = (BREITE / 4) * 3; //3/4 der Breite -> Höhe
	
	private Thread thread; //neuen Thread anlegen mit dem das Spiel laufen soll
	private Spielfeld spielfeld;
	
	private boolean running = false;
	
	public Hearthstone()
	{		
		//window init
		new Fenster(BREITE, HOEHE, TITEL, this);
		
		DeckHandler dH = new DeckHandler();
		spielfeld = new Spielfeld();
	}
	
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
	
	public void tick()
	{
		spielfeld.tick();
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
		
		g.setColor(Color.black);
		g.fillRect(0, 0, (int) BREITE, (int) HOEHE);
		
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

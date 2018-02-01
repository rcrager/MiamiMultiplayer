package game.main;
import java.awt.Canvas;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import game.main.window.graphics;

public class Game extends Canvas implements Runnable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * Creates the actual window for the game
	 * then adds the graphics jpanel to the
	 * jframe.
	 * Also limits the times of ticks per second
	 * and renders per second.
	 * 
	 */

	public static int WIDTH = 256;
	public static final int SCALE = 3;
	public static int HEIGHT = (WIDTH/12*9);
	public static final String NAME = "Miami Multiplayer";
	public static JFrame frame;
	
	public boolean running = false;
	public int tickCount = 0;
	
	
	public Game(){
		frame = new JFrame(NAME);
		frame.setSize(WIDTH*SCALE, HEIGHT*SCALE);
		frame.add(new graphics());

		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start() {
		running = true;
		new Thread(this).start();		
	}
	public synchronized void stop() {
		running = false;
	}
	
	
	public void run() {
		long lastTime = System.nanoTime();
		double nsPerTick = 1000000000D/60D;
		
		int ticks = 0;
		int frames = 0;
		long lastTimer = System.currentTimeMillis();
		double delta = 0;
		
		while(running){
			long now = System.nanoTime();
			delta+=(now-lastTime)/nsPerTick;
			lastTime = now;
			boolean shouldRender = true;
			
			while(delta>=1){
				ticks++;
				tick();
				delta-=1;
				shouldRender = true;
			}
			try {
				Thread.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(shouldRender){
				frames++;
				render();
			}
			
			if(System.currentTimeMillis()-lastTimer>=1000){
				lastTimer+=1000;
				frames = 0;
				ticks = 0;
			}
		}
	}
	public void tick(){
		tickCount++;
	}
	public void render(){
		BufferStrategy bs = frame.getBufferStrategy();
		if(bs==null){
			frame.createBufferStrategy(1);
			return;
		}
	}
	public static void main(String[] args){
		new Game().start();
	}
}


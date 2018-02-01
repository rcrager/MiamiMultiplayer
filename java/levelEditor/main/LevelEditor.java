package levelEditor.main;

import java.awt.Canvas;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import levelEditor.main.window.LevelEditorGraphics;

public class LevelEditor extends Canvas implements Runnable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * LevelEditor jframe class
	 * basically just the class that creates
	 * and runs the window to hold the 
	 * graphics and any other components
	 * in the other classes
	 */
	public static final int SCALE = 1;
	public static int WIDTH = 720;
	public static int HEIGHT = (WIDTH/12*9);
	public static final String NAME = "LevelEditor";
	public static JFrame frame;
	
	public boolean running = false;
	public int tickCount = 0;
	
	private LevelEditorGraphics graphics;
	
	public LevelEditor(){
		graphics = new LevelEditorGraphics();
		frame = new JFrame(NAME);
		frame.setSize(WIDTH*SCALE, HEIGHT*SCALE);
		
		frame.add(graphics);
		
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
				frame.setTitle(frames + " frames, " + ticks + " ticks");
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
		new LevelEditor().start();
		
	}
}


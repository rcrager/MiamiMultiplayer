package game.util.classes;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;

import game.main.Game;

public class Animation{
	/*
	 * literally just an ararylist of
	 * bufferedimages refered to as frames.
	 * depending on the speed the update method
	 * will change the current image to a different
	 * frame.
	 * Speed is in milliseconds.
	 */
	public BufferedImage sprite;
	
	private long previousTime,speed;
	private int pauseImg,curImg;
	private String name;
	private List<BufferedImage> frames;
	private volatile boolean running = false;
	private Point origin = null;
	
	private GameObject object;
	
	public Animation(List<BufferedImage> imgs, long speed,GameObject object,String name){
		frames = imgs;
		this.speed=speed;
		this.object = object;
		this.name = name;
	}
	public void addReverse(){
		for(int j = frames.size()-1; j>0; j--){
			frames.add(frames.get(j));
		}
	}
	public void addFrame(BufferedImage frame){
		frames.add(frame);
	}
	public void addAnimation(Animation anim){
		for(int x = 0; x<=anim.frames.size()-1; x++){
			frames.add(anim.frames.get(x));
		}
	}
	public void update(long time) {
		if(running){
			if(time-previousTime>=speed){
				curImg++;
				try{
					object.setImage(frames.get(curImg));
				}catch(IndexOutOfBoundsException e){
					curImg = 0;
					object.setImage(frames.get(curImg));
				}
				previousTime = time;
			}
		}
	}
	public void setOrigin(int x, int y){
		origin = new Point(x,y);
	}
	public void setSpeed(int spd){
		speed = spd;
	}
	public boolean isRunning(){
		return running;
	}
	public boolean atEnd(){
		if(curImg>=frames.size()-1){
			return true;
		}
		return false;
	}
	public Point getOrigin(){
		Point coor = null;
		if(origin!=null){
			coor = new Point(origin.x*Game.SCALE,origin.y*Game.SCALE);
		}else{
			coor = new Point(frames.get(curImg).getWidth()/2*Game.SCALE,frames.get(curImg).getHeight()/2*Game.SCALE);
		}
		return coor;
	}
	
	public void setImageIndex(int index){
		curImg = index;
	}
	
	public int getImageIndex(){
		return curImg;
	}
	
	public String getName(){
		return name;
	}
	
	public void play(){
		running = true;
		previousTime = 0;
		curImg = 0;
		pauseImg = 0;
	}
	public void resume(){
		running = true;
		curImg = pauseImg;
	}
	public void pause(){
		pauseImg = curImg;
		running = false;
	}
	public void stop(){
		previousTime = 0;
		curImg = 0;
		pauseImg = 0;
		object.setImage(frames.get(curImg));
		running = false;
	}
}

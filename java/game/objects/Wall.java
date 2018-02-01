package game.objects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;

import game.id.ObjectID;
import game.util.classes.GameObject;
import game.util.classes.ImageLoader;

public class Wall extends GameObject{
	/*
	 * When player collides with this they
	 * can't move in that direction anymore.
	 * In other words it's solid. No objects
	 * can go past it because it's solid.
	 */
	
	BufferedImage sprite;
	ImageLoader loader;
	
	public Wall(int x, int y, int w, int h,ObjectID id){
		super(x+w/2,y+h/2,id, 1);
		this.w = w;
		this.h = h;
		setSolid(true);
		loader = new ImageLoader();
	}
	
	
	
	public void setSprite(String path){
		sprite = loader.loadImage(path,true);
	}
	public BufferedImage getSprite(){
		return sprite;
	}
	
	public void update(List<GameObject> objects){
		
	}
	public void render(Graphics g){
//		Graphics2D g2d = (Graphics2D) g;
//		g2d.setColor(Color.black);
//		g2d.draw(getRectBounds());
	}
}

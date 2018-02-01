package game.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import game.id.ObjectID;
import game.main.Game;
import game.util.classes.GameObject;
import game.util.classes.ImageLoader;

public class Splatter extends GameObject{
	/*
	 * When a bullet is destroyed or hits any object
	 * including players it creates this on the x&y
	 * of the bullet before it gets destroyed.
	 * When this is created it gets the color of the
	 * bullet and sets the color of the image 
	 * "splatter.png" to that color.
	 */

	boolean splat = true;
	ImageLoader loader = new ImageLoader();
	boolean getNewColor = false;
	boolean showSplat = true;
	Color currentColor = new Color(0,0,0,0);
	BufferedImage splatter;
	BufferedImage coloredSplatter;
	
	public Splatter(int x, int y,Color color) {
		super(x, y,ObjectID.Splatter,7);
//		splatter = loader.loadImage("res/sprites/Splatter.png");
		splatter = loader.loadImage("/sprites/Splatter.png",true);
		currentColor = color;
		changeColor(splatter,color);
	}

	@Override
	public void update(List<GameObject> objects) {
		
	}
	
	private void changeColor(BufferedImage img, Color color){
		Graphics2D g = (Graphics2D) img.getGraphics();
		color = new Color (color.getRed(),color.getGreen(),color.getBlue());
		g.setColor(color);
		for(int x=0; x<img.getWidth(); x++){
			for(int y = 0; y<img.getHeight(); y++){
				int argb = img.getRGB(x, y);
				if(argb>>16 != 0x00){
					//if pixel isn't transparent
					img.setRGB(x, y, color.getRGB());
				}
			}
		}
	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(splatter,x-(splatter.getWidth()*Game.SCALE)/2, y-(splatter.getHeight()*Game.SCALE)/2,splatter.getWidth()*Game.SCALE,splatter.getHeight()*Game.SCALE,null);
	}
	
}

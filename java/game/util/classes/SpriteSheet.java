package game.util.classes;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class SpriteSheet {
	/*
	 * Takes in image.
	 * Allows user to simply either get a 
	 * single sprite or get an entire animation
	 * depending on the direction that they want
	 * the animation to go. 
	 * Animation requires # of frames and x&y and
	 * w&h of the animation.
	 */
	
	String path;
	BufferedImage sheet;
	ImageLoader loader;
	boolean horizontal=false,vertical=false;
	
	public SpriteSheet(String pat){
		path = pat;
		loader = new ImageLoader();
		sheet = loader.loadImage(path,true);
	}
	public BufferedImage getSprite(int x,int y, int w, int h){
		BufferedImage sprite;
		sprite = sheet.getSubimage(x, y, w, h);
		return sprite;
	}
	public void setDirection(String dir){
		switch(dir.toLowerCase()){
		case "vertical":
			vertical = true;
		break;
		case "horizontal":
			horizontal = true;
		break;
		case "none":
			horizontal = false;
			vertical = false;
		break;
		}
	}
	public Animation getAnimation(int frames, int x, int y, int width, int height, int hspacing, int vspacing,long speed,GameObject object,String name){
		Animation returnAnim;
		List<BufferedImage> animation = new ArrayList<BufferedImage>();
		int curXPos = x;
		int curYPos = y;
		for(int v = 0; v<frames; v++){
			animation.add(sheet.getSubimage(curXPos, curYPos, width,height));
			if(hspacing>0)
				curXPos+=(width+hspacing);
			if(vspacing>0)
				curYPos+=(height+vspacing);
			
			if(horizontal){
				curXPos+=(width);
			}
			if(vertical){
				curYPos+=(height);
			}
		}
		returnAnim = new Animation(animation,speed,object,name);
		return returnAnim;
	}
}

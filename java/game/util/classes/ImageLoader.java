package game.util.classes;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader {
	/*
	 * Loads image specified by the path given.
	 * has boolean for if the file being loaded
	 * is meant to be loaded as a resource
	 * or as a normal computer file.
	 */
	
	String path;
	BufferedImage img;
	
	public ImageLoader(){
		img = null;
	}
	
	public BufferedImage loadImage(String path, boolean local){
		this.path = path;
		try {
			if(local){
				img = ImageIO.read(getClass().getResourceAsStream(path));
			}else{
				img = ImageIO.read(new File(path));
			}
		} catch (IOException e) {
			System.err.println("Could Not Find Or Load Image From Path: (" + path + ")");
			return null;
		}
		return img;
	}
}

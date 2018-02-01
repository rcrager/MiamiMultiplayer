package game.levels;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;

import game.id.ObjectID;
import game.main.Game;
import game.util.classes.GameObject;
import game.util.classes.ImageLoader;

public class Level{
	/*
	 * Holds the collisions and spawn points
	 * for the level as well as the name and
	 * the image of the level.
	 * User creates their own level which
	 * creates this which then puts all the
	 * appropiate objects such as:
	 * weapons, walls, and spawnpoints in
	 * an arraylist for objects which gets
	 * added to the object pool when the 
	 * currentLevel in Controller.class is 
	 * switched to an instance of this class.
	 */
	LevelLoader loader;
	BufferedImage image;
	ImageLoader imageLoader;
	String collisionPath,imagePath;
	LinkedList<GameObject> collisions;
	boolean isLocal;
	
	public Level(String path,boolean local){
		imageLoader = new ImageLoader();
		imagePath = path;
		isLocal = local;
		image = imageLoader.loadImage(path,local);
		String colName = "";
		String jarLoc = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		String levelFolder = jarLoc.substring(0,jarLoc.lastIndexOf("/"));
		while(levelFolder.indexOf("%20")!=-1){
			String temp = levelFolder.substring(0,levelFolder.indexOf("%20")) + " "
					+ levelFolder.substring(levelFolder.indexOf("%20")+3,levelFolder.length());
			levelFolder = temp;
		}
		if(path.indexOf("/")==-1){
			colName = path.substring(path.lastIndexOf("\\")+1, path.indexOf("."));
		}else{
			colName = path.substring(path.lastIndexOf("/")+1,path.indexOf("."));
		}
		if(local){
			setCollisionPath("/levels/" + colName+".txt");
		}else{
			setCollisionPath(levelFolder + "/levels/" + colName + ".txt");
		}
	}
	
	public void setCollisionPath(String path){
		this.collisionPath = path;
	}
	
	public LinkedList<GameObject> loadCollisions(String path){
		collisionPath = path;
		loader = new LevelLoader(collisionPath,isLocal());
		collisions = loader.loadObjects();
		return collisions;
	}
	
	public LinkedList<GameObject> getCollisions(String path){
		if(collisions!=null){
			return collisions;
		}else{
			return null;
		}
	}
	
	public LinkedList<GameObject> loadSpawns(){
		LinkedList<GameObject> spawns = new LinkedList<GameObject>();
		if(getCollisions(collisionPath)!=null){
			for(GameObject gm: getCollisions(collisionPath)){
				if(gm.getId() == ObjectID.Spawn){
					spawns.add(gm);
				}
			}
		}
		return spawns;
	}
	
	public String getCollisionPath(){
		return collisionPath;
	}
	
	public String getName(){
		if(imagePath.indexOf("/")==-1){
			return imagePath.substring(imagePath.indexOf("\\",imagePath.indexOf("\\")+1)+1, imagePath.indexOf("."));
		}else{
			return imagePath.substring(imagePath.lastIndexOf("/")+1, imagePath.indexOf("."));
		}
	}
	
	public String getImagePath(){
		return imagePath;
	}
	
	public int getWidth(){
		return image.getWidth();
	}
	public int getHeight(){
		return image.getHeight();
	}
	
	public boolean isLocal(){
		return isLocal;
	}
	
	public void render(Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		g2d.drawImage(image, 0, 0, image.getWidth()*Game.SCALE, image.getHeight()*Game.SCALE, null);
	}
}

package game.levels;

import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import game.id.ObjectID;
import game.id.Weapons;
import game.main.Game;
import game.objects.SpawnPoint;
import game.objects.Wall;
import game.objects.Weapon;
import game.util.classes.GameObject;
import game.util.classes.ImageLoader;

public class LevelLoader extends GameObject{
	/*
	 * Scans the file given, searching for
	 * RECT,SPAWN,etc. then creating an object
	 * arraylist for the objects in the file.
	 * Then it the level class calls the 
	 * loadObjects method which will return 
	 * the arraylist of objects to be added.
	 */
	
	String path;
	ImageLoader loader;
	Scanner in;
	LinkedList<GameObject> objects;
	
	
	public LevelLoader(String path, boolean isLocal){
		//constructor for level loader class with path as input
		super(0,0,ObjectID.LevelLoader,100);
		this.path = path;
		objects = new LinkedList<GameObject>();
		loader = new ImageLoader();
		if(isLocal){
			in = new Scanner(getClass().getResourceAsStream(path));
		}else{			
			try {
				in = new Scanner(new File(path));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		loadObjects();
	}
	
	public LinkedList<GameObject> loadObjects(){
		//loads objects to be put into level
		int x,y,w,h;
		while(in.hasNextLine()){
			String temp = in.nextLine();
			String type = temp.substring(0, temp.indexOf(':'));
			switch(type){
			case "RECT":
				x = Integer.parseInt(temp.substring(temp.indexOf('(')+1, temp.indexOf(','))) * Game.SCALE;
				y = Integer.parseInt(temp.substring(temp.indexOf(',')+1, temp.indexOf(')'))) * Game.SCALE;
				w = Integer.parseInt(temp.substring(temp.indexOf('[')+1, temp.indexOf(',', temp.indexOf(',') + 1))) * Game.SCALE;
				h = Integer.parseInt(temp.substring(temp.indexOf(',', temp.indexOf(',') + 1)+1, temp.indexOf(']'))) * Game.SCALE;
				objects.add(new Wall(x,y,w,h,ObjectID.Wall));
			break;
			case "SPAWN":
				x = Integer.parseInt(temp.substring(temp.indexOf('(')+1, temp.indexOf(','))) * Game.SCALE;
				y = Integer.parseInt(temp.substring(temp.indexOf(',')+1, temp.indexOf(')'))) * Game.SCALE;
				objects.add(new SpawnPoint(x,y,ObjectID.Spawn));
			break;
			case "PAINTBRUSH":
				x = Integer.parseInt(temp.substring(temp.indexOf('(')+1, temp.indexOf(','))) * Game.SCALE;
				y = Integer.parseInt(temp.substring(temp.indexOf(',')+1, temp.indexOf(')'))) * Game.SCALE;
				objects.add(new Weapon(x,y,Weapons.PaintBrush));
			break;
			case "PISTOL":
				x = Integer.parseInt(temp.substring(temp.indexOf('(')+1, temp.indexOf(','))) * Game.SCALE;
				y = Integer.parseInt(temp.substring(temp.indexOf(',')+1, temp.indexOf(')'))) * Game.SCALE;
				objects.add(new Weapon(x,y,Weapons.Pistol));
			break;
			case "SHOTGUN":
				x = Integer.parseInt(temp.substring(temp.indexOf('(')+1, temp.indexOf(','))) * Game.SCALE;
				y = Integer.parseInt(temp.substring(temp.indexOf(',')+1, temp.indexOf(')'))) * Game.SCALE;
				objects.add(new Weapon(x,y,Weapons.Shotgun));
			break;
			case "RIFLE":
				x = Integer.parseInt(temp.substring(temp.indexOf('(')+1, temp.indexOf(','))) * Game.SCALE;
				y = Integer.parseInt(temp.substring(temp.indexOf(',')+1, temp.indexOf(')'))) * Game.SCALE;
				objects.add(new Weapon(x,y,Weapons.Rifle));
			break;
			}
		}
		for(GameObject gm: objects){
			if(gm.getId() == ObjectID.Wall){
				gm.setSolid(true);
			}
		}
		return objects;
	}

	@Override
	public void update(List<GameObject> objects) {}
	@Override
	public void render(Graphics g) {}
}

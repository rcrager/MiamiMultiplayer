package game.objects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import game.id.ObjectID;
import game.id.Weapons;
import game.main.Game;
import game.util.classes.GameObject;
import game.util.classes.ImageLoader;
import game.util.classes.SpriteSheet;

public class Weapon extends GameObject{
	/*
	 * This is the weapons that are on the ground
	 * that the player can pickup. Depending on an
	 * enum of weapon type it will set the sprite
	 * of the weapon to the correct image.
	 */
	Weapons type;
	boolean equipped = false;
	BufferedImage sprite;
	
	private ImageLoader loader;
	private SpriteSheet weapons;
	
	public Weapon(int x, int y, Weapons type){
		super(x,y,ObjectID.Weapon,2);
		this.type = type;
		setSolid(false);
		setBounds(25*Game.SCALE,25*Game.SCALE);
		sprite = new BufferedImage(1,1,1);
		
		loader = new ImageLoader();
		
		weapons = new SpriteSheet("/sprites/weaponSpriteSheet.png");
		//assign correct sprite
		switch(type){
			case PaintBrush:
				setImage(weapons.getSprite(200,200,25,15));
			break;
			case Pistol:
				setImage(weapons.getSprite(204,19,17,10));
			break;
			case Shotgun:
				setImage(weapons.getSprite(13, 81, 30, 12));
			break;
			case Rifle:
				setImage(weapons.getSprite(13, 18, 33, 11));
			break;
			default:
				setImage(loader.loadImage("/sprites/Splatter.png",true));
			break;
		}
	}
	@Override
	public void update(List<GameObject> objects) {
		
	}
	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
			g2d.drawImage(sprite, x-(getImage().getWidth()*Game.SCALE/2), y-(getImage().getHeight()*Game.SCALE/2),
					getImage().getWidth()*Game.SCALE,getImage().getHeight()*Game.SCALE,null);
	}
	public boolean isEquipped(){
		return equipped;
	}
	@Override
	public void rotate(int ang) {
		
	}
	@Override
	public int getRotation() {
		return 0;
	}
	public Weapons getWeapon(){
		return type;
	}
	@Override
	public BufferedImage getImage() {
		return sprite;
	}
	@Override
	public void setImage(BufferedImage img) {
		sprite = img;
	}
}

package game.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;

import game.id.ObjectID;
import game.main.Controller;
import game.main.window.graphics;
import game.net.OnlinePlayer;
import game.util.classes.GameObject;
import game.util.classes.ImageLoader;
import game.util.classes.Sound;

public class Bullet extends GameObject{
	/*
	 * Created when user shoots gun.
	 * has a constant speed and when
	 * it hits a 'solid' object(wall)
	 * it destroys itself and creates
	 * a splatter.
	 */
	
	ImageLoader loader;
	int imgOffset;
	Player player;
	int startX,startY;
	boolean splatter = false;
	Color color = new Color(0,0,0);
	private Sound shoot;
	
	public Bullet(int x, int y,Player player){
		super(x,y, ObjectID.Bullet,1);
		startX = x;
		startY = y;
		this.player = player;
		int rotation = player.getRotation();
		imgOffset = player.rotOffset;
		loader = new ImageLoader();
		setImage(sprite);
		setSolid(false);
		setBounds(10,10);
		setVelX(15);
		setVelY(15);
		setRotation(rotation);
		color = getRandomColor();
		double rand = Math.random()*4;
		if(rand>=0.0 && rand<=1.0){
			shoot = Controller.sounds.get(3);
		}
		if(rand>1.0 && rand<=2.0){
			shoot = Controller.sounds.get(4);
		}
		if(rand>2.0 && rand<=3.0){
			shoot = Controller.sounds.get(5);
		}
		if(rand>3.0 && rand<=4.0){
			shoot = Controller.sounds.get(6);
		}
//		shoot.setVolume(graphics.VOLUME);
		shoot.playSound();
	}

	public void setXSpeed(int speed) {
		this.velX = speed;
	}
	
	public void setYSpeed(int speed){
		this.velY= speed;
	}
	
	@Override
	public Rectangle getRectBounds(){
		return new Rectangle(x,y,w,h);
	}
	
	private void move(){
		double xdir = Math.cos(Math.toRadians(getRotation()));
		double ydir = Math.sin(Math.toRadians(getRotation()));
		x+=(velX*xdir);
		y+=(velY*ydir);
	}
	
	@Override
	public void update(List<GameObject> objects) {
		move();
		for(int j = 0; j<=objects.size()-1; j++){
			GameObject gm = objects.get(j);
			if(gm!=null){				
				checkCollision(gm);
			}
		}
	}
	
	private void checkCollision(GameObject object) {
		if(!object.equals(player) || object.getSolid()){
			if(getRectBounds().intersects(object.getRectBounds())){
				if(object instanceof OnlinePlayer){
					Splatter splat = new Splatter(x,y,color);
					graphics.controller.addGameObject(splat);
					((OnlinePlayer)object).health = 0;
					Sound playerHit = Controller.sounds.get(7);
					playerHit.setVolume(graphics.VOLUME);
					playerHit.playSound();
					shoot.stopSound();
					graphics.controller.removeGameObject(this);
				}else if(object instanceof Wall){
					double rand = Math.random()*2;
					Sound playerHit = null;
					if(rand>0.0 && rand<=1.0){
						playerHit = Controller.sounds.get(8);
					}
					if(rand>1.0 && rand<=2.0){
						playerHit = Controller.sounds.get(9);
					}
					playerHit.setVolume(graphics.VOLUME);
					playerHit.playSound();
					Splatter splat = new Splatter(x,y,color);
					graphics.controller.addGameObject(splat);
					shoot.stopSound();
					graphics.controller.removeGameObject(this);
				}
				
			}
		}
		if(Math.abs(x-startX)>5000 || Math.abs(y-startY)>5000){
			graphics.controller.removeGameObject(this);
		}
	}
	
	private Color getRandomColor(){
		int r = (int)(Math.random()*255);
		int g = (int)(Math.random()*255);
		int b = (int)(Math.random()*255);
		color = new Color(r,g,b);
		return color;
	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setColor(color);
		g2d.fillOval(x, y, getWidth(), getHeight());
	}
	
}

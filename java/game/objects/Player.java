package game.objects;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import game.id.ObjectID;
import game.id.Weapons;
import game.main.Game;
import game.main.window.graphics;
import game.util.classes.Animation;
import game.util.classes.GameObject;
import game.util.classes.ImageLoader;
import game.util.classes.SpriteSheet;

public class Player extends GameObject{
	/*
	 * the core class for OnlinePlayer
	 * basically just handles the client
	 * side processes such as: input
	 * handling, rotating, and changing
	 * animations correctly.
	 */

	public double mouseX,mouseY;
	public Animation currentAnim;
	public ArrayList<Animation> animations;
	public int health;
	
	protected int rotOffset = 90;
	protected Weapons weapon;
	
	private int colWidth = 0, colHeight = 0;
	private int speed;
	
	private boolean keyW,keyA,keyS,keyD = false;
	private boolean mbRight = false ,mbLeft = false,mbMiddle = false;
	
	private double rotation,tox,toy;

	private BufferedImage sprite;
	private ImageLoader loader;
	private JFrame frame;
	private double lastShot = 1000;
	boolean weaponAnimPlaying;
	
	public Player(int xx, int yy,ObjectID id){
		//constructor for player
		super(xx,yy,id,1);
		init();
	}
	public Player(int xx, int yy, JFrame frm,ObjectID id){
		//constructor for player with jframe included 
		super(xx,yy,id,1);
		frame = frm;
		init();
	}
	private void init(){
		//initialize variables
		setImage(new BufferedImage(1, 1, 1));
		speed = 1;
		health = 100;
		rotation = 0;
		colWidth = 20*Game.SCALE;
		colHeight = 20*Game.SCALE;
		weapon = Weapons.Fist;
		setSolid(false);
		setBounds(colWidth,colHeight);
		loader = new ImageLoader();
		createAnimations();
	}
	private void createAnimations() {
		//create animations and add them to the arraylist
		animations = new ArrayList<Animation>();
		SpriteSheet coreySheet = new SpriteSheet("/sprites/coreySpriteSheet.png");
		
		Animation temp = coreySheet.getAnimation(1, 522, 715, 20, 26, 0, 0, 50, this,"idle");
		temp.setOrigin(11,13);
		animations.add(temp);
		temp = coreySheet.getAnimation(5, 522, 715, 20, 26, 0, 7, 120, this,"walk");
		temp.setOrigin(11,13);
		temp.addAnimation(coreySheet.getAnimation(3,554,582,20,26,13,0,120,this,"walk2"));
		animations.add(temp);
		
		coreySheet.setDirection("vertical");
		temp = coreySheet.getAnimation(9, 487,551, 30,33, 0,0, 130, this, "paintbrushWalk");
		temp.setOrigin(14,17);
		animations.add(temp);
		coreySheet.setDirection("none");
		temp = coreySheet.getAnimation(1, 487,551, 30,33, 0,0, 100, this, "paintbrushIdle");
		temp.setOrigin(14,17);
		animations.add(temp);
		temp = coreySheet.getAnimation(3, 405,725, 31,41, 0,2, 43, this, "paintbrushHit");
		temp.setOrigin(14,17);
		animations.add(temp);
		
		temp = coreySheet.getAnimation(6, 263, 314, 43, 19, 2, 0, 85, this, "pistolWalk");
		temp.setOrigin(13, 11);
		animations.add(temp);
		temp = coreySheet.getAnimation(1, 263, 314, 43, 19, 0, 0, 120, this, "pistolIdle");
		temp.setOrigin(13, 11);
		animations.add(temp);
		
		temp = coreySheet.getAnimation(6, 552, 451, 40, 18, 5, 0, 85, this, "shotgunWalk");
		temp.setOrigin(9, 10);
		animations.add(temp);
		temp = coreySheet.getAnimation(1, 552, 451, 40, 18, 5, 0, 100, this, "shotgunIdle");
		temp.setOrigin(9, 10);
		animations.add(temp);
		
		temp = coreySheet.getAnimation(5, 593, 420, 40, 19, 5, 0, 70, this, "rifleWalk");
		temp.setOrigin(9,9);
		animations.add(temp);
		temp = coreySheet.getAnimation(1, 593, 420, 40, 19, 5, 0, 90, this, "rifleIdle");
		temp.setOrigin(9,9);
		animations.add(temp);
		temp = coreySheet.getAnimation(3, 412, 451, 31, 20, 14, 0, 42, this, "rifleShoot");
		temp.setOrigin(9,9);
		animations.add(temp);
	}
	
	protected void updateAnimations(){
		//update the animations of the player based on their current weapon.
		switch(weapon){
			case Fist:
				Animation walk=null, idle=null;
				for(Animation a: animations){
					if(a.getName().equals("walk")){
						walk = a;
					}
					if(a.getName().equals("idle")){
						idle = a;
					}
				}
				if(Math.abs(velX)>0 || Math.abs(velY)>0){
					currentAnim = walk;
				}else{
					currentAnim = idle;
				}
			break;
			case PaintBrush:
				Animation paintbrushWalk=null,paintbrushIdle = null,
				paintbrushHit=null;
				for(Animation a: animations){
					if(a.getName().equals("paintbrushWalk")){
						paintbrushWalk = a;
					}
					if(a.getName().equals("paintbrushIdle")){
						paintbrushIdle = a;
					}
					if(a.getName().equals("paintbrushHit")){
						paintbrushHit = a;
					}
				}
				if(!weaponAnimPlaying){
					if(Math.abs(velX)>0 || Math.abs(velY)>0){
						currentAnim = paintbrushWalk;
					}else{
						currentAnim = paintbrushIdle;
					}
				}else{
					if(currentAnim == paintbrushHit && currentAnim.atEnd()){
						weaponAnimPlaying = false;
					}
					currentAnim = paintbrushHit;
				}
			break;
			case Pistol:
				Animation pistolWalk=null,pistolIdle=null;
				for(Animation a: animations){
					if(a.getName().equals("pistolIdle")){
						pistolIdle = a;
					}
					if(a.getName().equals("pistolWalk")){
						pistolWalk = a;
					}
				}
				
				if(Math.abs(velX)>0 || Math.abs(velY)>0){
					currentAnim = pistolWalk;
				}else{
					currentAnim = pistolIdle;
				}
			break;
			case Rifle:
				Animation rifleWalk=null,rifleIdle=null;
				for(Animation a: animations){
					if(a.getName().equals("rifleWalk")){
						rifleWalk = a;
					}
					if(a.getName().equals("rifleIdle")){
						rifleIdle = a;
					}
				}
				
				if(Math.abs(velX)>0 || Math.abs(velY)>0){
					currentAnim = rifleWalk;
				}else{
				currentAnim = rifleIdle;
				}
			break;
			case Shotgun:
				Animation shotgunWalk=null,shotgunIdle=null,shotgunShoot=null;
				for(Animation a: animations){
					if(a.getName().equals("shotgunWalk")){
						shotgunWalk = a;
					}
					if(a.getName().equals("shotgunIdle")){
						shotgunIdle = a;
					}
					if(a.getName().equals("shotgunShoot")){
						shotgunShoot = a;
					}
				}
				
				if(Math.abs(velX)>0 || Math.abs(velY)>0){
					currentAnim = shotgunWalk;
				}else{
					currentAnim = shotgunIdle;
				}
			break;
		default:
			System.err.println("No Case for Current Weapon Animation.");
			break;
		}
	}
	
	public void shoot(int startx, int starty){
		//shoot weapon with no offset
		Bullet bullet = new Bullet(startx,starty,this);
		graphics.controller.addGameObject(bullet);
	}
	public void shoot(int startx, int starty,int rot){
		//shoot weapon with rotation offset
		Bullet bullet = new Bullet(startx,starty,this);
		bullet.setRotation(rot);
		graphics.controller.addGameObject(bullet);
	}
	
	protected void checkMouseInput(List<GameObject> checker) {
		//check for mouse input then either shoot or pickup weapon accordingly
		if(mbRight){
			boolean weaponChanged = false;
			for(int j = 0; j<=checker.size()-1; j++){
				GameObject gm = checker.get(j);
				if(gm.getRectBounds().intersects(getRectBounds())){
						if(weapon.equals(Weapons.Fist)){
							if(gm.getId() == ObjectID.Weapon && !weaponChanged){
								//pickup weapon
								weaponChanged = checkWeaponPickup(gm);
							}
						}else{
							if(!weaponChanged && gm.getId()==ObjectID.Weapon){
								//switch weapons
								Weapon wpn = new Weapon(x,y,weapon);
								sendWeaponAction(0);
								graphics.controller.addGameObject(wpn);
								graphics.controller.removeGameObject(gm);
								weapon = ((Weapon)gm).getWeapon();
								weaponChanged = true;
					}
				}
				}
			}
				if(!weaponChanged && (weapon != Weapons.Fist)){
					//drop weapon
					Weapon wpn = new Weapon(x,y,weapon);
					sendWeaponAction(1);
					graphics.controller.addGameObject(wpn);
					weapon = Weapons.Fist;
				}
			mbRight = false;
		}
		if(mbLeft){
			switch(weapon){
			case PaintBrush:
					weaponAnimPlaying = true;
					lastShot = System.currentTimeMillis();
					mbLeft = false;
			break;
			case Rifle:
				//shoot every quarter second or so...
				if(System.currentTimeMillis()-lastShot>=.25*1000){
					shoot((int)(x+(Math.cos(Math.toRadians(getRotation()))*50)),
							(int)(y+(Math.sin(Math.toRadians(getRotation()))*50)));
					lastShot = System.currentTimeMillis();
				}
			break;
			case Pistol:
				//shoot once
				if(System.currentTimeMillis()-lastShot>=.5*1000){
				shoot((int)(x+(Math.cos(Math.toRadians(getRotation()))*50)),
						(int)(y+(Math.sin(Math.toRadians(getRotation()))*50)));
				lastShot = System.currentTimeMillis();
				}
			break;
			case Shotgun:
				//shoot 3 pellets
				if(System.currentTimeMillis()-lastShot>=.75*1000){
				shoot(x,y,getRotation()+5);
				shoot(x,y,getRotation()-5);
				shoot(x,y);
				mbLeft = false;
				lastShot = System.currentTimeMillis();
				}
			break;
			default:
				break;
			}
		}
	}
		
	protected void sendWeaponAction(int action) {
		//empty bc OnlinePlayer overrides it.
		
	}
	protected boolean checkWeaponPickup(GameObject gm){
		//check if player can pickup a weapon or not
		switch(((Weapon)gm).getWeapon()){
		case PaintBrush:
		case Pistol:
		case Rifle:
		case Shotgun:
			if(gm.getRectBounds().intersects(getRectBounds())){
				weapon = ((Weapon)gm).getWeapon();
				sendWeaponAction(-1);
				graphics.controller.removeGameObject(gm);
				return true;
			}
		break;
		default:
			System.err.println("Could Not Find Weapon Being Picked Up.");
		break;
		}
		return false;
	}
	
	private Point getImageOrigin(){
		//get the current frame of the animation's origin
		if(currentAnim!=null){
		return new Point(currentAnim.getOrigin());
		}else{
			return new Point(getImage().getWidth()/2,getImage().getHeight()/2);
		}
	}
	
	public int getImageIndex(){
		//get animation index number of current animation
		return currentAnim.getImageIndex();
	}
	
	@Override
	public void update(List<GameObject> objects) {
		//update rotation,position,animation,and collision
		rotateToMouse(Game.WIDTH/2,Game.HEIGHT/2);
		
		int tempW,tempA,tempS,tempD;
		tempW = tempA = tempS = tempD = 0;
		if(keyW){
			tempW = 1;
		}
		if(keyA){
			tempA = 1;
		}
		if(keyS){
			tempS = 1;
		}	
		if(keyD){
			tempD = 1;
		}
		
		int vkey = tempS-tempW;
		int hkey = tempD-tempA;
		velY = vkey*speed;
		velX = hkey*speed;
		
		updateAnimations();
		if(currentAnim!=null){
			currentAnim.update(System.currentTimeMillis());
				if(!currentAnim.isRunning()){
					currentAnim.resume();
				}
		}
		
		move();
		for(int j = 0; j<=objects.size()-1; j++){
			GameObject gm = objects.get(j);
			collision(gm);
		}
		checkMouseInput(objects);
	}
	
	
	protected void collision(GameObject col){
		//checks for collisions on
		//rectangles and edits x&y accordingly.
			if(col.getSolid()){
					if(bottomBounds().intersects(col.getRectBounds())){
						y = col.getY()-col.getHeight()/2-getHeight()/2;
						velY = 0;
					}
					if(topBounds().intersects(col.getRectBounds())){
						y = col.getY()+col.getHeight()/2+getHeight()/2;
						velY = 0;
					}
					if(leftBounds().intersects(col.getRectBounds())){
						x = col.getX()+col.getWidth()/2+getWidth()/2;
						velX = 0;
					}
					if(rightBounds().intersects(col.getRectBounds())){
						x = col.getX()-col.getWidth()/2-getWidth()/2;
						velX = 0;
					}
//				}
			}
		}
	@Override
	public void render(Graphics g){
		//displays players current image of current anim.
		Graphics2D g2d = (Graphics2D)g;
		g2d.rotate(Math.toRadians(rotation),x,y);
		g2d.drawImage(getImage(),x-getImageOrigin().x,y-getImageOrigin().y,getImage().getWidth()*Game.SCALE,getImage().getHeight()*Game.SCALE,null);
		g2d.rotate(-Math.toRadians(rotation),x,y);
		
//		g2d.setColor(Color.black);
//		g2d.draw(topBounds());
//		g2d.draw(bottomBounds());
//		g2d.draw(leftBounds());
//		g2d.draw(rightBounds());
	}
	
	public void move() {
		//moves players x&y
		x+=velX;
		y+=velY;
	}
	
	protected void rotateToMouse(int centerX,int centerY) {
		//rotates player to face mouse while on screen
		tox = (graphics.mouseX-(centerX));
		toy = (graphics.mouseY-(centerY));
		rotation = Math.toDegrees(-(Math.atan2(tox, toy)))+rotOffset;
	}
	
	public void rotate(int ang) {
		//increases rotation by angle ang in degrees
		rotation += ang;
	}
	public void setRotation(int ang){
		//sets current rotation of player.
		rotation = ang;
	}
	public void setX(int x){
		//sets X
		this.x = x;
	}
	
	public void setY(int y){
		//sets Y
		this.y = y;
	}
	
	public int getRotation() {
		//gets cur rotation
		return (int)rotation;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public BufferedImage getImage() {
		return sprite;
	}

	public void setImage(BufferedImage img){
		sprite = img;
	}
	
	public void setWeapon(Weapons weapon){
		this.weapon = weapon;
	}
	
	
	@Override
	public void keyInput(KeyEvent e) {
		//receives key input
		if(e.getID() == KeyEvent.KEY_PRESSED){
			if(e.getKeyChar() == 'w'){
				keyW = true;
			}
			if(e.getKeyChar() == 'a'){
				keyA = true;
			}
			if(e.getKeyChar() == 's'){
				keyS = true;
			}
			if(e.getKeyChar() == 'd'){
				keyD = true;
			}
		}
		else if(e.getID() == KeyEvent.KEY_RELEASED){
			if(e.getKeyChar() == 'w'){
				keyW = false;
			}
			if(e.getKeyChar() == 'a'){
				keyA = false;
			}
			if(e.getKeyChar() == 's'){
				keyS = false;
			}
			if(e.getKeyChar() == 'd'){
				keyD = false;
			}
		}
	}
	
	@Override
	public void mouseInput(MouseEvent e){
		//gets mouse input then sets variables = true accordingly.
		
		if(e.getID() == MouseEvent.MOUSE_PRESSED && SwingUtilities.isRightMouseButton(e)){
			mbRight = true;
		}
		if(e.getID() == MouseEvent.MOUSE_PRESSED && SwingUtilities.isLeftMouseButton(e)){
			mbLeft = true;
		}
		if(e.getID() == MouseEvent.MOUSE_RELEASED && SwingUtilities.isLeftMouseButton(e)){
			mbLeft = false;
		}
		if(e.getID() == MouseEvent.MOUSE_RELEASED && SwingUtilities.isRightMouseButton(e)){
			mbRight = false;
		}
	}
}

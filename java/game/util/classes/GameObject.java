package game.util.classes;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;

import game.id.ObjectID;

public abstract class GameObject{
	/*
	 * The basic things needed for each object in
	 * the game. Has a bounding box, x,y,w,h,solid
	 * boolean, and a layer for image displaying,
	 * so nothing get's block that shouldn't be.
	 * Also has keyInput and mouse input
	 * which it's children classes can override it
	 * for example: the button class, so it can 
	 * receive mouse and key input.
	 */
		
		public int x,y;
		protected int w,h,rotation;
		protected double velX,velY;
		protected ObjectID id;
		protected boolean isSolid;
		protected BufferedImage sprite;
		protected int layer;
		
		public GameObject(int x, int y, ObjectID id, int layer){
			this.x = x;
			this.y = y;
			this.id = id;
			this.layer = layer;
			isSolid = false;
		}
		public void setBounds(int w, int h){
			this.w = w;
			this.h = h;
		}
		public int getLayer(){
			return layer;
		}
		public Rectangle getRectBounds(){
			return new Rectangle(x-w/2,y-h/2,w,h);
		}
		public Rectangle topBounds(){
			return new Rectangle(x+5-(w/2),y-(h/2),w-10,5);
		}
		public Rectangle rightBounds(){
			return new Rectangle(x+(w/2)-5,y-(h/2)+5,5,h-10);
		}
		public Rectangle leftBounds(){
			return new Rectangle(x-(w/2),y-(h/2)+5,5,h-10);
		}
		public Rectangle bottomBounds(){
			return new Rectangle(x-(w/2)+5,y+(h/2)-5,w-10,5);
		}
		public abstract void update(List<GameObject> objects);
			
		public abstract void render(Graphics g);
		
		//for key input
		public void keyInput(KeyEvent e){
			
		}
		//for mouse input
		public void mouseInput(MouseEvent e){
			
		}
		
		public void rotate(int rotation){
			this.rotation = rotation;
		}
		public void setRotation(int rotation){
			this.rotation = rotation;
		}
		public int getRotation(){
			return rotation;
		}
		
		public void setVelX(int xspeed){
			velX = xspeed;
		}
		
		public void setVelY(int yspeed){
			velY = yspeed;
		}
		
		public double getVelX(){
			return velX;
		}
		
		public double getVelY(){
			return velY;
		}
		
		public BufferedImage getImage(){
			return sprite;
		}
		
		public void setImage(BufferedImage sprite){
			this.sprite = sprite;
		}
		
		public ObjectID getId(){
			return id;
		}
		
		public void setId(ObjectID id){
			this.id = id;
		}
		
		public boolean getSolid(){
			return isSolid;
		}
		
		public void setSolid(boolean collide){
			isSolid = collide;
		}
		
		public int getY() {
			return y;
		}
		
		public void setY(int y) {
			this.y = y;
		}
		
		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}
		
		public int getWidth() {
			return w;
		}
		
		public void setWidth(int w) {
			this.w = w;
		}
		
		public int getHeight() {
			return h;
		}
		
		public void setHeight(int h) {
			this.h = h;
		}
}
